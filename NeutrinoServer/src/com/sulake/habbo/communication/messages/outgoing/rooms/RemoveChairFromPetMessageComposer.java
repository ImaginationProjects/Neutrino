package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.ItemManager.UserItem;
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

public class RemoveChairFromPetMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room RoomData = Room.Rooms.get(RoomId);
        
        int PetId = Client.inreader.readInt();
        Pet P = Pet.Pets.get(PetId);
        P.HaveChair = false;
        
        Server.GetDatabase().executeUpdate("UPDATE pets SET havechair = '0' WHERE id = " + P.Id);
        ServerMessage AddToPet = new ServerMessage(ServerEvents.AddCharToPet);
        AddToPet.writeInt(P.SessionId);
        AddToPet.writeInt(P.Id);
        AddToPet.writeInt(P.Race);
        AddToPet.writeInt(P.Type);
        AddToPet.writeUTF(P.Color.toLowerCase());
        AddToPet.writeInt(3); // state?
        AddToPet.writeInt(0); // have chair lol?
        AddToPet.writeInt(3);
        AddToPet.writeInt(-1);
        AddToPet.writeInt(1); // hair color
        AddToPet.writeInt(4);
        AddToPet.writeInt(9);
        AddToPet.writeInt(0);
        AddToPet.writeInt(2);
        AddToPet.writeInt(-1);
        AddToPet.writeInt(1); // tail color
        AddToPet.writeBoolean(P.HaveChair);
        AddToPet.writeBoolean(P.HaveUserOnMe);
        UserManager.SendMessageToUsersOnRoomId(P.RoomId, AddToPet);
        P.UpdateOnlyOneState("eyb");
        
        // 2963
        Server.GetDatabase().executeUpdate("INSERT INTO users_items (id, userid, itemid, extradata) VALUES (NULL, '" + cUser.Id + "', '2963', '');");
        UserItem I = new UserItem();
        I.Id = UserItem.LastId + 1;
        UserItem.LastId++;
        I.UserId = cUser.Id;
        I.ItemId = 2963;
        I.ExtraData = "";
        UserItem.Items.put(I.Id, I);
        
        ServerMessage Message = new ServerMessage(ServerEvents.UpdateInventory);
        Message.Send(Client.Socket);
     }
}
