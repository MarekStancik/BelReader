/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package belreader.dal;

import belreader.be.Order;

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
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasConnection()
    {
        return false;//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
