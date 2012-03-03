/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.UserManager;
import neutrino.Environment;
import neutrino.RoomManager.Coord;
import java.util.*;
import java.sql.*;
import java.util.concurrent.FutureTask;
import neutrino.Network.*;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.PacketsInformation.ClientEvents;
import org.jboss.netty.channel.Channel;
import neutrino.System.ServerMessage;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.SnowWarManager.SnowWar;;
/**
 *
 * @author Julián
 */
public class Habbo {
	public static List<Habbo> Users;
    public static Map<Integer, Habbo> UsersbyId;
    public static Map<String, Habbo> UsersLogged;
    public int Id;
    public String UserName;
    public String Look;
    public String Gender;
    public String Motto;
    public String Authenticator;
    public int Credits;
    public int Pixels;
    public int SnowFlakes;
    public int Home;
    public int RankLevel;
    public int CurrentRoomId;
    public int SessionId;
    public int X;
    public int Y;
    public int Rot;
    public int GoalX;
    public int GoalY;
    public String State;
    public boolean IsWalking;
    public Channel CurrentSocket;
    public boolean IsOnline;
    public boolean IsOnRoom;
    public SnowWar CurrentWar;
    public boolean MustCut;
    public List<Coord> Path;
    public static void InitUsers(Environment Server) throws Exception
    {
        UsersLogged = new HashMap<String, Habbo>();
        UsersbyId = new HashMap<Integer, Habbo>();
        Users = new ArrayList<Habbo>();
        ResultSet Users = Server.GetDatabase().executeQuery("SELECT * FROM users");
        while(Users.next())
        {
            Habbo U = new Habbo();
            U.Id = Users.getInt("id");
            U.UserName = Users.getString("username");
            U.Look = Users.getString("look");
            U.Gender = Users.getString("gender");
            U.Motto = Users.getString("motto");
            /*if(U.UserName.equals("Itachi"))
            	U.Authenticator = "127.0.0.1";
            else*/
                U.Authenticator = Users.getString("authenticator");
            U.Credits = Users.getInt("credits");
            U.Pixels = Users.getInt("pixels");
            U.SnowFlakes = Users.getInt("snowflakes");
            U.Home = Users.getInt("home");
            U.RankLevel = Users.getInt("level");
            U.State = "";
            U.IsOnline = false;
            U.CurrentSocket = null;
            U.CurrentRoomId = 0;
            U.IsOnRoom = false;
            U.MustCut = false;
            U.CurrentWar = null;
            U.Path = new ArrayList<Coord>();
            UsersLogged.put(U.Authenticator, U);
            UsersbyId.put(U.Id, U);
            Habbo.Users.add(U);
        }
        Server.WriteLine("Loaded " + UsersLogged.size() + " user(s).");
    }
    
    public void UpdateCredits(int NewCredits, Channel Socket, Environment Server) throws Exception
    {
        this.Credits = NewCredits;
        Server.GetDatabase().executeUpdate("UPDATE users SET credits = " + NewCredits +" WHERE id = " + this.Id);
        
        ServerMessage Credits = new ServerMessage(ServerEvents.SendCredits);
        Credits.writeUTF(NewCredits + ".0");
        Credits.Send(Socket);
    }
    
    public void UpdatePixels(int NewPixels, Channel Socket, Environment Server) throws Exception
    {
        this.Pixels = NewPixels;
        Server.GetDatabase().executeUpdate("UPDATE users SET pixels = " + NewPixels +" WHERE id = " + this.Id);
        
        ServerMessage Pixels = new ServerMessage(ServerEvents.Pixels);
        Pixels.writeInt(1);
        Pixels.writeInt(0);
        Pixels.writeInt(this.Pixels);
        Pixels.Send(Socket);
    }
    
    public void UpdateState(String State, Channel Socket, Environment Server) throws Exception
    {
        this.State = "State";
        ServerMessage UpdateState = new ServerMessage(ServerEvents.UpdateState);
        UpdateState.writeInt(1); // don't know
        UpdateState.writeInt(this.SessionId);
        UpdateState.writeInt(this.X);
        UpdateState.writeInt(this.Y);
        UpdateState.writeUTF("0.0");
        UpdateState.writeInt(this.Rot);
        UpdateState.writeInt(this.Rot);
        UpdateState.writeUTF("/flatctrl 4/" + State +"//");
        UpdateState.Send(Socket);
    }
    
    public void UpdateFuserights(Channel Socket, Environment Server) throws Exception
    {
        SubscriptionManager Sub = new SubscriptionManager(this, Server);
        ServerMessage FuseRights = new ServerMessage(ServerEvents.FuseRights);
        if(Sub.HasSubscription("habbo_vip"))
            FuseRights.writeInt(2); // HC/VIP/SOMESHIT????
        else
            FuseRights.writeInt(0); // same
        FuseRights.writeInt(this.RankLevel); // Haha admin level
        FuseRights.Send(Socket);
    }
    
    public void Updateclub(Environment Server, ServerHandler Main) throws Exception
    {
        Server.Request[ClientEvents.LoadClub].Load(Main, Server, null);
        FutureTask T = new FutureTask((Runnable)Environment.Request[ClientEvents.LoadClub], null);
        Server.Request[ClientEvents.LoadClub].Load(Main, Server, T);
        ThreadPoolManager._myGeneralThreads.execute(T);
    }
    
    public static Habbo GetDataFromIP(String IP)
    {
        return UsersLogged.get(IP);
    }
}
