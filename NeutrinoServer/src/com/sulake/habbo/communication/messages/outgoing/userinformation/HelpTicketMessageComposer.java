package com.sulake.habbo.communication.messages.outgoing.userinformation;

import java.util.ArrayList;

import neutrino.Environment;
import neutrino.AdministrationManager.CallsForHelp;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.RoomManager.Room;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;

public class HelpTicketMessageComposer {
	public static void Compose(ServerHandler Client, Habbo CurrentUser) throws Exception
	{
		Habbo User = CurrentUser;    
		String Content = Client.inreader.readUTF();
		if(!User.IsOnRoom)
			return;
		int Me = Client.inreader.readInt();
		int Category = Client.inreader.readInt();
		int ReportedUser = Client.inreader.readInt();
		Room CurrentId = Room.Rooms.get(CurrentUser.CurrentRoomId);
		
		CallsForHelp C = CallsForHelp.Add(1, Category, 0, User.Id, ReportedUser, CurrentId.Id, Content);
		Environment.returnDB().executeUpdate("INSERT INTO cfhs (id, state, category, timestamp, priority, reporterid, reportedid, message, roomid) VALUES (NULL, '1', '" + Category + "', '" + Environment.getIntUnixTimestamp() + "', '0', '" + User.Id + "', '" + ReportedUser + "', '" + Content + "', '" + User.CurrentRoomId + "')");
		
        UserManager.SendIssueToAMod(C);
	}
}
