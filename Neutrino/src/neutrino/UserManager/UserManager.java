package neutrino.UserManager;

import java.util.*;
import java.util.concurrent.TimeUnit;

import neutrino.Environment;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.System.ServerMessage;

public class UserManager implements Runnable {
    // Neutrino's Control System: Luz
	public static List<Habbo> VirtualUsers;
	public static int GestorsId = 3;
	public static Map<Integer, Habbo> VirtualUsersById;
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
		VirtualUsers = new ArrayList<Habbo>();
		VirtualUsersById = new HashMap<Integer, Habbo>();
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
		Iterator reader = VirtualUsers.iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)reader.next();
			Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendMessageToUsersOnRoomId(int RoomId, ServerMessage Message) throws Exception
	{
		Iterator reader = VirtualUsers.iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)reader.next();
			if(nUser.CurrentRoomId == RoomId)
			   Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendMessageToAllUsersButMe(Habbo User, ServerMessage Message) throws Exception
	{
		Iterator reader = VirtualUsers.iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)reader.next();
			if(nUser.Id != User.Id)
				Message.Send(nUser.CurrentSocket);
		}
	}
	
	public static void SendMessageToAUser(Habbo User, ServerMessage Message) throws Exception
	{
		Iterator reader = VirtualUsers.iterator();
		while(reader.hasNext())
		{
			Habbo nUser = (Habbo)reader.next();
			if(nUser.Id == User.Id)
				Message.Send(nUser.CurrentSocket);
		}
	}
	
	

}
