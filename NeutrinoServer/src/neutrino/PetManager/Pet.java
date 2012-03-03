package neutrino.PetManager;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.sql.*;

import org.jboss.netty.channel.Channel;

import com.sulake.habbo.communication.messages.outgoing.rooms.PetWalkMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.rooms.WalkMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.userinformation.LoadPetInventaryMessageComposer;

import neutrino.Environment;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.PathFinder;
import neutrino.RoomManager.PetPathFinder;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;

public class Pet implements Runnable {
	public int Id;
	public int UserId;
	public int Race;
	public String PetName;
	public int Type;
	public String Color;
	public int Experience;
	public int MaxExperience;
	public int Hungry;
	public int MaxHungry;
	public int Happiness;
	public int MaxHappiness;
	public int Level;
	public int CreatedStamp;
    public int RoomId;
	public boolean IsOnRoom;
	public int SessionId;
	public int X;
	public int Y;
	public String Z;
	public boolean IsDoingThings = false;
	public boolean IsWalking = false;
	public int GoalX;
	public int GoalY;
	public int Rot;
	public boolean HaveChair;
	public boolean HaveUserOnMe = false;
	
	public static Map<Integer, Pet> Pets;
	public static int LastId;
	private ScheduledFuture Future;
	private static int[] MaxLevels;
	
	public void Set(ScheduledFuture f)
	{
		this.Future = f;
	}
	
	public void AddExperience(int AddExperience) throws Exception
	{
		if(this.Experience >= 51900)
			return;
		
		this.Experience += AddExperience;
		Environment.returnDB().executeUpdate("UPDATE pets SET experience = '" + this.Experience + "' WHERE id = " + this.Id);
		
		ServerMessage AddExp = new ServerMessage(ServerEvents.AddExperience);
		AddExp.writeInt(this.Id);
		AddExp.writeInt(this.SessionId);
		AddExp.writeInt(AddExperience);
		UserManager.SendMessageToUsersOnRoomId(this.RoomId, AddExp);
		
		if(this.Experience > this.MaxExperience)
		{
			this.Level += 1;
			Environment.returnDB().executeUpdate("UPDATE pets SET level = '" + this.Level + "' WHERE id = " + this.Id);
			
			this.Talk("*He subido al nivel " + this.Level + "* ¡Cada día sé un poco más!");
		}
		
	}
	
	public void UpdateHappiness(int Add) throws Exception
	{
		this.Happiness += Add;
		Environment.returnDB().executeUpdate("UPDATE pets SET energy = '" + this.Happiness + "' WHERE id = " + this.Id);
	}
	
	public void UpdateHungry(int Add) throws Exception
	{
		this.Hungry += Add;
		Environment.returnDB().executeUpdate("UPDATE pets SET hungry = '" + this.Hungry + "' WHERE id = " + this.Id);
	}
	
