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

public class JoinToSnowStormMessageComposer {
	public static void Compose(ServerHandler Client, Habbo xUser, Environment Server) throws Exception
	{
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
    	
    	Iterator reader = CurrentWar.Players.entrySet().iterator();
        while (reader.hasNext())
        {
        	Habbo cUser = (Habbo)(((Map.Entry)reader.next()).getValue());
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
    	CurrentWar.GoToLobby(CurrentWar);
	}
}
