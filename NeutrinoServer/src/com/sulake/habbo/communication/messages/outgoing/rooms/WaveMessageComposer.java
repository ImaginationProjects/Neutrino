package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class WaveMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		 Habbo CurrentUser = Client.GetSession();
	     int RoomId = CurrentUser.CurrentRoomId;
	     Channel Socket = Client.Socket;
	     Room R = Room.Rooms.get(RoomId);
	     int Action = Client.in.readInt();
	     ServerMessage Wave = new ServerMessage(ServerEvents.Wave);
	     Wave.writeInt(CurrentUser.SessionId);
	     Wave.writeInt((Action == 5) ? -1 : Action);
	     UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, Wave);
	}
}
