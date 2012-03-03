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

public class AddItemToMyRoomMessageComposer {
	public String WallPositionCheck(String wallPosition)
    {
		
        //:w=3,2 l=9,63 l
        try
        {
            if (wallPosition.contains((char)13 + ""))
            { return null; }
            if (wallPosition.contains((char)9 + ""))
            { return null; }

            String[] posD = wallPosition.split(" ");
            if (posD[2] != "l" && posD[2] != "r")
                return null;

            String[] widD = posD[0].substring(3).split(",");
            int widthX = Integer.parseInt(widD[0]);
            int widthY = Integer.parseInt(widD[1]);
            if (widthX < 0 || widthY < 0 || widthX > 200 || widthY > 200)
                return null;

            String[] lenD = posD[1].substring(2).split(",");
            int lengthX = Integer.parseInt(lenD[0]);
            int lengthY = Integer.parseInt(lenD[1]);
            if (lengthX < 0 || lengthY < 0 || lengthX > 200 || lengthY > 200)
                return null;

            return ":w=" + widthX + "," + widthY + " l=" + lengthX + "," + lengthY + " " + posD[2];
        }
        catch (Exception e)
        {
            return null;
        }
    }
	
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		String[] Data = Client.inreader.readUTF().split(" ");
        int ItemId = (Integer.parseInt(Data[0])); 
        UserItem Item = UserItem.Items.get(ItemId);
        ItemInformation furniData = ItemInformation.Items.get(Item.ItemId);
        Habbo User = Client.GetSession();
        Room RoomData = Room.Rooms.get(User.CurrentRoomId);
        if(!RoomData.HavePowers(cUser.Id))
        {
        	return;
        }
        String ExtraData = Item.ExtraData;
        if(Data[1].startsWith(":"))
        {
        	String wallPosition = Data[1] + " " + Data[2] + " " + Data[3];
        	//Server.WriteLine(wallPosition);
            /*String WallItem = (new AddItemToMyRoomMessageComposer()).WallPositionCheck(wallPosition);
            if(WallItem == null)
            	return;*/
            
            //Server.WriteLine("Checked!");
            String WallItem = wallPosition;
            UserItem.DeleteItem(ItemId, Server);
            ServerMessage Message = new ServerMessage(ServerEvents.UpdateInventory);
            Message.Send(Client.Socket);
            
            RoomItem I = new RoomItem();
            I.Id = RoomItem.LastId + 1;
            RoomItem.LastId++;
            I.RoomId = User.CurrentRoomId;
            I.FurniId = Item.ItemId;
            I.MoodLight = "";
            I.W = WallItem;
            I.X = -1;
            I.Y = -1;
            I.Rot = -1;
            I.ExtraData = "";
            RoomItem.Items.put(I.Id, I);
            RoomData.WallItems.add(I);
            Server.GetDatabase().executeUpdate("INSERT INTO rooms_items (id, room_id, furni_id, extradata, x, y, w, rot, moodlight, wired_string, wired_items) VALUES (" + I.Id + ", '" + User.CurrentRoomId + "', '" + Item.ItemId + "', '0', '0', '0', '" + WallItem + "', '0','','','');");
            
            ItemInformation FurniData = ItemInformation.Items.get(I.FurniId);
            ServerMessage AddWallItem = new ServerMessage(ServerEvents.AddWallItem);
            AddWallItem.writeUTF(I.Id + "");
            AddWallItem.writeInt(FurniData.SpriteId);
            AddWallItem.writeUTF(I.W);
            AddWallItem.writeUTF("");
            AddWallItem.writeInt((FurniData.Interactor.equals("default")) ? 1 : 0);
            AddWallItem.writeInt(RoomData.OwnerId);
            AddWallItem.writeUTF(RoomData.OwnerName);
            UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, AddWallItem);
        } else {
            // Add FloorItem
            int X = Integer.parseInt(Data[1]); 
            int Y = Integer.parseInt(Data[2]); 
            int Rot = Integer.parseInt(Data[3]); 
            UserItem.DeleteItem(ItemId, Server);
            ServerMessage Message = new ServerMessage(ServerEvents.UpdateInventory);
            Message.Send(Client.Socket);
            
            RoomItem I = new RoomItem();
            I.Id = RoomItem.LastId + 1;
            RoomItem.LastId++;
            I.RoomId = User.CurrentRoomId;
            I.FurniId = Item.ItemId;
            I.W = "";
            I.MoodLight = "";
            I.X = X;
            I.Y = Y;
            I.Rot = Rot;
            I.ExtraData = "";
            RoomItem.Items.put(I.Id, I);
            RoomData.FloorItems.add(I);
            Server.GetDatabase().executeUpdate("INSERT INTO rooms_items (id, room_id, furni_id, extradata, x, y, w, rot, moodlight, wired_string, wired_items) VALUES (" + I.Id + ", '" + User.CurrentRoomId + "', '" + Item.ItemId + "', '" + ExtraData + "', '" + X + "', '" + Y + "', '', '" + Rot + "','','','');");
            
            ServerMessage FloorItem = new ServerMessage(ServerEvents.AddFloorItem);
            FloorItem.writeInt(I.Id);
            FloorItem.writeInt(furniData.SpriteId);
            FloorItem.writeInt(X);
            FloorItem.writeInt(Y);
            FloorItem.writeInt(Rot);
            FloorItem.writeUTF("0.0");
            FloorItem.writeInt(0);
            FloorItem.writeInt(0);
            FloorItem.writeUTF(ExtraData);
            FloorItem.writeInt(-1);
            FloorItem.writeInt((furniData.Interactor.equals("default")) ? 1 : 0);
            FloorItem.writeInt(RoomData.OwnerId);
            FloorItem.writeUTF(RoomData.OwnerName);
            UserManager.SendMessageToUsersOnRoomId(I.RoomId, FloorItem);
        }
	}

}
