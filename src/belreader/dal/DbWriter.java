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
        try(Connection con = provider.getConnection())
        {                   
            String sql = "INSERT INTO OrderTable (CustomerName,DeliveryDate,OrderNo) VALUES (?,?,?)"; 
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, order.getCustomerName());
            ps.setDate(2, order.getDeliveryDate());
            ps.setString(3, order.getOrderNo());
            ps.execute();
            if(order.getTaskList()!= null){
                List<Task> tasks = order.getTaskList(); /* Putting task of an order into database aswell */
                for (Task task : tasks) {
                    setTask(task,order.getOrderNo());
                }
            }
        } 
        catch(Exception ex){
            Logger.getLogger(DbWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   
    /* Method and Cyril from writing tasks inside database */
    public void setTask(Task task, String orderNo){
        try(Connection con = provider.getConnection())
        {
            String sql = "INSERT INTO Task (OrderNo, DepartmentName,StartDate,EndDate,Finished) VALUES (?,?,?,?,?)"; 
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, orderNo);
            ps.setString(2, task.getDepartmentName());
            ps.setDate(3, task.getStartDate());
            ps.setDate(4, task.getEndDate());
            ps.setBoolean(5, task.isFinished());
            ps.execute();
        } 
        catch(Exception ex){
            Logger.getLogger(DbWriter.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    @Override
    public boolean hasConnection()
    {
        try(Connection con = provider.getConnection())
        {
            return true;
        } 
        catch(Exception ex){
            Logger.getLogger(DbWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}
