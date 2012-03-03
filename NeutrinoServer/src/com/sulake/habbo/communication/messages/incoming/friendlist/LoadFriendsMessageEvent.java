package com.sulake.habbo.communication.messages.incoming.friendlist;

import java.util.concurrent.FutureTask;
import neutrino.MessageEvents.*;
import neutrino.Environment;
import neutrino.Network.ServerHandler;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;

import com.sulake.habbo.communication.messages.outgoing.friendlist.LoadFriendsMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.handshake.TryLoginMessageComposer;

public class LoadFriendsMessageEvent extends MessageEvent implements Runnable {
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
    		LoadFriendsMessageComposer.Compose(Client, User);
    		} catch (Exception e)
    	{
    		Server.WriteLine(e);
    	}
    }
}
