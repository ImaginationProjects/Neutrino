package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
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
import neutrino.RoomManager.RoomEvent;

import org.jboss.netty.channel.Channel;

public class EndEventMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        if(R.OwnerId == cUser.Id && !RoomEvent.GetEventForRoomId(RoomId).equals(null))
        {
        	RoomEvent E = RoomEvent.GetEventForRoomId(RoomId);
        	RoomEvent.Events.remove(E.Id);
        	
            ServerMessage RoomEvents = new ServerMessage(ServerEvents.RoomEvents);
            RoomEvents.writeUTF("-1");
            UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, RoomEvents);
        }
	}
}
