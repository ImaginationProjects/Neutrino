package neutrino.SnowWarManager;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import neutrino.CatalogManager.CatalogItem;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.RoomModel;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;

public class SnowWar {
	// time to do some awesome things with this, bro!
	public int Id;
	public int ArenaId;
	public int TeamCount;
	public String Owner;
	public Map<Integer, Habbo> Players;
	public Map<Integer, Habbo> BlueTeam;
	public Map<Integer, Habbo> RedTeam;
	public Map<Integer, Habbo> YellowTeam; // secondary team, maybe soon?
	public Map<Integer, Habbo> GreenTeam; // secondary team, maybe soon?
	public int RestTime;
	public boolean StartedCounter;
	public int CurrentPlayers;
	public int MaxPlayers;
	public Habbo UserMoreKO;
	public Habbo UserMoreHits;
	public boolean IsStarted;
	public boolean IsFinished;
	public static Map<Integer, SnowWar> Wars;
	public static void InitSnowWarSystem()
	{
		// some things there?, soon
		Wars = new HashMap<Integer, SnowWar>();
	}
	
	public static void SearchASnowWarForMe(Habbo cUser) throws Exception
	{
		// hmmm...
		Habbo User = Habbo.UsersbyId.get(cUser.Id);
		boolean IsOnAWar = false;
		if(Wars.size() > 0)
		{
			Iterator reader = Wars.entrySet().iterator();
	        while (reader.hasNext())
	        {
	        	SnowWar CurrentWar = (SnowWar)(((Map.Entry)reader.next()).getValue());
				if(!CurrentWar.IsStarted && CurrentWar.CurrentPlayers != 10)
				{
					IsOnAWar = true;
					// add to this snow war
					User.CurrentWar = CurrentWar;
					CurrentWar.Players.put(User.Id, User);
					if(CurrentWar.BlueTeam.size() == 0 && CurrentWar.RedTeam.size() == 0)
					{
						CurrentWar.RedTeam.put(User.Id, User); 
						CurrentWar.CurrentPlayers++;
					}
					else if(CurrentWar.RedTeam.size() == 5)
					{
						CurrentWar.BlueTeam.put(User.Id, User);
						CurrentWar.CurrentPlayers++;
					}
					else if(CurrentWar.BlueTeam.size() == 5)
					{
						CurrentWar.RedTeam.put(User.Id, User);
						CurrentWar.CurrentPlayers++;
					}
					else if(CurrentWar.RedTeam.size() > CurrentWar.BlueTeam.size())
					{
						CurrentWar.BlueTeam.put(User.Id, User);
						CurrentWar.CurrentPlayers++;
					}
					else if(CurrentWar.RedTeam.size() < CurrentWar.BlueTeam.size())
					{
						CurrentWar.RedTeam.put(User.Id, User);
						CurrentWar.CurrentPlayers++;
					}
				
				    ServerMessage AddUser = new ServerMessage(ServerEvents.AddUsersToGame);
	        	    AddUser.writeInt(User.Id);
	        	    AddUser.writeUTF(User.UserName);
	        	    AddUser.writeUTF(User.Look);
	        	    AddUser.writeUTF(User.Gender.toLowerCase());
	        	    AddUser.writeInt(-1);
	        	    AddUser.writeInt(0); // stars
	        	    AddUser.writeInt(0); // points
	        	    AddUser.writeInt(10); // points for next level
	        	    AddUser.writeBoolean(false);
	        	    Iterator reader2 = CurrentWar.Players.entrySet().iterator();
	    	        while (reader2.hasNext())
	    	        {
	    	        	Habbo xUser = (Habbo)(((Map.Entry)reader2.next()).getValue());
	         		    if(xUser.Id != User.Id)
	         			    AddUser.Send(xUser.CurrentSocket);
	        	    }
	    	        
	    	        if(CurrentWar.Players.size() > 1 && !CurrentWar.StartedCounter)
	    	        {
	    	        	CurrentWar.StartedCounter = true;
	    	        	CurrentWar.RestTime = 15;
	    	        	ServerMessage Counter = new ServerMessage(ServerEvents.GameStartCounter);
	    	        	Counter.writeInt(15); // Seconds to Begin
	    	        	UserManager.SendMessageToAllSnowPlayers(CurrentWar, Counter);
		    	        SnowWarCounter WarCounter = new SnowWarCounter();
		    	        WarCounter.GetData(CurrentWar);		                
		    			ScheduledFuture fut = ThreadPoolManager._myTimerPoolingThreads.scheduleAtFixedRate(WarCounter, 0L, 1L, TimeUnit.SECONDS);
		    			WarCounter.Set(fut);
		         		
	    	        } else if(CurrentWar.Players.size() > 1 && CurrentWar.StartedCounter)
	    	        {
	    	        	ServerMessage Counter = new ServerMessage(ServerEvents.GameStartCounter);
	    	        	Counter.writeInt(CurrentWar.RestTime); // Seconds to Begin
		        	    Counter.Send(User.CurrentSocket);	    	        	
	    	        }
			}
		}
		}
		
		if(!IsOnAWar)
		{
			CreateANewWar(User);
		}
	}
	
