package com.sulake.habbo.communication.messages.incoming.games.snowstorm;

import java.util.Iterator;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;

import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.PathFinder;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.SnowPathFinder;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

import com.sulake.habbo.communication.messages.outgoing.games.snowstorm.LoadPrincipalBoxMessageComposer;
import com.sulake.habbo.communication.messages.outgoing.rooms.WalkMessageComposer;

public class WalkOnSnow extends MessageEvent implements Runnable {
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
    	Environment cServer = Server;
    	try {
    		Habbo User = Client.GetSession();
    		Habbo CurrentUser = Client.GetSession();
            int RoomId = CurrentUser.CurrentRoomId;
            Channel Socket = Client.Socket;
            Room R = Room.Rooms.get(RoomId);
            int GoalX = Client.in.readInt();
            int GoalY = Client.in.readInt();
            if(GoalX == CurrentUser.GoalX && CurrentUser.GoalY == GoalY)
            	return;
            Server.WriteLine("Walk to: " + (GoalX/3600) + ";" + (GoalY/3600));
            CurrentUser.GoalX = GoalX;
            CurrentUser.GoalY = GoalY;
            CurrentUser.IsWalking = true;
            SnowPathFinder Finder = new SnowPathFinder(User.CurrentWar, User);
            if(Finder == null)
            {
            	//CurrentUser.UpdateState("", Client.Socket, Server);
                CurrentUser.IsWalking = false;
            	return;
            }
            
            ServerMessage Try = new ServerMessage(ServerEvents.GameUpdateState);
            Try.writeInt(4); // type (4 = walk)
            Try.writeInt(User.Id); // user id
            Try.writeInt(1); // rot
            Try.writeInt(1); // walk to?
            Try.writeInt(1);
            Try.writeInt(20);
            Try.writeInt(25600);
            Try.writeInt(83200);
            Try.writeInt(2);
            Try.writeInt(28);
            Try.writeInt(GoalX);
            Try.writeInt(GoalY);
            Try.writeInt(0);
            Try.writeInt(1);
            Try.writeInt(2);
            Try.writeInt(26);
            Try.writeInt(28);
            Try.writeInt(1);
            Try.Send(Client.Socket);
            //Iterator finalite = Finder.MakeFinder().iterator();
            //WalkMessageComposer W = new WalkMessageComposer(Client, User, Server, finalite, Finder);
            //ScheduledFuture future = ThreadPoolManager._myTimerPoolingThreads.scheduleAtFixedRate(W, 0L, 500L, TimeUnit.MILLISECONDS);
            //W.Set(future);
    	} catch (Exception e)
    	{
    		try {
    		Habbo CurrentUser = Client.GetSession();
    		Server.WriteLine(e);
    		//CurrentUser.UpdateState("", Client.Socket, cServer);
            CurrentUser.IsWalking = false;
    		return;
    		} catch (Exception e2)
    		{
    			Server.WriteLine(e2);
    			return;
    		}
    	}
    }

}
