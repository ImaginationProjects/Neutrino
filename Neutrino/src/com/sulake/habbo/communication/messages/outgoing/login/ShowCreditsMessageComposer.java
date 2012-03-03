package com.sulake.habbo.communication.messages.outgoing.login;

import neutrino.Network.*;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.UserManager.*;
import neutrino.Environment;
import neutrino.System.*;

import org.jboss.netty.channel.Channel;
public class ShowCreditsMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		ServerMessage ShowCredits = new ServerMessage(ServerEvents.SendCredits);
		ShowCredits.writeUTF(User.Credits + "");
		ShowCredits.Send(Client.Socket);
	}
}
