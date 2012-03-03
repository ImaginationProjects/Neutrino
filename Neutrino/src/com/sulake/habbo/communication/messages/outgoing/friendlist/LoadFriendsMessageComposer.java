package com.sulake.habbo.communication.messages.outgoing.friendlist;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadFriendsMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		ServerMessage Friends = new ServerMessage(ServerEvents.Friends);
        Friends.writeInt(900);
        Friends.writeInt(500);
        Friends.writeInt(300);
        Friends.writeInt(900);
        Friends.writeInt(0); // categorys
        Friends.writeInt(0); // friends
        Friends.Send(Client.Socket);
	}
}
