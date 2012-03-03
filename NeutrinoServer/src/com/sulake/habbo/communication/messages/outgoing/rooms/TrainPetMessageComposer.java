package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
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

public class TrainPetMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        int PetId = Client.in.readInt();
        Pet PetData = Pet.Pets.get(PetId);
        
        if(PetData.equals(null))
        	return;
        
        ServerMessage Panel = new ServerMessage(ServerEvents.ShowTrainingPanel);
        Panel.writeInt(PetId);
        if(PetData.Race > 0)
        {
        	int CommandCount = 22;
        	int SubCommandCount = 17;
        	Panel.writeInt(CommandCount);
        	for (int i = 0; i < (SubCommandCount); i++)
        	{
        		if (i != 14) // (removed order 14)
        		{
        			if(i != 1 & PetData.Race == 15)
        				Panel.writeInt(i); // unlock order id
        		}
        		if(i == 1)
        		{
        			Panel.writeInt(14); // add order 14
        			Panel.writeInt(43); // add order 43
        			if(PetData.Race == 15)
        				Panel.writeInt(44);
        		} else if(i == 4)
        			Panel.writeInt(17); // add order 17
        		else if(i == 16)
        		{
        			Panel.writeInt(24); // add order 24
        			Panel.writeInt(25); // add order 25
        			Panel.writeInt(26); // add order 26
        		}        		
        	}
        	int o = 0;
        	for (int i = 0; i < (PetData.Level + 1); i++)
        	{
        		// add counts (same at the other)
        		if (i != 14)
           		 o++;
        		if(i == 1)
        		{
        			o += 2;
        		} else if(i == 4)
        			o += 1;
        		else if(i == 16)
        		{
        			o += 3;
        		}
        		
        	}
        	Panel.writeInt(o); // unlocked commands
        	for (int i = 0; i < (PetData.Level + 1); i++)
        	{
        		// same
        		if (i != 14) // (removed order 14)
        		{
        			if(i != 1 & PetData.Race == 15)
        				Panel.writeInt(i); // unlock order id
        		}
        		if(i == 1)
        		{
        			Panel.writeInt(14); // add order 14
        			Panel.writeInt(43); // add order 43
        			if(PetData.Race == 15)
        				Panel.writeInt(44);
        		} else if(i == 4)
        			Panel.writeInt(17);
        		else if(i == 16)
        		{
        			Panel.writeInt(24);
        			Panel.writeInt(25);
        			Panel.writeInt(26);
        		}
        		
        	}
        	/*Panel.writeInt(0);
        	Panel.writeInt(1);
        	Panel.writeInt(14);
        	Panel.writeInt(43);
        	Panel.writeInt(2);
        	Panel.writeInt(3);
        	Panel.writeInt(4);
        	Panel.writeInt(17);
        	Panel.writeInt(5);
        	Panel.writeInt(6);
        	Panel.writeInt(7);
        	Panel.writeInt(8);
        	Panel.writeInt(9);
        	Panel.writeInt(10);
        	Panel.writeInt(11);
        	Panel.writeInt(12);
        	Panel.writeInt(13);
        	Panel.writeInt(15);
        	Panel.writeInt(16);
        	Panel.writeInt(24);
        	Panel.writeInt(25);
        	Panel.writeInt(26);
        	Panel.writeInt(4);
        	Panel.writeInt(0);
        	Panel.writeInt(1);
        	Panel.writeInt(14);
        	Panel.writeInt(43);*/
        } else {
        	cUser.SendAlert("Esta mascota todavia no tiene habilitado su panel, repórtalo.");
        	return;
        }
        Panel.Send(Socket);
	}
}
