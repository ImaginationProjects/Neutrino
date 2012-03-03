package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomEvent;

import org.jboss.netty.channel.Channel;

public class AllowDoorbellMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		String Name = Client.inreader.readUTF();
		Habbo User = Habbo.GetUserForName(Name);
		if(User == null)
			return;
		boolean Allow = Client.inreader.readBoolean();
		Habbo CurrentUser = User;
		if(Allow)
		{			
	        int	RoomId = User.CurrentRoomId;
	        Channel Socket = User.CurrentSocket;
	        Room R = Room.Rooms.get(RoomId);
	        
	        if(!User.IsWaitingForDoorbell)
	        	return;
	        
	        ServerMessage NoDoorbell = new ServerMessage(ServerEvents.GoodDoorbell);
	        NoDoorbell.writeUTF("");
        	NoDoorbell.Send(Socket);	        
	        User.IsWaitingForDoorbell = false;
	        
	        if(!R.UserList.contains(CurrentUser))
	        	R.UserList.add(CurrentUser);
	        R.CurrentUsers = R.UserList.size();
	        
	        ServerMessage Init = new ServerMessage(ServerEvents.InitRoomProcess);
	        Init.Send(Socket);
	        
	        ServerMessage Model = new ServerMessage(ServerEvents.RoomData1);
	        Model.writeUTF(R.Model);
	        Model.writeInt(RoomId);
	        Model.Send(Socket);
	        
	        // send papers
	        if(R.Wall != 0)
	        {
	            ServerMessage Papers = new ServerMessage(ServerEvents.Papers);
	            Papers.writeUTF("wallpaper");
	            Papers.writeUTF(((Integer)(R.Wall)).toString());
	            Papers.Send(Socket);
	        }
	        
	        if(R.Floor != 0)
	        {
	            ServerMessage Papers = new ServerMessage(ServerEvents.Papers);
	            Papers.writeUTF("floor");
	            Papers.writeUTF(((Integer)(R.Floor)).toString());
	            Papers.Send(Socket);
	        }
	        
	        ServerMessage Papers = new ServerMessage(ServerEvents.Papers);
	        Papers.writeUTF("landscape");
	        Papers.writeUTF(((Double)(R.Landscape)).toString());
	        Papers.Send(Socket);
	        
	        if(CurrentUser.Id == R.OwnerId || CurrentUser.RankLevel >= 5)
	        {
	            ServerMessage LoadPows = new ServerMessage(ServerEvents.LoadRightsOnRoom);
	            LoadPows.writeInt(4);
	            LoadPows.Send(Socket);
	            
	            ServerMessage IsOwner = new ServerMessage(ServerEvents.LoadPowersOnRoom);
	            IsOwner.Send(Socket);
	        } else if(R.HavePowers(CurrentUser.Id))
	        {
	        	ServerMessage LoadPows = new ServerMessage(ServerEvents.LoadRightsOnRoom);
	            LoadPows.writeInt(1);
	            LoadPows.Send(Socket);
	        } else {
	        	ServerMessage LoadPows = new ServerMessage(ServerEvents.LoadRightsOnRoom);
	            LoadPows.writeInt(0);
	            LoadPows.Send(Socket);
	        }
	        
	        ServerMessage MoreData = new ServerMessage(ServerEvents.RoomRating);
	        MoreData.writeInt(R.Score);
	        MoreData.writeBoolean(true);
	        MoreData.Send(Socket);
	        
	        ServerMessage RoomEvents = new ServerMessage(ServerEvents.RoomEvents);
	        if(RoomEvent.GetEventForRoomId(RoomId)==null)
	        	RoomEvents.writeUTF("-1");
	        else {
	        	RoomEvent E = RoomEvent.GetEventForRoomId(RoomId);
	        	RoomEvents.writeUTF(E.OwnerId + "");
	            RoomEvents.writeUTF(Habbo.UsersbyId.get(E.OwnerId).UserName);
	            RoomEvents.writeUTF(E.RoomId + "");
	            RoomEvents.writeInt(E.Category);
	            RoomEvents.writeUTF(E.Title);
	            RoomEvents.writeUTF(E.Description);
	            RoomEvents.writeUTF(E.Created);
	            RoomEvents.writeInt(E.Tags.size());
	            Iterator reader = E.Tags.iterator();
	            while(reader.hasNext())
	            {
	            	String tag = (String)reader.next();
	            	RoomEvents.writeUTF(tag);
	            }
	        }
	        RoomEvents.Send(Socket);
	        
	        ServerMessage WallItems = new ServerMessage(ServerEvents.sWallItems);
	        WallItems.writeInt(0);
	        WallItems.Send(Socket);
	        
	        ServerMessage FloorItems = new ServerMessage(ServerEvents.sFloorItems);
	        FloorItems.writeInt(0);
	        FloorItems.Send(Socket);
	        
	        CurrentUser.IsOnRoom = true;
	        CurrentUser.CurrentRoomId = RoomId;
	        User.UpdateStateForFriends();
		} else {
			ServerMessage NoDoorbell = new ServerMessage(ServerEvents.DoorbellNoAnswer);
        	NoDoorbell.Send(User.CurrentSocket);
        	User.IsWaitingForDoorbell = false;
        	User.CurrentRoomId = 0;
		}
	}
}
