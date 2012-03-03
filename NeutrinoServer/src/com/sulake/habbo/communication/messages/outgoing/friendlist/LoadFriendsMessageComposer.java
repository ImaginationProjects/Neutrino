package com.sulake.habbo.communication.messages.outgoing.friendlist;

import java.util.*;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class LoadFriendsMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		ServerMessage Friends = new ServerMessage(ServerEvents.Friends);
        Friends.writeInt(900);
        Friends.writeInt(500);
        Friends.writeInt(300);
        Friends.writeInt(900);
        Friends.writeInt(0); // categorys
        List<Friend> FriendsList = Friend.SelectFriendsForId(User.Id);
        Friends.writeInt(FriendsList.size()); // friends
        Iterator reader = FriendsList.iterator();
        while(reader.hasNext())
        {
        	Friend F = (Friend)reader.next();
        	Habbo fUser = Habbo.UsersbyId.get(F.FriendId);
        	Friends.writeInt(fUser.Id);
        	Friends.writeUTF(fUser.UserName);
        	Friends.writeInt(1);
        	Friends.writeBoolean(fUser.IsOnline);
        	Friends.writeBoolean(fUser.IsOnRoom);
        	Friends.writeUTF(fUser.Look);
        	Friends.writeInt(F.CategoryId);
        	Friends.writeUTF(fUser.Motto);
        	Friends.writeUTF("14-08-2011 09:55:52");
        	Friends.writeInt(0);
        }
        Friends.Send(Client.Socket);
        
        ServerMessage PendingFriends = new ServerMessage(ServerEvents.PendingFriends);
        List<Friend> PendingFriendsList = Friend.SelectPendingFriendsForId(User.Id);
        PendingFriends.writeInt(PendingFriendsList.size()); // friends
        PendingFriends.writeInt(PendingFriendsList.size()); // friends
        Iterator reader2 = PendingFriendsList.iterator();
        while(reader2.hasNext())
        {
        	Friend F = (Friend)reader2.next();
        	Habbo fUser = Habbo.UsersbyId.get(F.FriendId);
        	System.out.println("User: " + fUser.UserName + ", me: " + User.UserName);
        	PendingFriends.writeInt(fUser.Id);
        	PendingFriends.writeUTF(fUser.UserName);
        	PendingFriends.writeUTF(fUser.Look);
        }
        PendingFriends.Send(Client.Socket);
	}
}
