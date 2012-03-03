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

public class LoadMapsMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        ServerMessage HeightMap = new ServerMessage(ServerEvents.HeightMap1);
        HeightMap.writeUTF(R.GetModel().SerializeMap);
        HeightMap.Send(Socket);
        
        ServerMessage RelativeMap = new ServerMessage(ServerEvents.HeightMap2);
        String Map = R.GetModel().SerializeRelativeMap;
        RelativeMap.writeUTF(Map);
        RelativeMap.Send(Socket);		
	}
}
