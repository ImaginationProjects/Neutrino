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

import com.sulake.habbo.communication.messages.outgoing.userinformation.LoadPetInventaryMessageComposer;

public class PickUpPetMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        int PetId = Client.in.readInt();
        Pet PetData = Pet.Pets.get(PetId);
        ServerMessage GetOut = new ServerMessage(ServerEvents.GetOutOfRoom);
        GetOut.writeUTF(PetData.SessionId + "");
        UserManager.SendMessageToUsersOnRoomId(PetData.RoomId, GetOut);
        PetData.RoomId = 0;
        PetData.IsOnRoom = false;
        if(PetData.HaveUserOnMe)
        {
        	Habbo Owner = Habbo.UsersbyId.get(PetData.UserId);
        	ServerMessage Dance = new ServerMessage(ServerEvents.ShowEffect);
            Dance.writeInt(Owner.SessionId);
            Dance.writeInt(Effects.NONE);
            Dance.writeInt(0);
            UserManager.SendMessageToUsersOnRoomId(cUser.CurrentRoomId, Dance);
            
            Owner.CurrentEffect = Effects.NONE;
            Owner.X = PetData.X;
            Owner.Y = PetData.Y;
            Owner.Z = "0.0";
            Owner.Rot = PetData.Rot;
            Owner.RidingAHorseId = 0;
            Owner.UpdateState("", Socket, Server);
        }
        PetData.HaveUserOnMe = false;
        Server.GetDatabase().executeUpdate("UPDATE pets SET roomid = '0' WHERE id = " + PetId);
        LoadPetInventaryMessageComposer.Compose(Client, cUser, Server);
        R.PetList.remove(PetData);
	}
}
