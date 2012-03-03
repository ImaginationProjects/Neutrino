package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.ItemManager.RoomItem;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Coord;
import neutrino.RoomManager.OtherPathfinder;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.Rotation;

import org.jboss.netty.channel.Channel;

public class WalkMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        int GoalX = Client.in.readInt();
        int GoalY = Client.in.readInt();
        CurrentUser.GoalX = GoalX;
        CurrentUser.GoalY = GoalY;
        boolean MustCut = true;
        if(CurrentUser.IsWalking)
        {
            return;
        }
        CurrentUser.IsWalking = true;
        
        if(GoalX == CurrentUser.X && GoalY == CurrentUser.Y)
            return;
        int InitialX = CurrentUser.X;
        int InitialY = CurrentUser.Y;
        List<Coord> Path = (new OtherPathfinder()).GetPath(CurrentUser.X, CurrentUser.Y, GoalX, GoalY);
        Iterator reader = Path.iterator();
        while(reader.hasNext())
        {
        	/*if(!CurrentUser.IsWalking && CurrentUser.X != InitialY && CurrentUser.Y != InitialY)
                break;
            else
                CurrentUser.IsWalking = true;*/
            
            Coord Next = (Coord)reader.next();
            int NextX = Next.X;
            int NextY = Next.Y;
            
            CurrentUser.Rot = Rotation.Calculate(CurrentUser.X, CurrentUser.Y, NextX, NextY);
            CurrentUser.UpdateState("mv "+ NextX +","+ NextY +",0.0", Socket, Server);
            CurrentUser.X = NextX;
            CurrentUser.Y = NextY;            
            Thread.sleep(500);
            
            List<RoomItem> ItemsOnXY = R.GetItemOnXY(NextX, NextY);
            if(R.IsItemOnXY(NextX, NextY))
            {
            if(ItemsOnXY.size() > 0)
            {
                Server.WriteLine("user on item");
                Iterator reader2 = ItemsOnXY.iterator();
                while(reader2.hasNext())
                {
                    RoomItem Item = (RoomItem)reader2.next();
                    if(Item.GetBaseItem().CanSit)
                    {
                        CurrentUser.Rot = Item.Rot;
                        CurrentUser.UpdateState("sit 1.0", Socket, Server);
                        return;
                    }
                }
            } else {
                if(CurrentUser.State.startsWith("sit"))
                  CurrentUser.UpdateState("", Socket, Server);
            }         
            }
            
            if(NextX == GoalX && NextY == GoalY)
            {
                CurrentUser.UpdateState("", Socket, Server);
                CurrentUser.IsWalking = false;
                break;                
            }   
        }
        return;

	}
}
