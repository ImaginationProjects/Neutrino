package com.sulake.habbo.communication.messages.incoming.catalog;

import java.util.concurrent.FutureTask;

import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.UserManager.Habbo;

import com.sulake.habbo.communication.messages.outgoing.catalog.LoadCatalogMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.catalog.LoadPetRaceMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.catalog.ValidPetNameMessageComposer;

public class ValidPetNameMessageEvent extends MessageEvent implements Runnable {
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
    	    ValidPetNameMessageComposer.Compose(Client, User);
    	} catch (Exception e)
    	{
    		Server.WriteLine(e);
    	}
    }
}
