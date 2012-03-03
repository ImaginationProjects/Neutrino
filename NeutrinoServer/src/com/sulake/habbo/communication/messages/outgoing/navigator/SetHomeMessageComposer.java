package com.sulake.habbo.communication.messages.outgoing.navigator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Environment;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class SetHomeMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		Habbo CurrentHabbo = Client.GetSession();
		CurrentHabbo.Home = Client.inreader.readInt();
		
		ServerMessage Home = new ServerMessage(ServerEvents.SetHome);
        Home.writeInt(CurrentHabbo.Home);
        Home.writeInt(CurrentHabbo.Home);
        Home.Send(Client.Socket);
		Environment.returnDB().executeUpdate("UPDATE users SET home = '" + CurrentHabbo.Home + "' WHERE id = " + CurrentHabbo.Id);
	}

}
