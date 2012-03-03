package com.sulake.habbo.communication.messages.incoming.handshake;

import neutrino.MessageEvents.*;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;

import com.sulake.habbo.communication.messages.outgoing.handshake.TryLoginMessageComposer;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.sql.*;
import neutrino.Environment;
import neutrino.System.ServerMessage;
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
    		String Release = Client.inreader.readUTF();
    		Habbo User = Client.GetSession();
    		Server.WriteLine("Want connect: " + User.UserName + ";" + User.RankLevel);
    		if(User == null)
    		{
    			Server.WriteLine(Server.GetIPFromSocket(Client.Socket) + " try to connect but It isn't registered!");
    			return;
    		}
    		if(User.RankLevel == 0)
    		{
    			return;
    		}
    		User.IsOnline = true;
    		User.SessionId = 0;
    		Environment.UsersConnected++;
    		Server.GetDatabase().executeUpdates("UPDATE users SET connected = '1' WHERE id =" + User.Id);
    		Server.WriteLine(User.UserName + " has been conected under IP " + User.Authenticator);
    		TryLoginMessageComposer.Compose(Client, User, Server);
    		User.UpdateStateForFriends();
    	} catch (Exception e)
    	{
    		Server.WriteLine(e);
    	}
    }
}