	public void run()
	{
		try {
			if(this.RoomId == 0 || !Room.Rooms.containsKey(this.RoomId))
			{
				this.Future.cancel(true);	
				return;
			}
			Room R = Room.Rooms.get(this.RoomId);
			Habbo Owner = Habbo.UsersbyId.get(this.UserId);
			if(R.CurrentUsers == 0)
			{
				R.PetList.remove(this);
				this.Future.cancel(true);
				return;
			}
			if(R.OwnerId != this.UserId && Owner.CurrentRoomId != this.RoomId)
    		{
    			Pet PetData = this;
		        ServerMessage GetOut = new ServerMessage(ServerEvents.GetOutOfRoom);
		        GetOut.writeUTF(PetData.SessionId + "");
		        UserManager.SendMessageToUsersOnRoomId(PetData.RoomId, GetOut);
		        this.RoomId = 0;
		        this.IsOnRoom = false;
		        this.HaveUserOnMe = false;
		        Environment.returnDB().executeUpdate("UPDATE pets SET roomid = '0' WHERE id = " + this.Id);
		        if(Owner.IsOnline)
		        	LoadPetInventaryMessageComposer.Compose(Owner.CurrentClient, Owner, Owner.Server);
    		}
			int Mode = (new Random()).nextInt(10);
			if(Mode > 3 && Mode < 6)
			{
				String Gender = "o";
				if(Owner.Gender.equals("F"))
					Gender = "a";
				Map<Integer, String> ToSay = new HashMap<Integer, String>();
				ToSay.put(0, "*Sonreir*");
				ToSay.put(1, "¡Mi amo " + Owner.UserName + " se preocupa mucho por mí :)");
				ToSay.put(2, Owner.UserName + " es muy buen" + Gender + " conmigo.");
				ToSay.put(3, "*Yawn*");
				ToSay.put(4, "Nyananyanyannanyan!");
				ToSay.put(5, "*Reirse*");
				ToSay.put(6, "*Cantar*");
				ToSay.put(7, "Creo que hoy será un buen día :).");
				ToSay.put(8, "*Bostezar*");
				int Rand = (new Random()).nextInt(8);
				
				String Word = ToSay.get(Rand);
				this.Talk(Word);
			} else if(Mode >= 6 && !this.IsWalking && !this.HaveUserOnMe)
			{
				this.WalkTo((new Random()).nextInt(R.GetModel().MapSizeX), (new Random()).nextInt(R.GetModel().MapSizeY));
			} else if(1 < Mode || Mode <= 3)
			{
				//this.UpdateState("");
			} else if(Mode == 0)
			{
				// quit hungry and happiness
				this.UpdateHungry(-10);
				this.UpdateHappiness(10);
			}
		} catch (Exception e) {
			
		}
	}
	
	public void WalkTo(int GoalX, int GoalY) throws Exception
	{
		if(this.RoomId == 0 || !Room.Rooms.containsKey(this.RoomId))
		{
			return;
		}
		Room R = Room.Rooms.get(this.RoomId);
		Pet P = this;
		P.GoalX = GoalX;
		P.GoalY = GoalY;
		if(P.GoalX == P.X && P.GoalY == P.Y)
        	return;
        if(R.IsItemOnXY(GoalX, GoalY))
        	return;
		if(R.GetModel().Squares[P.X][P.Y] != neutrino.RoomManager.SquareState.WALKABLE)
		{
			P.X = R.GetModel().DoorX;
			P.Y = R.GetModel().DoorY;
		}
        P.IsWalking = true;
        PetPathFinder Finder = new PetPathFinder(R, P);
        if(Finder == null)
        {
        	P.UpdateState("");
            P.IsWalking = false;
        	return;
        }
        Iterator finalite = Finder.MakeFinder().iterator();
        PetWalkMessageComposer W = new PetWalkMessageComposer(P, finalite, Finder);
        ScheduledFuture future = ThreadPoolManager._myTimerPoolingThreads.scheduleAtFixedRate(W, 0L, 500L, TimeUnit.MILLISECONDS);
        W.Set(future);
	}
	
	 public void UpdateState(String State) throws Exception
	    {
	        ServerMessage UpdateState = new ServerMessage(ServerEvents.UpdateState);
	        UpdateState.writeInt(1); // don't know
	        UpdateState.writeInt(this.SessionId);
	        UpdateState.writeInt(this.X);
	        UpdateState.writeInt(this.Y);
	        UpdateState.writeUTF(this.Z);
	        UpdateState.writeInt(this.Rot);
	        UpdateState.writeInt(this.Rot);
	        UpdateState.writeUTF("/flatctrl 4/" + State +"//");
	        UserManager.SendMessageToUsersOnRoomId(this.RoomId, UpdateState);
	    }
	 
	 public void UpdateStateOfTwo(String State, String State2) throws Exception
	    {
	        ServerMessage UpdateState = new ServerMessage(ServerEvents.UpdateState);
	        UpdateState.writeInt(2); // count users
	        UpdateState.writeInt(this.SessionId);
	        UpdateState.writeInt(this.X);
	        UpdateState.writeInt(this.Y);
	        UpdateState.writeUTF(this.Z);
	        UpdateState.writeInt(this.Rot);
	        UpdateState.writeInt(this.Rot);
	        UpdateState.writeUTF("/flatctrl 4/" + State2 +"//");
	        Habbo Owner = Habbo.UsersbyId.get(this.UserId);
	        UpdateState.writeInt(Owner.SessionId);
	        UpdateState.writeInt(Owner.X);
	        UpdateState.writeInt(Owner.Y);
	        UpdateState.writeUTF(Owner.Z);
	        UpdateState.writeInt(Owner.Rot);
	        UpdateState.writeInt(Owner.Rot);
	        UpdateState.writeUTF("/flatctrl 4/" + State +"//");
	        UserManager.SendMessageToUsersOnRoomId(this.RoomId, UpdateState);	        
	    }
	 
