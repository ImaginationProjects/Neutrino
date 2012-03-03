package Requests.Games.SnowStorm;
import java.util.concurrent.FutureTask;
import java.util.*;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.SnowWarManager.SnowWar;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomModel;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.ItemManager.UserItem;
/**
 *
 * @author Julián
 */
public class InitSnowStorm extends Handler implements Runnable {
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
        	SnowWar.SearchASnowWarForMe(User);
        	SnowWar CurrentWar = User.CurrentWar;
        	ServerMessage Waiting = new ServerMessage(ServerEvents.WaitingForUsers);
        	Waiting.writeInt(CurrentWar.Id); //
        	Waiting.writeUTF("SnowStorm level " + CurrentWar.ArenaId);
        	Waiting.writeInt(0);
        	Waiting.writeInt(CurrentWar.ArenaId); // level
        	Waiting.writeInt(CurrentWar.TeamCount);
        	Waiting.writeInt(CurrentWar.MaxPlayers);
        	Waiting.writeUTF(CurrentWar.Owner); // user name of room creator?
        	Waiting.writeInt(14);
        	Waiting.writeInt(CurrentWar.Players.size());
        	
        	Iterator reader = CurrentWar.Players.iterator();
        	while(reader.hasNext())
        	{
        		Habbo cUser = (Habbo)reader.next();
        	    Waiting.writeInt(cUser.Id);
        	    Waiting.writeUTF(cUser.UserName);
        	    Waiting.writeUTF(cUser.Look);
        	    Waiting.writeUTF(cUser.Gender.toLowerCase());
        	    Waiting.writeInt(-1);
        	    Waiting.writeInt(0); // stars
        	    Waiting.writeInt(0); // points
        	    Waiting.writeInt(10); // points for next level
        	}        	
        	Waiting.Send(Client.Socket);
        	
        	/*ServerMessage AddUser = new ServerMessage(ServerEvents.AddUsersToGame);
        	AddUser.writeInt(654);
        	AddUser.writeUTF("Itachi2");
        	AddUser.writeUTF(User.Look);
        	AddUser.writeUTF(User.Gender.toLowerCase());
        	AddUser.writeInt(-1);
        	AddUser.writeInt(0); // stars
        	AddUser.writeInt(0); // points
        	AddUser.writeInt(10); // points for next level
        	AddUser.writeBoolean(false);*/
        	//AddUser.Send(Client.Socket);
        	Thread.sleep(10000);
        	// 2807
        	ServerMessage Waiting2 = new ServerMessage(2807);
        	Waiting2.writeInt(CurrentWar.Id); //
        	Waiting2.writeUTF("SnowStorm level " + CurrentWar.ArenaId);
        	Waiting2.writeInt(0);
        	Waiting2.writeInt(CurrentWar.ArenaId); // level
        	Waiting2.writeInt(CurrentWar.TeamCount);
        	Waiting2.writeInt(CurrentWar.MaxPlayers);
        	Waiting2.writeUTF(CurrentWar.Owner); // user name of room creator?
        	Waiting2.writeInt(14);
        	Waiting2.writeInt(CurrentWar.Players.size());
        	
        	Iterator reader2 = CurrentWar.BlueTeam.iterator();
        	while(reader2.hasNext())
        	{
        		Habbo cUser = (Habbo)reader2.next();
        	    Waiting2.writeInt(cUser.Id);
        	    Waiting2.writeUTF(cUser.UserName);
        	    Waiting2.writeUTF(cUser.Look);
        	    Waiting2.writeUTF(cUser.Gender.toLowerCase());
        	    Waiting2.writeInt(1); // team
        	    Waiting2.writeInt(0); // stars
        	    Waiting2.writeInt(0); // points
        	    Waiting2.writeInt(10); // points for next level
        	}
        	
        	Iterator reader3 = CurrentWar.RedTeam.iterator();
        	while(reader3.hasNext())
        	{
        		Habbo cUser = (Habbo)reader3.next();
        	    Waiting2.writeInt(cUser.Id);
        	    Waiting2.writeUTF(cUser.UserName);
        	    Waiting2.writeUTF(cUser.Look);
        	    Waiting2.writeUTF(cUser.Gender.toLowerCase());
        	    Waiting2.writeInt(2); // team
        	    Waiting2.writeInt(10); // stars
        	    Waiting2.writeInt(20000000); // points
        	    Waiting2.writeInt(20000000); // points for next level
        	}
        	Waiting2.Send(Client.Socket);
        	//Thread.sleep(7000);
        	// StartGame!
        	ServerMessage StartGame = new ServerMessage(ServerEvents.StartSnowGame);
        	StartGame.writeInt(0);
        	StartGame.writeInt(CurrentWar.ArenaId);
        	StartGame.writeInt(CurrentWar.TeamCount);
        	StartGame.writeInt(CurrentWar.Players.size());
        	Iterator treader = CurrentWar.Players.iterator();
        	while(treader.hasNext())
        	{
        		Habbo cUser = (Habbo)treader.next();
        		StartGame.writeInt(cUser.Id);
            	StartGame.writeUTF(cUser.UserName);
            	StartGame.writeUTF(cUser.Look);
            	StartGame.writeUTF(cUser.Gender.toLowerCase());
            	StartGame.writeInt(2); // team?
        	}
        	StartGame.writeInt(2);
        	StartGame.writeInt(2);
        	StartGame.writeUTF(RoomModel.Models.get("snowstorm_model" + CurrentWar.ArenaId).SerializeMap);
        	StartGame.writeInt(0);
        	/*StartGame.writeUTF("ads_background");
        	StartGame.writeInt(0);
        	StartGame.writeInt(0);
        	StartGame.writeInt(0);
        	StartGame.writeInt(1);
        	StartGame.writeInt(1);
        	StartGame.writeInt(0);
        	StartGame.writeInt(1);
        	StartGame.writeBoolean(false);
        	StartGame.writeInt(1);
        	StartGame.writeInt(1);
        	StartGame.writeInt(5);
        	StartGame.writeUTF("state");
        	StartGame.writeUTF("0");
        	StartGame.writeUTF("offsetZ");
        	StartGame.writeUTF("10000");
        	StartGame.writeUTF("offsetY");
        	StartGame.writeUTF("1542");
        	StartGame.writeUTF("imageUrl");
        	StartGame.writeUTF("http://images.habbo.com/c_images/DEV_tests/snst_bg_1_a_big.png");
        	StartGame.writeUTF("offsetX");
        	StartGame.writeUTF("-1166");*/

