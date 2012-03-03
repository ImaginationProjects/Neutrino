package neutrino.UserManager;

import java.util.*;
import java.util.concurrent.TimeUnit;

import neutrino.Environment;
import neutrino.AdministrationManager.CallsForHelp;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomEvent;
import neutrino.SnowWarManager.SnowWar;
import neutrino.System.ServerMessage;

public class UserManager implements Runnable {
    // Neutrino's Control System: Luz
	public static int GestorsId = 3;
	public static Map<Integer, UserManager> GestorManager;
	public static UserManager GeneralGestor;
	public Environment Server;
	public int Id;
	public String Action;
	public Habbo CurrentHabbo;
	public int Seconds;
	public static void InitManager(Environment cServer)
	{
		// Init all
		GeneralGestor = new UserManager();
		GeneralGestor.Server = cServer;
		GestorManager = new HashMap<Integer, UserManager>();
	}
	
	public static UserManager GetGestor()
	{
		return GeneralGestor;
	}
	
	public void InitTimerForUser(Habbo User, int Seconds, int Interval, String Action)
	{
		if(User == null || Seconds <= 0 || Action == "")
			return;
		
		UserManager Gestor = new UserManager();
		//if (Action == "Wave")
	         Gestor.Action = Action;
		Gestor.Id = GestorsId;
		Gestor.Seconds = Seconds;
		Gestor.CurrentHabbo = User;
		GestorManager.put(GestorsId, Gestor);
		GestorsId++;
		long l = (long)Interval;
		ThreadPoolManager._myTimerPoolingThreads.scheduleAtFixedRate(Gestor, 0L, Interval, TimeUnit.MILLISECONDS);
	}
	
