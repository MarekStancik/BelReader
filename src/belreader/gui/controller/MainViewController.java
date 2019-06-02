/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.gui.controller;

import belreader.bll.Model;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Marek
 */
public class MainViewController implements Initializable
{

    @FXML
    private TextField textServerName;
    @FXML
    private TextField textPortNumber;
    @FXML
    private TextField textDbName;
    @FXML
    private TextField textUserName;
    @FXML
    private TextField textUserPassword;
    @FXML
    private TextField textFilePath;
    
    //executor that takes care of dynamicall checking of new JSON data and than updating the database
    private ScheduledExecutorService executor;
    
    //Business model for the application
    private Model model;
    
    //Instance of trayIcon(the small Icon in system Tray)
    private TrayIcon trayIcon;
    
    //Parent stage
    private Stage stage;
    
    //States whether the program is minimized for firstTime - if it is than it will show message that it runs in background
    private boolean firstTime;
    
    //States whether the program is in the error or normal state
    private boolean isError;
    
    //Reference to properties that are used to set up the application
    private Properties properties;
    
    //Path to properties file
    private final String PROP_FILE = "src/belreader/resources/connection.properties";
    
    //The string that will be shown as a hint text when mouseover the trayIcon
    private final String TRAY_NAME = "BelReader";
    
    //Path to image which will be used in trayIcon in case an error would occur
    private final String ERROR_IMAGE_PATH = "src/belreader/resources/belmanRed.jpg";
    
    //Path to image which will be used in trayIcon while application is in working state
    private final String NORMAL_IMAGE_PATH = "src/belreader/resources/belmanBlue.jpg";
    
    //Default path to JSON file
    private final String JSON_PATH = "src/belreader/resources/json.txt";
    
