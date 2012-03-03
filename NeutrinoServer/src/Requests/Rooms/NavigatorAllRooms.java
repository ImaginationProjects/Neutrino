/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Requests.Rooms;
import java.util.*;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
/**
 *
 * @author Julián
 */
public class NavigatorAllRooms extends Handler implements Runnable {
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
        Habbo CurrentHabbo = Client.GetSession();
        ServerMessage MyRooms = new ServerMessage(ServerEvents.LookRooms);
        MyRooms.writeInt(5);
        MyRooms.writeUTF("");
        List<Room> mRooms = Room.AllRooms;
        MyRooms.writeInt(mRooms.size());
        Iterator reader = mRooms.iterator();
        while(reader.hasNext())
        {
            Room R = (Room)reader.next();
            MyRooms.writeInt(R.Id); // room id
            MyRooms.writeBoolean(false); // event
            MyRooms.writeUTF(R.Name);
            MyRooms.writeInt(R.OwnerId);
            MyRooms.writeUTF(R.OwnerName);
            MyRooms.writeInt(0);
            MyRooms.writeInt(R.CurrentUsers);
            MyRooms.writeInt(R.MaxUsers);
            MyRooms.writeUTF(R.Description);
            MyRooms.writeInt(0);
            MyRooms.writeInt((R.Category == 3) ? 2 : 0);
            MyRooms.writeInt(R.Score);
            MyRooms.writeInt(R.Category);
            MyRooms.writeInt(0);
            MyRooms.writeInt(0);
            MyRooms.writeInt(R.Tags.size());
            // Icons
            MyRooms.writeInt(0);
            MyRooms.writeInt(0);
            MyRooms.writeInt(0);
            // bools
            MyRooms.writeUTF("");
            MyRooms.writeBoolean(true);
            MyRooms.writeBoolean(true);
        }
        MyRooms.writeBoolean(false);
        MyRooms.Send(Client.Socket);
        } catch (Exception e)
        {
            
        }
        
    }    
}
