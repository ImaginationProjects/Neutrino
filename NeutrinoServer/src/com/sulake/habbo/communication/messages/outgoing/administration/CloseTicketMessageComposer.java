package com.sulake.habbo.communication.messages.outgoing.administration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.netty.channel.Channel;

import neutrino.Environment;
import neutrino.AdministrationManager.CallForHelpState;
import neutrino.AdministrationManager.CallsForHelp;
import neutrino.AdministrationManager.Chatlog;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomEvent;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;

public class CloseTicketMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		if(User.RankLevel < 5)
        {
			return;
        }
		Habbo CurrentUser = Client.GetSession();
        Channel Socket = Client.Socket;
        int Type = Client.inreader.readInt();
        int Junk = Client.inreader.readInt();
        int CallForHelpID = Client.inreader.readInt();
        CallsForHelp C = CallsForHelp.Calls.get(CallForHelpID);
        if(C == null)
        	return;
        
        CallForHelpState State = CallForHelpState.INVALID;
        int sState = 6;
        if(Type == 2)
        {
        	State = CallForHelpState.ABUSIVE;
        	sState = 4;
        } else if(Type == 3)
        {
        	State = CallForHelpState.RESOLVED;
        	sState = 5;
        }
        C.PickedBy = CurrentUser.Id;
        Server.GetDatabase().executeUpdate("UPDATE cfhs SET state = '" + sState + "' WHERE id = " + C.Id);
        Server.GetDatabase().executeUpdate("UPDATE cfhs SET pickedby = '" + CurrentUser.Id + "' WHERE id = " + C.Id);
        
        C.State = State;
        C.stateint = sState;
        ServerMessage Issue = new ServerMessage(ServerEvents.MOD_ADDISSUE);
		Issue.writeInt(C.Id); // id
        Issue.writeInt(C.State); // state 
        Issue.writeInt(1); // cat id
        Issue.writeInt(C.Category); // reported cat id
        Issue.writeInt((Environment.getIntUnixTimestamp() - C.Timestamp)); // timestamp
        Issue.writeInt(C.Priority); // priority
        Issue.writeInt(C.ReporterId); // reporter id
        Issue.writeUTF(Habbo.UsersbyId.get(C.ReporterId).UserName); // reporter name
        Issue.writeInt(C.ReportedId); // reported id
        Issue.writeUTF((Habbo.UsersbyId.containsKey(C.ReportedId)) ? Habbo.UsersbyId.get(C.ReportedId).UserName : "Usuario desconocido"); // reported name
        Issue.writeInt(0); // mod id
        Issue.writeUTF((Habbo.UsersbyId.containsKey(C.PickedBy)) ? Habbo.UsersbyId.get(C.PickedBy).UserName : "Usuario desconocido"); // mod name
        Issue.writeUTF(C.Message); // issue message
        Issue.writeInt(C.RoomId); // room id
        Room RoomData = Room.Rooms.get(C.RoomId);
        Issue.writeUTF(RoomData.Name); // room name
        Issue.writeInt(0); // room type: 0 private - 1 public
        // if private
        if(RoomEvent.GetEventForRoomId(C.RoomId)==null)
        	Issue.writeUTF("-1");
        else {
        	RoomEvent E = RoomEvent.GetEventForRoomId(C.RoomId);
        	Issue.writeUTF(E.OwnerId + "");
            Issue.writeUTF(Habbo.UsersbyId.get(E.OwnerId).UserName);
            Issue.writeUTF(E.RoomId + "");
            Issue.writeInt(E.Category);
            Issue.writeUTF(E.Title);
            Issue.writeUTF(E.Description);
            Issue.writeUTF(E.Created);
            Issue.writeInt(E.Tags.size());
            Iterator zreader = E.Tags.iterator();
            while(zreader.hasNext())
            {
            	String tag = (String)zreader.next();
            	Issue.writeUTF(tag);
            }
        }
        Issue.writeInt(C.Category); // cat of room
        Issue.writeInt(0); // not defined
        UserManager.SendMessageToAllStaffs(Issue);
                
	}
}
