package com.sulake.habbo.communication.messages.outgoing.catalog;

import java.util.*;

import neutrino.CatalogManager.CatalogPage;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.PetRace;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadPetRaceMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		String Race = Client.inreader.readUTF();
		String ToGet = Race.replace("a0", "");
		ToGet = ToGet.replace(" ", "");
		ToGet = ToGet.replace("pet", "");
		int RaceId = Integer.parseInt(ToGet);
		
		ServerMessage SendPetRace = new ServerMessage(ServerEvents.ShowPetRace);
		SendPetRace.writeUTF(Race);
		if(PetRace.RaceGotRaces(RaceId))
		{
			List<PetRace> Races = PetRace.GetRacesForRaceId(RaceId);
			Iterator reader = Races.iterator();
			SendPetRace.writeInt(Races.size());
			while(reader.hasNext())
			{
				PetRace R = (PetRace)reader.next();
				SendPetRace.writeInt(R.RaceId);
				SendPetRace.writeInt(R.Color1);
				SendPetRace.writeInt(R.Color2);
				SendPetRace.writeBoolean(R.Has1Color);
				SendPetRace.writeBoolean(R.Has2Color);
			}
		} else {
			User.SendAlert("Raza de Mascota no encontrada.");
			return;
		}
		SendPetRace.Send(Client.Socket);
		
	}
}
