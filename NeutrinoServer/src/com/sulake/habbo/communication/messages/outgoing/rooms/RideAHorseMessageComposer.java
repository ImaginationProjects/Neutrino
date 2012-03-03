package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Effects;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.SubscriptionManager;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class RideAHorseMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room RoomData = Room.Rooms.get(RoomId);
        
        int PetId = Client.inreader.readInt();
        boolean Go = Client.inreader.readBoolean();
        Pet P = Pet.Pets.get(PetId);
        if(Go)
        {
        	P.HaveUserOnMe = true;
            ServerMessage Dance = new ServerMessage(ServerEvents.ShowEffect);
            Dance.writeInt(CurrentUser.SessionId);
            Dance.writeInt(Effects.HORSE_SADDLE);
            Dance.writeInt(0);
            UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, Dance);
            
            CurrentUser.CurrentEffect = Effects.HORSE_SADDLE;
            CurrentUser.X = P.X;
            CurrentUser.Y = P.Y;
            CurrentUser.Z = "1.0";
            CurrentUser.Rot = P.Rot;
            CurrentUser.RidingAHorseId = P.Id;
            CurrentUser.UpdateState("", Socket, Server);
        } else {
        	P.HaveUserOnMe = false;
            ServerMessage Dance = new ServerMessage(ServerEvents.ShowEffect);
            Dance.writeInt(CurrentUser.SessionId);
            Dance.writeInt(Effects.NONE);
            Dance.writeInt(0);
            UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, Dance);
            
            CurrentUser.CurrentEffect = Effects.NONE;
            CurrentUser.X = P.X;
            CurrentUser.Y = P.Y;
            CurrentUser.Z = "0.0";
            CurrentUser.Rot = P.Rot;
            CurrentUser.RidingAHorseId = 0;
            CurrentUser.UpdateState("", Socket, Server);
        }
	}
}
