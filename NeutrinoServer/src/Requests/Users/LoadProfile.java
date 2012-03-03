/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Requests.Users;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import java.util.concurrent.FutureTask;
/**
 *
 * @author Julián
 */
public class LoadProfile extends Handler implements Runnable {
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
        int UserId = Client.in.readInt();
        Habbo User = Habbo.UsersbyId.get(UserId);
        
        ServerMessage Profile = new ServerMessage(ServerEvents.WatchProfile);
        Profile.writeInt(UserId);
        Profile.writeUTF(User.UserName);
        Profile.writeUTF(User.Look);
        Profile.writeUTF(User.Motto);
        Profile.writeUTF("21-12-2012");
        Profile.writeInt(55431); // achv
        Profile.writeInt(9522); // friends
        Profile.writeUTF(""); // registered
        Profile.writeBoolean(User.IsOnline); // online
        Profile.writeInt(0); // group count
        Profile.writeInt(503);
        Profile.Send(Client.Socket);
        } catch (Exception e)
        {
            
        }
        
    }
    
}
