package com.sulake.habbo.communication.messages.outgoing.administration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.netty.channel.Channel;

import neutrino.Environment;
import neutrino.AdministrationManager.CallsForHelp;
import neutrino.AdministrationManager.Chatlog;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadCFHChatlogMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		try {
		if(cUser.RankLevel < 5)
        {
			return;
        }
		Habbo CurrentUser = Client.GetSession();
		int CallForHelpID = Client.inreader.readInt();
        CallsForHelp C = CallsForHelp.Calls.get(CallForHelpID);
        if(C == null)
        	return;
        int RoomId = C.RoomId;
        Channel Socket = Client.Socket;
        Room RoomData = Room.Rooms.get(RoomId);
        
        ServerMessage Chatlogs = new ServerMessage(ServerEvents.MOD_SHOWCFHCHATLOG);
        Chatlogs.writeInt(C.Id);
        Chatlogs.writeInt(C.ReporterId);
        Chatlogs.writeInt(C.ReportedId);
        Chatlogs.writeInt(C.RoomId); // Id of room or Id of issue (it shows on the chatlog like: (call for help chatlog <int>)
    	Chatlogs.writeBoolean(false); // is public
    	Chatlogs.writeInt(RoomData.Id);
    	Chatlogs.writeUTF(RoomData.Name);
    	List<Chatlog> ChatlogsLoader = new ArrayList<Chatlog>();
    	ChatlogsLoader = Chatlog.GetChatlogsForRoomId(RoomData.Id);
    	Chatlogs.writeInt(ChatlogsLoader.size());
    	Iterator reader = ChatlogsLoader.iterator();
    	while(reader.hasNext())
    	{
    		Chatlog Ch = (Chatlog)reader.next();
    		Chatlogs.writeInt(Ch.Hour);
    		Chatlogs.writeInt(Ch.Minute);
    		Chatlogs.writeInt(Ch.UserId);
    		if(Habbo.UsersbyId.containsKey(Ch.UserId))
    			Chatlogs.writeUTF(Habbo.UsersbyId.get(Ch.UserId).UserName);
    		else
    			Chatlogs.writeUTF("Desconocido");
    		if(!Ch.Message.equals(" ") && !Ch.Message.equals(""))
    			Chatlogs.writeUTF(Ch.Message);
    		else
    			Chatlogs.writeUTF("Mensaje irreconocible (' ' o '')");
    	}
    	Chatlogs.Send(Client.Socket);
		} catch (NullPointerException e)
		{
			Server.WriteLine(e.getMessage());
		}
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
