package com.sulake.habbo.communication.messages.outgoing.rooms;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.ItemManager.UserItem;
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

public class PickUpItemMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		try {
		Room RoomData = Room.Rooms.get(cUser.CurrentRoomId);
		int lol = Client.inreader.readInt();
		int ItemId = Client.inreader.readInt();
		Server.WriteLine("To remove: " + ItemId);
		RoomItem Item = RoomItem.Items.get(ItemId);
		if(Item.equals(null))
		{
			Server.WriteLine("lol");
			return;
		}
        ItemInformation furniData = ItemInformation.Items.get(Item.FurniId);
        if(!RoomData.HavePowers(cUser.Id))
        {
        	return;
        }
        Server.GetDatabase().executeUpdate("DELETE FROM rooms_items WHERE id = " + Item.Id);        
        Server.GetDatabase().executeUpdate("INSERT INTO users_items (id, userid, itemid, extradata) VALUES (NULL, '" + cUser.Id + "', '" + Item.FurniId + "', '');");
        UserItem I = new UserItem();
        I.Id = UserItem.LastId + 1;
        UserItem.LastId++;
        I.UserId = cUser.Id;
        I.ItemId = Item.FurniId;
        I.ExtraData = "";
        UserItem.Items.put(I.Id, I);
        RoomItem.Items.remove(Item.Id);
        
        if(furniData.Type.equals("s"))
        {
        	ServerMessage RemoveFloorItem = new ServerMessage(ServerEvents.RemoveFloorItem);
        	RemoveFloorItem.writeUTF(Item.Id + "");
        	RemoveFloorItem.writeInt(0);
        	RemoveFloorItem.writeInt(cUser.Id);
        	UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, RemoveFloorItem);
            RoomData.FloorItems = RoomItem.GetFloorItemsForRoom(RoomData.Id);
        } else {
        	ServerMessage RemoveWallItem = new ServerMessage(ServerEvents.RemoveWallItem);
        	RemoveWallItem.writeUTF(I.Id + "");
        	RemoveWallItem.writeInt(cUser.Id);
        	UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, RemoveWallItem);
            RoomData.WallItems = RoomItem.GetWallItemsForRoom(RoomData.Id);
        }
        
        
        ServerMessage Remove = new ServerMessage(ServerEvents.RemoveWhatItem);
    	Remove.writeInt(Item.Id);
    	UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, Remove);
    	
        ServerMessage Message = new ServerMessage(ServerEvents.UpdateInventory);
        Message.Send(Client.Socket);
    	/*ServerMessage FloorInventory = new ServerMessage(ServerEvents.AddItemToInventary);
    	if(lol == 2)
    	{
    		FloorInventory.writeInt(I.Id);
            FloorInventory.writeUTF(furniData.Type.toUpperCase());
            FloorInventory.writeInt(furniData.Id);
            FloorInventory.writeInt(furniData.SpriteId);
            FloorInventory.writeInt(1);
            FloorInventory.writeUTF(Item.ExtraData);
            FloorInventory.writeInt(0);
            FloorInventory.writeBoolean(furniData.CanRecycle);
            FloorInventory.writeBoolean(furniData.CanTrade);
            FloorInventory.writeBoolean(furniData.CanStack);
            FloorInventory.writeBoolean(furniData.CanSell);
            FloorInventory.writeInt(-1);
            FloorInventory.writeUTF("");
            FloorInventory.writeInt(0);
            FloorInventory.writeInt(24);
    	} else {
    		FloorInventory.writeInt(I.Id);
            FloorInventory.writeUTF(furniData.Type.toUpperCase());
            FloorInventory.writeInt(furniData.Id);
            FloorInventory.writeInt(furniData.SpriteId);
            FloorInventory.writeInt(1);
            FloorInventory.writeInt(0);
            FloorInventory.writeUTF(Item.ExtraData);
            FloorInventory.writeBoolean(furniData.CanRecycle);
            FloorInventory.writeBoolean(furniData.CanTrade);
            FloorInventory.writeBoolean(furniData.CanStack);
            FloorInventory.writeBoolean(furniData.CanSell);
            FloorInventory.writeInt(-1);
            FloorInventory.writeInt(25);
    	}*/
    	//FloorInventory.Send(Client.Socket);
		} catch (NullPointerException e)
		{
			Server.WriteLine(e.toString());
		}
	}
}
