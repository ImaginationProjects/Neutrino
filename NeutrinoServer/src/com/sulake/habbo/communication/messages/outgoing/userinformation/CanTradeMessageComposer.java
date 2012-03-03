package com.sulake.habbo.communication.messages.outgoing.userinformation;

import java.util.ArrayList;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class CanTradeMessageComposer {
	public static void Compose(ServerHandler Client, Habbo CurrentUser) throws Exception
	{
		Habbo User = CurrentUser;    
		Habbo oUser = Habbo.UsersbyId.get(User.LookingTo);
		if(oUser.TradingWith != 0)
		{
			User.SendAlert("El usuario está tradeando en este momento.");
			return;
		}
		User.TradingWith = User.LookingTo;
		User.TradingItems = new ArrayList<Integer>();
		User.TradeAccepted = false;
        User.TradeConfirmed = false;
        User.UpdateState("trd", Client.Socket, User.Server);
		oUser.TradingWith = User.Id;
		oUser.TradingItems = new ArrayList<Integer>();
		oUser.TradeAccepted = false;
        oUser.TradeConfirmed = false;
        oUser.UpdateState("trd", oUser.CurrentSocket, oUser.Server);
        ServerMessage Profile = new ServerMessage(ServerEvents.CanTrade);
        Profile.writeInt(User.Id);
        Profile.writeInt(1);
        Profile.writeInt(User.LookingTo);
        Profile.writeInt(1);
        Profile.Send(Client.Socket);
        Profile.Send(Habbo.UsersbyId.get(User.LookingTo).CurrentSocket);

	}
}
