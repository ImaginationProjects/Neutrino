/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino;

import neutrino.SQL.*;
import neutrino.MessageEvents.MessageEvent;
import java.io.*;
import org.jboss.netty.channel.Channel;
/*import neutrino.PacketsInformation.*;
import Requests.Users.LoginHabbo;
import Requests.Users.LoadMyData;
import Requests.Users.GetCredits;
import Requests.Users.LoadClub;
import Requests.Users.LoadProfile;
import Requests.Users.ChangeLooks;
import Requests.Users.LoadFriends;
import Requests.Users.LoadInventory;
import Requests.Catalog.LoadCatalogPages;
import Requests.Catalog.LoadCatalogPageForm;
import Requests.Catalog.PurchaseCatalogItem;
import Requests.Rooms.AddItemToMyRoom;
import Requests.Rooms.NavigatorMyRooms;
import Requests.Rooms.EnterOnRoom;
import Requests.Rooms.SerializeHeightmap;
import Requests.Rooms.EndEnterRoom;
import Requests.Rooms.Talk;
import Requests.Rooms.Walk;
import Requests.Rooms.Wave;
import Requests.Rooms.Dance;
import Requests.Rooms.Sign;
import Requests.Rooms.SerializeRoomData;
import Requests.Rooms.UpdateItem;
import Requests.Rooms.UpdatePapers;
import Requests.Games.SnowStorm.CurrentTimesToPlay;
import Requests.Games.SnowStorm.GeneralLeaderBoard;
import Requests.Games.SnowStorm.FriendLeaderBoard;
import Requests.Games.SnowStorm.InitSnowStorm;
import Requests.Games.SnowStorm.TalkOnSnow;
import Requests.Guilds.InitGuildCreate;
import Requests.Guilds.PurchaseGuild;*/
import neutrino.PacketsInformation.*;
import neutrino.PetManager.Pet;
import neutrino.PetManager.PetRace;

