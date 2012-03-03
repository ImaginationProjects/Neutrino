package com.sulake.habbo.communication.messages.outgoing.administration;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.AdministrationManager.Chatlog;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class SendRoomAlertMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		if(cUser.RankLevel < 5)
        {
			return;
        }
		Habbo CurrentUser = Client.GetSession();
        Channel Socket = Client.Socket;
        int ModeratorId = Client.inreader.readInt();
        int Junk = Client.inreader.readInt();
        String Alerta = Client.inreader.readUTF();
        
        int RoomId = CurrentUser.CurrentRoomId;
        Room RoomData = Room.Rooms.get(RoomId);
        Iterator reader = RoomData.UserList.iterator();
        while(reader.hasNext())
        {
        	Habbo User = (Habbo)reader.next();
        	User.SendAlert(Alerta);
        }
	}
}
