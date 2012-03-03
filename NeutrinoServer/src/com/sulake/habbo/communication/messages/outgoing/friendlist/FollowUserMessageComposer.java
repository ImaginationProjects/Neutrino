package com.sulake.habbo.communication.messages.outgoing.friendlist;

import neutrino.Environment;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class FollowUserMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		int UserId = Client.inreader.readInt();
		Habbo User = Habbo.UsersbyId.get(UserId);
		if(!User.IsOnRoom)
			return;
		int RoomId = User.CurrentRoomId;
		Room RoomData = Room.Rooms.get(RoomId);
		ServerMessage Can = new ServerMessage(ServerEvents.CreateNewRoom);
		Can.writeInt(RoomId);
		Can.writeUTF(RoomData.Name);
		Can.Send(Client.Socket);
	}
}