import com.sulake.habbo.communication.messages.incoming.administration.CloseTicketMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.LoadCFHChatlogMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.LoadRoomChatlogMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.LoadRoomMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.LoadUserChatlogMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.LoadUserMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.PerformRoomActionMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.PickTicketMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.ReleaseTicketMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.SendAlertToUserMessageEvent;
import com.sulake.habbo.communication.messages.incoming.administration.SendRoomAlertMessageEvent;
import com.sulake.habbo.communication.messages.incoming.catalog.LoadCatalogMessageEvent;
import com.sulake.habbo.communication.messages.incoming.catalog.LoadCatalogPageMessageEvent;
import com.sulake.habbo.communication.messages.incoming.catalog.LoadPetRaceMessageEvent;
import com.sulake.habbo.communication.messages.incoming.catalog.PurchaseCatalogItemMessageEvent;
import com.sulake.habbo.communication.messages.incoming.catalog.ValidPetNameMessageEvent;
import com.sulake.habbo.communication.messages.incoming.friendlist.AcceptFriendMessageEvent;
import com.sulake.habbo.communication.messages.incoming.friendlist.AskToBeMyFriendMessageEvent;
import com.sulake.habbo.communication.messages.incoming.friendlist.FollowUserMessageEvent;
import com.sulake.habbo.communication.messages.incoming.friendlist.LoadFriendsMessageEvent;
import com.sulake.habbo.communication.messages.incoming.friendlist.RemoveFriendMessageEvent;
import com.sulake.habbo.communication.messages.incoming.friendlist.TalkOnChatMessageEvent;
import com.sulake.habbo.communication.messages.incoming.games.snowstorm.JoinToSnowStormMessageEvent;
import com.sulake.habbo.communication.messages.incoming.games.snowstorm.LoadFriendsRankingMessageEvent;
import com.sulake.habbo.communication.messages.incoming.games.snowstorm.LoadGeneralRankingMessageEvent;
import com.sulake.habbo.communication.messages.incoming.games.snowstorm.LoadPrincipalBoxMessageEvent;
import com.sulake.habbo.communication.messages.incoming.games.snowstorm.LoadWeeklyFriendsRankingMessageEvent;
import com.sulake.habbo.communication.messages.incoming.games.snowstorm.LoadWeeklyGeneralRankingMessageEvent;
import com.sulake.habbo.communication.messages.incoming.games.snowstorm.OutFromSnowStormMessageEvent;
import com.sulake.habbo.communication.messages.incoming.games.snowstorm.WalkOnSnow;
import com.sulake.habbo.communication.messages.incoming.handshake.HelloMessageEvent;
import com.sulake.habbo.communication.messages.incoming.login.ShowCreditsMessageEvent;
import com.sulake.habbo.communication.messages.incoming.login.LoadClubMessageEvent;
import com.sulake.habbo.communication.messages.incoming.login.SendMyDataMessageEvent;
import com.sulake.habbo.communication.messages.incoming.navigator.CanCreateNewRoomMessageEvent;
import com.sulake.habbo.communication.messages.incoming.navigator.CreateNewRoomMessageEvent;
import com.sulake.habbo.communication.messages.incoming.navigator.LoadAllRoomsMessageEvent;
import com.sulake.habbo.communication.messages.incoming.navigator.LoadCategorysMessageEvent;
import com.sulake.habbo.communication.messages.incoming.navigator.LoadEventRoomsMessageEvent;
import com.sulake.habbo.communication.messages.incoming.navigator.LoadMyRoomsMessageEvent;
import com.sulake.habbo.communication.messages.incoming.navigator.LoadPublicRoomsMessageEvent;
import com.sulake.habbo.communication.messages.incoming.navigator.SearchForAPageMessageEvent;
import com.sulake.habbo.communication.messages.incoming.navigator.SetHomeMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.AddChairToPetMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.AddItemToMyRoomMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.AllowDoorbellMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.BanUserFromRoomMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.ChangeLooksMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.ChangeMottoMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.ClickOnFurniMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.CreateNewEventMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.DanceMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.EndEnterRoomMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.EndEventMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.EnterOnRoomMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.KickMeMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.KickUserByMToolsMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.KickUserMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.LoadMapsMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.LoadNewEventMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.LoadRoomDataMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.LookToMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.MovePetToRoomMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.PickUpPetMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.PickupItemMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.RemoveChairFromPetMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.RideAHorseMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.SaveRoomUpdatesMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.ShoutMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.ShowPetInformationMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.SingMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.SitMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.TalkMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.TalkOnSnowMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.TrainPetMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.UpdateItemMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.UpdatePapersMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.UpdateWallItemMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.WalkMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.WaveMessageEvent;
import com.sulake.habbo.communication.messages.incoming.rooms.WhispMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.AcceptTradeMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.CanTradeMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.ChangeTradeMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.ConfirmTradeMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.EndTradeMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.HelpTicketMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.LoadBadgesMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.LoadInventaryMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.LoadPetInventaryMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.LoadProfileMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.RemoveTradeObjectMessageEvent;
import com.sulake.habbo.communication.messages.incoming.userinformation.TradeObjectMessageEvent;

import neutrino.UserManager.Habbo;
import neutrino.Network.*;
import neutrino.Network.ThreadPool.ThreadPoolManager;

import java.util.*;
import java.text.*;
import java.sql.Timestamp;

import neutrino.AdministrationManager.CallsForHelp;
import neutrino.AdministrationManager.Chatlog;
import neutrino.AdministrationManager.ModerationPreset;
import neutrino.CatalogManager.CatalogPage;
import neutrino.CatalogManager.CatalogItem;
import neutrino.RoomManager.NavigatorCategories;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomEvent;
import neutrino.RoomManager.RoomModel;
import neutrino.RoomManager.RoomManager;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import neutrino.ItemManager.RoomItem;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Subscription;
import neutrino.UserManager.UserManager;
import neutrino.GuildManager.GuildItem;
import neutrino.SnowWarManager.SnowWar;
import neutrino.SnowWarManager.SnowWarItem;
/**
 *
 * @author Julián
 */
