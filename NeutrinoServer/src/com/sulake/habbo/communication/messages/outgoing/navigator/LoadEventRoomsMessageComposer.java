package com.sulake.habbo.communication.messages.outgoing.navigator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomEvent;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadEventRoomsMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		Habbo CurrentHabbo = Client.GetSession();
		String Category = Client.inreader.readUTF();
		int CategoryId = Integer.parseInt(Category);
		
		List<RoomEvent> mRooms = RoomEvent.GetRoomsForCategory(CategoryId);
        
        List<RoomEvent> ezRoom = new ArrayList<RoomEvent>();
        List<Room> finalRooms = new ArrayList<Room>();
        List<RoomEvent> efinalRooms = new ArrayList<RoomEvent>();
        int CurrentMax = 0;
        Iterator reader1 = mRooms.iterator();
        while (reader1.hasNext())
        {
        	RoomEvent eData = (RoomEvent)reader1.next();
        	Room Data = eData.GetRoom();
            if (Data.CurrentUsers > 0)
            {
                if (Data.CurrentUsers > CurrentMax)
                    CurrentMax = Data.CurrentUsers;
                ezRoom.add(eData);
            }
        }
        
        int MaxRooms = 0;
        
        for (int i = 0; CurrentMax != i; )
        {
        	Iterator reader2 = ezRoom.iterator();
            while (reader2.hasNext())
            {
            	if(MaxRooms >= 50)
            		break;
            	MaxRooms++;
            	RoomEvent eData = (RoomEvent)reader2.next();
            	Room Data = eData.GetRoom();
                if (Data.CurrentUsers == CurrentMax)
                {
                    finalRooms.add(Data);
                    efinalRooms.add(eData);
                }
            }
            CurrentMax--;
        }

		
        ServerMessage MyRooms = new ServerMessage(ServerEvents.LookRooms);
        MyRooms.writeInt(12);
        MyRooms.writeUTF(Category);
        MyRooms.writeInt(efinalRooms.size());
        Iterator reader = efinalRooms.iterator();
        while(reader.hasNext())
        {
        	RoomEvent E = (RoomEvent)reader.next();
        	Room R = E.GetRoom();
            MyRooms.writeInt(R.Id); // room id
            MyRooms.writeBoolean(true); // event
            MyRooms.writeUTF(E.Title);
            MyRooms.writeInt(E.OwnerId);
            MyRooms.writeUTF(R.OwnerName);
            MyRooms.writeInt(R.State);
            MyRooms.writeInt(R.CurrentUsers);
            MyRooms.writeInt(R.MaxUsers);
            MyRooms.writeUTF(E.Description);
            MyRooms.writeInt(0);
            MyRooms.writeInt((R.Category == 3) ? 2 : 0);
            MyRooms.writeInt(R.Score);
            MyRooms.writeInt(E.Category);
            MyRooms.writeInt(0);
            MyRooms.writeInt(0);
            MyRooms.writeUTF(E.Created);
            MyRooms.writeInt(E.Tags.size());
            Iterator ereader = E.Tags.iterator();
            while(ereader.hasNext())
            {
            	String tag = (String)ereader.next();
            	MyRooms.writeUTF(tag);
            }
            // Icons
            MyRooms.writeInt(0);
            MyRooms.writeInt(0);
            MyRooms.writeInt(0);
            MyRooms.writeBoolean(true);
            MyRooms.writeBoolean(true);
        }
        MyRooms.writeBoolean(false);
        MyRooms.Send(Client.Socket);
        
	}
}
