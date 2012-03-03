package com.sulake.habbo.communication.messages.outgoing.navigator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class CanCreateNewRoomMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		Habbo CurrentHabbo = Client.GetSession();
		ServerMessage Can = new ServerMessage(ServerEvents.CanCreateNewRoom);
		Can.writeInt(0);
		Can.writeInt(9000);
		Can.Send(Client.Socket);
	}

}
