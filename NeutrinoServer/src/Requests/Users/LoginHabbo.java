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
import neutrino.UserManager.SubscriptionManager;
import neutrino.UserManager.UserManager;
import org.jboss.netty.channel.Channel;
/**
 *
 * @author Julián
 */
public class LoginHabbo extends Handler implements Runnable {
    private ServerHandler Client;
    private Environment Server;
    private FutureTask T;
    public void run() 
    {
        try {
        // Login Me!
        Channel Socket = Client.Socket;
        Habbo CurrentHabbo = Client.GetSession();
        CurrentHabbo.CurrentSocket = Socket;
        CurrentHabbo.IsOnline = true;
        
        ServerMessage Initial = new ServerMessage(ServerEvents.InitSystem);
        Initial.Send(Socket); 
        
        ServerMessage SetData = new ServerMessage(ServerEvents.InitData);
        SetData.writeInt(0);
        SetData.Send(Socket);
        
        ServerMessage Home = new ServerMessage(ServerEvents.SetHome);
        Home.writeInt(CurrentHabbo.Home);
        Home.writeInt(CurrentHabbo.Home);
        Home.Send(Socket);
        
        SubscriptionManager Sub = new SubscriptionManager(CurrentHabbo, Server);
        if(Sub.HasSubscription("habbo_vip"))
        {
            //ServerMessage Gifts = new ServerMessage(ServerEvents.Gifts);
            //Gifts.writeInt(0);
            //Gifts.Send(Socket);
        }
        
        ServerMessage Favs = new ServerMessage(ServerEvents.SetFavs);
        Favs.writeInt(30);
        Favs.writeInt(0);
        Favs.Send(Socket);
        
        ServerMessage FuseRights = new ServerMessage(ServerEvents.FuseRights);
        if(Sub.HasSubscription("habbo_vip"))
            FuseRights.writeInt(2); // HC/VIP/SOMESHIT???? 
        else
            FuseRights.writeInt(0); // same
        FuseRights.writeInt(CurrentHabbo.RankLevel); // Haha admin level
        FuseRights.Send(Socket);
        
        ServerMessage MoreData = new ServerMessage(ServerEvents.MoreData);
        MoreData.writeBoolean(true);
        MoreData.writeBoolean(false);
        MoreData.Send(Socket);
        
        ServerMessage UnData = new ServerMessage(ServerEvents.UnData);
        UnData.writeBoolean(false);
        UnData.Send(Socket);
        
        ServerMessage Pixels = new ServerMessage(ServerEvents.Pixels);
        Pixels.writeInt(2); // Count
        Pixels.writeInt(0); // TYPE = PIXELS
        Pixels.writeInt(CurrentHabbo.Pixels); // Count of type (pixels)
        Pixels.writeInt(1); // TYPE = SNOWFLAKES
        Pixels.writeInt(CurrentHabbo.SnowFlakes); // blabla...
        Pixels.Send(Socket);
        
        ServerMessage Snow = new ServerMessage(ServerEvents.Snow);
        Snow.writeBoolean(true);
        Snow.Send(Socket);
        
        ServerMessage GameState = new ServerMessage(ServerEvents.GameState);
        GameState.writeInt(2762); // state
        GameState.writeInt(20); // don't know
        GameState.writeInt(0); // timeleft
        GameState.Send(Socket);
        
        ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
        Alert.writeUTF("Hola " + CurrentHabbo.UserName + ", ¡bienvenido a Habbo.ht!, ¿Qué tal estás hoy?");
        Alert.writeUTF("");
        Alert.Send(Socket);
        
        ServerMessage Categorys = new ServerMessage(432);
        Categorys.writeInt(2);
        Categorys.writeInt(1);
        Categorys.writeUTF("Salas del Staff");
        Categorys.writeUTF("");
        Categorys.writeInt(1);
        Categorys.writeUTF("");
        Categorys.writeUTF("officialrooms_hq/staffpickfolder.gif");
        Categorys.writeInt(0);
        Categorys.writeInt(0);
        Categorys.writeInt(4);
        Categorys.writeBoolean(false);
        Categorys.writeInt(1);
        Categorys.writeUTF("este es diferente?");
        Categorys.writeUTF("");
        Categorys.writeInt(1);
        Categorys.writeUTF("");
        Categorys.writeUTF("officialrooms_hq/staffpickfolder.gif");
        Categorys.writeInt(0);
        Categorys.writeInt(0);
        Categorys.writeInt(4);
        Categorys.writeBoolean(false);
        Categorys.writeInt(-1);
        Categorys.Send(Socket);
        
        if(CurrentHabbo.RankLevel >= 4)
        {
            ServerMessage MOD_TOOLS = new ServerMessage(ServerEvents.ModTools);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeInt(0);
          //  MOD_TOOLS.Send(Socket);
        }
        

        // 3672
        // 1946
        ServerMessage lol = new ServerMessage(401);
        lol.Send(Socket);
        
        Environment.UsersConnected++;
        CurrentHabbo.CurrentSocket = Client.Socket;
        CurrentHabbo.SessionId = Environment.SessionIds;
        Environment.SessionIds++;
        UserManager.VirtualUsers.add(CurrentHabbo);
        UserManager.VirtualUsersById.put(CurrentHabbo.Id, CurrentHabbo);
        Server.WriteLine(CurrentHabbo.UserName + " has logged in on IP: " + CurrentHabbo.Authenticator);
        } catch (Exception e) {
            
        }
    }
    
