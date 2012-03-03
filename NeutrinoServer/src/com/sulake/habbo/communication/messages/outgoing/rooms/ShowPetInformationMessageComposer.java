package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class ShowPetInformationMessageComposer {
	public static void Compose(ServerHandler Client, Habbo cUser, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        int PetId = Client.in.readInt();
        Pet PetToMove = Pet.Pets.get(PetId);
        
        ServerMessage AddPetToRoom = new ServerMessage(ServerEvents.ShowPetInfo);
        AddPetToRoom.writeInt(PetToMove.Id);
        AddPetToRoom.writeUTF(PetToMove.PetName);
        AddPetToRoom.writeInt(PetToMove.Level);
        AddPetToRoom.writeInt(20); // Max Level
        AddPetToRoom.writeInt(PetToMove.Experience);
        AddPetToRoom.writeInt(PetToMove.MaxExperience); // Max Experience
        AddPetToRoom.writeInt(PetToMove.Hungry);
        AddPetToRoom.writeInt(PetToMove.MaxHungry); // Max Energy
        AddPetToRoom.writeInt(PetToMove.Happiness);
        AddPetToRoom.writeInt(PetToMove.MaxHappiness); // Max Hapiness
        AddPetToRoom.writeInt(2);
        AddPetToRoom.writeInt(PetToMove.UserId);
        double TimeLeft2 = Environment.getIntUnixTimestamp() - PetToMove.CreatedStamp;
        int TotalDaysLeft2 = (int)Math.ceil(TimeLeft2 / 86400);
        AddPetToRoom.writeInt(TotalDaysLeft2); // days
        AddPetToRoom.writeUTF(Habbo.UsersbyId.get(PetToMove.UserId).UserName);
        AddPetToRoom.writeInt(3);
        AddPetToRoom.writeBoolean(PetToMove.HaveChair);
        AddPetToRoom.writeBoolean(PetToMove.HaveUserOnMe);
        AddPetToRoom.writeInt(4);
        AddPetToRoom.writeInt(2);
        AddPetToRoom.writeInt(6);
        AddPetToRoom.writeInt(10);
        AddPetToRoom.writeInt(0);
        /*AddPetToRoom.writeInt(5);
        AddPetToRoom.writeInt(0);
        AddPetToRoom.writeUTF("");*/
        AddPetToRoom.Send(Socket);
	}
}
