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

public class CreateNewEventMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        int Category = Client.inreader.readInt();
        String Title = Client.inreader.readUTF();
        String Description = Client.inreader.readUTF();
        int CountTags = Client.inreader.readInt();
        List<String> Tags = new ArrayList<String>();
        for(int i = 0; i < CountTags; i++)
        {
        	Tags.add(Client.inreader.readUTF());
        }
        
        RoomEvent.Add(Category, Title, Description, cUser.Id, R.Id, Tags);
        
        ServerMessage RoomEvents = new ServerMessage(ServerEvents.RoomEvents);
        RoomEvents.writeUTF(CurrentUser.Id + "");
        RoomEvents.writeUTF(CurrentUser.UserName);
        RoomEvents.writeUTF(R.Id + "");
        RoomEvents.writeInt(Category);
        RoomEvents.writeUTF(Title);
        RoomEvents.writeUTF(Description);
		Calendar calendario = new GregorianCalendar();
		String Minute = "";
		int Minuter = Calendar.MINUTE;
		if(Minuter < 10)
			Minute += "0" + Minuter;
		else
			Minute += Minuter;
        RoomEvents.writeUTF(calendario.get(Calendar.HOUR_OF_DAY) + ":" + Minute);
        RoomEvents.writeInt(Tags.size());
        Iterator reader = Tags.iterator();
        while(reader.hasNext())
        {
        	String tag = (String)reader.next();
        	RoomEvents.writeUTF(tag);
        }
        UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, RoomEvents);
	}
}
