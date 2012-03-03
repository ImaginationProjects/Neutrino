package com.sulake.habbo.communication.messages.incoming.login;
import neutrino.MessageEvents.*;
import neutrino.Network.ServerHandler;
import com.sulake.habbo.communication.messages.outgoing.login.LoadClubMessageComposer;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.sql.*;
import neutrino.Environment;
import neutrino.UserManager.*;

public class LoadClubMessageEvent extends MessageEvent implements Runnable {
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
    		LoadClubMessageComposer.Compose(Client, User, Server);
    	} catch (Exception e)
    	{
    		Server.WriteLine(e);
    	}
    }
}

