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

public class DanceMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        int DanceId = Client.in.readInt();
        SubscriptionManager s = new SubscriptionManager(CurrentUser, Server);
        if(!s.HasSubscription("habbo_vip") & DanceId > 1)
            return;
        
        CurrentUser.CurrentDance = DanceNumber.GetDanceFromNum(DanceId);
        ServerMessage Dance = new ServerMessage(ServerEvents.Dance);
        Dance.writeInt(CurrentUser.SessionId);
        Dance.writeInt(DanceId);
        UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, Dance);
	}
}
