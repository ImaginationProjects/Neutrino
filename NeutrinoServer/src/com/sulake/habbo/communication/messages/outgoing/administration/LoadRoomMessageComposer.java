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

public class LoadRoomMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		if(cUser.RankLevel < 5)
        {
			return;
        }
		Habbo CurrentUser = Client.GetSession();
        int RoomId = Client.inreader.readInt();
        Channel Socket = Client.Socket;
        Room RoomData = Room.Rooms.get(RoomId);
        
        ServerMessage RoomInfo = new ServerMessage(ServerEvents.MOD_SHOWROOMDATA);
        RoomInfo.writeInt(RoomData.Id);
        RoomInfo.writeInt(RoomData.CurrentUsers);
        RoomInfo.writeBoolean((Habbo.UsersbyId.get(RoomData.OwnerId).CurrentRoomId == RoomData.Id)); // Is owner on room?
        RoomInfo.writeInt(RoomData.OwnerId);
        RoomInfo.writeUTF(RoomData.OwnerName);
        RoomInfo.writeBoolean(false); // boolean value
        RoomInfo.writeBoolean(true); // boolean value
        RoomInfo.writeUTF(RoomData.Name);
        RoomInfo.writeUTF(RoomData.Description);
        RoomInfo.writeInt(0);
        // serialize tags
        RoomInfo.writeInt(0); // has event
        RoomInfo.writeInt(0); // has event
        RoomInfo.writeInt(0); // has event
        RoomInfo.Send(Client.Socket);
        
        /*ServerMessage Chatlogs = new ServerMessage(ServerEvents.MOD_SHOWUSERCHATLOG);
    	/*Chatlogs.writeBoolean(false);
    	Chatlogs.writeInt(R.Id);
    	Chatlogs.writeUTF(R.Name);
    	List<Chatlog> Chats = Chatlog.GetChatlogsForRoomId(R.Id);
    	Chatlogs.writeInt(Chats.size());
    	Iterator reader4 = Chats.iterator();
    	while(reader4.hasNext())
    	{
    		Chatlog C = (Chatlog)reader4.next();
    		Chatlogs.writeInt(C.Hour);
    		Chatlogs.writeInt(C.Minute);
    		Chatlogs.writeInt(C.UserId);
    		Chatlogs.writeUTF(Habbo.UsersbyId.get(C.UserId).UserName);
    		Chatlogs.writeUTF(C.Message);
    	}*/
	}
}