public class Environment {
	public static int UsersConnected = 0;
    public static int SessionIds = 0;
    public int MaxSQLUses = 500000000;
    public boolean Active = true;
    public static Database databaseManager;
    private String Version = "0.1.533.1039";
    public static MessageEvent[] Request;
    public int RequestGetted = 0;
    public static Database returnDB()
    {
    	return databaseManager;
    }
    public static String dateToUnixTimestamp(Date fecha){
        String res = "";
        Date aux = stringToDate("1970-01-01 00:00:00");
        Timestamp aux1 = dateToTimeStamp(aux);
        Timestamp aux2 = dateToTimeStamp(fecha);
        long diferenciaMils = aux2.getTime() - aux1.getTime();
        long segundos = diferenciaMils / 1000;
        return res+segundos;
    }
    
    public static Date stringToDate(String fecha){
        // Solo funciona con string en el formato yyyy-MM-dd HH:mm:ss
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date res = null;
        try{
                res = format.parse(fecha);
        }catch(Exception e){}
        return res;
    }
    
     public static Timestamp dateToTimeStamp(Date fecha){
        return new Timestamp(fecha.getTime());
    }
     
     public static Date fechaHoraSistema(){
        return new Date(System.currentTimeMillis());
    }
     
     public static String getUnixTimestamp()
     {
         return dateToUnixTimestamp(fechaHoraSistema());
     }
     
     public static int getIntUnixTimestamp()
     {
         return (int)(Integer.parseInt(getUnixTimestamp()));
     }

    public static Map<String, String> Properties;
    public void init() throws Exception {
        WriteLine("Starting Neutrino...");
        Properties = new HashMap<String, String>();
        File propers = new File("neutrino.properties");
        BufferedReader propersreader = new BufferedReader(new FileReader(propers));
        while(propersreader.ready())
        {
        	String NextLine = propersreader.readLine();
        	if(!NextLine.startsWith("#") && NextLine.length() > 0)
        	{
        		String du = NextLine;
        		String[] d = du.split("=");
        		if(Properties.containsKey(d[0]))
        			break;
        		Properties.put(d[0], du.replace(d[0] + "=", ""));
        	}
        }
        //WriteLine("Neutrino is for the name of the fastest particle of the universe");
        //WriteLine("Timestamp: " + getUnixTimestamp());
        
        // sql System
        Database.Init(Properties.get("db.connection.server") + ":" + Properties.get("db.connection.port"), Properties.get("db.connection.user"), Properties.get("db.connection.password"), Properties.get("db.connection.dbname"), this);
        java.sql.ResultSet rVersion = Database.executeQuery("SELECT `release` FROM server_settings;");
        int Version = 0;
        while(rVersion.next()) { Version = rVersion.getInt("release"); }
        Version += 20;
        WriteLine("Neutrino v0.3 BUILD" + Version);
        WriteLine("Readed " + Properties.size() + " properties on neutrino.properties");
        WriteLine("Starting database under BoneCP...");
        Database.sendinit();
        //Database.Init("localhost", "root", "natalia12", "habbo", this);
        //WriteLine("Sql initialized correctly.");
        
        CatalogPage.Init(this);
        CatalogItem.Init(this);
        PetRace.Init(this);
        GuildItem.init(this);
        ItemInformation.Init(this);
        UserItem.init(this);
        RoomItem.Init(this);
        Habbo.InitUsers(this);
        Friend.Init(this);
        Subscription.Init(this);
        ModerationPreset.Init(this);
        NavigatorCategories.init(this);
        SnowWarItem.Init(this);
        Room.Init(this);
        Pet.Init(this);
        CallsForHelp.Init(this);
        Chatlog.Init(this);
        RoomModel.Init(this);
        Database.executeUpdates("UPDATE users SET connected = '0'");
        
        // some systems
        SnowWar.InitSnowWarSystem();
        
        int TCPPort = Integer.parseInt(Properties.get("net.connextions.port"));
        int MUSPort = Integer.parseInt(Properties.get("luz.connections.port"));
        // requests system
        InitRequests();
        WriteLine(RequestGetted + " requests logged correctly.");
        ThreadPoolManager.Init(this); 
        UserManager.InitManager(this);
        DatabaseManager.init(this);
        RoomManager.Init();
        RoomEvent.Init();
        WriteLine("Automatic 'luz' system started.");
        Database.executeUpdate("UPDATE server_settings SET `release` = " + Version + ";");
        Database.executeUpdate("UPDATE server_settings SET `timestamp` = " + Environment.getIntUnixTimestamp() + ";");
        // Init sockets
        WriteLine("Loading sockets...");
        (new Server()).init(TCPPort, this);
        WriteLine("Neutrino listening on port " + TCPPort + ".");
        (new MUSServer()).init(MUSPort, this);
        WriteLine("Luz listening on port " + MUSPort + ".");
        WriteLine("Neutrino is ready to work!");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        
        String sTexto;
        do {
        	  sTexto = br.readLine();
        	  if(sTexto.equals("showstats"))
        	  {
        		  WriteLine("Neutrino: Show Statics");
        		  Database.WriteStatics();
        		  ThreadPoolManager.WriteStatics(this);
        	  } else if(sTexto.equals("close"))
        	  {
        		  this.CloseEmulation();
        	  }
        	} while(!sTexto.equals("endcommandprocess"));
    }
    