    //Message that will show up in balloon hint when there will be no connection to database
    private final String NO_CONNECTION_MSG = "Cannot connect to the database";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
    }    
    
    public MainViewController()
    {
        isError = false;
        firstTime = true;
        properties = new Properties();
        model = new Model(properties, JSON_PATH); 
    }

    /***********Event that will be fired when user press Connect button*****************/
    @FXML
    private void pressConnect(ActionEvent event)
    {
        stop(); //Stops the executor in case it would already run
        if(isFilled()) //Checks if the textFields are filled, If they are, than the properties instance will be set accordingly to the textFields
        {
            properties.setProperty("ServerName", textServerName.getText());
            properties.setProperty("PortNumber", textPortNumber.getText());
            properties.setProperty("DbName", textDbName.getText());
            properties.setProperty("UserName", textUserName.getText());
            properties.setProperty("Password", textUserPassword.getText());
            properties.setProperty("FilePath", textFilePath.getText());
        }
        else //If text fields are not filled than Message will show up
        {
            Alert alert = new Alert(AlertType.WARNING, "Database parameters are not set up.\r\nLoad from file?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();
            if(!(alert.getResult() == ButtonType.YES && tryLoadFromPropFile()))
                return;
        }
        connect();
    }
    
    /*****************Method used to connect to the database*********************/
    private void connect()
    {
        stop(); //Stops the executor in case it would already run
        if(model.hasConnection()) //Try connect to the database
        {
            normalState("Sucessfully connected to database");
            start(); //Starts the executor again
        }
        else
        {
           isError = false;  //To actually show the error message - this one is called on user input so we want the message to be shown all time/unlike on thread
           errorState(NO_CONNECTION_MSG); 
        }
    }
    
    /*****************Method used to correctly quit the application*************/
    private void exit()
    {
        stop(); //Stops the executor 
        try{
             properties.store(new FileOutputStream(new File(PROP_FILE)), TRAY_NAME); //Stores the properties to file
         }catch (IOException ex){
             Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
         }

         System.exit(0);  //Exits the application
    }
    
    /*********************STOPS the execuotr*******************/
    private void stop()
    {
        if(executor != null && !executor.isShutdown())
            executor.shutdown();
    }
    
    /*******************Sets up the TRAY icon-Should be used only once at start************/
    private void setUpTray()
    {
        if(SystemTray.isSupported()) //Checks if tray Icon is supported on this platfrorm
        {
            try
            {
                SystemTray tray = SystemTray.getSystemTray(); //Recieves the systemtray 
                java.awt.Image img = ImageIO.read(new File(NORMAL_IMAGE_PATH)); //Sets up the image for the icon
                
                //Setting up PopupMenu for trayIcon
                PopupMenu pm = new PopupMenu();
                
                //Creating "Parameters" item
                MenuItem itemParams = new MenuItem("Parameters");
                itemParams.addActionListener((event)->{ Platform.runLater(()->{stage.show();});  });
                
                //Creating "Connect" item
                MenuItem itemConnect = new MenuItem("Connect");
                itemConnect.addActionListener((event)->{ connect();});
                
                //Creating "Exit" item
                MenuItem itemExit = new MenuItem("Exit");
                itemExit.addActionListener((e)->{exit();});
                
                //Adding the items to the popup menu
                pm.add(itemParams);
                pm.add(itemConnect);
                pm.add(itemExit);
                
                //Creates the trayIcon with loaded image and created PopupMenu, than adds it into system Tray
                trayIcon = new TrayIcon(img,TRAY_NAME, pm);
                tray.add(trayIcon);
                Platform.setImplicitExit(false); //So the application would not close on "close" icon in the application bar
            } catch (Exception ex)
            {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = new Alert(AlertType.ERROR, "Error occured while seting up application enviroment", ButtonType.OK);
                alert.showAndWait();
                System.exit(0);
            }
        }  
        else
        {
            Alert alert = new Alert(AlertType.ERROR, "Application is not supported on this machine", ButtonType.OK);
            alert.showAndWait();
            System.exit(0);
        }
    }
    
    //**************Method to setUp the whole stage- should be called just once at start**************/
    public void setUpStage(Stage stage)
    {
        this.stage = stage;
        
        //Setting application so it will minimize in case of pressing quit icon
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
            {
                @Override
                public void handle(WindowEvent t) {
                    hide(stage);
                }
            });
        
        //Sets up the trayIcon
        setUpTray();
        
        //Try to load properties from file, if they are loaded than It will just start functioning and minimize
        if(tryLoadFromPropFile())
        {
            start();
            hide(stage);
        }
    }
    
    public void showProgramIsMinimizedMsg() 
    {
        if (firstTime) 
        {
            trayIcon.displayMessage("BelReader","Application runs in background",TrayIcon.MessageType.INFO);
            firstTime = false;
        }
    }
    
    /**
     * Hides the stage correctly, if tray is not supported the application will just close
     * @param stage stage to hide 
     */
    private void hide(final Stage stage) 
    {
        Platform.runLater(new Runnable() 
        {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                    showProgramIsMinimizedMsg();
                } else {
                    System.exit(0);
                }
            }
        });
    }
    
    /**
     * 
     * @return true if all of the textFields are filled else false
     */
    private boolean isFilled()
    {
        TextField tfs[] = {textDbName,textPortNumber,textServerName,textUserName,textUserPassword,textFilePath};
        for(TextField tf: tfs)
        {
            if(tf.getText() == null || tf.getText().isEmpty())
                return false;
        }
        return true;
    }
    
    /**
     * 
     * @return True if all of the params for database are loaded from the file, otherwise returns false
     */
    private boolean tryLoadFromPropFile()
    {
        try
        {
            File f = new File(PROP_FILE);
            if(f.exists())
                properties.load(new FileInputStream(f));
        }  catch (IOException ex)
        {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        textServerName.setText(properties.getProperty("ServerName"));
        textPortNumber.setText(properties.getProperty("PortNumber"));
        textDbName.setText(properties.getProperty("DbName"));
        textUserName.setText(properties.getProperty("UserName"));
        textUserPassword.setText(properties.getProperty("Password"));
        textFilePath.setText(properties.getProperty("FilePath"));
        return isFilled();
    }
    
    /**
     * Starts the executor that periodically checks for new data every 5 seconds
     */
    private void start()
    {
        stop(); //Stops the executor in case it was already running
        model.changeJsonFilePath(textFilePath.getText()); //Setts the most current file path
        executor = Executors.newScheduledThreadPool(1); //Creates new executor -- Old one is stopped by now and will be taken care of by GarbageCollector
        executor.scheduleAtFixedRate(()->
            {
                if(model.hasConnection())
                {
                    if(model.hasNewData())
                        model.update();
                    if(isError)
                        normalState("Connection Refreshed");
                }
                else
                    errorState(NO_CONNECTION_MSG);
            }
                , 1, 5, TimeUnit.SECONDS);
    }
    
    /**
     * Puts application in error state and shows message that is provided as parameter
     * @param reason Message to be show in error state 
     */
    private void errorState(String reason)
    {
        if(!isError)
        {
            try{
                trayIcon.setImage(ImageIO.read(new File(ERROR_IMAGE_PATH))); //Sets up the error image for tray icon
            } catch (IOException ex){
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if(!reason.isEmpty())
                trayIcon.displayMessage("BelReader error", reason, TrayIcon.MessageType.ERROR); //Shows the balloon hint
        }   
        isError = true;
    }
    
    /**
     * Puts the application back to normal state
     * @param message The message to be shown when application is put to normal state
     */
    private void normalState(String message)
    {
        try{
            trayIcon.setImage(ImageIO.read(new File(NORMAL_IMAGE_PATH))); //Sets up the "normal" image for tray icon
        } catch (IOException ex){
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!message.isEmpty())
            trayIcon.displayMessage("BelReader", message, TrayIcon.MessageType.INFO);
        
        isError = false;
    }
    
}
