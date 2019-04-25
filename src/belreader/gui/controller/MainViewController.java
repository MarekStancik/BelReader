/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.gui.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

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
    
    final String PROP_FILE = "connection.properties";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        if(tryLoadFromPropFile())
        {
            start();
        }
    }    

    @FXML
    private void pressConnect(ActionEvent event)
    {
        
    }
    
    private boolean isFilled()
    {
        return !(textServerName.getText().isEmpty() || textPortNumber.getText().isEmpty() || textDbName.getText().isEmpty() 
                || textUserName.getText().isEmpty() || textUserPassword.getText().isEmpty());
    }
    
    private boolean tryLoadFromPropFile()
    {
        try
        {
            Properties prop = new Properties();
            prop.load(new FileInputStream(PROP_FILE));
            textServerName.setText(prop.getProperty("ServerName"));
            textPortNumber.setText(prop.getProperty("PortNumber"));
            textDbName.setText(prop.getProperty("DbName"));
            textUserName.setText(prop.getProperty("UserName"));
            textUserPassword.setText(prop.getProperty("Password"));
            return isFilled() ? tryConnect() : false;
        } catch (IOException ex)
        {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private boolean tryConnect()
    {
        
    }
    
    private void start()
    {
        
    }
    
}
