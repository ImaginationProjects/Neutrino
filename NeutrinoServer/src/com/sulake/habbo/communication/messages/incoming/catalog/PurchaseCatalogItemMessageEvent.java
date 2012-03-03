package com.sulake.habbo.communication.messages.incoming.catalog;

import java.util.concurrent.FutureTask;

import com.sulake.habbo.communication.messages.outgoing.catalog.PurchaseCatalogItemMessageComposer;

import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.UserManager.Habbo;


public class PurchaseCatalogItemMessageEvent extends MessageEvent implements Runnable {
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
    		PurchaseCatalogItemMessageComposer.Compose(Client, User, Server);
    	} catch (Exception e)
    	{
    		Server.WriteLine(e);
    	}
    }
}
