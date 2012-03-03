package com.sulake.habbo.communication.messages.outgoing.rooms;
// [0][0][0][0]OMG It's Itachi's Rom D:![0]faggy faggy fag![0][0][0][0][0][0][0][0][0][0][0][0][1][0][0][0][0][0][0][0][1][0][0][0][0][0][0][0][0][0][0][0][0]
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class SaveRoomUpdatesMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		//[0][0][0][0]OMG It's Itachi's Rom D:![0]faggy faggy fag!
		//[0][0][0][1][0][0][0][0][0][0][0][0][0][0][0][0]
		int RoomId = Client.inreader.readInt();
		Room RoomData = Room.Rooms.get(RoomId);
		String Name = Client.inreader.readUTF();
		if(RoomData.Name != Name)
			RoomData.Name = Name; Server.GetDatabase().executeUpdate("UPDATE rooms SET name = \"" + Name + "\" WHERE id = " + RoomId);
		String Description = Client.inreader.readUTF();
		if(RoomData.Description != Description)
			RoomData.Description = Description; Server.GetDatabase().executeUpdate("UPDATE rooms SET description = \"" + Description + "\" WHERE id = " + RoomId);
		int State = Client.inreader.readInt();
		if(RoomData.State != State)
			RoomData.State = State; Server.GetDatabase().executeUpdate("UPDATE rooms SET state = \"" + State + "\" WHERE id = " + RoomId);
		String Password = Client.inreader.readUTF();
		if(RoomData.Password != Password)
			RoomData.Password = Password; Server.GetDatabase().executeUpdate("UPDATE rooms SET password = \"" + State + "\" WHERE id = " + RoomId);
		int MaxUsers = Client.inreader.readInt();
		if(RoomData.MaxUsers != MaxUsers)
			RoomData.MaxUsers = State; Server.GetDatabase().executeUpdate("UPDATE rooms SET maxusers = " + MaxUsers + " WHERE id = " + RoomId);
		int Category = Client.inreader.readInt();
		if(RoomData.Category != Category)
			RoomData.Category = Category; Server.GetDatabase().executeUpdate("UPDATE rooms SET category = " + Category + " WHERE id = " + RoomId);
		int TagCount = Client.inreader.readInt();
		if(TagCount == 1)
		{
			String Tag = Client.inreader.readUTF();
		} 
		else if(TagCount == 2)
		{
			String Tag = Client.inreader.readUTF();
			String Tag2 = Client.inreader.readUTF();
		}
		int DontKnow = Client.inreader.readInt();
		boolean AllowPets = Client.inreader.readBoolean();
		if(RoomData.Pets != AllowPets)
			RoomData.Pets = AllowPets; Server.GetDatabase().executeUpdate("UPDATE rooms SET pets = " + ((AllowPets) ? 1 : 0) + " WHERE id = " + RoomId);
		boolean AllowPetsEat = Client.inreader.readBoolean();
		if(RoomData.PetsCanEat != AllowPetsEat)
			RoomData.PetsCanEat = AllowPetsEat; Server.GetDatabase().executeUpdate("UPDATE rooms SET petseat = " + ((AllowPetsEat) ? 1 : 0) + " WHERE id = " + RoomId);
		boolean AllowWalk = Client.inreader.readBoolean();
		boolean HideWalls = Client.inreader.readBoolean();
		boolean GoWalls = false;
		if(RoomData.Hidewalls != HideWalls)
			GoWalls = HideWalls; RoomData.Hidewalls = HideWalls; Server.GetDatabase().executeUpdate("UPDATE rooms SET hidewalls = " + ((HideWalls) ? 1 : 0) + " WHERE id = " + RoomId);
		boolean VIPSHaveChanged = false;;
		int VipWalls = Client.inreader.readInt();
		if(RoomData.VipWalls != VipWalls)
			VIPSHaveChanged = true; RoomData.VipWalls = VipWalls; Server.GetDatabase().executeUpdate("UPDATE rooms SET vip_walls = " + VipWalls + " WHERE id = " + RoomId);
		int VipFloors = Client.inreader.readInt();
		if(RoomData.VipFloors != VipFloors)
			VIPSHaveChanged = true; RoomData.VipFloors = VipFloors; Server.GetDatabase().executeUpdate("UPDATE rooms SET vip_floors = " + VipFloors + " WHERE id = " + RoomId);
		
		if(VIPSHaveChanged || GoWalls)
		{
			ServerMessage ExtraData = new ServerMessage(ServerEvents.VipWallsAndFloors);
	        ExtraData.writeInt(RoomData.VipWalls);
	        ExtraData.writeInt(RoomData.VipFloors);
	        ExtraData.writeBoolean(false);
	        ExtraData.Send(Client.Socket);
		}
	}
}
