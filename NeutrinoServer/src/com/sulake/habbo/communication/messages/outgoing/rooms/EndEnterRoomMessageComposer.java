package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import neutrino.Environment;
import neutrino.AdministrationManager.Chatlog;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Dances;
import neutrino.UserManager.Effects;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomManager;
import neutrino.RoomManager.SquareState;

import org.jboss.netty.channel.Channel;

import com.sulake.habbo.communication.messages.outgoing.userinformation.LoadPetInventaryMessageComposer;

public class EndEnterRoomMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
        if(cUser.IsPlayingSnow)
        	return;
		
		Habbo CurrentUser = cUser;
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        /*ServerMessage SomeData = new ServerMessage(ServerEvents.RoomData5);
        SomeData.writeInt(0);
        SomeData.Send(Socket);*/
        
        R.WallItems = new ArrayList<RoomItem>();
        R.WallItems = RoomItem.GetWallItemsForRoom(RoomId);
        R.FloorItems = new ArrayList<RoomItem>();
        R.FloorItems = RoomItem.GetFloorItemsForRoom(RoomId);
        
        ServerMessage WallItems = new ServerMessage(ServerEvents.WallItems);
        WallItems.writeInt(1);
        WallItems.writeInt(R.OwnerId);
        WallItems.writeUTF(R.OwnerName);
        WallItems.writeInt(R.WallItems.size());
        Iterator ireader = R.WallItems.iterator();
        while(ireader.hasNext())
        {
            RoomItem Item = (RoomItem)ireader.next();
            ItemInformation furnidata = ItemInformation.Items.get(Item.FurniId);
            if(furnidata == null)
            	continue;
            WallItems.writeUTF(Item.Id + "");
            WallItems.writeInt(furnidata.SpriteId);
            WallItems.writeUTF(Item.W);
            WallItems.writeUTF(Item.ExtraData);
            WallItems.writeInt((furnidata.Interactor.equals("default")) ? 1 : 0);
            WallItems.writeInt(R.OwnerId);
        }
        WallItems.Send(Socket);
        
        List<Pet> sPetList = Pet.GetPetsForRoomId(RoomId);
        if(R.PetList.size() != sPetList.size())
        {
        	Iterator reader4 = sPetList.iterator();
        	while(reader4.hasNext())
        	{
        		Pet P = (Pet)reader4.next();
    			Habbo Owner = Habbo.UsersbyId.get(P.UserId);
        		if(R.OwnerId != P.UserId && Owner.CurrentRoomId != P.RoomId)
        		{
        			Pet PetData = P;
    		        ServerMessage GetOut = new ServerMessage(ServerEvents.GetOutOfRoom);
    		        GetOut.writeUTF(PetData.SessionId + "");
    		        UserManager.SendMessageToUsersOnRoomId(PetData.RoomId, GetOut);
    		        P.RoomId = 0;
    		        P.IsOnRoom = false;
    		        P.HaveUserOnMe = false;
    		        Environment.returnDB().executeUpdate("UPDATE pets SET roomid = '0' WHERE id = " + P.Id);
    		        if(Owner.IsOnline)
    		        	LoadPetInventaryMessageComposer.Compose(Client, Owner, Server);
        		} else {
        			P.SessionId = R.GetNextSessionId();
            		P.X = (new Random()).nextInt(R.GetModel().MapSizeX);
            		P.Y = (new Random()).nextInt(R.GetModel().MapSizeY);
            		if(R.GetModel().Squares[P.X][P.Y] != neutrino.RoomManager.SquareState.WALKABLE)
            		{
            			P.X = R.GetModel().DoorX;
            			P.Y = R.GetModel().DoorY;
            		}
            		String PetZ = ((Double)R.SecondGetHForXY(P.X, P.Y)).toString().replace(',', '.');
                    if (!PetZ.contains("."))
                        PetZ += ".0";
                    P.Z = PetZ;
                    if(!P.IsDoingThings)
                    {
                    	P.IsDoingThings = true;
                        ScheduledFuture future = ThreadPoolManager._myTimerPoolingThreads.scheduleAtFixedRate(P, 0L, 4L, TimeUnit.SECONDS);
                        P.Set(future);
                    }
                    R.PetList.add(P);
        		}
        	}
        }
        
        ServerMessage BotList = new ServerMessage(ServerEvents.SerializeBotsOnRoom);
        BotList.writeInt(R.PetList.size());
        Iterator reader5 = R.PetList.iterator();
        while(reader5.hasNext())
        {
        	Pet P = (Pet)reader5.next();
        	BotList.writeInt(P.Id);
            BotList.writeUTF(P.PetName);
            BotList.writeUTF("");
            BotList.writeUTF(P.Race + " " + P.Type + " " + P.Color.toLowerCase() + " ");
            BotList.writeInt(P.SessionId);
            BotList.writeInt(P.X);
            BotList.writeInt(P.Y);
            BotList.writeUTF(P.Z);
            BotList.writeInt(P.Type);
            BotList.writeInt(2);
            BotList.writeInt(P.Race);
            BotList.writeInt(P.UserId);
            BotList.writeUTF("");
        }	
        BotList.Send(Socket);
        
        Iterator reader6 = R.PetList.iterator();
        while(reader6.hasNext())
        {
        	Pet P = (Pet)reader6.next();
        	if(P.HaveChair)
            {
                ServerMessage AddToPet = new ServerMessage(ServerEvents.AddCharToPet);
                AddToPet.writeInt(1);
                AddToPet.writeInt(P.Id);
                AddToPet.writeInt(P.Race);
                AddToPet.writeInt(P.Type);
                AddToPet.writeUTF(P.Color.toLowerCase());
                AddToPet.writeInt(3);
                AddToPet.writeInt(3);
                AddToPet.writeInt(3);
                AddToPet.writeInt(-1);
                AddToPet.writeInt(1);
                AddToPet.writeInt(4);
                AddToPet.writeInt(9);
                AddToPet.writeInt(0);
                AddToPet.writeInt(2);
                AddToPet.writeInt(-1);
                AddToPet.writeInt(1);
                AddToPet.writeBoolean(P.HaveChair);
                AddToPet.writeBoolean(P.HaveUserOnMe);
                AddToPet.Send(Socket);
            }
        }
        
        ServerMessage RoomItems = new ServerMessage(ServerEvents.FloorItems);
        RoomItems.writeInt(1);
        RoomItems.writeInt(R.OwnerId);
        RoomItems.writeUTF(R.OwnerName);
        RoomItems.writeInt(R.FloorItems.size());
        Iterator reader = R.FloorItems.iterator();
        while(reader.hasNext())
        {
            RoomItem Item = (RoomItem)reader.next();
            ItemInformation furnidata = ItemInformation.Items.get(Item.FurniId);
            if(furnidata == null)
            	continue;
            RoomItems.writeInt(Item.Id);
            RoomItems.writeInt(furnidata.SpriteId);
            RoomItems.writeInt(Item.X);
            RoomItems.writeInt(Item.Y);
            RoomItems.writeInt(Item.Rot);
            RoomItems.writeUTF("0.0");
            RoomItems.writeInt(0);
            RoomItems.writeInt(0);
            RoomItems.writeUTF(Item.ExtraData);
            RoomItems.writeInt(-1);
            RoomItems.writeInt((furnidata.Interactor.equals("default")) ? 1 : 0);
            RoomItems.writeInt(R.OwnerId);
        }
        RoomItems.Send(Socket);
        
        ServerMessage UserData1 = new ServerMessage(ServerEvents.preUserData);
        UserData1.writeInt(1);
        UserData1.writeInt(CurrentUser.Id);
        UserData1.writeUTF(CurrentUser.UserName);
        UserData1.writeInt(0);
        UserData1.Send(Socket);
        
        CurrentUser.X = R.GetModel().DoorX;
        CurrentUser.Y = R.GetModel().DoorY;
        CurrentUser.Rot = R.GetModel().DoorDir;
        CurrentUser.SessionId = R.GetNextSessionId();
        
        ServerMessage UserData = new ServerMessage(ServerEvents.UserData);
        UserData.writeInt(R.UserList.size());
        Iterator treader = R.UserList.iterator();
        while(treader.hasNext())
        {
            Habbo User = (Habbo)treader.next();
            UserData.writeInt(User.Id);
            UserData.writeUTF(User.UserName);
            UserData.writeUTF(User.Motto);
            UserData.writeUTF(User.Look);
            UserData.writeInt(User.SessionId); // session id
            UserData.writeInt(User.X);
            UserData.writeInt(User.Y);
            UserData.writeUTF(User.Z); // user z
            UserData.writeInt(User.Rot);
            UserData.writeInt(1);
            UserData.writeUTF(User.Gender.toLowerCase());
            UserData.writeInt(-1);
            UserData.writeInt(-1);
            UserData.writeInt(0);
            UserData.writeInt(525);
            //User.UsersReadedWithPows.add(User.Id);
        }
        UserData.Send(Socket);
        
        ServerMessage UserToLoad = new ServerMessage(ServerEvents.UserData);
        UserToLoad.writeInt(1);
        UserToLoad.writeInt(CurrentUser.Id);
        UserToLoad.writeUTF(CurrentUser.UserName);
        UserToLoad.writeUTF(CurrentUser.Motto);
        UserToLoad.writeUTF(CurrentUser.Look);
        UserToLoad.writeInt(CurrentUser.SessionId); // session id
        UserToLoad.writeInt(CurrentUser.X);
        UserToLoad.writeInt(CurrentUser.Y);
        UserToLoad.writeUTF(CurrentUser.Z); // user z
        UserToLoad.writeInt(CurrentUser.Rot);
        UserToLoad.writeInt(1);
        UserToLoad.writeUTF(CurrentUser.Gender.toLowerCase());
        UserToLoad.writeInt(-1);
        UserToLoad.writeInt(-1);
        UserToLoad.writeInt(0);
        UserToLoad.writeInt(525);
        UserManager.SendMessageToUsersOnRoomIdButMe(RoomId, CurrentUser.Id, UserToLoad);
        
        ServerMessage ExtraData = new ServerMessage(ServerEvents.VipWallsAndFloors);
        ExtraData.writeInt(R.VipWalls);
        ExtraData.writeInt(R.VipFloors);
        ExtraData.writeBoolean(R.Hidewalls);
        ExtraData.Send(Socket);
        
        CurrentUser.UpdateState("", Socket, Server);
        
        ServerMessage RoomPanel = new ServerMessage(ServerEvents.RoomPanel);
        RoomPanel.writeBoolean(true);
        RoomPanel.writeInt(RoomId);
        RoomPanel.writeBoolean(true);
        RoomPanel.Send(Socket);
        
        Room RoomData = R;
        ServerMessage sRoomData = new ServerMessage(ServerEvents.RoomData);
        sRoomData.writeBoolean(true);
        sRoomData.writeInt(RoomId);
        sRoomData.writeBoolean(false);
        sRoomData.writeUTF(RoomData.Name);
        sRoomData.writeInt(RoomData.OwnerId);
        sRoomData.writeUTF(RoomData.OwnerName);
        sRoomData.writeInt(0);
        sRoomData.writeInt(RoomData.CurrentUsers);
        sRoomData.writeInt(RoomData.MaxUsers);
        sRoomData.writeUTF(RoomData.Description);
        sRoomData.writeInt(0);
        sRoomData.writeInt((RoomData.Category == 3) ? 2 : 0);
        sRoomData.writeInt(RoomData.Score);
        sRoomData.writeInt(RoomData.Category);
        sRoomData.writeUTF("");
        sRoomData.writeInt(0);
        sRoomData.writeInt(0);
        sRoomData.writeInt(RoomData.Tags.size());
        // Icons
        sRoomData.writeInt(0);
        sRoomData.writeInt(0);
        sRoomData.writeInt(0);
        // bools
        sRoomData.writeBoolean(true);
        sRoomData.writeBoolean(true);
        sRoomData.writeBoolean(false);
        sRoomData.writeBoolean(false);
        sRoomData.writeBoolean(false);
        sRoomData.Send(Socket);
        
        Iterator treader2 = R.UserList.iterator();
        while(treader2.hasNext())
        {
            Habbo User = (Habbo)treader2.next();
            if(User.CurrentEffect != Effects.NONE)
            {
            	ServerMessage Dance = new ServerMessage(ServerEvents.ShowEffect);
                Dance.writeInt(User.SessionId);
                Dance.writeInt(User.CurrentEffect);
                Dance.writeInt(0);
                Dance.Send(Socket);
            }
            
            if(User.CurrentDance != Dances.NONE)
            {
            	ServerMessage Dance = new ServerMessage(ServerEvents.Dance);
                Dance.writeInt(User.SessionId);
                Dance.writeInt(User.CurrentDance);
                Dance.Send(Socket);
            }
        }
        
        if(!RoomManager.Managers.containsKey(RoomId))
        	RoomManager.AddRoomToProcess(RoomId);
        

	}
}
