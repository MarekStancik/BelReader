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
    
    private ScheduledExecutorService executor;
    private Model model;
    private TrayIcon trayIcon;
    private Stage stage;
    private boolean firstTime;
    private boolean isError;
    private Properties properties;
    
    private final String PROP_FILE = "src/belreader/resources/connection.properties";
    private final String TRAY_NAME = "BelReader";
    private final String ERROR_IMAGE_PATH = "src/belreader/resources/belmanRed.jpg";
    private final String NORMAL_IMAGE_PATH = "src/belreader/resources/belmanBlue.jpg";
    private final String JSON_PATH = "src/belreader/resources/json.txt";
    private final String NO_CONNECTION_MSG = "Cannot connect to the database";
    @FXML
    private TextField textFilePath;

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

    @FXML
    private void pressConnect(ActionEvent event)
    {
        stop();
        if(isFilled())
        {
            properties.setProperty("ServerName", textServerName.getText());
            properties.setProperty("PortNumber", textPortNumber.getText());
            properties.setProperty("DbName", textDbName.getText());
            properties.setProperty("UserName", textUserName.getText());
            properties.setProperty("Password", textUserPassword.getText());
            properties.setProperty("FilePath", textFilePath.getText());
        }
        else
        {
            Alert alert = new Alert(AlertType.WARNING, "Database parameters are not set up.\r\nLoad from file?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();
            if(!(alert.getResult() == ButtonType.YES && tryLoadFromPropFile()))
                return;
        }
        connect();
    }
    
    private void connect()
    {
        stop();
        if(model.hasConnection())
        {
            normalState("Sucessfully connected to database");
            start();
        }
        else
        {
           isError = false;  //To actually show the error message - this one is called on user input so we want the message to be shown all time/unlike on thread
           errorState(NO_CONNECTION_MSG); 
        }
    }
    
    private void exit()
    {
        stop(); 
        try{
             properties.store(new FileOutputStream(new File(PROP_FILE)), TRAY_NAME);
         }catch (IOException ex){
             Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
         }

         System.exit(0); 
    }
    
    private void stop()
    {
        if(executor != null && !executor.isShutdown())
            executor.shutdown();
    }
    
    private void setUpTray()
    {
        if(SystemTray.isSupported())
        {
            try
            {
                SystemTray tray = SystemTray.getSystemTray();
                java.awt.Image img = ImageIO.read(new File(NORMAL_IMAGE_PATH));
                
                PopupMenu pm = new PopupMenu();
                MenuItem itemParams = new MenuItem("Parameters");
                itemParams.addActionListener((event)->{ Platform.runLater(()->{stage.show();});  });
                
                MenuItem itemConnect = new MenuItem("Connect");
                itemConnect.addActionListener((event)->{ connect();});
                
                MenuItem itemExit = new MenuItem("Exit");
                itemExit.addActionListener((e)->{exit();});
                
                pm.add(itemParams);
                pm.add(itemConnect);
                pm.add(itemExit);
                
                trayIcon = new TrayIcon(img,TRAY_NAME, pm);
                tray.add(trayIcon);
                Platform.setImplicitExit(false);
            } catch (IOException ex)
            {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (AWTException ex)
            {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }
    
    public void setUpStage(Stage stage)
    {
        this.stage = stage;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() 
            {
                @Override
                public void handle(WindowEvent t) {
                    hide(stage);
                }
            });
        
        setUpTray();
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
    
    private void start()
    {
        stop();
        model.changeJsonFilePath(textFilePath.getText());
        executor = Executors.newScheduledThreadPool(1);
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
    
    private void errorState(String reason)
    {
        if(!isError)
        {
            try{
                trayIcon.setImage(ImageIO.read(new File(ERROR_IMAGE_PATH)));
            } catch (IOException ex){
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

            if(!reason.isEmpty())
                trayIcon.displayMessage("BelReader error", reason, TrayIcon.MessageType.ERROR);
        }   
        isError = true;
    }
    
    private void normalState(String message)
    {
        try{
            trayIcon.setImage(ImageIO.read(new File(NORMAL_IMAGE_PATH)));
        } catch (IOException ex){
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!message.isEmpty())
            trayIcon.displayMessage("BelReader", message, TrayIcon.MessageType.INFO);
        
        isError = false;
    }
    
}
