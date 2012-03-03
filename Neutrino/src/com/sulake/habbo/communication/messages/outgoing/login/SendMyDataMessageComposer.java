package com.sulake.habbo.communication.messages.outgoing.login;

import neutrino.Network.*;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.UserManager.*;
import neutrino.Environment;
import neutrino.System.*;

import org.jboss.netty.channel.Channel;
public class SendMyDataMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		ServerMessage Data = new ServerMessage(ServerEvents.MyInformation);
        Data.writeInt(User.Id);
        Data.writeUTF(User.UserName);
        Data.writeUTF(User.Look);
        Data.writeUTF(User.Gender.toUpperCase());
        Data.writeUTF(User.Motto);
        Data.writeUTF(""); // fbname, blablabla
        Data.writeBoolean(false);
        Data.writeInt(3); // Respect
        Data.writeInt(3); // Respect to give
        Data.writeInt(3); // to give to pets
        Data.writeBoolean(true);
        Data.Send(Client.Socket);
        
        ServerMessage AchivPoints = new ServerMessage(ServerEvents.SendAchievements);
        AchivPoints.writeInt(500); // achiv points
        AchivPoints.Send(Client.Socket);
	}
}
