package com.sulake.habbo.communication.messages.incoming.handshake;

import core.Environment;
import core.game.users.Session;
import core.messages.MessageEvent;
import core.network.SocketHandler;

public class HelloMessageEvent extends MessageEvent {
	public void Load(SocketHandler Main, Session Client) throws Exception
	{
		// Load all shit
		Environment.WriteLine("Hi, i'm " + Client.myHabbo.Username + " and we are on " + Main.inreader.readUTF());
	}
}
