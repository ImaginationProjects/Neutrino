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

public class LoadPrincipalBoxMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		ServerMessage Plays = new ServerMessage(ServerEvents.CurrentSnowStormPlays);
		Plays.writeInt(0);
		Plays.writeInt(0);
		Plays.writeInt(9);
    	Plays.writeInt(-1); // full play, anyway, 3 = 3 plays an more idiot things // IF VIP => -1
    	Plays.Send(Client.Socket);
	}
}
