package com.sulake.habbo.communication.messages.outgoing.games.snowstorm;
import java.util.concurrent.FutureTask;
import java.util.*;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.SnowWarManager.SnowWar;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomModel;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.ItemManager.UserItem;

public class LoadWeeklyGeneralRankingMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo User = Client.GetSession();
    	ServerMessage Board = new ServerMessage(ServerEvents.WeeklyGeneralLeaderBoard);
    	List<Habbo> SnowScores = Habbo.OrderBySnowWeeklyScore();
    	Board.writeInt(2012); // year of other week
    	Board.writeInt(15); // day of other week
    	Board.writeInt(2); // month of week
    	Board.writeInt(60 * 24 * 12); // minutes to reset
    	Board.writeInt(SnowScores.size() + 1); // count  
    	int MyPos = 1;
    	int Pos = 0;
    	Iterator reader = SnowScores.iterator();
    	while(reader.hasNext())
    	{
    		Pos++;
    		Habbo xUser = (Habbo)reader.next();
    		if(xUser.Id == User.Id)
    			MyPos = Pos;
    		Board.writeInt(xUser.Id);
        	Board.writeInt(xUser.SnowWarWeeklyScore);
        	Board.writeInt(Pos); // position?
        	Board.writeUTF(xUser.UserName);
        	Board.writeUTF(xUser.Look);
        	Board.writeUTF(xUser.Gender.toLowerCase());
    	}
    	
    	// my data
    	Board.writeInt(User.Id);
    	Board.writeInt(User.SnowWarWeeklyScore);
    	Board.writeInt(MyPos); // position?
    	Board.writeUTF(User.UserName);
    	Board.writeUTF(User.Look);
    	Board.writeUTF(User.Gender.toLowerCase());
    	Board.writeInt(MyPos); // repeat position (if i'm not in the count?)
    	Board.Send(Client.Socket);
	}
}
