package Requests.Games.SnowStorm;

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
public class TalkOnSnow extends Handler implements Runnable {
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
        
        ServerMessage Talk = new ServerMessage(ServerEvents.TalkOnSnow);
        Talk.writeInt(CurrentUser.Id);
        Talk.writeUTF(Message);
        Talk.writeInt(0);
        Talk.Send(Socket);
        } catch (Exception e)
        {
            
        }
        
    }
    
}
