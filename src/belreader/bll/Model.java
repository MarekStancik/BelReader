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
import belreader.dal.JSONFileReader;
import java.util.List;
import java.util.Properties;


/**
 *
 * @author Marek
 */
public class Model
{
    private IWriter writer;
    private IReader reader;
    private List<Order> lastOrders;
    
    public Model(final Properties dbProps,final String filePath)
    {
        writer = new DbWriter(new DbConnectionProvider(dbProps));
        initializeReader(filePath);
    }
    
    private void initializeReader(final String filePath)
    {
        reader = new JSONFileReader(filePath);
    }

    /**
     * Updates the writer with the new data from reader
     */
    public void update()
    {
        lastOrders = reader.getOrders();
        if(lastOrders != null)
        {
            for(Order order: lastOrders)
            {
                if(!writer.setOrder(order)) //If write is not successful than return from function
                    return;
            }
        }
    }
    
    /**
     * Checks if the data loaded from reader are newer than the one that was already written
     * @return true if has new data
     */
    public boolean hasNewData()
    {
        List<Order> newOrders = reader.getOrders();
        if(newOrders != null && newOrders.size() > 0)
        {
            if(lastOrders != null && lastOrders.size() == newOrders.size())
            {
                for (int i = 0; i < lastOrders.size(); i++)
                    if(!lastOrders.get(i).equals(newOrders.get(i)))
                        return true;
            }
            else
                return true;
        }
        return false;
    }
    
    /**
     * Indicate whether the model has connection to needed sources
     * @return true if connection is enstabilished 
     */
    public boolean hasConnection()
    {
        return writer.hasConnection();
    }
    
    /**
    * changes path to the JSON file
    *
    * @param  newPath  an absolute path to the JSON file
    */
    public void changeJsonFilePath(String newPath)
    {
        initializeReader(newPath);
    }
}
