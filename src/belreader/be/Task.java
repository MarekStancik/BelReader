/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package belreader.be;

import java.sql.Date;
import java.util.Objects;

/**
 *
 * @author alex
 */
public class Task {
    
    private Date startDate;
    private Date endDate;
    private String departmentName;
    private boolean finished;

    public Task(Date startDate, Date endDate, String departmentName, boolean finished) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.departmentName = departmentName;
        this.finished = finished;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Task other = (Task) obj;
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        if (!Objects.equals(this.endDate, other.endDate)) {
            return false;
        }
        if (!Objects.equals(this.departmentName, other.departmentName)) {
            return false;
        }
        if (this.finished != other.finished) {
            return false;
        }
        return true;
    }
    
    
    
    @Override
    public String toString() {
        return "Task{" + "startDate=" + startDate + ", endDate=" + endDate + ", departmentName=" + departmentName + ", finished=" + finished + '}';
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}