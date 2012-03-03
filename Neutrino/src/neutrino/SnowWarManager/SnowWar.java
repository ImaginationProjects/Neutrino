package neutrino.SnowWarManager;
import java.util.*;

import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;;

public class SnowWar {
	// time to do some awesome things with this, bro!
	public int Id;
	public int ArenaId;
	public int TeamCount;
	public String Owner;
	public List<Habbo> Players;
	public List<Habbo> BlueTeam;
	public List<Habbo> RedTeam;
	public List<Habbo> YellowTeam; // secondary team, maybe soon?
	public List<Habbo> GreenTeam; // secondary team, maybe soon?
	public int RestTime;
	public int CurrentPlayers;
	public int MaxPlayers;
	public Habbo UserMoreKO;
	public Habbo UserMoreHits;
	public boolean IsStarted;
	public boolean IsFinished;
	public static List<SnowWar> Wars;
	public static void InitSnowWarSystem()
	{
		// some things there?, soon
		Wars = new ArrayList<SnowWar>();
	}
	
	public static void SearchASnowWarForMe(Habbo User) throws Exception
	{
		// hmmm...
		boolean IsOnAWar = false;
		if(Wars.size() > 0)
		{
		Iterator reader = Wars.iterator();
		while(reader.hasNext())
		{
			SnowWar CurrentWar = (SnowWar)reader.next();
			if(!CurrentWar.IsStarted && CurrentWar.CurrentPlayers != 10)
			{
				IsOnAWar = true;
				// add to this snow war
				User.CurrentWar = CurrentWar;
				CurrentWar.Players.add(User);
				if(CurrentWar.BlueTeam.size() == 0 && CurrentWar.RedTeam.size() == 0)
				{
					CurrentWar.RedTeam.add(User); 
					CurrentWar.CurrentPlayers++;
				}
				else if(CurrentWar.RedTeam.size() == 5)
				{
					CurrentWar.BlueTeam.add(User);
					CurrentWar.CurrentPlayers++;
				}
				else if(CurrentWar.BlueTeam.size() == 5)
				{
					CurrentWar.RedTeam.add(User);
					CurrentWar.CurrentPlayers++;
				}
				else if(CurrentWar.RedTeam.size() > CurrentWar.BlueTeam.size())
				{
					CurrentWar.BlueTeam.add(User);
					CurrentWar.CurrentPlayers++;
				}
				else if(CurrentWar.RedTeam.size() < CurrentWar.BlueTeam.size())
				{
					CurrentWar.RedTeam.add(User);
					CurrentWar.CurrentPlayers++;
				}
			
			    ServerMessage AddUser = new ServerMessage(ServerEvents.AddUsersToGame);
        	    AddUser.writeInt(User.Id + (new Random()).nextInt(100));
        	    AddUser.writeUTF(User.UserName);
        	    AddUser.writeUTF(User.Look);
        	    AddUser.writeUTF(User.Gender.toLowerCase());
        	    AddUser.writeInt(-1);
        	    AddUser.writeInt(0); // stars
        	    AddUser.writeInt(0); // points
        	    AddUser.writeInt(10); // points for next level
        	    AddUser.writeBoolean(false);
        	    Iterator ureader = CurrentWar.Players.iterator();
        	    while(ureader.hasNext())
             	{
         		   Habbo cUser = (Habbo)ureader.next();
         		   System.out.println("Send packet to: " + User.SessionId + "; " + User.UserName);
         		   if(cUser.Id != User.Id)
         			   AddUser.Send(cUser.CurrentSocket);
        	    }
			}
		}
		}
		
		if(!IsOnAWar)
		{
			CreateANewWar(User);
		}
	}
	
	public static void CreateANewWar(Habbo User)
	{
		SnowWar S = new SnowWar();
		int count = 0;
		Iterator reader = Wars.iterator();
		while(reader.hasNext())
		{
			SnowWar CurrentWar = (SnowWar)reader.next();
			if(CurrentWar.Id > count)
				count = CurrentWar.Id;
		}
		S.Id = count + 1;
		S.ArenaId = 9; // soon (new Random()).nextInt(10);
		S.TeamCount = 2; // maybe random 2,4 soon?
		S.Owner = User.UserName;
		S.Players = new ArrayList<Habbo>();
		S.RedTeam = new ArrayList<Habbo>();
		S.BlueTeam = new ArrayList<Habbo>();
		S.YellowTeam = new ArrayList<Habbo>();
		S.GreenTeam = new ArrayList<Habbo>();
		S.RestTime = 0;
		S.CurrentPlayers = 0;
		S.MaxPlayers = 10; // for the moment
		S.UserMoreHits = null;
		S.UserMoreKO = null;
		S.IsStarted = false;
		S.IsFinished = false;
		S.RedTeam.add(User);
		S.Players.add(User);
		User.CurrentWar = S;
		Wars.add(S);
	}
}
