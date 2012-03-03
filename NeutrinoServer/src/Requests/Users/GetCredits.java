/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Requests.Users;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
/**
 *
 * @author Julián
 */
public class GetCredits extends Handler implements Runnable {
    private ServerHandler Client;
    private Environment Server;
    private FutureTask T;
    @Override
    public void Load(ServerHandler Client, Environment Server, FutureTask T) throws Exception
    {
        this.Client = Client;
        this.Server = Server;
        this.T = T;
    }
    
    public void run() {
        try {
        ServerMessage Credits = new ServerMessage(ServerEvents.SendCredits);
        Credits.writeUTF(Client.GetSession().Credits + ".0");
        Credits.Send(Client.Socket);
        } catch (Exception e)
        {
            
        }
        
    }
}
