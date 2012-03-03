package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.Iterator;
import java.util.concurrent.FutureTask;

import org.jboss.netty.channel.Channel;

import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomManager;

public class ChangeMottoMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        String Motto = Client.inreader.readUTF();
        Server.GetDatabase().executeUpdate("UPDATE users SET motto = '" + Motto + "' WHERE id = " + CurrentUser.Id);
        CurrentUser.Motto = Motto;  
        
        ServerMessage UpdateInfo = new ServerMessage(ServerEvents.UpdateInfo);
        UpdateInfo.writeInt(-1);
        UpdateInfo.writeUTF(CurrentUser.Look);
        UpdateInfo.writeUTF(CurrentUser.Gender.toLowerCase());
        UpdateInfo.writeUTF(Motto);
        UpdateInfo.writeInt(525); // achv points
        UpdateInfo.Send(Socket);
        
        ServerMessage UpdateGInfo = new ServerMessage(ServerEvents.UpdateInfo);
        UpdateGInfo.writeInt(CurrentUser.SessionId);
        UpdateGInfo.writeUTF(CurrentUser.Look);
        UpdateGInfo.writeUTF(CurrentUser.Gender.toLowerCase());
        UpdateGInfo.writeUTF(Motto);
        UpdateGInfo.writeInt(525); // achv points
        UserManager.SendMessageToUsersOnRoomId(RoomId, UpdateGInfo);	
        CurrentUser.UpdateStateForFriends();
	}
}
