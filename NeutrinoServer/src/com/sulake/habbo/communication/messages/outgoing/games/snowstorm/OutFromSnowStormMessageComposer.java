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

public class OutFromSnowStormMessageComposer {
	public static void Compose(ServerHandler Client, Habbo xUser, Environment Server) throws Exception
	{
		Habbo User = Client.GetSession();
    	SnowWar CurrentWar = User.CurrentWar;
    	CurrentWar.RemoveFromMyWar(User);
	}
}
