package com.sulake.habbo.communication.messages.incoming.userinformation;

import java.util.concurrent.FutureTask;

import com.sulake.habbo.communication.messages.outgoing.userinformation.AcceptTradeMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.userinformation.CanTradeMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.userinformation.ChangeTradeMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.userinformation.LoadBadgesMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.userinformation.LoadProfileMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.userinformation.TradeObjectMessageComposer;

import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.*;
import neutrino.UserManager.Habbo;

public class ChangeTradeMessageEvent extends MessageEvent implements Runnable {
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
    		ChangeTradeMessageComposer.Compose(Client, User);
    	} catch (Exception e)
    	{
    		Server.WriteLine(e);
    	}
    }
}