	public void GoToLobby(SnowWar CurrentWar) throws Exception
	{
		//Thread.sleep(5000);
		ServerMessage GoToLobby = new ServerMessage(ServerEvents.GameStarted);
		CurrentWar.IsStarted = true;
    	GoToLobby.writeInt(CurrentWar.Id); //
    	GoToLobby.writeUTF("SnowStorm level " + CurrentWar.ArenaId);
    	GoToLobby.writeInt(0);
    	GoToLobby.writeInt(CurrentWar.ArenaId); // level
    	GoToLobby.writeInt(CurrentWar.TeamCount);
    	GoToLobby.writeInt(CurrentWar.MaxPlayers);
    	GoToLobby.writeUTF(CurrentWar.Owner); // user name of room creator?
    	GoToLobby.writeInt(14);
    	GoToLobby.writeInt(CurrentWar.Players.size());
    	
    	Iterator reader2 = CurrentWar.BlueTeam.entrySet().iterator();
        while (reader2.hasNext())
        {
        	Habbo cUser = (Habbo)(((Map.Entry)reader2.next()).getValue());
        	cUser.IsPlayingSnow = true;
    	    GoToLobby.writeInt(cUser.Id);
    	    GoToLobby.writeUTF(cUser.UserName);
    	    GoToLobby.writeUTF(cUser.Look);
    	    GoToLobby.writeUTF(cUser.Gender.toLowerCase());
    	    GoToLobby.writeInt(1); // team
    	    GoToLobby.writeInt(0); // stars
    	    GoToLobby.writeInt(0); // points
    	    GoToLobby.writeInt(10); // points for next level
    	}
    	
    	Iterator reader3 = CurrentWar.RedTeam.entrySet().iterator();
        while (reader3.hasNext())
        {
        	Habbo cUser = (Habbo)(((Map.Entry)reader3.next()).getValue());    GoToLobby.writeInt(cUser.Id);
        	cUser.IsPlayingSnow = true;
    	    GoToLobby.writeUTF(cUser.UserName);
    	    GoToLobby.writeUTF(cUser.Look);
    	    GoToLobby.writeUTF(cUser.Gender.toLowerCase());
    	    GoToLobby.writeInt(2); // team
    	    GoToLobby.writeInt(9); // stars
    	    GoToLobby.writeInt(200000); // points
    	    GoToLobby.writeInt(20000000); // points for next level
    	}
        UserManager.SendMessageToAllSnowPlayers(CurrentWar, GoToLobby);
        
        ServerMessage StartGame = new ServerMessage(ServerEvents.StartSnowGame);
    	StartGame.writeInt(0);
    	StartGame.writeInt(CurrentWar.ArenaId); // arena id
    	StartGame.writeInt(2); // team count
    	StartGame.writeInt(1); // owner count or some idiot thing?
    	//Iterator reader4 = CurrentWar.Players.entrySet().iterator();
    	//while (reader4.hasNext())
        //{
        	Habbo zUser = Habbo.GetUserForName(CurrentWar.Owner);   
        	StartGame.writeInt(zUser.Id);
            StartGame.writeUTF(zUser.UserName);
            StartGame.writeUTF(zUser.Look);
            StartGame.writeUTF(zUser.Gender.toLowerCase());
            StartGame.writeInt((CurrentWar.RedTeam.containsKey(zUser.Id)) ? 2 : 1); // team
    	    
            //}
    	StartGame.writeInt(50); // ?
    	StartGame.writeInt(50); // ?
    	System.out.println(CurrentWar.Players.size() + "");
    	StartGame.writeUTF("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxx000000000000000xxxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxx00000000000000000xxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxx0000000000000000000xxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxx000000000000000000000xxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxx00000000000000000000000xxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxx0000000000000000000000000xxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxx000000000000000000000000000xxxxxxxxxxxxxxx" + (char)13 + "xxxxxxx00000000000000000000000000000xxxxxxxxxxxxxx" + (char)13 + "xxxxxx0000000000000000000000000000000xxxxxxxxxxxxx" + (char)13 + "xxxxx000000000000000000000000000000000xxxxxxxxxxxx" + (char)13 + "xxxxx0000000000000000000000000000000000xxxxxxxxxxx" + (char)13 + "xxxxx00000000000000000000000000000000000xxxxxxxxxx" + (char)13 + "xxxxx000000000000000000000000000000000000xxxxxxxxx" + (char)13 + "xxxxx0000000000000000000000000000000000000xxxxxxxx" + (char)13 + "xxxxx00000000000000000000000000000000000000xxxxxxx" + (char)13 + "xxxxx000000000000000000000000000000000000000xxxxxx" + (char)13 + "xxxxx0000000000000000000000000000000000000000xxxxx" + (char)13 + "0xxxx00000000000000000000000000000000000000000xxxx" + (char)13 + "xxxxx00000000000000000000000000000000000000000xxxx" + (char)13 + "xxxxx00000000000000000000000000000000000000000xxxx" + (char)13 + "xxxxx000000000000000000000000000000000000000000xxx" + (char)13 + "xxxxx000000000000000000000000000000000000000000xxx" + (char)13 + "xxxxx000000000000000000000000000000000000000000xxx" + (char)13 + "xxxxxx00000000000000000000000000000000000000000xxx" + (char)13 + "xxxxxxx0000000000000000000000000000000000000000xxx" + (char)13 + "xxxxxxxx0000000000000000000000000000000000000xxxxx" + (char)13 + "xxxxxxxxx00000000000000000000000000000000000xxxxxx" + (char)13 + "xxxxxxxxxx000000000000000000000000000000000xxxxxxx" + (char)13 + "xxxxxxxxxxx00000000000000000000000000000000xxxx0xx" + (char)13 + "xxxxxxxxxxxx0000000000000000000000000000000xxxxxxx" + (char)13 + "xxxxxxxxxxxxx00000000000000000000000000000xxxxxxxx" + (char)13 + "xxxxxxxxxxxxxx0000000000000000000000000000xxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxx00000000000000000000000000xxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxx0000000000000000000000000xxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxx00000000000000000000000xxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxx0000000000000000000000xxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxx00000000000000000000xxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxx000000000000000xxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxx0000000000000xxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxx00000000000xxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxxx0000000xxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + (char)13 + "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" + (char)13);
    	List<SnowWarItem> WarItems = SnowWarItem.GetItemsForArena(CurrentWar.ArenaId);
    	StartGame.writeInt(WarItems.size()); // count items
    	Iterator readeritems = WarItems.iterator();
    	while(readeritems.hasNext())
    	{
    		SnowWarItem W = (SnowWarItem)readeritems.next();
    		// serialize item
        	StartGame.writeUTF(W.ItemName); // name
        	StartGame.writeInt(W.Initial); // x
        	StartGame.writeInt(W.Y); // z
        	StartGame.writeInt(W.X); // y
        	StartGame.writeInt(W.Rot); // height
        	StartGame.writeInt(W.Height); // object id
        	StartGame.writeInt(W.SpriteId); // ?
        	StartGame.writeBoolean(W.Walkable); // walkable
        	StartGame.writeInt(0); // ...?
        	StartGame.writeInt(W.Information); // ?
        	StartGame.writeInt((W.HaveMoreThanOneParam) ? 1 : 0); // ?
        	if(W.HaveMoreThanOneParam)
        	{
        		StartGame.writeInt(W.Params.size()); // params
            	// Extra data of ads_background
        		Iterator readerforitems = SnowWarItem.sortByComparator(W.Params).entrySet().iterator();
                while (readerforitems.hasNext())
                {
                    UnsortMap P = (UnsortMap)(((Map.Entry)readerforitems.next()).getValue());
                    StartGame.writeUTF(P.Set);
                    StartGame.writeUTF(P.Value);
                }
            	/*StartGame.writeUTF("state");
            	StartGame.writeUTF("0");
            	StartGame.writeUTF("offsetZ");
            	StartGame.writeUTF("9920");
            	StartGame.writeUTF("offsetY");
            	StartGame.writeUTF("1520");
            	StartGame.writeUTF("imageUrl");
            	StartGame.writeUTF("http://images.voroko.net/c_images/DEV_tests/snst_bg_2_big.png");
            	StartGame.writeUTF("offsetX");
            	StartGame.writeUTF("-1070");*/
        	} else {
        		StartGame.writeUTF(W.OneParam);
        	}
    	}
    	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StartGame);
        
    	
         
        // All users entered on arena?
        Iterator reader6 = CurrentWar.BlueTeam.entrySet().iterator();
         while (reader6.hasNext())
         {
         	Habbo cUser = (Habbo)(((Map.Entry)reader6.next()).getValue());
         	ServerMessage UserInfo = new ServerMessage(ServerEvents.EnteredOnArena);
         	System.out.println(cUser.Id);
        	UserInfo.writeInt(cUser.Id);
        	UserInfo.writeUTF(cUser.UserName);
        	UserInfo.writeUTF(cUser.Look);
        	UserInfo.writeUTF(cUser.Gender.toLowerCase());
        	UserInfo.writeInt(1);
            UserManager.SendMessageToAllSnowPlayers(CurrentWar, UserInfo);
         }
         
         Iterator reader9 = CurrentWar.RedTeam.entrySet().iterator();
         while (reader9.hasNext())
         {
         	Habbo cUser = (Habbo)(((Map.Entry)reader9.next()).getValue());
         	ServerMessage UserInfo = new ServerMessage(ServerEvents.EnteredOnArena);
        	UserInfo.writeInt(cUser.Id);
        	UserInfo.writeUTF(cUser.UserName);
        	UserInfo.writeUTF(cUser.Look);
        	UserInfo.writeUTF(cUser.Gender.toLowerCase());
        	UserInfo.writeInt(2);
            UserManager.SendMessageToAllSnowPlayers(CurrentWar, UserInfo);
         }
        
        ServerMessage StageLoaded = new ServerMessage(ServerEvents.StageLoaded);
      	StageLoaded.writeInt(0); 
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StageLoaded);
      	
      	
      	ServerMessage StillLoad = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad.writeInt(0);
      	StillLoad.writeInt(0);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad);
      	
      	ServerMessage StillLoad2 = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad2.writeInt(11);
      	StillLoad2.writeInt(0);
      	//StillLoad2.writeInt(48775613);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad2);
      	
      	ServerMessage StillLoad3 = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad3.writeInt(11);
      	StillLoad3.writeInt(0);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad3);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad3);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad3);
      	
      	ServerMessage StillLoad4 = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad4.writeInt(22);
      	StillLoad4.writeInt(0);
      	//StillLoad4.writeInt(41524809);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad4);
      	
      	ServerMessage StillLoad5 = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad5.writeInt(22);
      	StillLoad5.writeInt(0);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad5);
      	
      	ServerMessage StillLoad6 = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad6.writeInt(33);
      	StillLoad6.writeInt(0);
      	//StillLoad6.writeInt(31198871);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad6);
      	
      	ServerMessage StillLoad7 = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad7.writeInt(33);
      	StillLoad7.writeInt(0);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad7);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad7);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad7);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad7);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad7);
      	
      	ServerMessage StillLoad7B = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad7B.writeInt(44);
      	StillLoad7B.writeInt(0);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad7B);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad7B);
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad7B);
      	
      	ServerMessage StillLoad8 = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad8.writeInt(44);
      	StillLoad8.writeInt(CurrentWar.RedTeam.size());
     	Iterator readeruserss = CurrentWar.RedTeam.entrySet().iterator();
     	while (readeruserss.hasNext())
        {
        	Habbo xUser = (Habbo)(((Map.Entry)readeruserss.next()).getValue());
        	StillLoad8.writeInt(xUser.Id);
        }
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad8);
      	
      	ServerMessage StillLoad9 = new ServerMessage(ServerEvents.StillLoading);
      	StillLoad9.writeInt(44);
      	StillLoad9.writeInt(CurrentWar.BlueTeam.size());
     	Iterator readerusersb = CurrentWar.BlueTeam.entrySet().iterator();
     	while (readerusersb.hasNext())
        {
        	Habbo xUser = (Habbo)(((Map.Entry)readerusersb.next()).getValue());
        	StillLoad9.writeInt(xUser.Id);
        }
      	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StillLoad9);
        
         Thread.sleep(9000);     	
     	//Thread.sleep(5000);
     	// load stage         
     	ServerMessage Arena = new ServerMessage(ServerEvents.LoadingSnowStage);
     	Arena.writeInt(0); // arena
     	Arena.writeUTF("snowwar_arena_0");
     	Arena.writeInt(0);
     	Arena.writeInt(CurrentWar.Players.size());    	// players?
     	Iterator reader5 = CurrentWar.RedTeam.entrySet().iterator();
        while (reader5.hasNext())
        {
        	Habbo xUser = (Habbo)(((Map.Entry)reader5.next()).getValue());
        	xUser.X = 20;
        	xUser.Y = 24;
        	Arena.writeInt(5); // type => human
        	Arena.writeInt(19); // id
        	Arena.writeInt(64000); // x
        	Arena.writeInt(64000); // y
        	Arena.writeInt(20); // currenttile (maybe x)
        	Arena.writeInt(24); // currenttile (maybe y)
        	Arena.writeInt(0); // dir/rot?
        	Arena.writeInt(5); // no idea?
        	Arena.writeInt(5); // h or some idiot thing? 
        	Arena.writeInt(0); // ?
        	Arena.writeInt(0); // ?
        	Arena.writeInt(0); // ?
        	Arena.writeInt(20); // currentitle again
        	Arena.writeInt(24); // currenttile again
        	Arena.writeInt(20); // x again
        	Arena.writeInt(24); // y again
         	Arena.writeInt(0); // score
         	Arena.writeInt(2); // team
         	Arena.writeInt(xUser.Id);
         	Arena.writeUTF(xUser.UserName);
         	Arena.writeUTF(xUser.Motto);
         	Arena.writeUTF(xUser.Look);
         	Arena.writeUTF(xUser.Gender);
         }
        Iterator reader60 = CurrentWar.BlueTeam.entrySet().iterator();
        while (reader60.hasNext())
        {
        	Habbo xUser = (Habbo)(((Map.Entry)reader60.next()).getValue());
        	xUser.X = 20;
        	xUser.Y = 20;
        	Arena.writeInt(5); // type => human
        	Arena.writeInt(19); // id
        	Arena.writeInt(64000); // x
        	Arena.writeInt(64000); // y
        	Arena.writeInt(20); // currenttile (maybe x)
        	Arena.writeInt(20); // currenttile (maybe y)
        	Arena.writeInt(0); // dir/rot?
        	Arena.writeInt(5); // no idea?
        	Arena.writeInt(5); // h or some idiot thing? 
        	Arena.writeInt(0); // ?
        	Arena.writeInt(0); // ?
        	Arena.writeInt(0); // ?
        	Arena.writeInt(20); // currentitle again
        	Arena.writeInt(20); // currenttile again
        	Arena.writeInt(20); // x again
        	Arena.writeInt(20); // y again
         	Arena.writeInt(0); // score
         	Arena.writeInt(2); // team
         	Arena.writeInt(xUser.Id);
         	Arena.writeUTF(xUser.UserName);
         	Arena.writeUTF(xUser.Motto);
         	Arena.writeUTF(xUser.Look);
         	Arena.writeUTF(xUser.Gender);
         }
        UserManager.SendMessageToAllSnowPlayers(CurrentWar, Arena);
     	Thread.sleep(5500);
        

        ServerMessage StageLoaded2 = new ServerMessage(ServerEvents.StageLoaded);
      	StageLoaded2.writeInt(1); 
      	//UserManager.SendMessageToAllSnowPlayers(CurrentWar, StageLoaded2);
        
     	ServerMessage StageRunning = new ServerMessage(ServerEvents.StageRunning);
     	StageRunning.writeInt(120); // time?
     	UserManager.SendMessageToAllSnowPlayers(CurrentWar, StageRunning);
	}
	
	public void RemoveFromMyWar(Habbo cUser) throws Exception
	{
		Habbo User = Habbo.UsersbyId.get(cUser.Id);
		if(User.CurrentWar.equals(null))
			return;
		SnowWar War = User.CurrentWar;
		War.CurrentPlayers--;
		War.Players.remove(User);
		if(War.BlueTeam.containsKey(User.Id))
			War.BlueTeam.remove(User);
		else if(War.RedTeam.containsKey(User.Id))
			War.RedTeam.remove(User);
		else if(War.YellowTeam.containsKey(User.Id))
			War.YellowTeam.remove(User);
		else if(War.GreenTeam.containsKey(User.Id))
			War.GreenTeam.remove(User);
		
		if(War.Players.size() == 0)
		{
			// remove war
			Wars.remove(War.Id);
		} else {	
			// add a new owner
			if(User.UserName.equals(War.Owner))
			{
				Iterator reader = War.Players.entrySet().iterator();
		        while (reader.hasNext())
		        {
		        	Habbo xUser = (Habbo)(((Map.Entry)reader.next()).getValue());
					War.Owner = xUser.UserName;
				}
			}
			// and remove the user
			ServerMessage UserLeaveGame = new ServerMessage(ServerEvents.UserLeaveGame);
			UserLeaveGame.writeInt(User.Id);
			Iterator reader = War.Players.entrySet().iterator();
	        while (reader.hasNext())
	        {
	        	Habbo xUser = (Habbo)(((Map.Entry)reader.next()).getValue());
     		    UserLeaveGame.Send(xUser.CurrentSocket);
    	    }
			
		}
		User.CurrentWar = null;
	}
	
	public static void CreateANewWar(Habbo cUser) throws Exception
	{
		Habbo User = Habbo.UsersbyId.get(cUser.Id);
		SnowWar S = new SnowWar();
		int count = 0;
		Iterator reader = Wars.entrySet().iterator();
        while (reader.hasNext())
        {
        	SnowWar CurrentWar = (SnowWar)(((Map.Entry)reader.next()).getValue());
        	if(CurrentWar.Id > count)
				count = CurrentWar.Id;
		}
		S.Id = count + 1;
		S.ArenaId = 9; // soon (new Random()).nextInt(10);
		S.TeamCount = 2; // maybe random 2,4 soon?
		S.Owner = User.UserName;
		S.Players = new HashMap<Integer, Habbo>();
		S.RedTeam = new HashMap<Integer, Habbo>();
		S.BlueTeam = new HashMap<Integer, Habbo>();
		S.YellowTeam = new HashMap<Integer, Habbo>();
		S.GreenTeam = new HashMap<Integer, Habbo>();
		S.RestTime = 0;
		S.StartedCounter = false;
		S.CurrentPlayers = 0;
		S.MaxPlayers = 10; // for the moment
		S.UserMoreHits = null;
		S.UserMoreKO = null;
		S.IsStarted = false;
		S.IsFinished = false;
		S.RedTeam.put(User.Id, User);
		S.Players.put(User.Id, User);
		User.CurrentWar = S;
		Wars.put(S.Id, S);
	}
}
