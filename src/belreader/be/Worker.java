/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.be;

/**
 *
 * @author Marek
 */
public class Worker
{
    private String type;
    private String name;
    private String initials;
    private int salaryNo;

    public Worker(String type, String name, String initials, int salaryNo)
    {
        this.type = type;
        this.name = name;
        this.initials = initials;
        this.salaryNo = salaryNo;
    }

    @Override
    public String toString()
    {
        return "Worker{" + "type=" + type + ", name=" + name + ", initials=" + initials + ", salaryNo=" + salaryNo + '}';
    }

    
    
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getInitials()
    {
        return initials;
    }

    public void setInitials(String initials)
    {
        this.initials = initials;
    }

    public int getSalaryNo()
    {
        return salaryNo;
    }

    public void setSalaryNo(int salaryNo)
    {
        this.salaryNo = salaryNo;
    }
    
    
}