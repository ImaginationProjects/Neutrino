package com.sulake.habbo.communication.messages.outgoing.userinformation;

import java.util.Iterator;

import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class ChangeTradeMessageComposer {
	public static void Compose(ServerHandler Client, Habbo CurrentUser) throws Exception
	{
		Habbo User = CurrentUser;    
		int oId = User.TradingWith;
		Habbo oUser = Habbo.UsersbyId.get(User.TradingWith);
		User.TradeAccepted = false;
		ServerMessage Trade = new ServerMessage(ServerEvents.AcceptTrade);
		Trade.writeInt(User.Id);
		Trade.writeInt(0);
        Trade.Send(Client.Socket);
        Trade.Send(oUser.CurrentSocket);

	}
}
