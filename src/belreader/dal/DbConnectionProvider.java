/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marek
 */
public class DbConnectionProvider
{
    private SQLServerDataSource ds;
    
    public DbConnectionProvider(Properties prop)
    {
        ds = new SQLServerDataSource();
        ds.setServerName(prop.getProperty("ServerName"));
        ds.setPortNumber(Integer.parseInt(prop.getProperty("PortNumber")));
        ds.setDatabaseName(prop.getProperty("DbName"));
        ds.setUser(prop.getProperty("UserName"));
        ds.setPassword(prop.getProperty("Password"));
    }
    
    public Connection getConnection()
    {
        try
        {
            return ds.getConnection();
        } catch (SQLServerException ex)
        {
            Logger.getLogger(DbConnectionProvider.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