	 public void UpdateOnlyOneState(String State) throws Exception
	    {
	        ServerMessage UpdateState = new ServerMessage(ServerEvents.UpdateState);
	        UpdateState.writeInt(1); // don't know
	        UpdateState.writeInt(this.SessionId);
	        UpdateState.writeInt(this.X);
	        UpdateState.writeInt(this.Y);
	        UpdateState.writeUTF(this.Z);
	        UpdateState.writeInt(this.Rot);
	        UpdateState.writeInt(this.Rot);
	        UpdateState.writeUTF("//" + State +"");
	        UserManager.SendMessageToUsersOnRoomId(this.RoomId, UpdateState);
	    }
	
	public void Talk(String Message) throws Exception
	{
		int PacketId = ServerEvents.Shout;
        //if(Type == "talk")
        	PacketId = ServerEvents.Talk;
       // else
        	//PacketId = ServerEvents.Shout;
        
        ServerMessage Talk = new ServerMessage(PacketId);
        Talk.writeInt(this.SessionId);
        Talk.writeUTF(Message);
        Talk.writeInt(0);
        Talk.writeInt(0);
        Talk.writeInt(0);
        UserManager.SendMessageToUsersOnRoomId(this.RoomId, Talk);
	}
	
	public void ReadCommand(String Command) throws Exception
	{
		if(this.Happiness > 20)
		{
			if(Command.toLowerCase().equals("siéntate") || Command.toLowerCase().equals("sientate"))
		    {
		    		this.UpdateOnlyOneState("sit");
		    		if(this.Level < 10)
		    			this.AddExperience(10);
		    		this.UpdateHappiness(-15);
		    } else if(Command.toLowerCase().equals("mover cola"))
		    {
		    	this.UpdateOnlyOneState("wag/gst sml");
	    		if(this.Level < 10)
	    			this.AddExperience(5);
	    		this.UpdateHappiness(-10);
		    } else if(Command.toLowerCase().equals("descansa"))
		    {
		    	this.UpdateOnlyOneState("eyb");
		    } else if(Command.toLowerCase().equals("come"))
		    {
		    	//
		    } else if(Command.toLowerCase().equals("bebe"))
		    {
		    } else if(Command.toLowerCase().equals("tumbate"))
		    {
		    	this.UpdateOnlyOneState("lay");
	    		if(this.Level < 10)
	    			this.AddExperience(15);
	    		this.UpdateHappiness(-10);
		    } else if(Command.toLowerCase().equals("hazte el muerto"))
		    {
		    	this.UpdateOnlyOneState("ded");
	    		if(this.Level < 10)
	    			this.AddExperience(15);
	    		this.UpdateHappiness(-10);
		    } else if(Command.toLowerCase().equals("ven aquí") || Command.toLowerCase().equals("ven aquí"))
		    {
		    	Habbo Owner = Habbo.UsersbyId.get(this.UserId);
		    	this.WalkTo(Owner.X, Owner.Y);
	    		if(this.Level < 10)
	    			this.AddExperience(15);
	    		this.UpdateHappiness(-10);
		    }
			
			// eyb
		} else {
			Map<Integer, String> ToSay = new HashMap<Integer, String>();
			ToSay.put(0, "*No me apetece*");
			ToSay.put(1, "*Negarme*");
			ToSay.put(2, "Como si no dijeras nada...");
			ToSay.put(3, "*Llorar*");
			ToSay.put(4, "Hoy no, ¡Mañana!");
			ToSay.put(5, "*Reirse de mi dueño*");
			ToSay.put(6, "*Quizás luego*");
			ToSay.put(7, "Que vale...");
			ToSay.put(8, "*Bostezar*");
			int Rand = (new Random()).nextInt(8);
			
			String Word = ToSay.get(Rand);
			this.Talk(Word);
		}
	}
	
