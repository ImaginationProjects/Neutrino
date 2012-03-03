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
import neutrino.UserManager.Habbo;
/**
 *
 * @author Julián
 */
public class LoadMyData extends Handler implements Runnable {
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
        Habbo User = Client.GetSession();
        ServerMessage Data = new ServerMessage(ServerEvents.MyInformation);
        Data.writeInt(User.Id);
        Data.writeUTF(User.UserName);
        Data.writeUTF(User.Look);
        Data.writeUTF(User.Gender.toUpperCase());
        Data.writeUTF(User.Motto);
        Data.writeUTF(""); // fbname, blablabla
        Data.writeBoolean(false);
        Data.writeInt(3); // Respect
        Data.writeInt(3); // Respect to give
        Data.writeInt(3); // to give to pets
        Data.writeBoolean(true);
        Data.Send(Client.Socket);
        
        ServerMessage AchivPoints = new ServerMessage(ServerEvents.SendAchievements);
        AchivPoints.writeInt(500); // achiv points
        AchivPoints.Send(Client.Socket);
        } catch (Exception e)
        {
            
        }
        
    }
}
