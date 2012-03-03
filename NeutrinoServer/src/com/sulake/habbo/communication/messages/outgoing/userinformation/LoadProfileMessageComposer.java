package com.sulake.habbo.communication.messages.outgoing.userinformation;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class LoadProfileMessageComposer {
	public static void Compose(ServerHandler Client, Habbo CurrentUser) throws Exception
	{
		int UserId = Client.in.readInt();
        Habbo User = Habbo.UsersbyId.get(UserId);
        
        ServerMessage Profile = new ServerMessage(ServerEvents.WatchProfile);
        Profile.writeInt(UserId);
        Profile.writeUTF(User.UserName);
        Profile.writeUTF(User.Look);
        Profile.writeUTF(User.Motto);
        Profile.writeUTF("21-12-2012"); // created ?
        Profile.writeInt(55431); // achv
        Profile.writeInt(Friend.SelectFriendsForId(UserId).size()); // friends
        Profile.writeUTF(""); // registered
        Profile.writeBoolean(User.IsOnline); // online
        Profile.writeInt(0); // group count (have other packet to serialize)
        Profile.writeInt(503); // don't know
        Profile.Send(Client.Socket);

	}
}
