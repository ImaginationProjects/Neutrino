package com.sulake.habbo.communication.messages.outgoing.userinformation;

import java.util.ArrayList;
import java.util.Iterator;

import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class EndTradeMessageComposer {
	public static void Compose(ServerHandler Client, Habbo CurrentUser) throws Exception
	{
		Habbo User = CurrentUser;    
		int oId = User.TradingWith;
		Habbo oUser = Habbo.UsersbyId.get(User.TradingWith);
		
		User.TradingWith = User.LookingTo;
		User.TradingItems = new ArrayList<Integer>();
		User.TradeAccepted = false;
        User.TradeConfirmed = false;
        User.TradingWith = 0;
        User.UpdateState("", User.CurrentSocket, User.Server);
		oUser.TradingWith = User.Id;
		oUser.TradingItems = new ArrayList<Integer>();
		oUser.TradeAccepted = false;
        oUser.TradeConfirmed = false;
        oUser.TradingWith = 0;
        oUser.UpdateState("", oUser.CurrentSocket, oUser.Server);
    	/*ServerMessage Confirm = new ServerMessage(ServerEvents.EndTrade);
        Confirm.Send(Client.Socket);
        Confirm.Send(oUser.CurrentSocket);*/
        
        ServerMessage Confirm2 = new ServerMessage(ServerEvents.TradeCancelled);
        Confirm2.writeInt(User.Id);
        Confirm2.Send(Client.Socket);
        Confirm2.Send(oUser.CurrentSocket);

	}
}
