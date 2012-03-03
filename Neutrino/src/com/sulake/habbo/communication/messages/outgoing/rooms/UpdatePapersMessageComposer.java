package com.sulake.habbo.communication.messages.outgoing.rooms;
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

public class UpdatePapersMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		int ItemId = Client.in.readInt();
        UserItem Item = UserItem.Items.get(ItemId);
        ItemInformation furniData = ItemInformation.Items.get(Item.ItemId);
        Habbo User = Client.GetSession();
        Room RoomData = Room.Rooms.get(User.CurrentRoomId);
        if(!RoomData.HavePowers(cUser.Id))
        {
        	return;
        }
        String ExtraData = Item.ExtraData;
        String type = "floor";
        String type2 = "floor";
        if(furniData.Name.contains("a2"))
        {
            RoomData.Floor = Integer.parseInt(ExtraData);
        }
        else if(furniData.Name.contains("wall"))
        {
            type = "wall";
            type2 = "wallpaper";
            RoomData.Wall = Integer.parseInt(ExtraData);
        }
        else if(furniData.Name.contains("land"))
        {
            type = "landscape";
            type2 = "landscape";
            RoomData.Landscape = Double.parseDouble(ExtraData);
        }
        
        Server.GetDatabase().executeUpdate("UPDATE rooms SET " + type + " = '" + ExtraData + "' WHERE id = " + User.CurrentRoomId);
        UserItem.DeleteItem(ItemId, Server);
        ServerMessage Message = new ServerMessage(ServerEvents.UpdateInventory);
        Message.Send(Client.Socket);
        
        ServerMessage UpdatePapers = new ServerMessage(ServerEvents.Papers);
        UpdatePapers.writeUTF(type2);
        UpdatePapers.writeUTF(ExtraData);
        UserManager.SendMessageToUsersOnRoomId(User.CurrentRoomId, UpdatePapers);
	}
}