    public void CloseEmulation() throws Exception
    {
    	WriteLine("Closing Neutrino...");
    	
    	Iterator freader = RoomItem.Items.entrySet().iterator();
        while (freader.hasNext())
        {
            RoomItem Item = (RoomItem)(((Map.Entry)freader.next()).getValue());
            if(Item.IsUpdated)
            {
            	if ((Item.X + "").length() > 0)
            		this.GetDatabase().executeUpdates("UPDATE rooms_items SET x = " + Item.X + " WHERE id = " + Item.Id);
                if ((Item.Y + "").length() > 0)
                	this.GetDatabase().executeUpdates("UPDATE rooms_items SET y = " + Item.Y + " WHERE id = " + Item.Id);
                if ((Item.Rot + "").length() > 0)
                	this.GetDatabase().executeUpdates("UPDATE rooms_items SET rot = " + Item.Rot + " WHERE id = " + Item.Id);
                if (Item.W.length() > 0)
                	this.GetDatabase().executeUpdates("UPDATE rooms_items SET w = '" + Item.W + "' WHERE id = " + Item.Id);
                this.GetDatabase().executeUpdates("UPDATE rooms_items SET extradata = '" + Item.ExtraData + "' WHERE id = " + Item.Id);
            }
        }
    	
    	if(DatabaseManager.SQLs.size() > 0)
		{
			Iterator reader = DatabaseManager.SQLs.iterator();
			while(reader.hasNext())
			{
				String SQL = (String)reader.next();
				this.GetDatabase().executeUpdates(SQL);
			}
			DatabaseManager.SQLs.clear();
			WriteLine("Updated SQL succesfully, closing...");
		}
    }
    
    public static boolean esPar(int x) {
		if ((x % 2) == 0) {
			return true;
		}
 
		return false;
	}
 
	public static boolean esImpar(int x) {
		return !esPar(x);
	}
    
