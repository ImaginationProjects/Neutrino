package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.DanceNumber;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.SubscriptionManager;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class LoadNewEventMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        if(!R.HavePowers(cUser.Id))
        	return;
        
        ServerMessage Dance = new ServerMessage(ServerEvents.AcceptEvent);
        Dance.writeBoolean(true); // is valid
        Dance.writeInt(0); // errorid
        Dance.Send(Socket);
	}
}
