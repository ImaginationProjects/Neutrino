package com.sulake.habbo.communication.messages.outgoing.catalog;

import java.util.*;

import neutrino.CatalogManager.CatalogPage;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.PetRace;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class ValidPetNameMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		String Name = Client.inreader.readUTF();
		int Junk = Client.inreader.readInt();
		
		boolean HasError = false;
		String ErrorId = "";
		
		if(Name.length() >= 15)
		{
			HasError = true;
			ErrorId = "15";
		}
		
		ServerMessage SendPetRace = new ServerMessage(ServerEvents.IsValidPetName);
		SendPetRace.writeInt((HasError) ? 1 : 0);
		SendPetRace.writeUTF(ErrorId);
		SendPetRace.Send(Client.Socket);
		
	}
}
