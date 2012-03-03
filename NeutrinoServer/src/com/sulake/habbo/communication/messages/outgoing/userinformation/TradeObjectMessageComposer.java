package com.sulake.habbo.communication.messages.outgoing.userinformation;

import java.util.Iterator;

import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;

public class TradeObjectMessageComposer {
	public static void Compose(ServerHandler Client, Habbo CurrentUser) throws Exception
	{
		Habbo User = CurrentUser;    
		int oId = User.TradingWith;
		Habbo oUser = Habbo.UsersbyId.get(User.TradingWith);
		int ItemToAdd = Client.inreader.readInt();
		User.TradingItems.add(ItemToAdd);
        ServerMessage Trade = new ServerMessage(ServerEvents.TradeObject);
        Trade.writeInt(User.Id);
        Trade.writeInt(User.TradingItems.size());
        Iterator reader = User.TradingItems.iterator();
        while(reader.hasNext())
        {
        	UserItem Item = UserItem.Items.get(((Integer)reader.next()));
            ItemInformation furniData = ItemInformation.Items.get(Item.ItemId);
        	Trade.writeInt(Item.Id);
        	Trade.writeUTF(furniData.Type.toLowerCase());
            Trade.writeInt(Item.Id);
            Trade.writeInt(furniData.SpriteId);
            Trade.writeInt(1);
            Trade.writeBoolean(true);
            Trade.writeInt(0);
            Trade.writeUTF("1"); // extradata
            Trade.writeInt(0); // day of furni
            Trade.writeInt(0); // month of furni
            Trade.writeInt(0); // year of furni
            Trade.writeInt(0); // final 0
        }
        Trade.writeInt(oUser.Id);
        Trade.writeInt(oUser.TradingItems.size());
        Iterator oreader = oUser.TradingItems.iterator();
        while(oreader.hasNext())
        {
        	UserItem Item = UserItem.Items.get(((Integer)oreader.next()));
            ItemInformation furniData = ItemInformation.Items.get(Item.ItemId);
        	Trade.writeInt(Item.Id);
        	Trade.writeUTF(furniData.Type.toLowerCase());
            Trade.writeInt(Item.Id);
            Trade.writeInt(furniData.SpriteId);
            Trade.writeInt(1);
            Trade.writeBoolean(true);
            Trade.writeInt(0);
            Trade.writeUTF("1"); // extradata
            Trade.writeInt(0); // day of furni
            Trade.writeInt(0); // month of furni
            Trade.writeInt(0); // year of furni
            Trade.writeInt(0); // final 0
        }
        Trade.Send(Client.Socket);
        Trade.Send(oUser.CurrentSocket);
        
        if(User.TradeAccepted)
        {
    		User.TradeAccepted = false;
    		ServerMessage oTrade = new ServerMessage(ServerEvents.AcceptTrade);
    		oTrade.writeInt(User.Id);
    		oTrade.writeInt(0);
            oTrade.Send(Client.Socket);
            oTrade.Send(oUser.CurrentSocket);
        }
        
        if(oUser.TradeAccepted)
        {
    		oUser.TradeAccepted = false;
    		ServerMessage oTrade = new ServerMessage(ServerEvents.AcceptTrade);
    		oTrade.writeInt(oUser.Id);
    		oTrade.writeInt(0);
            oTrade.Send(Client.Socket);
            oTrade.Send(oUser.CurrentSocket);
        }

	}
}
