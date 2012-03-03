package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.SubscriptionManager;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class AddChairToPetMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room RoomData = Room.Rooms.get(RoomId);
        
        int ItemOfChair = Client.inreader.readInt();
        int PetId = Client.inreader.readInt();
        Pet P = Pet.Pets.get(PetId);
        P.HaveChair = true;
        
        Server.GetDatabase().executeUpdate("UPDATE pets SET havechair = '1' WHERE id = " + P.Id);
        ServerMessage AddToPet = new ServerMessage(ServerEvents.AddCharToPet);
        AddToPet.writeInt(P.SessionId);
        AddToPet.writeInt(P.Id);
        AddToPet.writeInt(P.Race);
        AddToPet.writeInt(P.Type);
        AddToPet.writeUTF(P.Color.toLowerCase());
        AddToPet.writeInt(3);
        AddToPet.writeInt(3);
        AddToPet.writeInt(3);
        AddToPet.writeInt(-1);
        AddToPet.writeInt(1);
        AddToPet.writeInt(4);
        AddToPet.writeInt(9);
        AddToPet.writeInt(0);
        AddToPet.writeInt(2);
        AddToPet.writeInt(-1);
        AddToPet.writeInt(1);
        AddToPet.writeBoolean(P.HaveChair);
        AddToPet.writeBoolean(P.HaveUserOnMe);
        UserManager.SendMessageToUsersOnRoomId(P.RoomId, AddToPet);
        P.UpdateOnlyOneState("eyb");
        
        RoomItem Item = RoomItem.Items.get(ItemOfChair);
		if(Item.equals(null))
		{
			Server.WriteLine("lol");
			return;
		}
        RoomItem.Items.remove(Item.Id);
        Server.GetDatabase().executeUpdate("DELETE FROM rooms_items WHERE id = " + Item.Id);
        ItemInformation furniData = ItemInformation.Items.get(Item.FurniId);
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
        	RemoveWallItem.writeUTF(Item.Id + "");
        	RemoveWallItem.writeInt(cUser.Id);
        	UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, RemoveWallItem);
            RoomData.WallItems = RoomItem.GetWallItemsForRoom(RoomData.Id);
        }
     }
}
