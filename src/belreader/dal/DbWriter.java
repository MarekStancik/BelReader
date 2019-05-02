/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.dal;

import belreader.be.Order;
import belreader.be.Task;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marek
 */
public class DbWriter implements IWriter
{
    private DbConnectionProvider provider;
    
    public DbWriter(DbConnectionProvider provider)
    {
        this.provider = provider;
        //MADS HERE IT IS
    }
    
   @Override  
    public void setOrder(Order order)
            /* WRITING ORDER OBJECT INSIDE A DATABASE */
    {
        try
        {
            
            Connection con = provider.getConnection();                     
            String sql = "INSERT INTO Orders (CustomerName,DeliveryDate,OrderNo,) VALUES (?,?,?)"; 
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, order.getCustomerName());
            ps.setDate(2, order.getDeliveryDate());
            ps.setString(3, order.getOrderNo());
            ps.execute();
            List<Task> tasks = order.getTaskList();
            /*  Now order is written and we need to write also Tasks inside of Order object */
            for (Task task : tasks) {
                setTask(task);
                setRelation(order, task);
                
            }
            
            
            
        } 
        catch(Exception ex){
            Logger.getLogger(DbWriter.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    /* Method to creating relation between specific tasks inside an order */
    public void setRelation(Order order, Task task){
         try
        {
            
            Connection con = provider.getConnection();                     
            String sql = "INSERT INTO OrderTasks (OrderNo,TaskNo) VALUES (?,?)"; 
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, order.getOrderNo());
            ps.setInt(2, task.getID());
            ps.execute();
           
            
 
        } 
        catch(Exception ex){
            Logger.getLogger(DbWriter.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    /* Method and Cyril from writing tasks inside database */
    public void setTask(Task task){
         try
        {
            
            Connection con = provider.getConnection();                     
            String sql = "INSERT INTO Tasks (Department,StartDate,EndDate,Finished) VALUES (?,?,?,?)"; 
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, task.getDepartment());
            ps.setDate(2, task.getStartDate());
            ps.setDate(3, task.getEndDate());
            ps.setBoolean(4, task.isFinished());
            ps.execute();
           
            
 
        } 
        catch(Exception ex){
            Logger.getLogger(DbWriter.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }

    @Override
    public boolean hasConnection()
    {
        return false;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
