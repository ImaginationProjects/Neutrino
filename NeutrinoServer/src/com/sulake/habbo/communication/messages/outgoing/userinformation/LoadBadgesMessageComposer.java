package com.sulake.habbo.communication.messages.outgoing.userinformation;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class LoadBadgesMessageComposer {
	public static void Compose(ServerHandler Client, Habbo CurrentUser) throws Exception
	{
		Habbo User = CurrentUser;
        
        ServerMessage Profile = new ServerMessage(ServerEvents.LoadBadges);
        Profile.writeInt(1);
        Profile.writeInt(1);
        Profile.writeUTF("ADM");
        Profile.writeInt(0);
        Profile.Send(Client.Socket);

	}
}
