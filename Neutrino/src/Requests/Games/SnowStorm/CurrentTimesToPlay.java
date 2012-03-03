package Requests.Games.SnowStorm;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.ItemManager.UserItem;
/**
 *
 * @author Julián
 */
public class CurrentTimesToPlay extends Handler implements Runnable {
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
        	ServerMessage Plays = new ServerMessage(ServerEvents.CurrentSnowStormPlays);
        	Plays.writeInt(-1); // full play, anyway, 3 = 3 plays an more idiot things
        	Plays.Send(Client.Socket);
        } catch (Exception e)
        {
        	
        }
    }
}
