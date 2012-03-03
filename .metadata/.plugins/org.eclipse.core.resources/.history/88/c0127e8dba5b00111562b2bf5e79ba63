package com.sulake.habbo.communication.messages.outgoing.userinformation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

import org.jboss.netty.channel.Channel;

public class LoadInventaryMessageComposer {
	
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		int RoomId = cUser.CurrentRoomId;
        Channel Socket = cUser.CurrentSocket;
        Room R = Room.Rooms.get(RoomId);
        
        List<UserItem> AllItems = UserItem.GetItemsbyUserId(cUser.Id);
        List<UserItem> WallItems = new ArrayList<UserItem>();
        List<UserItem> FloorItems = new ArrayList<UserItem>();
        Iterator reader = AllItems.iterator();
        while(reader.hasNext())
        {
            UserItem I = (UserItem)(reader.next());
            ItemInformation furniData = ItemInformation.Items.get(I.ItemId);
            if(furniData.Type.equals("s"))
                FloorItems.add(I);
            else
            {
                WallItems.add(I);
            }
        }
        
        ServerMessage FloorInventory = new ServerMessage(ServerEvents.SendInventory);
        FloorInventory.writeUTF("S");
        FloorInventory.writeInt(1);
        FloorInventory.writeInt(1);
        FloorInventory.writeInt(FloorItems.size());
        Iterator Rreader = FloorItems.iterator();
        while(Rreader.hasNext())
        {
            UserItem I = (UserItem)(Rreader.next());
            ItemInformation furniData = ItemInformation.Items.get(I.ItemId);
            FloorInventory.writeInt(I.Id);
            FloorInventory.writeUTF(furniData.Type.toUpperCase());
            FloorInventory.writeInt(I.Id);
            FloorInventory.writeInt(furniData.SpriteId);
            FloorInventory.writeInt(1);
            FloorInventory.writeUTF(I.ExtraData);
            FloorInventory.writeInt(0);
            FloorInventory.writeBoolean(furniData.CanRecycle);
            FloorInventory.writeBoolean(furniData.CanTrade);
            FloorInventory.writeBoolean(furniData.CanStack);
            FloorInventory.writeBoolean(furniData.CanSell);
            FloorInventory.writeInt(-1);
            FloorInventory.writeUTF("");
            FloorInventory.writeInt(0);
        }
        FloorInventory.Send(Socket);
        
        ServerMessage WallInventory = new ServerMessage(ServerEvents.SendInventory);
        WallInventory.writeUTF("I");
        WallInventory.writeInt(1);
        WallInventory.writeInt(1);
        WallInventory.writeInt(WallItems.size());
        Iterator Wreader = WallItems.iterator();
        while(Wreader.hasNext())
        {
            UserItem I = (UserItem)(Wreader.next());
            ItemInformation furniData = ItemInformation.Items.get(I.ItemId);
            WallInventory.writeInt(I.Id);
            WallInventory.writeUTF(furniData.Type.toUpperCase());
            WallInventory.writeInt(I.Id);
            WallInventory.writeInt(furniData.SpriteId);
            if(furniData.Name.contains("a2"))
                WallInventory.writeInt(3);
            else if(furniData.Name.contains("wall"))
                WallInventory.writeInt(2);
            else if(furniData.Name.contains("land"))
                WallInventory.writeInt(4);
            else
                WallInventory.writeInt(1);
            WallInventory.writeInt(0);
            WallInventory.writeUTF(I.ExtraData);
            WallInventory.writeBoolean(furniData.CanRecycle);
            WallInventory.writeBoolean(furniData.CanTrade);
            WallInventory.writeBoolean(furniData.CanStack);
            WallInventory.writeBoolean(furniData.CanSell);
            WallInventory.writeInt(-1);
        }	
        WallInventory.Send(cUser.CurrentSocket);
	}
	
	public static void Composer(Habbo cUser) throws Exception
	{
		int RoomId = cUser.CurrentRoomId;
        Channel Socket = cUser.CurrentSocket;
        Room R = Room.Rooms.get(RoomId);
        
        List<UserItem> AllItems = UserItem.GetItemsbyUserId(cUser.Id);
        List<UserItem> WallItems = new ArrayList<UserItem>();
        List<UserItem> FloorItems = new ArrayList<UserItem>();
        Iterator reader = AllItems.iterator();
        while(reader.hasNext())
        {
            UserItem I = (UserItem)(reader.next());
            ItemInformation furniData = ItemInformation.Items.get(I.ItemId);
            if(furniData.Type.equals("s"))
                FloorItems.add(I);
            else
            {
                WallItems.add(I);
            }
        }
        
        ServerMessage FloorInventory = new ServerMessage(ServerEvents.SendInventory);
        FloorInventory.writeUTF("S");
        FloorInventory.writeInt(1);
        FloorInventory.writeInt(1);
        FloorInventory.writeInt(FloorItems.size());
        Iterator Rreader = FloorItems.iterator();
        while(Rreader.hasNext())
        {
            UserItem I = (UserItem)(Rreader.next());
            ItemInformation furniData = ItemInformation.Items.get(I.ItemId);
            FloorInventory.writeInt(I.Id);
            FloorInventory.writeUTF(furniData.Type.toUpperCase());
            FloorInventory.writeInt(I.Id);
            FloorInventory.writeInt(furniData.SpriteId);
            FloorInventory.writeInt(1);
            FloorInventory.writeUTF(I.ExtraData);
            FloorInventory.writeInt(0);
            FloorInventory.writeBoolean(furniData.CanRecycle);
            FloorInventory.writeBoolean(furniData.CanTrade);
            FloorInventory.writeBoolean(furniData.CanStack);
            FloorInventory.writeBoolean(furniData.CanSell);
            FloorInventory.writeInt(-1);
            FloorInventory.writeUTF("");
            FloorInventory.writeInt(0);
        }
        FloorInventory.Send(Socket);
        
        ServerMessage WallInventory = new ServerMessage(ServerEvents.SendInventory);
        WallInventory.writeUTF("I");
        WallInventory.writeInt(1);
        WallInventory.writeInt(1);
        WallInventory.writeInt(WallItems.size());
        Iterator Wreader = WallItems.iterator();
        while(Wreader.hasNext())
        {
            UserItem I = (UserItem)(Wreader.next());
            ItemInformation furniData = ItemInformation.Items.get(I.ItemId);
            WallInventory.writeInt(I.Id);
            WallInventory.writeUTF(furniData.Type.toUpperCase());
            WallInventory.writeInt(I.Id);
            WallInventory.writeInt(furniData.SpriteId);
            if(furniData.Name.contains("a2"))
                WallInventory.writeInt(3);
            else if(furniData.Name.contains("wall"))
                WallInventory.writeInt(2);
            else if(furniData.Name.contains("land"))
                WallInventory.writeInt(4);
            else
                WallInventory.writeInt(1);
            WallInventory.writeInt(0);
            WallInventory.writeUTF(I.ExtraData);
            WallInventory.writeBoolean(furniData.CanRecycle);
            WallInventory.writeBoolean(furniData.CanTrade);
            WallInventory.writeBoolean(furniData.CanStack);
            WallInventory.writeBoolean(furniData.CanSell);
            WallInventory.writeInt(-1);
        }	
        WallInventory.Send(cUser.CurrentSocket);
	}

}
