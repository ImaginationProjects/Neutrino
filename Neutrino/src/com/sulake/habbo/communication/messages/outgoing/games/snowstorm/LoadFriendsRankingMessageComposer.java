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

public class LoadFriendsRankingMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo User = Client.GetSession();
    	ServerMessage Board = new ServerMessage(ServerEvents.FriendsLeaderBoard);
    	Board.writeInt(1); // count
    	Board.writeInt(User.Id);
    	Board.writeInt(85000);
    	Board.writeInt(1); // position?
    	Board.writeUTF(User.UserName);
    	Board.writeUTF(User.Look);
    	Board.writeUTF(User.Gender.toLowerCase());
    	Board.writeInt(1); // repeat position (if it's me)
    	Board.Send(Client.Socket);
	}
}
