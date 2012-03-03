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

public class LoadUserMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		if(cUser.RankLevel < 5)
        {
			return;
        }
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        int UserId = Client.inreader.readInt();
        Habbo User = Habbo.UsersbyId.get(UserId);
        if(User == null)
        	return;
        
        ServerMessage UserInfo = new ServerMessage(ServerEvents.MOD_SHOWUSERINFO);
        UserInfo.writeInt(UserId);
        UserInfo.writeUTF(User.UserName);
        UserInfo.writeInt(0); // reg timestamp (minutes)
        UserInfo.writeInt(0); // login timestamp (minutes)
        UserInfo.writeBoolean(User.IsOnline);
        UserInfo.writeInt(1); // cfhs
        //UserInfo.writeBoolean(true);
        UserInfo.writeInt(5); // cfhs abusive
        UserInfo.writeInt(3); // cautions
        UserInfo.writeInt(2); // bans
        UserInfo.Send(Client.Socket);
        
        ServerMessage Chatlogs = new ServerMessage(ServerEvents.MOD_SHOWUSERCHATLOG);
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
