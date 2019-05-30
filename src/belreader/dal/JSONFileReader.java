/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.dal;

import belreader.be.Order;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marek
 */
public class JSONFileReader extends JSONReader
{
    private final String filePath;
    
    public JSONFileReader(final String filePath)
    {    
        this.filePath = filePath;
    }

    @Override
    public List<Order> getOrders()
    {
        if(Files.exists(Paths.get(filePath)))
        {
            try(InputStream is = new FileInputStream(filePath)){
                setInputStream(is);
                return super.getOrders(); //To change body of generated methods, choose Tools | Templates.
            } catch (IOException ex){
                Logger.getLogger(JSONFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }   
    
}