    @Override
    public void Load(ServerHandler Client, Environment Server, FutureTask T) throws Exception
    {
        this.Client = Client;
        this.Server = Server;
        this.T = T;
        /*
        // Login Me!
        Channel Socket = Client.Socket;
        Habbo CurrentHabbo = Client.GetSession();
        
        ServerMessage Initial = new ServerMessage(ServerEvents.InitSystem);
        Initial.Send(Socket); 
        
        ServerMessage SetData = new ServerMessage(ServerEvents.InitData);
        SetData.writeInt(0);
        SetData.Send(Socket);
        
        ServerMessage Home = new ServerMessage(ServerEvents.SetHome);
        Home.writeInt(CurrentHabbo.Home);
        Home.writeInt(CurrentHabbo.Home);
        Home.Send(Socket);
        
        SubscriptionManager Sub = new SubscriptionManager(CurrentHabbo, Server);
        if(Sub.HasSubscription("habbo_vip"))
        {
            //ServerMessage Gifts = new ServerMessage(ServerEvents.Gifts);
            //Gifts.writeInt(0);
            //Gifts.Send(Socket);
        }
        
        ServerMessage Favs = new ServerMessage(ServerEvents.SetFavs);
        Favs.writeInt(30);
        Favs.writeInt(0);
        Favs.Send(Socket);
        
        ServerMessage SomeData = new ServerMessage(ServerEvents.InventoryWarn); // new item on inventory purchased
        SomeData.writeInt(2);
        SomeData.writeInt(1);
        SomeData.writeInt(1);
        SomeData.writeInt(280635963);
        SomeData.writeInt(4);
        SomeData.writeInt(5);
        SomeData.writeInt(497);
        SomeData.writeInt(506);
        SomeData.writeInt(710);
        SomeData.writeInt(184);
        SomeData.writeInt(213);
        //SomeData.Send(Socket);
        
        
        ServerMessage FuseRights = new ServerMessage(ServerEvents.FuseRights);
        if(Sub.HasSubscription("habbo_vip"))
            FuseRights.writeInt(2); // HC/VIP/SOMESHIT???? 
        else
            FuseRights.writeInt(0); // same
        FuseRights.writeInt(CurrentHabbo.RankLevel); // Haha admin level
        FuseRights.Send(Socket);
        
        ServerMessage MoreData = new ServerMessage(ServerEvents.MoreData);
        MoreData.writeBoolean(true);
        MoreData.writeBoolean(false);
        MoreData.Send(Socket);
        
        ServerMessage UnData = new ServerMessage(ServerEvents.UnData);
        UnData.writeBoolean(false);
        UnData.Send(Socket);
        
        ServerMessage Pixels = new ServerMessage(ServerEvents.Pixels);
        Pixels.writeInt(2); // Count
        Pixels.writeInt(0); // TYPE = PIXELS
        Pixels.writeInt(CurrentHabbo.Pixels); // Count of type (pixels)
        Pixels.writeInt(1); // TYPE = SNOWFLAKES
        Pixels.writeInt(CurrentHabbo.SnowFlakes); // blabla...
        Pixels.Send(Socket);
        
        ServerMessage Snow = new ServerMessage(ServerEvents.Snow);
        Snow.writeBoolean(true);
        Snow.Send(Socket);
        
        ServerMessage GameState = new ServerMessage(ServerEvents.GameState);
        GameState.writeInt(2762); // state
        GameState.writeInt(20); // don't know
        GameState.writeInt(0); // timeleft
        //GameState.Send(Socket);
        
        ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
        Alert.writeUTF("Hola " + CurrentHabbo.UserName + ", Â¡bienvenido a Habbo.ht!, Â¿quÃ© tal estÃ¡s hoy?");
        Alert.writeUTF("");
        Alert.Send(Socket);
        
        ServerMessage Categorys = new ServerMessage(3102);
        Categorys.writeInt(2);
        Categorys.writeInt(1);
        Categorys.writeUTF("Salas del Staff");
        Categorys.writeUTF("");
        Categorys.writeInt(1);
        Categorys.writeUTF("");
        Categorys.writeUTF("officialrooms_hq/staffpickfolder.gif");
        Categorys.writeInt(0);
        Categorys.writeInt(0);
        Categorys.writeInt(4);
        Categorys.writeBoolean(false);
        Categorys.writeInt(1);
        Categorys.writeUTF("este es diferente?");
        Categorys.writeUTF("");
        Categorys.writeInt(1);
        Categorys.writeUTF("");
        Categorys.writeUTF("officialrooms_hq/staffpickfolder.gif");
        Categorys.writeInt(0);
        Categorys.writeInt(0);
        Categorys.writeInt(4);
        Categorys.writeBoolean(false);
        Categorys.writeInt(-1);
        Categorys.Send(Socket);
        
        if(CurrentHabbo.RankLevel >= 4)
        {
            ServerMessage MOD_TOOLS = new ServerMessage(ServerEvents.ModTools);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.Send(Socket);
        }
        CurrentHabbo.SessionId = Environment.SessionIds;
        Environment.SessionIds++;
        Server.WriteLine(CurrentHabbo.UserName + " has logged in on IP: " + CurrentHabbo.Authenticator);*/
    }    
}
