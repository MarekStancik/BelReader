/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package belreader.be;

import java.sql.Date;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author alex
 */
public class Order {
    
    private SimpleStringProperty customerName;
    private Date deliveryDate;
    private String orderNo;
    private List<Task> taskList;
    
    public Order(String customerName, Date deliveryDate, String orderNo, List<Task> taskNo) {
        this.customerName = new SimpleStringProperty(customerName);
        this.deliveryDate = deliveryDate;
        this.orderNo = orderNo;
        this.taskList = taskNo;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setCustomerName(SimpleStringProperty customerName) {
        this.customerName = customerName;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    @Override
    public String toString()
    {
        String tasksStr = "";
        if(taskList != null)
        {
            for(Task task: taskList)
            {
                tasksStr += "\r\n\t" + task.toString();
            }
        }
        return "Order{" + "customerName=" + customerName + ", deliveryDate=" + deliveryDate + ", orderNo=" + orderNo + '}' + tasksStr;     
    }
    
}