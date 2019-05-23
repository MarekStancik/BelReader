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
    boolean setOrder(Order order);
    boolean hasConnection();
}
