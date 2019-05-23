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
import java.sql.SQLException;
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
    
    /**
    * Writes order inside of the database.  
    * If order is already existing in database, than nothing happens.
    * Commits only if there is no errors.
    * 
    * @param  order  order object which will be written to the database
    * @return true if writing was sucesful false if it wasn't
    */
    @Override  
    public boolean setOrder(Order order)
    {
        try(Connection con = provider.getConnection())
        {                   
            con.setAutoCommit(false);
            String sql = "IF NOT EXISTS"
                    + "(SELECT 1 FROM OrderTable WHERE OrderNo = ?) "
                    + "BEGIN "
                    + "INSERT INTO OrderTable (CustomerName,DeliveryDate,OrderNo) VALUES (?,?,?) "
                    + "END"; 
            PreparedStatement ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, order.getOrderNo());
            ps.setString(i++, order.getCustomerName());
            ps.setDate(i++, order.getDeliveryDate());
            ps.setString(i++, order.getOrderNo());
            int count = ps.executeUpdate();
            if(count > 0 && order.getTaskList()!= null){
                List<Task> tasks = order.getTaskList(); /* Putting task of an order into database aswell */
                for (Task task : tasks) {
                    if(!setTask(task,order.getOrderNo(),con)) //If setting task was not succesfull, we can safely return false since tha change has not been commited
                        return false;
                }
            }
            con.commit();
            return true;
        } 
        catch(SQLException ex)
        {
            Logger.getLogger(DbWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
   
    /**
    * Writes task for provided order inside of the database. 
    * Commiting is based on provided Connection.
    * Has no checking if task exists
    *
    * @param  task  task object to write to database
    * @param orderNo orderNumber that belongs to the task
    * @param con Connection to use to update database
    * @return true if writing was sucesful false if it wasn't
    */
    private boolean setTask(Task task, String orderNo,Connection con){
        try
        {
            //We are not checking if task exists because it is not possible and the primary key constrait is not broken since id is incremented automatically
            String sql = "INSERT INTO Task (OrderNo, DepartmentName,StartDate,EndDate,Finished) VALUES (?,?,?,?,?)"; 
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, orderNo);
            ps.setString(2, task.getDepartmentName());
            ps.setDate(3, task.getStartDate());
            ps.setDate(4, task.getEndDate());
            ps.setBoolean(5, task.isFinished());
            ps.execute();
            return true;
        } 
        catch(SQLException ex){
            Logger.getLogger(DbWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
    * Checks connection to the database
    * 
    * @return true if reader has connection to the database
    */
    @Override
    public boolean hasConnection()
    {
        try
        {
            Connection con = provider.getConnection();
            con.close();
            return true;
        } 
        catch(Exception ex){
            return false;
        }
    }
    
}
