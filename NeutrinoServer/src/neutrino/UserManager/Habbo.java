/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.UserManager;
import neutrino.Environment;
import neutrino.RoomManager.Coord;
import neutrino.RoomManager.Room;

import java.util.*;
import java.sql.*;
import java.util.concurrent.FutureTask;

import neutrino.CatalogManager.CatalogItem;
import neutrino.CatalogManager.CatalogPage;
import neutrino.Network.*;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.PacketsInformation.ClientEvents;
import org.jboss.netty.channel.Channel;
import neutrino.System.ServerMessage;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.SnowWarManager.SnowWar;;
/**
 *
 * @author Julián
 */
public class Habbo {
    public static Map<Integer, Habbo> UsersbyId;
    public static int LastId;
    public int Id;
    public int RidingAHorseId = 0;
    public int EffectId = 0;
    public Environment Server;
    public ServerHandler CurrentClient;
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
    public String Z;
    public int Rot;
    public int GoalX;
    public int GoalY;
    public String State;
    public boolean IsWalking;
    public Channel CurrentSocket;
    public boolean IsOnline;
    public boolean IsOnRoom;
    public boolean IsSit;
    public SnowWar CurrentWar;
    public int SnowWarScore;
    public int SnowWarWeeklyScore;
    public int ReceivedFriends;
    public boolean IsPlayingSnow = false;
    public boolean IsTeleportOn;
    public List<Integer> UsersReadedWithPows;
    public Effects CurrentEffect;
    public int LookingTo;
    public int TradingWith = 0;
    public List<Integer> TradingItems;
    public boolean TradeAccepted;
    public boolean TradeConfirmed;
    public boolean IsWaitingForDoorbell = false;
    public Map<Integer, Integer> BannedForId;
    public int GetCurrentEffect()
    {
    	return EffectNumber.GetEffectNum(CurrentEffect);
    }
    
    public SmileStates CurrentSmile;
    public int GetCurrentSmile()
    {
    	return SmileNumber.GetSmileNum(CurrentSmile);
    }
    
    public Dances CurrentDance;
    public int GetCurrentDance()
    {
    	return DanceNumber.GetDanceNum(CurrentDance);
    }
    
    public static void InitUsers(Environment Server) throws Exception
    {
        UsersbyId = new HashMap<Integer, Habbo>();
        LastId = 0;
        ResultSet Users = Server.GetDatabase().executeQuery("SELECT * FROM users");
        while(Users.next())
        {
            Habbo U = new Habbo();
            U.Id = Users.getInt("id");
            if(LastId < U.Id)
            	LastId = U.Id;
            U.UserName = Users.getString("username");
            U.Look = Users.getString("look");
            U.Gender = Users.getString("gender");
            U.Motto = Users.getString("motto");
            U.Authenticator = Users.getString("authenticator");
            U.Credits = Users.getInt("credits");
            U.Pixels = Users.getInt("pixels");
            U.SnowFlakes = Users.getInt("snowflakes");
            U.Home = Users.getInt("home");
            String Rank = Users.getString("level");
            U.RankLevel = 1;
            U.UsersReadedWithPows = new ArrayList<Integer>();
            if(Rank.equals("BANNED"))
            	U.RankLevel = 0;
            else if(Rank.equals("GUIDE"))
            	U.RankLevel = 2;
            else if(Rank.equals("XTREME"))
            	U.RankLevel = 3;
            else if(Rank.equals("LINCE"))
            	U.RankLevel = 4;
            else if(Rank.equals("MODERATOR"))
            	U.RankLevel = 5;
            else if(Rank.equals("HEAD MODERATOR"))
            	U.RankLevel = 6;
            else if(Rank.equals("MANAGER"))
            	U.RankLevel = 7;
            else if(Rank.equals("ADMINISTRATOR"))
            	U.RankLevel = 8;
            U.SnowWarScore = Users.getInt("snowwar_points");
            U.SnowWarWeeklyScore = Users.getInt("snowwar_weeklypoints");
            U.ReceivedFriends = 0;
            U.State = "";
            U.IsOnline = false;
            U.CurrentSocket = null;
            U.CurrentClient = null;
            U.CurrentRoomId = 0;
            U.IsOnRoom = false;
            U.IsTeleportOn = false;
            U.CurrentWar = null;
            U.IsSit = false;
            U.Z = "0.0";
            U.CurrentDance = Dances.NONE;
            U.CurrentEffect = Effects.NONE;
            U.CurrentSmile = SmileStates.NONE;
            U.BannedForId = new HashMap<Integer, Integer>();
            U.LookingTo = 0;
            U.TradingItems = new ArrayList<Integer>();
            U.TradeAccepted = false;
            U.TradeConfirmed = false;
            UsersbyId.put(U.Id, U);
        }
        Server.WriteLine("Loaded " + UsersbyId.size() + " user(s).");
    }
    
