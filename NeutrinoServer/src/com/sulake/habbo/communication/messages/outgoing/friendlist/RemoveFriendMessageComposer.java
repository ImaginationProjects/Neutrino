package com.sulake.habbo.communication.messages.outgoing.friendlist;

import neutrino.Environment;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class RemoveFriendMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		int CountFriends = Client.inreader.readInt();
		for(int i = 0; i < CountFriends; i++)
		{
			int FriendId = Client.inreader.readInt();
			Friend F = Friend.SelectFriendById(User.Id, FriendId);
			Friend.Friends.remove(F);
			Server.GetDatabase().executeUpdate("DELETE FROM users_friends WHERE userid = " + User.Id + " AND friendid = " + FriendId + ";");
		}
	}
}
