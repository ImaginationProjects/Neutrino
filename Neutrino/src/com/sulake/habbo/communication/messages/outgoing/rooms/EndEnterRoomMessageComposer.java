package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.Iterator;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomManager;

import org.jboss.netty.channel.Channel;

public class EndEnterRoomMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = cUser;
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        ServerMessage SomeData = new ServerMessage(ServerEvents.RoomData5);
        SomeData.writeInt(0);
        SomeData.Send(Socket);
        
        R.WallItems = RoomItem.GetWallItemsForRoom(RoomId);
        R.FloorItems = RoomItem.GetFloorItemsForRoom(RoomId);
        
        ServerMessage WallItems = new ServerMessage(ServerEvents.WallItems);
        WallItems.writeInt(1);
        WallItems.writeInt(R.OwnerId);
        WallItems.writeUTF(R.OwnerName);
        WallItems.writeInt(R.WallItems.size());
        Iterator ireader = R.WallItems.iterator();
        while(ireader.hasNext())
        {
            RoomItem Item = (RoomItem)ireader.next();
            Server.WriteLine("Serialized: " + Item.W);
            ItemInformation furnidata = ItemInformation.Items.get(Item.FurniId);
            WallItems.writeUTF(Item.Id + "");
            WallItems.writeInt(furnidata.SpriteId);
            WallItems.writeUTF(Item.W);
            WallItems.writeUTF(Item.ExtraData);
            WallItems.writeInt((furnidata.Interactor.equals("default")) ? 1 : 0);
            WallItems.writeInt(R.OwnerId);
        }
        WallItems.Send(Socket);
        
        ServerMessage RoomItems = new ServerMessage(ServerEvents.FloorItems);
        RoomItems.writeInt(1);
        RoomItems.writeInt(R.OwnerId);
        RoomItems.writeUTF(R.OwnerName);
        RoomItems.writeInt(R.FloorItems.size());
        Iterator reader = R.FloorItems.iterator();
        while(reader.hasNext())
        {
            RoomItem Item = (RoomItem)reader.next();
            ItemInformation furnidata = ItemInformation.Items.get(Item.FurniId);
            RoomItems.writeInt(Item.Id);
            RoomItems.writeInt(furnidata.SpriteId);
            RoomItems.writeInt(Item.X);
            RoomItems.writeInt(Item.Y);
            RoomItems.writeInt(Item.Rot);
            RoomItems.writeUTF("0.0");
            RoomItems.writeInt(0);
            RoomItems.writeInt(0);
            RoomItems.writeUTF(Item.ExtraData);
            RoomItems.writeInt(-1);
            RoomItems.writeInt((furnidata.Interactor.equals("default")) ? 1 : 0);
            RoomItems.writeInt(R.OwnerId);
        }
        RoomItems.Send(Socket);
        
        ServerMessage UserData1 = new ServerMessage(ServerEvents.preUserData);
        UserData1.writeInt(1);
        UserData1.writeInt(CurrentUser.Id);
        UserData1.writeUTF(CurrentUser.UserName);
        UserData1.writeInt(0);
        UserData1.Send(Socket);
        
        CurrentUser.X = R.GetModel().DoorX;
        CurrentUser.Y = R.GetModel().DoorY;
        CurrentUser.Rot = R.GetModel().DoorDir;
        R.UserList.add(CurrentUser);
        R.CurrentUsers++;
        
        ServerMessage UserData = new ServerMessage(ServerEvents.UserData);
        UserData.writeInt(R.UserList.size());
        Iterator treader = R.UserList.iterator();
        while(treader.hasNext())
        {
            Habbo User = (Habbo)treader.next();
            Server.WriteLine("Loaded: " + User.UserName);
            UserData.writeInt(User.Id);
            UserData.writeUTF(User.UserName);
            UserData.writeUTF(User.Motto);
            UserData.writeUTF(User.Look);
            UserData.writeInt(User.SessionId); // session id
            UserData.writeInt(User.X);
            UserData.writeInt(User.Y);
            UserData.writeUTF("0.0"); // user z
            UserData.writeInt(User.Rot);
            UserData.writeInt(1);
            UserData.writeUTF(User.Gender.toLowerCase());
            UserData.writeInt(-1);
            UserData.writeInt(-1);
            UserData.writeInt(0);
            UserData.writeInt(525);
        }
        UserData.Send(Socket);
        
        ServerMessage ExtraData = new ServerMessage(ServerEvents.VipWallsAndFloors);
        ExtraData.writeInt(R.VipWalls);
        ExtraData.writeInt(R.VipFloors);
        ExtraData.writeBoolean(R.Hidewalls);
        ExtraData.Send(Socket);
        
        CurrentUser.UpdateState("", Socket, Server);
        
        ServerMessage RoomPanel = new ServerMessage(ServerEvents.RoomPanel);
        RoomPanel.writeBoolean(true);
        RoomPanel.writeInt(RoomId);
        RoomPanel.writeBoolean(true);
        RoomPanel.Send(Socket);
        
        ServerMessage RoomData = new ServerMessage(ServerEvents.RoomData);
        RoomData.writeBoolean(true);
        RoomData.writeInt(RoomId);
        RoomData.writeBoolean(false);
        RoomData.writeUTF(R.Name);
        RoomData.writeInt(R.OwnerId);
        RoomData.writeUTF(R.OwnerName);
        RoomData.writeInt(0);
        RoomData.writeInt(R.CurrentUsers);
        RoomData.writeInt(R.MaxUsers);
        RoomData.writeUTF(R.Description);
        RoomData.writeInt(0);
        RoomData.writeInt((R.Category == 3) ? 2 : 0);
        RoomData.writeInt(R.Score);
        RoomData.writeInt(R.Category);
        RoomData.writeUTF("");
        RoomData.writeInt(0);
        RoomData.writeInt(0);
        RoomData.writeInt(R.Tags.size());
        // Icons
        RoomData.writeInt(0);
        RoomData.writeInt(0);
        RoomData.writeInt(0);
        // bools
        RoomData.writeBoolean(true);
        RoomData.writeBoolean(true);
        RoomData.writeBoolean(false);
        RoomData.writeBoolean(false);
        RoomData.writeBoolean(false);
        RoomData.Send(Socket);
        
        if(!RoomManager.Managers.containsKey(RoomId))
        	RoomManager.AddRoomToProcess(RoomId);
        

	}
}