	public void run()
	{
		if(this.Seconds == 0)
		{
			GestorManager.remove(this.Id); 
			return;
		}
		
		GetGestor().Server.WriteLine(Action);
		this.Seconds--;
	}
	
	
	// Some ServerMessage things
	public static void SendMessageToAllUsers(ServerMessage Message) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.IsOnline)
				Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendMessageToAllStaffs(ServerMessage Message) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.IsOnline && nUser.RankLevel >= 5)
				Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendIssueToAMod(CallsForHelp C) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.IsOnline && nUser.RankLevel >= 5)
			{

				ServerMessage Issue = new ServerMessage(ServerEvents.MOD_ADDISSUE);
				Issue.writeInt(C.Id); // id
		        Issue.writeInt(C.State); // state 
		        Issue.writeInt(1); // cat id
		        Issue.writeInt(C.Category); // reported cat id
		        Issue.writeInt((Environment.getIntUnixTimestamp() - C.Timestamp)); // timestamp
		        Issue.writeInt(C.Priority); // priority
		        Issue.writeInt(C.ReporterId); // reporter id
		        Issue.writeUTF(Habbo.UsersbyId.get(C.ReporterId).UserName); // reporter name
		        Issue.writeInt(C.ReportedId); // reported id
		        Issue.writeUTF((Habbo.UsersbyId.containsKey(C.ReportedId)) ? Habbo.UsersbyId.get(C.ReportedId).UserName : "Usuario desconocido"); // reported name
		        Issue.writeInt(C.PickedBy); // mod id
		        Issue.writeUTF(""); // mod name
		        Issue.writeUTF(C.Message); // issue message
		        Issue.writeInt(C.RoomId); // room id
		        Room RoomData = Room.Rooms.get(C.RoomId);
		        Issue.writeUTF(RoomData.Name); // room name
		        Issue.writeInt(0); // room type: 0 private - 1 public
                // if private
                if(RoomEvent.GetEventForRoomId(C.RoomId)==null)
                	Issue.writeUTF("-1");
                else {
                	RoomEvent E = RoomEvent.GetEventForRoomId(C.RoomId);
                	Issue.writeUTF(E.OwnerId + "");
                    Issue.writeUTF(Habbo.UsersbyId.get(E.OwnerId).UserName);
                    Issue.writeUTF(E.RoomId + "");
                    Issue.writeInt(E.Category);
                    Issue.writeUTF(E.Title);
                    Issue.writeUTF(E.Description);
                    Issue.writeUTF(E.Created);
                    Issue.writeInt(E.Tags.size());
                    Iterator zreader = E.Tags.iterator();
                    while(zreader.hasNext())
                    {
                    	String tag = (String)zreader.next();
                    	Issue.writeUTF(tag);
                    }
                }
                Issue.writeInt(C.Category); // cat of room
                Issue.writeInt(0); // not defined
		        Issue.Send(nUser.CurrentSocket);
			}
		}
	}
	
	public static void SendMessageToAllSnowPlayers(SnowWar War, ServerMessage Message) throws Exception
	{
		Iterator reader = War.Players.entrySet().iterator();
		while (reader.hasNext())
        {
        	Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
 		    if(!(nUser.CurrentWar.equals(null)) && nUser.CurrentWar.Id == War.Id)
 		    	Message.Send(nUser.CurrentSocket);
        }
	}
	
	public static void SendMessageToAllUsersOnARoom(ServerMessage Message) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.IsOnline && nUser.IsOnRoom)
				Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendMessageToMyFriends(ServerMessage Message, Habbo User) throws Exception
	{
		List<Friend> FriendsList = Friend.SelectFriendsForId(User.Id);
	    Iterator reader = FriendsList.iterator();
	    while(reader.hasNext())
	    {
	    	Friend F = (Friend)reader.next();
	    	Habbo fUser = Habbo.UsersbyId.get(F.FriendId);
	    	if(fUser.IsOnline)
	    		Message.Send(fUser.CurrentSocket);
	    }
	}
	
	public static void SendMessageToUsersOnRoomId(int RoomId, ServerMessage Message) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.CurrentRoomId == RoomId && nUser.IsOnline && nUser.IsOnRoom)
			   Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendMessageToUsersOnRoomIdWithPows(int RoomId, ServerMessage Message) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Room R = Room.Rooms.get(RoomId);
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.CurrentRoomId == RoomId && nUser.IsOnline && nUser.IsOnRoom && R.HavePowers(nUser.Id))
			   Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendMessageToUsersOnRoomIdButMe(int RoomId, int UserId, ServerMessage Message) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.CurrentRoomId == RoomId && nUser.IsOnline && nUser.IsOnRoom && nUser.Id != UserId)
			   Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendTwoMessagesToUsersOnRoomId(int RoomId, ServerMessage Message, ServerMessage Message2) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.CurrentRoomId == RoomId && nUser.IsOnline && nUser.IsOnRoom)
			{
			   Message.Send(nUser.CurrentSocket);
			   Message2.Send(nUser.CurrentSocket);
			}
		}
	}
	
	public static void RemoveFromPows(int UserId) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.UsersReadedWithPows.contains(UserId))
				nUser.UsersReadedWithPows.remove(UserId);
		}
	}
	
	
	
	public static void SendMessageToUsersOnRoomIdByUserList(int RoomId, ServerMessage Message) throws Exception
	{
		Iterator reader = Room.Rooms.get(RoomId).UserList.iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)reader.next();
			if(nUser.CurrentRoomId == RoomId && nUser.IsOnline && nUser.IsOnRoom)
			   Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendMessageToAllUsersButMe(Habbo User, ServerMessage Message) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.Id != User.Id && nUser.IsOnline)
				Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendMessageToAUser(Habbo User, ServerMessage Message) throws Exception
	{
		Iterator reader = Habbo.UsersbyId.entrySet().iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)(((Map.Entry)reader.next()).getValue());
			if(nUser.Id == User.Id && nUser.IsOnline)
				Message.Send(nUser.CurrentSocket);
		}
	}
	
	

}
