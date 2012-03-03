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

public class ChangeLooksMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        String Gender = Client.inreader.readUTF();
        String Look = Client.inreader.readUTF();
        Server.GetDatabase().executeUpdate("UPDATE users SET look = '" + Look + "' WHERE id = " + CurrentUser.Id);
        Server.GetDatabase().executeUpdate("UPDATE users SET gender = '" + Gender + "' WHERE id = " + CurrentUser.Id);
        CurrentUser.Look = Look;
        CurrentUser.Gender = Gender;        
        
        ServerMessage UpdateInfo = new ServerMessage(ServerEvents.UpdateInfo);
        UpdateInfo.writeInt(-1);
        UpdateInfo.writeUTF(Look);
        UpdateInfo.writeUTF(Gender.toLowerCase());
        UpdateInfo.writeUTF(CurrentUser.Motto);
        UpdateInfo.writeInt(525); // achv points
        UpdateInfo.Send(Socket);
        
        ServerMessage UpdateGInfo = new ServerMessage(ServerEvents.UpdateInfo);
        UpdateGInfo.writeInt(CurrentUser.SessionId);
        UpdateGInfo.writeUTF(Look);
        UpdateGInfo.writeUTF(Gender.toLowerCase());
        UpdateGInfo.writeUTF(CurrentUser.Motto);
        UpdateGInfo.writeInt(525); // achv points
        UserManager.SendMessageToUsersOnRoomId(RoomId, UpdateGInfo);	
        CurrentUser.UpdateStateForFriends();
	}
}