	public static void Init(Environment Server) throws Exception
	{
		Pets = new HashMap<Integer, Pet>();
		LastId = 0;
		MaxLevels = new int[] { 0, 100, 200, 400, 600, 1000, 1300, 1800, 2400, 3200, 4300, 7200, 8500, 10100, 13300, 17500, 23000, 30200, 39600, 47200, 51900 };
		
		ResultSet petz = Server.GetDatabase().executeQuery("SELECT * FROM pets");
		while(petz.next())
		{
			Pet P = new Pet();
			P.Id = petz.getInt("id");
			if(LastId < P.Id)
				LastId = P.Id;
			P.UserId = petz.getInt("userid");
			P.Race = petz.getInt("race");
			P.PetName = petz.getString("petname");
			P.Type = petz.getInt("type");
			P.Color = petz.getString("color");
			P.Level = petz.getInt("level");
			P.Experience = petz.getInt("experience");
			P.MaxExperience = MaxLevels[P.Level];
			P.Hungry = petz.getInt("hungry");
			P.MaxHungry = (100 + (P.Level*20));
			P.Happiness = petz.getInt("energy");
			P.MaxHappiness = 100;
			P.CreatedStamp = petz.getInt("createdstamp");
			P.RoomId = petz.getInt("roomid");
			P.IsOnRoom = (P.RoomId != 0);
			P.HaveChair = (petz.getInt("havechair") == 1);
			P.SessionId = Environment.SessionIds;
			P.X = 0;
			P.Y = 0;
			P.Z = "0.0";
			Pets.put(P.Id, P);
		}
		
		Server.WriteLine("Loaded " + Pets.size() + " pet(s).");
	}
	
	public static void ReadCommand(int cUserId, String Command) throws Exception
	{
		Iterator reader = Pets.entrySet().iterator();
		while(reader.hasNext())
		{
			Pet P = (Pet)(((Map.Entry)reader.next()).getValue());
			if(P.IsOnRoom && P.UserId == cUserId && P.RoomId == Habbo.UsersbyId.get(cUserId).CurrentRoomId)
			{
				String[] sep = Command.split(" ");
				String Name = sep[0];
				String sCommand = Command.replace(Name + " ", "");
				if(P.PetName.equals(Name))
					P.ReadCommand(sCommand);
			}
		}
	}
	
	public static List<Pet> ConvertToInventory(int cUserId)
	{
		List<Pet> Inv = new ArrayList<Pet>();
		Iterator reader = Pets.entrySet().iterator();
		while(reader.hasNext())
		{
			Pet P = (Pet)(((Map.Entry)reader.next()).getValue());
			if(!P.IsOnRoom && P.UserId == cUserId)
				Inv.add(P);
		}
		return Inv;
	}
	
	public static List<Pet> GetPetsForRoomId(int cRoomId)
	{
		List<Pet> Inv = new ArrayList<Pet>();
		Iterator reader = Pets.entrySet().iterator();
		while(reader.hasNext())
		{
			Pet P = (Pet)(((Map.Entry)reader.next()).getValue());
			if(P.IsOnRoom && P.RoomId == cRoomId)
				Inv.add(P);
		}
		return Inv;
	}
	
	public static void AddPet(int xUserId, int xRace, String xName, int xType, String xColor)
	{
		Pet P = new Pet();
		P.Id = LastId + 1;
		LastId++;
		P.UserId = xUserId;
		P.Race = xRace;
		P.PetName = xName;
		P.Type = xType;
		P.Color = xColor;
		P.Level = 1;
		P.Experience = 0;
		P.MaxExperience = MaxLevels[1];
		P.Hungry = 120;
		P.MaxHungry = (100 + (P.Level*20));
		P.Happiness = 100;
		P.MaxHappiness = 100;
		P.CreatedStamp = Environment.getIntUnixTimestamp();
		P.RoomId = 0;
		P.IsOnRoom = false;
		P.X = 0;
		P.Y = 0;
		P.HaveChair = false;
		P.Z = "0.0";
		Pets.put(P.Id, P);
	}
}
