package com.sulake.habbo.communication.messages.outgoing.navigator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class SearchForAPageMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		Habbo CurrentHabbo = Client.GetSession();
		String Page = Client.inreader.readUTF();
		if(Page.startsWith("owner:"))
		{
			Page = Page.split(":")[1];
		}
		List<Room> mRooms = new ArrayList<Room>();
		if(Habbo.GetUserForName(Page) != null)
		{
			mRooms = Room.GetRoomsForUserId(Habbo.GetUserForName(Page).Id);
		} else if(Room.GetRoomsForRoomName(Page).size() > 0)
		{
			mRooms = Room.GetRoomsForRoomName(Page);
		}
        ServerMessage MyRooms = new ServerMessage(ServerEvents.LookRooms);
        MyRooms.writeInt(8);
        MyRooms.writeUTF("");
        MyRooms.writeInt(mRooms.size());
        Iterator reader = mRooms.iterator();
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
