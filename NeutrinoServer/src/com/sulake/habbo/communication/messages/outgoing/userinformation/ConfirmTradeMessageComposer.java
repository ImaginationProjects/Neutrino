package com.sulake.habbo.communication.messages.outgoing.userinformation;

import java.util.ArrayList;
import java.util.Iterator;

import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Friend;
import neutrino.UserManager.Habbo;
import com.sulake.habbo.communication.messages.outgoing.userinformation.*;

public class ConfirmTradeMessageComposer {
	public static void Compose(ServerHandler Client, Habbo CurrentUser, Environment Server) throws Exception
	{
		Habbo User = CurrentUser;    
		int oId = User.TradingWith;
		Habbo oUser = Habbo.UsersbyId.get(User.TradingWith);
		User.TradeConfirmed = true;
        
        if(oUser.TradeConfirmed)
        {  
        	Iterator reader = User.TradingItems.iterator();
        	while(reader.hasNext())
        	{
        		UserItem Item = UserItem.Items.get(((Integer)reader.next()));
                ItemInformation furniData = ItemInformation.Items.get(Item.ItemId);
                
                UserItem I = new UserItem();
                I.Id = UserItem.LastId + 1;
                UserItem.LastId++;
                I.UserId = oUser.Id;
                I.ItemId = Item.ItemId;
                I.ExtraData = Item.ExtraData;
                UserItem.Items.put(I.Id, I);
                Server.GetDatabase().executeUpdate("INSERT INTO users_items (id, userid, itemid, extradata) VALUES (NULL, '" + oUser.Id + "', '" + Item.ItemId + "', '" + Item.ExtraData + "');");                
                UserItem.DeleteItem(Item.Id, Server);
                
                ServerMessage RemoveFromInventary = new ServerMessage(ServerEvents.RemoveFromInventary);
                RemoveFromInventary.writeInt(Item.Id);
                RemoveFromInventary.Send(Client.Socket);
                
                LoadInventaryMessageComposer.Composer(oUser);
        	}
        	
        	Iterator oreader = oUser.TradingItems.iterator();
        	while(oreader.hasNext())
        	{
        		UserItem Item = UserItem.Items.get(((Integer)oreader.next()));
                ItemInformation furniData = ItemInformation.Items.get(Item.ItemId);
                
                UserItem I = new UserItem();
                I.Id = UserItem.LastId + 1;
                UserItem.LastId++;
                I.UserId = User.Id;
                I.ItemId = Item.ItemId;
                I.ExtraData = Item.ExtraData;
                UserItem.Items.put(I.Id, I);
                Server.GetDatabase().executeUpdate("INSERT INTO users_items (id, userid, itemid, extradata) VALUES (NULL, '" + User.Id + "', '" + Item.ItemId + "', '" + Item.ExtraData + "');");                
                UserItem.DeleteItem(Item.Id, Server);
                
                ServerMessage RemoveFromInventary = new ServerMessage(ServerEvents.RemoveFromInventary);
                RemoveFromInventary.writeInt(Item.Id);
                RemoveFromInventary.Send(oUser.CurrentSocket);
                
                LoadInventaryMessageComposer.Composer(User);
        	}
        	
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
        	ServerMessage Confirm = new ServerMessage(ServerEvents.EndTrade);
            Confirm.Send(Client.Socket);
            Confirm.Send(oUser.CurrentSocket);
        }

	}
}
