package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

import com.sulake.habbo.communication.messages.outgoing.userinformation.LoadPetInventaryMessageComposer;

public class MovePetToRoomMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        int PetId = Client.in.readInt()/1024/30;
        Pet PetToMove = Pet.Pets.get(PetId);
        int ToX = Client.in.readInt();
        int ToY = Client.in.readInt();
        PetToMove.X = ToX;
        PetToMove.Y = ToY;
        PetToMove.RoomId = RoomId;
        PetToMove.IsOnRoom = true;
        PetToMove.HaveUserOnMe = false;
        PetToMove.Rot = 2;
        PetToMove.SessionId = R.GetNextSessionId();
        PetToMove.IsDoingThings = true;
        ScheduledFuture future = ThreadPoolManager._myTimerPoolingThreads.scheduleAtFixedRate(PetToMove, 0L, 4L, TimeUnit.SECONDS);
        PetToMove.Set(future);
        Server.GetDatabase().executeUpdate("UPDATE pets SET roomid = '" + RoomId + "' WHERE id = " + PetId);
        LoadPetInventaryMessageComposer.Compose(Client, cUser, Server);
        R.PetList.add(PetToMove);
        
        ServerMessage AddPetToRoom = new ServerMessage(ServerEvents.SerializeBotsOnRoom);
        AddPetToRoom.writeInt(1);
        AddPetToRoom.writeInt(PetToMove.Id);
        AddPetToRoom.writeUTF(PetToMove.PetName);
        AddPetToRoom.writeUTF("");
        AddPetToRoom.writeUTF(PetToMove.Race + " " + PetToMove.Type + " " + PetToMove.Color.toLowerCase() + " ");
        AddPetToRoom.writeInt(PetToMove.SessionId);
        Server.WriteLine("SENDED PET UNDER :" + PetToMove.SessionId);
        AddPetToRoom.writeInt(PetToMove.X);
        AddPetToRoom.writeInt(PetToMove.Y);
        AddPetToRoom.writeUTF(PetToMove.Z);
        AddPetToRoom.writeInt(PetToMove.Type);
        AddPetToRoom.writeInt(PetToMove.Rot);
        AddPetToRoom.writeInt(PetToMove.Race);
        AddPetToRoom.writeInt(PetToMove.UserId);
        AddPetToRoom.writeBoolean(PetToMove.HaveChair);
        AddPetToRoom.writeBoolean(PetToMove.HaveUserOnMe);
        UserManager.SendMessageToUsersOnRoomId(RoomId, AddPetToRoom);
        
        if(PetToMove.HaveChair)
        {
        	ServerMessage AddToPet = new ServerMessage(ServerEvents.AddCharToPet);
            AddToPet.writeInt(PetToMove.SessionId);
            Server.WriteLine("SENDED PET UNDER :" + PetToMove.SessionId);
            AddToPet.writeInt(PetToMove.Id);
            AddToPet.writeInt(PetToMove.Race);
            AddToPet.writeInt(PetToMove.Type);
            AddToPet.writeUTF(PetToMove.Color.toLowerCase());
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
            AddToPet.writeBoolean(PetToMove.HaveChair);
            AddToPet.writeBoolean(PetToMove.HaveUserOnMe);
            UserManager.SendMessageToUsersOnRoomId(PetToMove.RoomId, AddToPet);
        }
	}
}
