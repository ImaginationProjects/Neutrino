package com.sulake.habbo.communication.messages.outgoing.rooms;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class LoadRoomDataMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		int RoomId = Client.in.readInt();
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        if(!R.HavePowers(User.Id))
        {
        	return;
        }
        
        Server.WriteLine("Reading...");
        
        ServerMessage RoomData = new ServerMessage(ServerEvents.SerializeRoomData);
        RoomData.writeInt(R.Id);
        RoomData.writeUTF(R.Name);
        RoomData.writeUTF(R.Description);
        RoomData.writeInt(R.State);
        RoomData.writeInt(R.Category);
        RoomData.writeInt(R.MaxUsers);
        RoomData.writeInt((R.GetModel().MapSizeX >= 20 || R.GetModel().MapSizeY >= 20) ? 50 : 25);
        RoomData.writeInt(R.Tags.size()); // tags count
        RoomData.writeInt(1); // Room Blocking Disabled
        RoomData.writeInt((R.Pets) ? 1 : 0);
        RoomData.writeInt((R.PetsCanEat) ? 1 : 0);
        RoomData.writeInt((R.CanWalkOnUsers) ? 1 : 0);
        RoomData.writeInt((R.Hidewalls) ? 1 : 0);
        RoomData.writeInt(R.VipWalls); // Walls Type
        RoomData.writeInt(R.VipFloors); // Floors Type
        RoomData.Send(Socket);

	}
}
