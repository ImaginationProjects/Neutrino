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
import neutrino.RoomManager.Room;
import neutrino.RoomManager.CommandHandler;
import org.jboss.netty.channel.Channel;
/**
 *
 * @author cb2z8eb
 */
public class Talk extends Handler implements Runnable {
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
        
        String Message = Client.in.readUTF();
        CommandHandler C = new CommandHandler(Message, Socket);
        if(Message.startsWith(":") && CurrentUser.RankLevel > 4 && C.ThereIsACommand())
        {
            return;
        }
        
        ServerMessage Talk = new ServerMessage(ServerEvents.Talk);
        Talk.writeInt(CurrentUser.SessionId);
        Talk.writeUTF(Message);
        Talk.writeInt(0);
        Talk.writeInt(0);
        Talk.writeInt(0);
        Talk.Send(Socket);
        } catch (Exception e)
        {
            
        }
        
    }
    
}
