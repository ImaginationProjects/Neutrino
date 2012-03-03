package com.sulake.habbo.communication.messages.incoming.handshake;

import neutrino.MessageEvents.*;
import neutrino.Network.ServerHandler;
import com.sulake.habbo.communication.messages.outgoing.handshake.TryLoginMessageComposer;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.sql.*;
import neutrino.Environment;
import neutrino.UserManager.*;

public class HelloMessageEvent extends MessageEvent implements Runnable {
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
    		//Server.WriteLine("Hello world! We are on release " + Client.inreader.readUTF());
    		Habbo User = Client.GetSession();
    		if(User == null)
    		{
    			Server.WriteLine(Server.GetIPFromSocket(Client.Socket) + " try to connect but It isn't registered!");
    			return;
    		}
    		TryLoginMessageComposer.Compose(Client, User, Server);
    		User.IsOnline = true;
    		User.CurrentSocket = this.Client.Socket;
    		UserManager.VirtualUsers.add(User);
    		UserManager.VirtualUsersById.put(User.Id, User);
    		Environment.UsersConnected++;
    		Server.GetDatabase().executeUpdates("UPDATE users SET connected = '1' WHERE id =" + User.Id);
    		Server.WriteLine(User.UserName + " has been conected under IP " + User.Authenticator);
    	} catch (Exception e)
    	{
    		Server.WriteLine(e);
    	}
    }
}
