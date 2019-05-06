/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package belreader.be;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author alex
 */
public class Order {
    
    private String orderNo;
    private String customerName;
    private Date deliveryDate;
    private List<Task> taskList = new ArrayList();
    
    public Order(String orderNo, String customerName, Date deliveryDate, List<Task> taskList) {
        this.orderNo = orderNo;
        this.customerName = customerName;
        this.deliveryDate = deliveryDate;
        this.taskList = taskList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (!Objects.equals(this.orderNo, other.orderNo)) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.deliveryDate, other.deliveryDate)) {
            return false;
        }
        if (!Objects.equals(this.taskList, other.taskList)) {
            return false;
        }
        return true;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
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