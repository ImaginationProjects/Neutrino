package com.sulake.habbo.communication.messages.outgoing.friendlist;

import neutrino.Environment;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class TalkOnChatMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		int toFriend = Client.inreader.readInt();
		String Message = Client.inreader.readUTF();
		
		ServerMessage Talk = new ServerMessage(ServerEvents.TalkOnChat);
		Talk.writeInt(User.Id);
		Talk.writeUTF(Message);
		Talk.Send(Habbo.UsersbyId.get(toFriend).CurrentSocket);
	}
}
