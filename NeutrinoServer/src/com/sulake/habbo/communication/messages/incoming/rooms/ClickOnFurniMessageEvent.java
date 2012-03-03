package com.sulake.habbo.communication.messages.incoming.rooms;

import java.util.concurrent.FutureTask;

import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.UserManager.Habbo;

import com.sulake.habbo.communication.messages.outgoing.rooms.ClickOnFurniMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.rooms.SitMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.rooms.WaveMessageComposer;

public class ClickOnFurniMessageEvent extends MessageEvent implements Runnable {
	private ServerHandler Client;
    private Environment Server;
    private FutureTask T;
    @Override
    public void Load(ServerHandler Client, Environment Server, FutureTask T) throws Exception
    {
        this.Client = Client;
        this.Server = Server;
        this.T = T;
    }
    
    public void run()
    {
    	try {
    		Habbo User = Client.GetSession();
    		ClickOnFurniMessageComposer.Compose(Client, User, Server);
    	} catch (Exception e)
    	{
    		Server.WriteLine(e);
    	}
    }


}
