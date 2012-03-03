package com.sulake.habbo.communication.messages.outgoing.navigator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadAllRoomsMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		Habbo CurrentHabbo = Client.GetSession();
		String Category = Client.inreader.readUTF();
		int Cat = Integer.parseInt(Category);
        ServerMessage MyRooms = new ServerMessage(ServerEvents.LookRooms);
        MyRooms.writeInt(5);
        MyRooms.writeUTF(Category);
        List<Room> mRooms = null;
        if(Cat == -1)
        	mRooms = Room.GetRoomsForCategory(-1);
        else if(Cat == 0)
        	mRooms = Room.GetRoomsForCategory(8);
        else
        	mRooms = Room.GetRoomsForCategory(Cat);
        
        List<Room> zRoom = new ArrayList<Room>();
        List<Room> finalRooms = new ArrayList<Room>();
        int CurrentMax = 0;
        Iterator reader1 = mRooms.iterator();
        while (reader1.hasNext())
        {
        	Room Data = (Room)reader1.next();
            if (Data.CurrentUsers > 0)
            {
                if (Data.CurrentUsers > CurrentMax)
                    CurrentMax = Data.CurrentUsers;
                zRoom.add(Data);
            }
        }
        
        int MaxRooms = 0;

        for (int i = 0; CurrentMax != i; )
        {
        	Iterator reader2 = zRoom.iterator();
            while (reader2.hasNext())
            {
            	if(MaxRooms >= 50)
            		break;
            	MaxRooms++;
            	Room Data = (Room)reader2.next();
                if (Data.CurrentUsers == CurrentMax)
                    finalRooms.add(Data);
            }
            CurrentMax--;
        }
        
        
        MyRooms.writeInt(finalRooms.size());
        Iterator reader = finalRooms.iterator();
        while(reader.hasNext())
        {
            Room R = (Room)reader.next();
            MyRooms.writeInt(R.Id); // room id
            MyRooms.writeBoolean(false); // event
            MyRooms.writeUTF(R.Name);
            MyRooms.writeInt(R.OwnerId);
            MyRooms.writeUTF(R.OwnerName);
            MyRooms.writeInt(R.State);
            MyRooms.writeInt(R.CurrentUsers);
            MyRooms.writeInt(R.MaxUsers);
            MyRooms.writeUTF(R.Description);
            MyRooms.writeInt(0);
            MyRooms.writeInt((R.Category == 3) ? 2 : 0);
            MyRooms.writeInt(R.Score);
            MyRooms.writeInt(R.Category);
            MyRooms.writeInt(0);
            MyRooms.writeInt(0);
            MyRooms.writeInt(R.Tags.size());
            // Icons
            MyRooms.writeInt(0);
            MyRooms.writeInt(0);
            MyRooms.writeInt(0);
            // bools
            MyRooms.writeUTF("");
            MyRooms.writeBoolean(true);
            MyRooms.writeBoolean(true);
        }
        MyRooms.writeBoolean(false);
        MyRooms.Send(Client.Socket);
	}
}