    public static Habbo GetUserForIP(String IP)
    {
    	Habbo User = null;
    	Map<Integer, CatalogItem> SubPages = new HashMap<Integer, CatalogItem>();
        Iterator reader = UsersbyId.entrySet().iterator();
        while (reader.hasNext())
        {
            Habbo H = (Habbo)(((Map.Entry)reader.next()).getValue());
            if(H.Authenticator.equals(IP))
            	return H;
        }        
        return User;
    }
    
    public static List<Habbo> GetUsersOnline()
    {
    	List<Habbo> i = new ArrayList<Habbo>();
    	Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.IsOnline)
				i.add(nUser);
		}
		
		return i;
    }
    
    public static List OrderBySnowScore() {
    	Map unsortMap = UsersbyId;
        List list = new LinkedList(unsortMap.entrySet());

        //sort list based on comparator
        Collections.sort(list, new Comparator() {
             public int compare(Object o1, Object o2) {
                 Habbo e1 = (Habbo) ((Map.Entry) (o1)).getValue();
                 Habbo e2 = (Habbo) ((Map.Entry) (o2)).getValue();
	           return ((Integer)e2.SnowWarScore).compareTo(e1.SnowWarScore);
             }
	});

        //put sorted list into map again
	List sortedMap = new ArrayList();
	for (Iterator it = list.iterator(); it.hasNext();) {
	     Map.Entry entry = (Map.Entry)it.next();
	     sortedMap.add(entry.getValue());
	}
	return sortedMap;
   }
    
    public static List OrderBySnowWeeklyScore() {
    	Map unsortMap = UsersbyId;
        List list = new LinkedList(unsortMap.entrySet());

        //sort list based on comparator
        Collections.sort(list, new Comparator() {
             public int compare(Object o1, Object o2) {
                 Habbo e1 = (Habbo) ((Map.Entry) (o1)).getValue();
                 Habbo e2 = (Habbo) ((Map.Entry) (o2)).getValue();
	           return ((Integer)e2.SnowWarWeeklyScore).compareTo(e1.SnowWarWeeklyScore);
             }
	});

        //put sorted list into map again
	List sortedMap = new ArrayList();
	for (Iterator it = list.iterator(); it.hasNext();) {
	     Map.Entry entry = (Map.Entry)it.next();
	     sortedMap.add(entry.getValue());
	}
	return sortedMap;
   }
    
    public void UpdateStateForFriends() throws Exception
    {
    	ServerMessage UpdateState = new ServerMessage(ServerEvents.UpdateFriendState);
		UpdateState.writeInt(0);
		UpdateState.writeInt(1);
	    UpdateState.writeInt(0);
		UpdateState.writeInt(this.Id);
		UpdateState.writeUTF(this.UserName);
		UpdateState.writeInt(1);
		UpdateState.writeBoolean(this.IsOnline);
		UpdateState.writeBoolean(this.IsOnRoom);
		UpdateState.writeUTF(this.Look);
		UpdateState.writeInt(0); // category ID
		UpdateState.writeUTF(this.Motto);
    	UpdateState.writeInt(0);
    	UpdateState.writeInt(0);
    	UpdateState.writeInt(0);
    	UserManager.SendMessageToMyFriends(UpdateState, this);
    }
    
    public Room CurrentRoom()
    {
    	return Room.Rooms.get(this.CurrentRoomId);
    }

    public void SendAlert(String Message) throws Exception
    {
    	ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
        Alert.writeUTF(Message);
        Alert.writeUTF("");
        Alert.Send(this.CurrentSocket);
    }
    
    public void SendAlert(String Message, String Url) throws Exception
    {
    	ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
        Alert.writeUTF(Message);
        Alert.writeUTF(Url);
        Alert.Send(this.CurrentSocket);
    }
    
    public static void CreateUser(String Name)
    {
    	Habbo U = new Habbo();
        U.Id = (LastId + 1);
        LastId++;
        U.UserName = Name;
        U.UsersReadedWithPows = new ArrayList<Integer>();
        U.Look = "sh-3089-1336.hr-3048-61-1345.hd-180-7.ca-3225-84-93.ch-3030-110.ea-1405-62.lg-275-96";
        U.Gender = "M";
        U.Motto = ":D";
        U.IsTeleportOn = false;
            U.Authenticator = "";
        U.Credits = 500000;
        U.Pixels = 90000;
        U.SnowFlakes = 0;
        U.ReceivedFriends = 0;
        U.Home = 0;
        U.RankLevel = 1;
        U.State = "";
        U.IsOnline = false;
        U.CurrentSocket = null;
        U.CurrentRoomId = 0;
        U.IsOnRoom = false;
        U.CurrentWar = null;
        U.IsSit = false;
        U.CurrentClient = null;
        U.SnowWarScore = 0;
        U.SnowWarWeeklyScore = 0;
        U.Z = "0.0";
        U.CurrentDance = Dances.NONE;
        U.CurrentEffect = Effects.NONE;
        U.CurrentSmile = SmileStates.NONE;
        U.BannedForId = new HashMap<Integer, Integer>();
        U.LookingTo = 0;
        U.TradingItems = new ArrayList<Integer>();
        U.TradeAccepted = false;
        U.TradeConfirmed = false;
        UsersbyId.put(U.Id, U);
    }
    
    public void KickMe(String Why) throws Exception
    {
    	String gen = "o";
    	if(this.Gender.toLowerCase().equals("f"))
    		gen = "a";
    	
    	this.SendAlert("Has sido expulsad" + gen + " de la sala por la siguiente razon: " + Why);
    	this.KickMe(this.Id);
    }
    
    public void KickMe(int KickedBy) throws Exception
    {
    	String gen = "o";
    	if(this.Gender.toLowerCase().equals("f"))
    		gen = "a";
    	if(KickedBy != this.Id)
    		this.SendAlert("Has sido expulsad" + gen + " de la sala");
    	
    	
    	ServerMessage Leavin = new ServerMessage(ServerEvents.LeavingRoom);
    	Leavin.writeBoolean(false);
    	Leavin.Send(this.CurrentSocket);
    	
    	Room RoomData = Room.Rooms.get(this.CurrentRoomId);
        RoomData.UserList.remove(this);
        RoomData.CurrentUsers--;
        this.IsOnRoom = false;
        this.IsWalking = false;
        /*UserManager.RemoveFromPows(User.Id);
        User.UsersReadedWithPows = new ArrayList<Integer>();*/
        ServerMessage GetOut = new ServerMessage(ServerEvents.GetOutOfRoom);
        GetOut.writeUTF(this.SessionId + "");
        UserManager.SendMessageToUsersOnRoomId(this.CurrentRoomId, GetOut);
        this.SessionId = 0;
        
        if(this.RidingAHorseId != 0)
        {
	        Pet P = Pet.Pets.get(this.RidingAHorseId);
	        P.HaveUserOnMe = false;
        }
        
        this.EffectId = 0;
        this.RidingAHorseId = 0;
        this.CurrentRoomId = 0;
        
        ServerMessage Leave = new ServerMessage(ServerEvents.LeaveRoom);
    	Leave.Send(this.CurrentSocket);
    }
    
    public static Habbo GetUserForName(String Name)
    {
    	Habbo User = null;
    	Map<Integer, CatalogItem> SubPages = new HashMap<Integer, CatalogItem>();
        Iterator reader = UsersbyId.entrySet().iterator();
        while (reader.hasNext())
        {
            Habbo H = (Habbo)(((Map.Entry)reader.next()).getValue());
            if(H.UserName.equals(Name))
            	return H;
        }        
        return User;
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
        UpdateState.writeUTF(this.Z);
        UpdateState.writeInt(this.Rot);
        UpdateState.writeInt(this.Rot);
        UpdateState.writeUTF("/flatctrl 4/" + State +"//");
        UserManager.SendMessageToUsersOnRoomId(this.CurrentRoomId, UpdateState);
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
        return GetUserForIP(IP);
    }
}
