package com.sulake.habbo.communication.messages.incoming.rooms;

import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.*;

import org.jboss.netty.channel.Channel;

import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.RoomManager.PathFinder;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.Coord;
import neutrino.UserManager.Habbo;

import antlr.collections.List;

import com.sulake.habbo.communication.messages.outgoing.rooms.WalkMessageComposer;

public class WalkMessageEvent extends MessageEvent implements Runnable {
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
            if(R.IsItemOnXY(GoalX, GoalY) && !User.IsTeleportOn)
            	return;
            CurrentUser.GoalX = GoalX;
            CurrentUser.GoalY = GoalY;
            if(CurrentUser.IsTeleportOn)
            {
            	String UserZ = ((Double)R.SecondGetHForXY(GoalX, GoalY)).toString().replace(',', '.');
                if (!UserZ.contains("."))
                    UserZ += ".0";
                CurrentUser.UpdateState("mv " + GoalX + "," + GoalY + "," + UserZ, Socket, cServer);
                CurrentUser.X = GoalX;
                CurrentUser.Y = GoalY;
                CurrentUser.Z = UserZ;
                Thread.sleep(500);
                CurrentUser.UpdateState("", Socket, cServer);
            	return;
            }
            CurrentUser.IsWalking = true;
            PathFinder Finder = new PathFinder(R, User);
            if(Finder == null)
            {
            	CurrentUser.UpdateState("", Client.Socket, Server);
                CurrentUser.IsWalking = false;
            	return;
            }
            Iterator finalite = Finder.MakeFinder().iterator();
            WalkMessageComposer W = new WalkMessageComposer(Client, User, Server, finalite, Finder);
            ScheduledFuture future = ThreadPoolManager._myTimerPoolingThreads.scheduleAtFixedRate(W, 0L, 500L, TimeUnit.MILLISECONDS);
            W.Set(future);
    	} catch (Exception e)
    	{
    		try {
    		Habbo CurrentUser = Client.GetSession();
    		Server.WriteLine(e);
    		CurrentUser.UpdateState("", Client.Socket, cServer);
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
