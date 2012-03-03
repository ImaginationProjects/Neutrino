package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class TalkMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server, String Type) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        String Message = Client.inreader.readUTF();
        CommandHandler C = new CommandHandler(Message, Socket);
        if(Message.startsWith(":") && CurrentUser.RankLevel > 4 && C.ThereIsACommand())
        {
            return;
        }
        
        int PacketId = ServerEvents.Shout;
        if(Type == "talk")
        	PacketId = ServerEvents.Talk;
        else
        	PacketId = ServerEvents.Shout;
        
        ServerMessage Talk = new ServerMessage(PacketId);
        Talk.writeInt(CurrentUser.SessionId);
        Talk.writeUTF(Message);
        Talk.writeInt(0);
        Talk.writeInt(0);
        Talk.writeInt(0);
        Talk.Send(Socket);
	}
}
