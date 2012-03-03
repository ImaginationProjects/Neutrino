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
public class EnterOnRoom extends Handler implements Runnable {
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
        
        if(CurrentUser.IsOnRoom)
        {
            Room RoomData = Room.Rooms.get(CurrentUser.CurrentRoomId);
            RoomData.UserList.remove(CurrentUser);
            RoomData.CurrentUsers--;
        }
        
        int RoomId = Client.in.readInt();
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        ServerMessage Init = new ServerMessage(ServerEvents.InitRoomProcess);
        Init.Send(Socket);
        
        ServerMessage Model = new ServerMessage(ServerEvents.RoomData1);
        Model.writeUTF(R.Model);
        Model.writeInt(RoomId);
        Model.Send(Socket);
        
        // send papers
        if(R.Wall != 0)
        {
            ServerMessage Papers = new ServerMessage(ServerEvents.Papers);
            Papers.writeUTF("wallpaper");
            Papers.writeUTF(((Integer)(R.Wall)).toString());
            Papers.Send(Socket);
        }
        
        if(R.Floor != 0)
        {
            ServerMessage Papers = new ServerMessage(ServerEvents.Papers);
            Papers.writeUTF("floor");
            Papers.writeUTF(((Integer)(R.Floor)).toString());
            Papers.Send(Socket);
        }
        
        ServerMessage Papers = new ServerMessage(ServerEvents.Papers);
        Papers.writeUTF("landscape");
        Papers.writeUTF(((Double)(R.Landscape)).toString());
        Papers.Send(Socket);
        
        ServerMessage SomeData = new ServerMessage(ServerEvents.RoomData2);
        SomeData.writeInt(4);
        SomeData.Send(Socket);
        
        ServerMessage SomeData2 = new ServerMessage(ServerEvents.RoomData3);
        SomeData2.Send(Socket);
        
        ServerMessage MoreData = new ServerMessage(ServerEvents.RoomData4);
        MoreData.writeInt(0);
        MoreData.writeBoolean(false);
        MoreData.Send(Socket);
        
        ServerMessage RoomEvents = new ServerMessage(ServerEvents.RoomEvents);
        RoomEvents.writeUTF("-1");
        RoomEvents.Send(Socket);
        
        ServerMessage WallItems = new ServerMessage(ServerEvents.sWallItems);
        WallItems.writeInt(0);
        WallItems.Send(Socket);
        
        ServerMessage FloorItems = new ServerMessage(ServerEvents.sFloorItems);
        FloorItems.writeInt(0);
        FloorItems.Send(Socket);
        
        CurrentUser.IsOnRoom = true;
        CurrentUser.CurrentRoomId = RoomId;
        } catch (Exception e)
        {
            
        }
       
    }    
}
