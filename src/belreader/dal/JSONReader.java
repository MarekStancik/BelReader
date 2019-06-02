/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.dal;

import belreader.be.Order;
import belreader.be.Task;
import belreader.be.Worker;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;

/**
 *
 * @author Marek
 */
public class JSONReader implements IReader
{
    private InputStream json;
    
    /**
     * Creates instance and sets the input stream that will be used to load data
     * @param json input stream with json data
     */
    public JSONReader(InputStream json)
    {
        setInputStream(json);
    }
    
    //Is here in case of inheritance
    protected JSONReader()
    {
        
    }
    
     /**
     * Sets the input stream that will be used to load data
     * @param json input stream with json data
     */
    protected void setInputStream(InputStream json)
    {
        this.json = json;
    }
    
    /**
     * Loads the orders from provided InputStream
     * @return list of orders loaded from InputStream 
     */
    @Override
    public List<Order> getOrders()
    {
        JSONTokener tokener = new JSONTokener(json); //I have suspicion thath the JSONTokener constructor would throw Exception in case of invalid data
                                                     //But I have no idea what is the type of the exception
        JSONObject obj = new JSONObject(tokener);
        if(obj == null) //Same suspicion here
            return null;
        
        List<Order> orders = new ArrayList<>(); //Instance that will be returned
        JSONArray arr = obj.getJSONArray("ProductionOrders"); //Loads json array of Orders
        if(arr == null) 
                return null;
        
        Iterator itOrders = arr.iterator(); //Gets iterator on a first place in an array
        while(itOrders.hasNext()) //While there is a next order
        {
            /*************************Loads the Order attributes**************************/
            JSONObject order = (JSONObject) itOrders.next(); //Takes the order and change it into JSONObject
            String customerName = ((JSONObject)order.get("Customer")).getString("Name"); 
            String deliveryDate = ((JSONObject)order.get("Delivery")).getString("DeliveryTime");
            String orderNum = ((JSONObject)order.get("Order")).getString("OrderNumber");
            
            /***********************Loads the Tasks for actual order************************************/
            JSONArray tasks = order.getJSONArray("DepartmentTasks"); //Loads the task Array
            Iterator itTasks = tasks.iterator(); //Gets the iterator to the first element in array
            List<Task> taskList = new ArrayList<Task>();
            while(itTasks.hasNext()) //Loads tasks while there are any
            {
                JSONObject task = (JSONObject) itTasks.next();
                
                /***************************Loads Task attributes**************************/
                String departmentName = ((JSONObject)task.get("Department")).getString("Name");
                String endDate = task.getString("EndDate");
                String startDate = task.getString("StartDate");
                boolean isFinished = task.getBoolean("FinishedOrder");
                
                taskList.add(new Task(epochDateToDate(startDate),epochDateToDate(endDate),departmentName,isFinished)); //Add the task to the list
            }
            orders.add(new Order(orderNum,customerName,epochDateToDate(deliveryDate),taskList));
        }       
        
        return orders;
    }
   
    /**
     * Selects only digit from provided string and tries to parse it into date
     * @param str String of date that is loaded from provided JSON file
     * @return the SqlDate that is parsed from the provided string, 
     * in case of invalid String return value is the "minimum" value of Date
     */
    public static Date epochDateToDate(String str)
    {
        String epochStr = "";
        
        /***************Goes through every char in string and checks if it is and digit*****************/
        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if(Character.isDigit(ch) && epochStr.length() < 10) //Goes only until 10 because we dont care about milliseconds
                epochStr += ch;
        }
        if(!epochStr.isEmpty() && epochStr.length() == 10)
        {
            long epoch = Long.parseLong(epochStr);
            Date date = new Date(epoch*1000);
            return date;
        }
        else
            return new Date(0);   
    }   
}
