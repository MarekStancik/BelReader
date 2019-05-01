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
    InputStream json;
    
    public JSONReader(InputStream json)
    {
        this.json = json;
    }
    
  /*  public static void main(String... args) throws FileNotFoundException
    {
        JSONReader reader = new JSONReader(new FileInputStream("JSON.txt"));
        reader.getOrders();
    }*/
    
    @Override
    public List<Order> getOrders()
    {
        JSONTokener tokener = new JSONTokener(json);
        JSONObject obj = new JSONObject(tokener);
        JSONArray arr = obj.getJSONArray("AvailableWorkers");
        Iterator itWorkers = arr.iterator(); 
          
        List<Worker> workers = new ArrayList<Worker>();
        while (itWorkers.hasNext())  
        { 
            JSONObject worker = (JSONObject) itWorkers.next();
            workers.add(new Worker(worker.getString("__type"),worker.getString("Name"),worker.getString("Initials"),worker.getInt("SalaryNumber")));
        } 
      /*  for(Worker wk: workers)
        {
            System.out.println(wk.toString());
        }*/ //FOR TESTING
        
        List<Order> orders = new ArrayList<>();
        arr = obj.getJSONArray("ProductionOrders");
        Iterator itOrders = arr.iterator();
        
        while(itOrders.hasNext())
        {
            JSONObject order = (JSONObject) itOrders.next();
            String customerName = ((JSONObject)order.get("Customer")).getString("Name");
            String deliveryDate = ((JSONObject)order.get("Delivery")).getString("DeliveryTime");
            String orderNum = ((JSONObject)order.get("Order")).getString("OrderNumber");
            JSONArray tasks = ((JSONObject)order).getJSONArray("DepartmentTasks");
            Iterator itTasks = tasks.iterator();
            List<Task> taskList = new ArrayList<Task>();
            while(itTasks.hasNext())
            {
                JSONObject task = (JSONObject) itTasks.next();
                String departmentName = ((JSONObject)task.get("Department")).getString("Name");
                String endDate = task.getString("EndDate");
                String startDate = task.getString("StartDate");
                boolean isFinished = task.getBoolean("FinishedOrder");
                taskList.add(new Task(departmentName, parseDate(endDate), parseDate(startDate), isFinished));
            }
            orders.add(new Order(customerName,parseDate(deliveryDate),orderNum,taskList));
        }       
        
        /*for(Order or: orders)
            System.out.println(or.toString());*/ //FOR TESTING
            
        return orders;
    }
    
    public Date parseDate(String str)
    {
        String epochStr = "";
        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            if(Character.isDigit(ch) && epochStr.length() < 10)
                epochStr += ch;
        }
        long epoch = Long.parseLong(epochStr);
        Date date = new Date(epoch*1000);
        return date;
    }
    
}
