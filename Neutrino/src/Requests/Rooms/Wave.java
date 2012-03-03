/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Requests.Rooms;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.*;
import org.jboss.netty.channel.Channel;
/**
 *
 * @author Julián
 */
public class Wave extends Handler implements Runnable  {
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
        Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        int Action = Client.in.readInt();
        
        ServerMessage Wave = new ServerMessage(ServerEvents.Wave);
        Wave.writeInt(CurrentUser.SessionId);
        Wave.writeInt(Action);
        Wave.Send(Socket);
        } catch (Exception e)
        {
            
        }
        
    }    
}
