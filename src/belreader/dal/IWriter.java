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
public interface IWriter
{
    /**
     * 
     * @param Order to be stored
     * @return true if writing has been sucessfull
     */
    boolean setOrder(Order order);
    
    /**
     * Checks if writer has connection to its storage
     * @return true if it has connection
     */
    boolean hasConnection();
}
