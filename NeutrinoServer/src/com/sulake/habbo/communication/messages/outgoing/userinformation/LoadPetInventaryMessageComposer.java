package com.sulake.habbo.communication.messages.outgoing.userinformation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

import org.jboss.netty.channel.Channel;

public class LoadPetInventaryMessageComposer {
	
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        // pets errors: PetInventary.writeInt(5); // not allowed on this room (0 = not allowed in this hotel / 1 = not allowed in this room / 2 = too much pets on room // 3 = tile error / 4 = pet error (no tile valid))
        
        List<Pet> PetList = Pet.ConvertToInventory(cUser.Id);
        ServerMessage PetInventary = new ServerMessage(ServerEvents.LoadPetInventary);
        PetInventary.writeInt(PetList.size());
        Iterator reader = PetList.iterator();
        while(reader.hasNext())
        {
        	Pet P = (Pet)reader.next();
        	PetInventary.writeInt(P.Id*1024*30);
        	PetInventary.writeUTF(P.PetName);
        	PetInventary.writeInt(P.Race);
        	PetInventary.writeInt(P.Type);
        	PetInventary.writeUTF(P.Color.toLowerCase());
        	PetInventary.writeInt(5);
        	PetInventary.writeInt(0);
        }
        PetInventary.Send(Socket);
	}

}
