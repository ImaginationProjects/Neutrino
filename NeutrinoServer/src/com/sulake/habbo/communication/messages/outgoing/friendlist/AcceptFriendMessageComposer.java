package com.sulake.habbo.communication.messages.outgoing.friendlist;

import neutrino.Environment;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class AcceptFriendMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		int CountFriends = Client.inreader.readInt();
		for(int i = 0; i < CountFriends; i++)
		{
			int FriendId = Client.inreader.readInt();
			Friend F = Friend.SelectFriendById(User.Id, FriendId);
			if(F == null)
			{
				Server.WriteLine("fail " + User.Id + " " + FriendId); 
				return;
			}
			F.Pending = 0;
			Server.GetDatabase().executeUpdate("UPDATE users_friends SET pending = '0' WHERE userid = " + User.Id + " AND friendid = " + FriendId + ";");
			Server.GetDatabase().executeUpdate("INSERT INTO users_friends (userid, friendid, category, pending) VALUES ('" + F.FriendId + "', '" + F.UserId + "', '0', '0');");
			Server.WriteLine(Friend.Friends.size() + "; " + Friend.SelectFriendsForId(FriendId).size());
			Friend sFriend = new Friend();
			sFriend.UserId = FriendId;
			sFriend.FriendId = User.Id;
			sFriend.CategoryId = 0;
			sFriend.Pending = 0;
			Friend.AddFriend(sFriend);
			Friend oF = Friend.SelectFriendById(FriendId, User.Id);
			
			ServerMessage UpdateState = new ServerMessage(ServerEvents.UpdateFriendState);
			UpdateState.writeInt(0);
			UpdateState.writeInt(1);
			UpdateState.writeInt(1);
			UpdateState.writeInt(User.Id);
			UpdateState.writeUTF(User.UserName);
			UpdateState.writeInt(1);
			UpdateState.writeBoolean(User.IsOnline);
			UpdateState.writeBoolean(User.IsOnRoom);
			UpdateState.writeUTF(User.Look);
			UpdateState.writeInt(F.CategoryId);
			UpdateState.writeUTF(User.Motto);
        	UpdateState.writeInt(0);
        	UpdateState.writeInt(0);
        	UpdateState.writeInt(0);
        	UpdateState.Send(Habbo.UsersbyId.get(FriendId).CurrentSocket);
        	
        	Habbo fUser = Habbo.UsersbyId.get(FriendId);
        	ServerMessage fUpdateState = new ServerMessage(ServerEvents.UpdateFriendState);
			fUpdateState.writeInt(0);
			fUpdateState.writeInt(1);
			fUpdateState.writeInt(1);
			fUpdateState.writeInt(fUser.Id);
			fUpdateState.writeUTF(fUser.UserName);
			fUpdateState.writeInt(1);
			fUpdateState.writeBoolean(fUser.IsOnline);
			fUpdateState.writeBoolean(fUser.IsOnRoom);
			fUpdateState.writeUTF(fUser.Look);
			fUpdateState.writeInt(F.CategoryId);
			fUpdateState.writeUTF(fUser.Motto);
        	fUpdateState.writeInt(0);
        	fUpdateState.writeInt(0);
        	fUpdateState.writeInt(0);
        	fUpdateState.Send(Client.Socket);
		}
	}
}
