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
import org.jboss.netty.channel.Channel;
/**
 *
 * @author cb2z8eb
 */
public class SerializeRoomData extends Handler implements Runnable {
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
        
        ServerMessage RoomData = new ServerMessage(ServerEvents.SerializeRoomData);
        RoomData.writeBoolean(true);
        RoomData.writeInt(RoomId);
        RoomData.writeBoolean(false);
        RoomData.writeUTF(R.Name);
        RoomData.writeInt(R.OwnerId);
        RoomData.writeUTF(R.OwnerName);
        RoomData.writeInt(0);
        RoomData.writeInt(R.CurrentUsers);
        RoomData.writeInt(R.MaxUsers);
        RoomData.writeUTF(R.Description);
        RoomData.writeInt(0);
        RoomData.writeInt((R.Category == 3) ? 2 : 0);
        RoomData.writeInt(R.Score);
        RoomData.writeInt(R.Category);
        RoomData.writeUTF("");
        RoomData.writeInt(0);
        RoomData.writeInt(0);
        RoomData.writeInt(R.Tags.size());
        // Icons
        RoomData.writeInt(0);
        RoomData.writeInt(0);
        RoomData.writeInt(0);
        RoomData.Send(Socket);
        } catch (Exception e)
        {
            
        }
        
    }
}
