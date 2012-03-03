package com.sulake.habbo.communication.messages.outgoing.navigator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Environment;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class CreateNewRoomMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		Habbo CurrentHabbo = Client.GetSession();
		
		String Name = Client.inreader.readUTF();
		String Model = Client.inreader.readUTF();
		
		int RoomId = Room.LastId + 1;
		Room.LastId++;
		Room R = Room.AddRoom(RoomId, Name, Model, User.Id, User.UserName);
		Server.GetDatabase().executeUpdate("INSERT INTO rooms (id, ownerid, name, description, state, maxusers, score, category, tags, model, public_room, icon_bg, icon_fg, wall, floor, landscape) VALUES (NULL , '" + User.Id + "', '" + Name + "', '', '0', '" + R.MaxUsers + "', '0', '0', '', '" + Model + "', '0', '0', '0', 0, 0 ,'0.0');");
		
		ServerMessage Can = new ServerMessage(ServerEvents.CreateNewRoom);
		Can.writeInt(RoomId);
		Can.writeUTF(Name);
		Can.Send(Client.Socket);
	}

}

