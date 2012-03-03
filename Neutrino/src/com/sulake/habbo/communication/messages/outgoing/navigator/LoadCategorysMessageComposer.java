package com.sulake.habbo.communication.messages.outgoing.navigator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.NavigatorCategories;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadCategorysMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		Habbo CurrentHabbo = Client.GetSession();
		ServerMessage Can = new ServerMessage(ServerEvents.SendNavCategories);
		Can.writeInt(NavigatorCategories.Cats.size());
		Iterator reader = NavigatorCategories.Cats.iterator();
		while(reader.hasNext())
		{
			NavigatorCategories N = (NavigatorCategories)reader.next();
			Can.writeInt(N.Id);
			Can.writeUTF(N.Caption);
			Can.writeBoolean((N.MinRank < User.RankLevel));
		}
		Can.Send(Client.Socket);
	}
	
	public static int CountBadCatsForMe(Habbo User)
	{
		int i = 0;
		Iterator reader = NavigatorCategories.Cats.iterator();
		while(reader.hasNext())
		{
			NavigatorCategories N = (NavigatorCategories)reader.next();
			if(N.MinRank > User.RankLevel)
				i++;
		}
		return i;
	}

}

