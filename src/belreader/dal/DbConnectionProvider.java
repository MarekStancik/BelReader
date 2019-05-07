/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.dal;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
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
    final private Properties props;
    
    public DbConnectionProvider(final Properties prop)
    {
        props = prop;
        ds = new SQLServerDataSource();
    }
    
    public Connection getConnection()
    {
        try{
            ds.setServerName(props.getProperty("ServerName"));
            int pnum = 0;
            try{
                pnum = Integer.parseInt(props.getProperty("PortNumber"));
            }catch(NumberFormatException ex){
                pnum = 0;
            }
            ds.setPortNumber(pnum);
            ds.setDatabaseName(props.getProperty("DbName"));
            ds.setUser(props.getProperty("UserName"));
            ds.setPassword(props.getProperty("Password"));
            return ds.getConnection();
        } catch (SQLServerException ex){
            Logger.getLogger(DbConnectionProvider.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
