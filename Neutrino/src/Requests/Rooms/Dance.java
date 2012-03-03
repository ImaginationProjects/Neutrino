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
import neutrino.UserManager.SubscriptionManager;
import neutrino.RoomManager.*;
import org.jboss.netty.channel.Channel;
/**
 *
 * @author Julián
 */
public class Dance extends Handler implements Runnable  {
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
        
        int DanceId = Client.in.readInt();
        SubscriptionManager s = new SubscriptionManager(CurrentUser, Server);
        if(!s.HasSubscription("habbo_vip") & DanceId > 1)
            return;
        
        ServerMessage Dance = new ServerMessage(ServerEvents.Dance);
        Dance.writeInt(CurrentUser.SessionId);
        Dance.writeInt(DanceId);
        Dance.Send(Socket);
        } catch (Exception e)
        {
            
        }
        
    }    
}