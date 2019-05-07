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
public class UpdateState
{
    public enum State { OK,NO_JSON,NO_NEWS,NO_CONNECTION };
    private State state;
    private String message;
    
    public UpdateState(State state,String msg)
    {
        this.state = state;
        this.message = message;
    }
    
}
