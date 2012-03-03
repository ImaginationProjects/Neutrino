package com.sulake.habbo.communication.messages.outgoing.friendlist;

import neutrino.Environment;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class AskToBeMyFriendMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		String Name = Client.inreader.readUTF();
		Habbo cUser = Habbo.GetUserForName(Name);
		if(cUser == null)
			return;
		
		ServerMessage Ask = new ServerMessage(ServerEvents.AskToBeMyFriend);
		Ask.writeInt(User.Id);
		Ask.writeUTF(User.UserName);
		Ask.writeUTF(User.Look);
		Ask.Send(cUser.CurrentSocket);
		
		Friend F = new Friend();
		F.UserId = cUser.Id;
		F.FriendId = User.Id;
		F.CategoryId = 0;
		F.Pending = 1;
		Friend.Friends.add(F);
		Server.GetDatabase().executeUpdate("INSERT INTO users_friends (userid, friendid, category, pending) VALUES ('" + cUser.Id + "', '" + User.Id + "', '0', '1');");
	}
}
