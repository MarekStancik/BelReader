/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.bll;

import belreader.be.Order;
import belreader.dal.DbConnectionProvider;
import belreader.dal.DbWriter;
import belreader.dal.IReader;
import belreader.dal.IWriter;
import belreader.dal.JSONReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marek
 */
public class Model
{
    private IWriter writer;
    private IReader reader;
    private List<Order> lastOrders;
    private String filePath;
    
    public Model(Properties dbProps,String filePath)
    {
        writer = new DbWriter(new DbConnectionProvider(dbProps));
        this.filePath = filePath; 
    }

    private void update()
    {
        try
        {
            InputStream is = new FileInputStream(filePath);
            reader = new JSONReader(is);
            lastOrders = reader.getOrders();
            if(lastOrders != null)
            {
                for(Order order: lastOrders)
                {
                    writer.setOrder(order);
                }
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
