package com.sulake.habbo.communication.messages.outgoing.administration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.netty.channel.Channel;

import neutrino.Environment;
import neutrino.AdministrationManager.Chatlog;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadUserChatlogMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		if(cUser.RankLevel < 5)
        {
			return;
        }
		Habbo CurrentUser = Client.GetSession();
        int UserId = Client.inreader.readInt();
        Channel Socket = Client.Socket;
        Habbo User = Habbo.UsersbyId.get(UserId);        
        
        ServerMessage Chatlogs = new ServerMessage(ServerEvents.MOD_SHOWUSERCHATLOG);
    	Chatlogs.writeInt(User.Id);
    	Chatlogs.writeUTF(User.UserName);
    	List<Integer> Chats = new ArrayList<Integer>();
    	Chats = Chatlog.GetRoomsOfChatlogsForUserId(UserId);
    	Chatlogs.writeInt(Chats.size());
    	Iterator reader1 = Chats.iterator();
    	while(reader1.hasNext())
    	{
    		int RoomId = (Integer)reader1.next();
    		Room RoomData = Room.Rooms.get(RoomId);
        	Chatlogs.writeBoolean(false); // is public
        	Chatlogs.writeInt(RoomData.Id);
        	Chatlogs.writeUTF(RoomData.Name);
        	List<Chatlog> ChatlogsLoader2 = new ArrayList<Chatlog>();
        	ChatlogsLoader2 = Chatlog.GetChatlogsForRoomId(RoomData.Id);
        	Chatlogs.writeInt(ChatlogsLoader2.size());
        	Iterator reader = ChatlogsLoader2.iterator();
        	while(reader.hasNext())
        	{
        		Chatlog C = (Chatlog)reader.next();
        		Chatlogs.writeInt(C.Hour);
        		Chatlogs.writeInt(C.Minute);
        		Chatlogs.writeInt(C.UserId);
        		if(Habbo.UsersbyId.containsKey(C.UserId) && !Habbo.UsersbyId.get(C.UserId).UserName.equals(null))
        			Chatlogs.writeUTF(Habbo.UsersbyId.get(C.UserId).UserName);
        		else
        			Chatlogs.writeUTF("Desconocido");
        		if(!C.Message.equals(" ") && !C.Message.equals(""))
        			Chatlogs.writeUTF(C.Message);
        		else
        			Chatlogs.writeUTF("Mensaje irreconocible (' ' o '')");
        	}
    	}
    	Chatlogs.Send(Client.Socket);
	}
	
	/*
	 ServerMessage Chatlogs = new ServerMessage(ServerEvents.MOD_SHOWROOMCHATLOG);
        	Chatlogs.writeBoolean(false);
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
        	}
        	//Chatlogs.Send(Client.Socket);
	 */
}
