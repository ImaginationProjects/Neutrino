package com.sulake.habbo.communication.messages.outgoing.rooms;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Coord;
import neutrino.RoomManager.OtherPathfinder;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.Rotation;

import org.jboss.netty.channel.Channel;

public class UpdateItemMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server, String Type) throws Exception
	{
		if(Type == "wall")
	    {
			Habbo User = Client.GetSession();
	        Room RoomData = Room.Rooms.get(User.CurrentRoomId);
	        if(!RoomData.HavePowers(cUser.Id))
	        {
	        	return;
	        }
	        int ItemId = Client.inreader.readInt();
	        String WallPos = Client.inreader.readUTF();
	        
	        RoomItem Item = RoomItem.Items.get(ItemId);
	        ItemInformation furniData = ItemInformation.Items.get(Item.FurniId);
	        Item.W = WallPos;
	        Item.IsUpdated = true;	        

	        RoomData.WallItems.remove(Item);
	        RoomData.WallItems.add(Item);
	        
	        ServerMessage MoveWallItem = new ServerMessage(ServerEvents.UpdateWallItem);
	        MoveWallItem.writeUTF(Item.Id + "");
            MoveWallItem.writeInt(furniData.SpriteId);
            MoveWallItem.writeUTF(Item.W);
            MoveWallItem.writeUTF("");
            MoveWallItem.writeInt((furniData.Interactor.equals("default")) ? 1 : 0);
            MoveWallItem.writeInt(RoomData.OwnerId);
            MoveWallItem.writeUTF(RoomData.OwnerName);
            UserManager.SendMessageToUsersOnRoomId(User.CurrentRoomId, MoveWallItem);
		}
		else if(Type == "floor")
		{
		Habbo User = Client.GetSession();
        Room RoomData = Room.Rooms.get(User.CurrentRoomId);
        if(!RoomData.HavePowers(cUser.Id))
        {
        	return;
        }
        int ItemId = Client.in.readInt();
        int X = Client.in.readInt();
        int Y = Client.in.readInt();
        int Rot = Client.in.readInt();
        RoomItem Item = RoomItem.Items.get(ItemId);
        ItemInformation furniData = ItemInformation.Items.get(Item.FurniId);
        
        Item.X = X;
        Item.Y = Y;
        Item.Rot = Rot;
        Item.IsUpdated = true;
        RoomData.FloorItems.remove(Item);
        RoomData.FloorItems.add(Item);
        
        ServerMessage Update = new ServerMessage(ServerEvents.UpdateFloorItem);
        Update.writeInt(Item.Id);
        Update.writeInt(furniData.SpriteId);
        Update.writeInt(X);
        Update.writeInt(Y);
        Update.writeInt(Rot);
        String H = ((Double)RoomData.GetHForXY(Item.X, Item.Y, Item)).toString().replace(",", ".");
        Update.writeUTF("0.0");
        Update.writeInt(0);
        Update.writeInt(0);
        Update.writeUTF(Item.ExtraData);
        Update.writeInt(-1);
        Update.writeInt((furniData.Interactor.equals("default")) ? 1 : 0);
        Update.writeInt(RoomData.OwnerId);
        UserManager.SendMessageToUsersOnRoomId(User.CurrentRoomId, Update);
		}
	}
}