    public void InitRequests() throws Exception
    {
        Request = new MessageEvent[20000];
        SetRequest(ClientEvents.ReadRelease, new HelloMessageEvent());
        SetRequest(ClientEvents.LoadCredits, new ShowCreditsMessageEvent());
        SetRequest(ClientEvents.MyData, new SendMyDataMessageEvent());
        SetRequest(ClientEvents.LoadClub, new LoadClubMessageEvent());
        SetRequest(ClientEvents.LoadFriends, new LoadFriendsMessageEvent());
        SetRequest(ClientEvents.LookOnMyRooms, new LoadMyRoomsMessageEvent());
        SetRequest(ClientEvents.LookOnAllRooms, new LoadAllRoomsMessageEvent());
        SetRequest(ClientEvents.LookPublics, new LoadPublicRoomsMessageEvent());
        SetRequest(ClientEvents.LookOnEvents, new LoadEventRoomsMessageEvent());
        SetRequest(ClientEvents.LookProfile, new LoadProfileMessageEvent());
        SetRequest(ClientEvents.LoadCatalog, new LoadCatalogMessageEvent());
        SetRequest(ClientEvents.LoadCatalogPage, new LoadCatalogPageMessageEvent());
        SetRequest(ClientEvents.PurchaseCatalogItem, new PurchaseCatalogItemMessageEvent());
        SetRequest(ClientEvents.EnterOnRoom, new EnterOnRoomMessageEvent());
        SetRequest(ClientEvents.EndEnterRoom, new EndEnterRoomMessageEvent());
        SetRequest(ClientEvents.SerializeHeightmap, new LoadMapsMessageEvent());
        SetRequest(ClientEvents.Talk, new TalkMessageEvent());
        SetRequest(ClientEvents.Shout, new ShoutMessageEvent());
        SetRequest(ClientEvents.ChangeLooks, new ChangeLooksMessageEvent());
        SetRequest(ClientEvents.Walk, new WalkMessageEvent());
        SetRequest(ClientEvents.Sign, new SingMessageEvent());
        SetRequest(ClientEvents.Dance, new DanceMessageEvent());
        SetRequest(ClientEvents.Wave, new WaveMessageEvent());
        SetRequest(ClientEvents.LoadInventary, new LoadInventaryMessageEvent());
        SetRequest(ClientEvents.LoadInventary2, new LoadInventaryMessageEvent());
        SetRequest(ClientEvents.UpdatePapers, new UpdatePapersMessageEvent());
        SetRequest(ClientEvents.AddItemToMyRoom, new AddItemToMyRoomMessageEvent());
        SetRequest(ClientEvents.MoveOrRotateItem, new UpdateItemMessageEvent());
        SetRequest(ClientEvents.MoveWallItem, new UpdateWallItemMessageEvent());
        SetRequest(ClientEvents.PickUpItem, new PickupItemMessageEvent());
        SetRequest(ClientEvents.LoadSnowStormState, new LoadPrincipalBoxMessageEvent());
        SetRequest(ClientEvents.FriendLeaderBoard, new LoadFriendsRankingMessageEvent());
        SetRequest(ClientEvents.GeneralLeaderBoard, new LoadGeneralRankingMessageEvent());
        SetRequest(ClientEvents.CanCreateRoom, new CanCreateNewRoomMessageEvent());
        SetRequest(ClientEvents.CreateNewRoom, new CreateNewRoomMessageEvent());
        SetRequest(ClientEvents.LoadRoomData, new LoadRoomDataMessageEvent());
        SetRequest(ClientEvents.LoadNavigatorCategories, new LoadCategorysMessageEvent());
        SetRequest(ClientEvents.SaveRoomData, new SaveRoomUpdatesMessageEvent());
        SetRequest(ClientEvents.SearchForAPage, new SearchForAPageMessageEvent());
        SetRequest(ClientEvents.AskToBeAFriend, new AskToBeMyFriendMessageEvent());
        SetRequest(ClientEvents.AcceptFriend, new AcceptFriendMessageEvent());
        SetRequest(ClientEvents.RemoveFriend, new RemoveFriendMessageEvent());
        SetRequest(ClientEvents.TalkOnChat, new TalkOnChatMessageEvent());
        SetRequest(ClientEvents.FollowUser, new FollowUserMessageEvent());
        SetRequest(ClientEvents.ChangeMotto, new ChangeMottoMessageEvent());
        SetRequest(ClientEvents.SitOnRoom, new SitMessageEvent());
        SetRequest(ClientEvents.LookTo, new LookToMessageEvent());
        SetRequest(ClientEvents.GetPetRace, new LoadPetRaceMessageEvent());
        SetRequest(ClientEvents.IsValidPetName, new ValidPetNameMessageEvent());
        SetRequest(ClientEvents.LoadBadgesInventary, new LoadBadgesMessageEvent());
        SetRequest(ClientEvents.OnClickOnItem, new ClickOnFurniMessageEvent());
        SetRequest(ClientEvents.LoadUserInfo, new LoadUserMessageEvent());
        SetRequest(ClientEvents.LoadRoomInfo, new LoadRoomMessageEvent());
        SetRequest(ClientEvents.LoadRoomChatlog, new LoadRoomChatlogMessageEvent());
        SetRequest(ClientEvents.LoadUserChatlog, new LoadUserChatlogMessageEvent());
        SetRequest(ClientEvents.SendAlertToUser, new SendAlertToUserMessageEvent());
        SetRequest(ClientEvents.SendAlertToUser2, new SendAlertToUserMessageEvent());
        SetRequest(ClientEvents.PerformRoomAction, new PerformRoomActionMessageEvent());
        SetRequest(ClientEvents.SendRoomAlert, new SendRoomAlertMessageEvent());
        SetRequest(ClientEvents.LoadFriendWeeklyLeaderBoard, new LoadWeeklyFriendsRankingMessageEvent());
        SetRequest(ClientEvents.LoadGeneralWeeklyLeaderBoard, new LoadWeeklyGeneralRankingMessageEvent());
        SetRequest(ClientEvents.JoinToSnowStorm, new JoinToSnowStormMessageEvent());
        SetRequest(ClientEvents.UserLeaveGame, new OutFromSnowStormMessageEvent());
        SetRequest(ClientEvents.TalkOnSnow, new TalkOnSnowMessageEvent());
        SetRequest(ClientEvents.WalkToOnSnow, new WalkOnSnow());
        SetRequest(ClientEvents.LoadPetInventary, new LoadPetInventaryMessageEvent());
        SetRequest(ClientEvents.MovePetToRoom, new MovePetToRoomMessageEvent());
        SetRequest(ClientEvents.LoadPetData, new ShowPetInformationMessageEvent());
        SetRequest(ClientEvents.AddChairToPet, new AddChairToPetMessageEvent());
        SetRequest(ClientEvents.RideAHorse, new RideAHorseMessageEvent());
        SetRequest(ClientEvents.PickUpPet, new PickUpPetMessageEvent());
        SetRequest(ClientEvents.RemoveChairFromPet, new RemoveChairFromPetMessageEvent());
        SetRequest(ClientEvents.WhispToUser, new WhispMessageEvent());
        SetRequest(ClientEvents.TrainPet, new TrainPetMessageEvent());
        SetRequest(ClientEvents.LoadNewEvent, new LoadNewEventMessageEvent());
        SetRequest(ClientEvents.CreateNewEvent, new CreateNewEventMessageEvent());
        SetRequest(ClientEvents.EndEvent, new EndEventMessageEvent());
        SetRequest(ClientEvents.CanTrade, new CanTradeMessageEvent());
        SetRequest(ClientEvents.TradeObject, new TradeObjectMessageEvent());
        SetRequest(ClientEvents.RemoveObjectFromTrading, new RemoveTradeObjectMessageEvent());
        SetRequest(ClientEvents.AcceptTrade, new AcceptTradeMessageEvent());
        SetRequest(ClientEvents.ConfirmTrade, new ConfirmTradeMessageEvent());
        SetRequest(ClientEvents.ChangeTrade, new ChangeTradeMessageEvent());
        SetRequest(ClientEvents.CancelTrade, new EndTradeMessageEvent());
        SetRequest(ClientEvents.CancelTrade2, new EndTradeMessageEvent());
        SetRequest(ClientEvents.AllowDoorbell, new AllowDoorbellMessageEvent());
        SetRequest(ClientEvents.SetHome, new SetHomeMessageEvent());
        SetRequest(ClientEvents.KickMe, new KickMeMessageEvent());
        SetRequest(ClientEvents.KickUser, new KickUserMessageEvent());
        SetRequest(ClientEvents.BanUserOfRoom, new BanUserFromRoomMessageEvent());
        SetRequest(ClientEvents.KickUserBYMODTOOLS, new KickUserByMToolsMessageEvent());
        SetRequest(ClientEvents.HelpTicket, new HelpTicketMessageEvent());
        SetRequest(ClientEvents.PickTicket, new PickTicketMessageEvent());
        SetRequest(ClientEvents.CloseTicket, new CloseTicketMessageEvent());
        SetRequest(ClientEvents.ReleaseTicket, new ReleaseTicketMessageEvent());
        SetRequest(ClientEvents.LoadCFHChatlog, new LoadCFHChatlogMessageEvent());
        /*
        SetRequest(ClientEvents.ReadRelease, new LoginHabbo());
        SetRequest(ClientEvents.MyData, new LoadMyData());
        SetRequest(ClientEvents.LoadCredits, new GetCredits());
        SetRequest(ClientEvents.LoadCatalog, new LoadCatalogPages());
        SetRequest(ClientEvents.LoadCatalogPage, new LoadCatalogPageForm());
        SetRequest(ClientEvents.PurchaseCatalogItem, new PurchaseCatalogItem());
        SetRequest(ClientEvents.LoadClub, new LoadClub());        
        SetRequest(ClientEvents.LookOnMyRooms, new NavigatorMyRooms());
        SetRequest(ClientEvents.LookProfile, new LoadProfile());
        SetRequest(ClientEvents.EnterOnRoom, new EnterOnRoom());
        SetRequest(ClientEvents.SerializeHeightmap, new SerializeHeightmap());
        SetRequest(ClientEvents.EndEnterRoom, new EndEnterRoom());
        SetRequest(ClientEvents.Talk, new Talk());
        SetRequest(ClientEvents.Walk, new Walk());
        SetRequest(ClientEvents.Wave, new Wave());
        SetRequest(ClientEvents.Sign, new Sign());
        SetRequest(ClientEvents.ChangeLooks, new ChangeLooks());
        SetRequest(ClientEvents.LoadFriends, new LoadFriends());
        SetRequest(ClientEvents.LoadRoomData, new SerializeRoomData());
        SetRequest(ClientEvents.Dance, new Dance());
        SetRequest(ClientEvents.LoadInventary, new LoadInventory());
        SetRequest(ClientEvents.LoadInventary2, new LoadInventory());
        SetRequest(ClientEvents.UpdatePapers, new UpdatePapers());
        SetRequest(ClientEvents.AddItemToMyRoom, new AddItemToMyRoom());
        SetRequest(ClientEvents.MoveOrRotateItem, new UpdateItem());
        SetRequest(ClientEvents.LoadSnowStormState, new CurrentTimesToPlay());
        SetRequest(ClientEvents.FriendLeaderBoard, new FriendLeaderBoard());
        SetRequest(ClientEvents.GeneralLeaderBoard, new GeneralLeaderBoard());
        SetRequest(ClientEvents.InitSnowStorm, new InitSnowStorm());
        SetRequest(ClientEvents.TalkOnSnow, new TalkOnSnow());
        SetRequest(ClientEvents.InitGuildCreate, new InitGuildCreate());
        SetRequest(ClientEvents.PurchaseGroup, new PurchaseGuild());*/
    }
    
    private void SetRequest(int MsgId, MessageEvent hdlr) throws Exception
    {
        Request[MsgId] = hdlr;
        RequestGetted++;
    }
    
    public String GetIPFromSocket(Channel Sock)
    {
        return Sock.getRemoteAddress().toString().split(":")[0].replace("/", "");
    
    }
    
    public void WriteLine(String s)
    {
        System.out.println(s.toString());
    }
    
    public void WriteLine(Exception e)
    {
        System.out.println(e.toString());    
    }
    
    public Database GetDatabase()
    {
        return databaseManager;
    }
}
