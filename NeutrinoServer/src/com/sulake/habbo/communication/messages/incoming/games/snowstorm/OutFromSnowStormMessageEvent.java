package com.sulake.habbo.communication.messages.incoming.games.snowstorm;

import java.util.concurrent.FutureTask;

import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.UserManager.Habbo;

import com.sulake.habbo.communication.messages.outgoing.games.snowstorm.JoinToSnowStormMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.games.snowstorm.LoadGeneralRankingMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.games.snowstorm.LoadWeeklyGeneralRankingMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.games.snowstorm.OutFromSnowStormMessageComposer;

public class OutFromSnowStormMessageEvent extends MessageEvent implements Runnable {
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
    		OutFromSnowStormMessageComposer.Compose(Client, User, Server);
    	} catch (Exception e)
    	{
    		Server.WriteLine(e);
    	}
    }

}
