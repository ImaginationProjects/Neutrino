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

public class KickUserMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server, int UserToKick) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        if(CurrentUser.RankLevel < 5)
        	return;
        
        Habbo oUser = Habbo.UsersbyId.get(UserToKick);
        Room RoomData = Room.Rooms.get(oUser.CurrentRoomId);
        if(oUser.IsOnRoom && RoomData.HavePowers(CurrentUser.Id) && oUser.IsOnline && oUser.RankLevel <= cUser.RankLevel)
        {
        	oUser.KickMe(cUser.Id);
        }
	}
}
