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

public class PerformRoomActionMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		if(cUser.RankLevel < 5)
        {
			return;
        }
		Habbo CurrentUser = Client.GetSession();
        int RoomId = Client.inreader.readInt();
        Room RoomData = Room.Rooms.get(RoomId);
        Channel Socket = Client.Socket;
        boolean DoorBell = (Client.inreader.readInt() == 1);    
        boolean InapropiateRoom = (Client.inreader.readInt() == 1);
        boolean KickAllUsers = (Client.inreader.readInt() == 1);
        
        if(DoorBell)
        {
        	RoomData.State = 1;
        	Server.GetDatabase().executeUpdate("UPDATE rooms SET state = '1' WHERE id = " + RoomId);
        } 
        if(InapropiateRoom)
        {
        	RoomData.Name = "Sala contra las normas";
        	RoomData.Description = "Sala contra las normas";
        	Server.GetDatabase().executeUpdate("UPDATE rooms SET name = \"" + RoomData.Name + "\" WHERE id = " + RoomId);
        	Server.GetDatabase().executeUpdate("UPDATE rooms SET description = \"" + RoomData.Description + "\" WHERE id = " + RoomId);
        } 
        if(KickAllUsers)
        {
        	// soon
        }
        
        if(DoorBell || InapropiateRoom)
        {
        	ServerMessage sRoomData = new ServerMessage(ServerEvents.RoomData);
            sRoomData.writeBoolean(true);
            sRoomData.writeInt(RoomId);
            sRoomData.writeBoolean(false);
            sRoomData.writeUTF(RoomData.Name);
            sRoomData.writeInt(RoomData.OwnerId);
            sRoomData.writeUTF(RoomData.OwnerName);
            sRoomData.writeInt(0);
            sRoomData.writeInt(RoomData.CurrentUsers);
            sRoomData.writeInt(RoomData.MaxUsers);
            sRoomData.writeUTF(RoomData.Description);
            sRoomData.writeInt(0);
            sRoomData.writeInt((RoomData.Category == 3) ? 2 : 0);
            sRoomData.writeInt(RoomData.Score);
            sRoomData.writeInt(RoomData.Category);
            sRoomData.writeUTF("");
            sRoomData.writeInt(0);
            sRoomData.writeInt(0);
            sRoomData.writeInt(RoomData.Tags.size());
            // Icons
            sRoomData.writeInt(0);
            sRoomData.writeInt(0);
            sRoomData.writeInt(0);
            // bools
            sRoomData.writeBoolean(true);
            sRoomData.writeBoolean(true);
            sRoomData.writeBoolean(false);
            sRoomData.writeBoolean(false);
            sRoomData.writeBoolean(false);
            UserManager.SendMessageToUsersOnRoomId(RoomId, sRoomData);
        }
	}
}
