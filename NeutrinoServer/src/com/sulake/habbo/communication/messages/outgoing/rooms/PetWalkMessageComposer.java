package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;

import neutrino.Environment;
import neutrino.ItemManager.RoomItem;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Coord;
import neutrino.RoomManager.OtherPathfinder;
import neutrino.RoomManager.PathFinder;
import neutrino.RoomManager.PetPathFinder;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.Rotation;

import org.jboss.netty.channel.Channel;

public class PetWalkMessageComposer implements Runnable {
	public Iterator reader;
	public Pet cUser;
	public PetPathFinder Finder;
	public PetWalkMessageComposer(Pet ccUser, Iterator creader, PetPathFinder cFinder) throws Exception
	{
		this.reader = creader;
		this.cUser = ccUser;
		this.Finder = cFinder;
	}
	
	public ScheduledFuture future;
	public void Set(ScheduledFuture ft)
	{
		this.future = ft;
	}
	
	public void run()
	{
		try {
		Pet CurrentUser = cUser;
		if(CurrentUser.HaveUserOnMe)
		{
			future.cancel(true);
			return;
		}
        Room R = Room.Rooms.get(cUser.RoomId);
		if(!reader.hasNext() && CurrentUser.IsWalking)
        {
			if(CurrentUser.GoalX != Finder.GoX || CurrentUser.GoalY != Finder.GoY)
			{
				future.cancel(true);
				return;
			}
			CurrentUser.UpdateState("");
            CurrentUser.IsWalking = false;
            future.cancel(true);
            return;
        }
		if(CurrentUser.GoalX != Finder.GoX || CurrentUser.GoalY != Finder.GoY)
		{
			future.cancel(true);
			return;
		}
        Coord Next = (Coord)reader.next();
            int NextX = Next.X;
            int NextY = Next.Y;
            
            String UserZ;
            UserZ = ((Double)R.SecondGetHForXY(NextX, NextY)).toString().replace(',', '.');
            if (!UserZ.contains("."))
                UserZ += ".0";
            CurrentUser.Rot = Rotation.Calculate(CurrentUser.X, CurrentUser.Y, NextX, NextY);
            CurrentUser.UpdateState("mv "+ NextX +","+ NextY +","+ UserZ);
            CurrentUser.X = NextX;
            CurrentUser.Y = NextY;
            CurrentUser.Z = UserZ;
            if(CurrentUser.GoalX != Finder.GoX || CurrentUser.GoalY != Finder.GoY)
    		{
    			future.cancel(true);
    			return;
    		}
		} catch (Exception e)
		{
			System.out.println(e.toString());
			future.cancel(true);
			return;
		}
	}
}
