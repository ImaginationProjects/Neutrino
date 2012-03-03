package com.sulake.habbo.communication.messages.outgoing.games.snowstorm;

import java.util.concurrent.FutureTask;
import java.util.*;

import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.SnowWarManager.SnowWar;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomModel;
import neutrino.AdministrationManager.Chatlog;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.ItemManager.UserItem;

public class LoadFriendsRankingMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo User = Client.GetSession();
		User.ReceivedFriends++;
		if(User.ReceivedFriends == 2)
			return;
		
		List<Friend> FriendsList = Friend.SelectFriendsForId(User.Id);
        Iterator reader = FriendsList.iterator();
        List<Habbo> FriendsData = new ArrayList<Habbo>();
        while(reader.hasNext())
        {
        	Friend F = (Friend)reader.next();
        	Habbo fUser = Habbo.UsersbyId.get(F.FriendId);
        	if(!FriendsData.contains(fUser))
        		FriendsData.add(fUser);
        }
        FriendsData.add(User);
		
        Collections.sort(FriendsData, new Comparator<Habbo>() {
    		public int compare(Habbo e1, Habbo e2)
    		{
    			return Integer.valueOf(e2.SnowWarScore).compareTo(e1.SnowWarScore);
    		}
    	});
        
    	ServerMessage Board = new ServerMessage(ServerEvents.FriendsLeaderBoard);
    	Board.writeInt(FriendsData.size() + 1); // count
    	int MyPos = 1;
    	int Pos = 0;
    	Iterator ceader = FriendsData.iterator();
    	while(ceader.hasNext())
    	{
    		Pos++;
    		Habbo xUser = (Habbo)ceader.next();
    		if(xUser.Id == User.Id)
    			MyPos = Pos;
    		Board.writeInt(xUser.Id);
        	Board.writeInt(xUser.SnowWarScore);
        	Board.writeInt(Pos); // position?
        	Board.writeUTF(xUser.UserName);
        	Board.writeUTF(xUser.Look);
        	Board.writeUTF(xUser.Gender.toLowerCase());
    	}    	
    	
    	// my data
    	Board.writeInt(User.Id);
    	Board.writeInt(User.SnowWarScore);
    	Board.writeInt(MyPos); // position?
    	Board.writeUTF(User.UserName);
    	Board.writeUTF(User.Look);
    	Board.writeUTF(User.Gender.toLowerCase());
    	Board.writeInt(MyPos); // repeat position (if it's me)
    	Board.Send(Client.Socket);
	}
}
