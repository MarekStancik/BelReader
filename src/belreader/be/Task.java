/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package belreader.be;

import java.sql.Date;

/**
 *
 * @author alex
 */
public class Task {
    
    private String department;
    private Date endDate;
    private Date startDate;
    private boolean finished;

    public Task(String department, Date endDate, Date startDate, boolean finished) {
        this.department = department;
        this.endDate = endDate;
        this.startDate = startDate;
        this.finished = finished;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }


    @Override
    public String toString() {
        return "Task{" + "department=" + department + ", endDate=" + endDate + ", startDate=" + startDate + ", finished=" + finished + '}';
    }
    
    
}
