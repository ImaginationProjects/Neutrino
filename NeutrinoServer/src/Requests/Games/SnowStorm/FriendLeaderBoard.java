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
public class FriendLeaderBoard extends Handler implements Runnable {
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
        	ServerMessage Board = new ServerMessage(ServerEvents.FriendsLeaderBoard);
        	Board.writeInt(1); // count
        	Board.writeInt(User.Id);
        	Board.writeInt(85000);
        	Board.writeInt(1); // position?
        	Board.writeUTF(User.UserName);
        	Board.writeUTF(User.Look);
        	Board.writeUTF(User.Gender.toLowerCase());
        	Board.writeInt(1); // repeat position (if it's me)
        	Board.Send(Client.Socket);
        } catch (Exception e)
        {
        	
        }
    }
}
