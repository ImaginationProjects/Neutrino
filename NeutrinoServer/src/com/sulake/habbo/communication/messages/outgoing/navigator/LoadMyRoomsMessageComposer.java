package com.sulake.habbo.communication.messages.outgoing.navigator;

import java.util.Iterator;
import java.util.List;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadMyRoomsMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		Habbo CurrentHabbo = Client.GetSession();
        ServerMessage MyRooms = new ServerMessage(ServerEvents.LookRooms);
        MyRooms.writeInt(5);
        MyRooms.writeUTF("");
        List<Room> mRooms = Room.GetRoomsForUserId(CurrentHabbo.Id);
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
            MyRooms.writeUTF("");
            MyRooms.writeUTF("");
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
