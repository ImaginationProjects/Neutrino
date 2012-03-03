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
import neutrino.RoomManager.Room;
import neutrino.RoomManager.Rotation;

import org.jboss.netty.channel.Channel;

public class WalkMessageComposer implements Runnable {
	public Iterator reader;
	public Habbo cUser;
	public ServerHandler Client;
	public Environment Server;
	public PathFinder Finder;
	public WalkMessageComposer(ServerHandler cClient, Habbo ccUser, Environment cServer, Iterator creader, PathFinder cFinder) throws Exception
	{
		this.reader = creader;
		this.cUser = ccUser;
		this.Client = cClient;
		this.Server = cServer;
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
		Habbo CurrentUser = Client.GetSession();
		boolean IsRidingAHorse = (cUser.RidingAHorseId != 0);
		Pet P = null;
		if(IsRidingAHorse)
			P = Pet.Pets.get(cUser.RidingAHorseId);

        Room R = Room.Rooms.get(cUser.CurrentRoomId);
		if(!reader.hasNext() && CurrentUser.IsWalking)
        {
			if(R.GetItemOnXY(CurrentUser.GoalX, CurrentUser.GoalY).size() == 0)
			{
				CurrentUser.IsSit = false;
			}
			if(CurrentUser.GoalX != Finder.GoX || CurrentUser.GoalY != Finder.GoY)
			{
				future.cancel(true);
				return;
			}
			if(!CurrentUser.IsSit)
			{
            CurrentUser.UpdateState("", Client.Socket, Server);
            if(IsRidingAHorse)
            {
            	P.UpdateState("");
            }
			}
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
            int w = ((IsRidingAHorse) ? 1 : 0);
            UserZ = ((Double)(R.SecondGetHForXY(NextX, NextY) + w)).toString().replace(',', '.');
            if (!UserZ.contains("."))
                UserZ += ".0";
            if(IsRidingAHorse)
            {
            	String PetZ = ((Double)(R.SecondGetHForXY(NextX, NextY))).toString().replace(',', '.');
                if (!PetZ.contains("."))
                    PetZ += ".0";
            	P.Rot = Rotation.Calculate(P.X, P.Y, NextX, NextY);
            	CurrentUser.Rot = Rotation.Calculate(CurrentUser.X, CurrentUser.Y, NextX, NextY);
                P.UpdateStateOfTwo("mv "+ NextX +","+ NextY +","+ UserZ, "mv "+ NextX +","+ NextY +","+ PetZ);
                P.X = NextX;
                P.Y = NextY;
                P.Z = PetZ;
                CurrentUser.X = NextX;
                CurrentUser.Y = NextY;
                CurrentUser.Z = UserZ;
            } else {
            	CurrentUser.Rot = Rotation.Calculate(CurrentUser.X, CurrentUser.Y, NextX, NextY);
            	CurrentUser.UpdateState("mv "+ NextX +","+ NextY +","+ UserZ, Client.Socket, Server);
            	CurrentUser.X = NextX;
                CurrentUser.Y = NextY;
                CurrentUser.Z = UserZ;
            }
            
            List<RoomItem> ItemsOnXY = R.GetItemOnXY(NextX, NextY);
            if(ItemsOnXY.size() > 0 && !IsRidingAHorse)
            {
                Iterator reader2 = ItemsOnXY.iterator();
                while(reader2.hasNext())
                {
                	if(CurrentUser.GoalX != Finder.GoX || CurrentUser.GoalY != Finder.GoY)
            		{
            			future.cancel(true);
            			return;
            		}
                    RoomItem Item = (RoomItem)reader2.next();
                    if(Item.GetBaseItem().CanSit)
                    {
                    	UserZ = ((Double)R.GetHForXY(Item.X, Item.Y, Item)).toString().replace(',', '.');
                        if (!UserZ.contains("."))
                            UserZ += ".0";
                        CurrentUser.Z = UserZ;
                        CurrentUser.Rot = Item.Rot;
                        CurrentUser.Rot = Item.Rot;
                        CurrentUser.IsSit = true;
                    	String H = Item.GetBaseItem().Height + "";
                    	if (!H.contains("."))
                           H += ".0";
                        CurrentUser.UpdateState("sit 1.0", Client.Socket, Server);
                        CurrentUser.IsWalking = false;
                        future.cancel(true);
                        return;
                    }
                }
            } else {
                if(CurrentUser.State.startsWith("sit"))
                {
                	CurrentUser.IsSit = false;
                    CurrentUser.UpdateState("", Client.Socket, Server);
                }
            }
            if(CurrentUser.GoalX != Finder.GoX || CurrentUser.GoalY != Finder.GoY)
    		{
    			future.cancel(true);
    			return;
    		}
		} catch (Exception e)
		{
			Server.WriteLine(e);
			future.cancel(true);
			return;
		}
	}
}
