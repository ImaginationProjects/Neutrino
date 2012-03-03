package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.Rotation;

import org.jboss.netty.channel.Channel;

public class LookToMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server, String Type) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        int UserId = Client.inreader.readInt();
       // int UserId2 = Client.inreader.readInt();
        User.LookingTo = UserId;
        Habbo cUser = Habbo.UsersbyId.get(UserId);
        
        if(!User.IsWalking && !User.IsSit && User.RidingAHorseId == 0)
        {
        	if(!(cUser.Id == User.Id))
            {
                  User.Rot = Rotation.Calculate(User.X, User.Y, cUser.X, cUser.Y);
                  User.UpdateState("", Socket, Server);
            }
        }
        
        	ServerMessage ShowInfo = new ServerMessage(ServerEvents.LoadUserInformation);
            ShowInfo.writeInt(cUser.Id);
            ShowInfo.writeInt(0);
            ShowInfo.Send(Socket);
	}
}

/*
 * ServerMessage ShowInfo = new ServerMessage(ServerEvents.LoadUserInformation);
        ShowInfo.writeInt(cUser.Id);
        ShowInfo.writeInt(0);
        /*ShowInfo.writeInt(1);
        ShowInfo.writeUTF("ADM");
        ShowInfo.Send(Socket);
 */