        	StartGame.Send(Client.Socket);
        	
        	ServerMessage UserInfo = new ServerMessage(3180);
        	UserInfo.writeInt(User.Id);
        	UserInfo.writeUTF(User.UserName);
        	UserInfo.writeUTF(User.Look);
        	UserInfo.writeUTF(User.Gender.toLowerCase());
        	UserInfo.writeInt(2);
        	UserInfo.Send(Client.Socket); // User ready!
        	
        	ServerMessage More = new ServerMessage(3630);
        	More.writeInt(0);;
        	More.Send(Client.Socket);
        	
        	Thread.sleep(15000);
        	ServerMessage Arena = new ServerMessage(2873);
        	Arena.writeInt(0);
        	Arena.writeUTF("snowwar_arena_0");
        	/*Arena.writeInt(5);
        	Arena.writeInt(27);
        	Arena.writeInt(2);
        	Arena.writeInt(0);
        	Arena.writeInt(41535);
        	Arena.writeInt(47935);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(0);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(1);
        	Arena.writeInt(2);
        	Arena.writeInt(1);
        	Arena.writeInt(96000);
        	Arena.writeInt(22335);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(6);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(47935);
        	Arena.writeInt(32000);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(7);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(3);
        	Arena.writeInt(15935);
        	Arena.writeInt(76800);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(8);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(4);
        	Arena.writeInt(83200);
        	Arena.writeInt(762800);
        	Arena.writeInt(0);
        	Arena.writeInt(5);
        	Arena.writeInt(0);
        	Arena.writeInt(10);
        	Arena.writeInt(2);
        	Arena.writeInt(5);
        	Arena.writeInt(150335);
        	Arena.writeInt(102400);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(16);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(6);
        	Arena.writeInt(83200);
        	Arena.writeInt(19200);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(16);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(6);
        	Arena.writeInt(83200);
        	Arena.writeInt(19200);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(20);
        	Arena.writeInt(3);
        	Arena.writeInt(7);
        	Arena.writeInt(7);
        	Arena.writeInt(28735);
        	Arena.writeInt(81727);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(37);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(8);
        	Arena.writeInt(19200);
        	Arena.writeInt(64000);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(450);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(9);
        	Arena.writeInt(156735);
        	Arena.writeInt(89600);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(41);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(10);
        	Arena.writeInt(79935);
        	Arena.writeInt(121600);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(52);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(1);
        	Arena.writeInt(64000);
        	Arena.writeInt(12800);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(53);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(12);
        	Arena.writeInt(115200);
        	Arena.writeInt(47935);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(55);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(13);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(56);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(14);
        	Arena.writeInt(143935);
        	Arena.writeInt(79935);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(59);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(15);
        	Arena.writeInt(47935);
        	Arena.writeInt(108800);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(65);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(16);
        	Arena.writeInt(32000);
        	Arena.writeInt(83200);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(82);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(17);
        	Arena.writeInt(64000);
        	Arena.writeInt(25600);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(92);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(2);
        	Arena.writeInt(18);
        	Arena.writeInt(86900);
        	Arena.writeInt(150335);
        	Arena.writeInt(0);
        	Arena.writeInt(4800);
        	Arena.writeInt(0);
        	Arena.writeInt(3);
        	Arena.writeInt(0);
        	Arena.writeInt(5);*/
        	// user serialize? wtf?
        	Arena.writeInt(0);
        	Arena.writeInt(0);
        	Arena.writeInt(0);
        	Arena.writeInt(0);
        	Arena.writeInt(0);
        	/*
        	Arena.writeInt(5);
        	Arena.writeInt(21);
        	Arena.writeInt(60735);
        	Arena.writeInt(15935);
        	Arena.writeInt(19);
        	Arena.writeInt(5);
        	Arena.writeInt(4);
        	Arena.writeInt(5);
        	Arena.writeInt(0);
        	Arena.writeInt(0);
        	Arena.writeInt(0);
        	Arena.writeInt(19);
        	Arena.writeInt(5);
        	Arena.writeInt(60735);
        	Arena.writeInt(15935);
        	Arena.writeInt(0);
        	Arena.writeInt(2);            //[1]¾?¨[0][10]julian6820[0][5]LOL!![0]+hr-893-31.hd-3095-8.ch-3111-82-62.lg-270-91[0][1]m[0][0][0][5][0][0][0][0][0]í?[0][0]>?[0][0][0][0][0][0][5][0][0][0][4][0][0][0][5][0][0][0][5][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][5][0][0]í?[0][0]>?[0][0][0][0][0][0][0]
        	Arena.writeInt(User.Id);
        	Arena.writeUTF(User.UserName);
        	Arena.writeUTF(User.Motto);
        	Arena.writeUTF(User.Look);
        	Arena.writeUTF(User.Gender.toLowerCase());*/
        	Arena.Send(Client.Socket);
        	//[INT] > [0]: [0][0][0][0]
        } catch (Exception e)
        {
        	Server.WriteLine(e);
        }
    }
}