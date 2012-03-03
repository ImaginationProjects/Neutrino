package com.sulake.habbo.communication.messages.outgoing.catalog;

import java.util.*;

import org.jboss.netty.channel.Channel;

import com.sulake.habbo.communication.messages.outgoing.userinformation.LoadPetInventaryMessageComposer;

import neutrino.Environment;
import neutrino.CatalogManager.CatalogItem;
import neutrino.CatalogManager.CatalogPage;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.SubscriptionManager;

public class PurchaseCatalogItemMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		Habbo CurrentHabbo = User;
        Channel Socket = Client.Socket;
        int PageId = Client.inreader.readInt();
        int ItemId = Client.inreader.readInt();
        String ExtraData = Client.inreader.readUTF();
        
        CatalogPage Page = CatalogPage.Pages.get(PageId);
        CatalogItem Item = CatalogItem.Items.get(ItemId);
        
        if(Item.Name.contains("HABBO_CLUB_VIP_"))
        {
            if(Item.CostCredits > 0 && CurrentHabbo.Credits > Item.CostCredits)
                CurrentHabbo.UpdateCredits((CurrentHabbo.Credits - Item.CostCredits), Client.Socket, Server);
            else
                return;
            SubscriptionManager Sub = new SubscriptionManager(CurrentHabbo, Server);
            if(Item.Id == 331)
            {
                // 1 day
                Sub.AddOrExtendSubscription("habbo_vip", (60 * 60 * 24));
                CurrentHabbo.UpdateFuserights(Client.Socket, Server);
                CurrentHabbo.Updateclub(Server, Client);
            } else if(Item.Id == 332)
            {
                // 7 days
                Sub.AddOrExtendSubscription("habbo_vip", (60 * 60 * 24 * 7));
                CurrentHabbo.UpdateFuserights(Client.Socket, Server);
                CurrentHabbo.Updateclub(Server, Client);
            } else if(Item.Id == 333)
            {
                // 3 days
                Sub.AddOrExtendSubscription("habbo_vip", (60 * 60 * 24 * 3));
                CurrentHabbo.UpdateFuserights(Client.Socket, Server);
                CurrentHabbo.Updateclub(Server, Client);
            } else if(Item.Id == 334)
            {
                // 1 month
                Server.WriteLine(CurrentHabbo.UserName + " wants 1 month of vip!");
                Sub.AddOrExtendSubscription("habbo_vip", (60 * 60 * 24 * 31));
                CurrentHabbo.UpdateFuserights(Client.Socket, Server);
                CurrentHabbo.Updateclub(Server, Client);
            } else if(Item.Id == 335)
            {
                // 3 months
                Sub.AddOrExtendSubscription("habbo_vip", (60 * 60 * 24 * 31 * 3));
                CurrentHabbo.UpdateFuserights(Client.Socket, Server);
                CurrentHabbo.Updateclub(Server, Client);
            }
        } else if(Item.Name.contains("avatar_effect"))
        {
        	// null
        } else {
            
            Iterator ItemsToPurchase = Item.ItemIds.iterator();
            Item.TimesSelled++;
            if(Item.CostCredits > 0)
            {
            if(CurrentHabbo.Credits > Item.CostCredits)
                CurrentHabbo.UpdateCredits((CurrentHabbo.Credits - Item.CostCredits), Client.Socket, Server);
            else
                return;
            }
            
            if(Item.CostPixels > 0)
            {
            if(CurrentHabbo.Pixels > Item.CostPixels)
                CurrentHabbo.UpdatePixels((CurrentHabbo.Pixels - Item.CostPixels), Client.Socket, Server);
            else
                return;
            }
            
            
            
            while(ItemsToPurchase.hasNext())
            {
                int furniId = (Integer)ItemsToPurchase.next();
                ItemInformation furniData = ItemInformation.Items.get(furniId);
                if(furniData.Interactor.equals("pet"))
            	{
            		int PetRace = Integer.parseInt(Item.Name.replace("a0 pet", ""));
                    String[] SeparePets = ExtraData.split("\n");
                    String Name = SeparePets[0];
                    int PetType = Integer.parseInt(SeparePets[1]);
                    String Color = SeparePets[2];
                    ServerMessage PurchaseItem = new ServerMessage(ServerEvents.SendItem);
                    PurchaseItem.writeInt(furniData.Id);
                    if(Item.ItemIds.size() == 1)
                        PurchaseItem.writeUTF(Item.Name);
                    else
                        PurchaseItem.writeUTF(furniData.Name);
                    PurchaseItem.writeInt(Item.CostCredits);
                    PurchaseItem.writeInt(Item.CostPixels);
                    PurchaseItem.writeInt(0);
                    PurchaseItem.writeInt(0);
                    PurchaseItem.writeBoolean(true);
                    PurchaseItem.writeUTF(furniData.Type.toLowerCase());
                    PurchaseItem.writeInt(furniData.SpriteId);
                    if (Item.Name.contains("wallpaper"))
                        PurchaseItem.writeUTF(Item.Name.split("_")[2]); // shit for wallpapers
                    else if(Item.Name.contains("floor"))
                        PurchaseItem.writeUTF(Item.Name.split("_")[2]); // shit for wallpapers
                    else if(Item.Name.contains("landscape"))
                        PurchaseItem.writeUTF(Item.Name.split("_")[2]); // shit for wallpapers
                    else
                        PurchaseItem.writeUTF(Item.ExtraInformation); // shit for music and other shit                        
                    PurchaseItem.writeInt(1);
                    PurchaseItem.writeInt(0);
                    PurchaseItem.writeInt(0);               
                    PurchaseItem.Send(Socket);
                    
                    ServerMessage AlertPurchase = new ServerMessage(ServerEvents.InventoryWarn);
                    AlertPurchase.writeInt(1); // items alert!
                    AlertPurchase.writeInt(3);
                    int Amount = Item.Amount;
                    AlertPurchase.writeInt(1);
                    AlertPurchase.writeInt((Pet.LastId + 1)*1024*30);
                    AlertPurchase.Send(Socket);
                    
                    Server.GetDatabase().executeUpdate("INSERT INTO pets (id, userid, race, petname, type, color, experience, hungry, energy, level, createdstamp, roomid) VALUES (NULL, '" + User.Id + "', '" + PetRace + "', '" + Name + "', '" + PetType + "', '" + Color + "', '0', '150', '100', '1', '" + Server.getIntUnixTimestamp() + "', '0');");
                    Pet.AddPet(User.Id, PetRace, Name, PetType, Color);
                    LoadPetInventaryMessageComposer.Compose(Client, User, Server);
            	} else {
            		ServerMessage PurchaseItem = new ServerMessage(ServerEvents.SendItem);
                    PurchaseItem.writeInt(furniData.Id);
                    if(Item.ItemIds.size() == 1)
                        PurchaseItem.writeUTF(Item.Name);
                    else
                        PurchaseItem.writeUTF(furniData.Name);
                    PurchaseItem.writeInt(Item.CostCredits);
                    PurchaseItem.writeInt(Item.CostPixels);
                    PurchaseItem.writeInt(0);
                    PurchaseItem.writeInt(0);
                    PurchaseItem.writeBoolean(true);
                    PurchaseItem.writeUTF(furniData.Type.toLowerCase());
                    PurchaseItem.writeInt(furniData.SpriteId);
                    if (Item.Name.contains("wallpaper"))
                        PurchaseItem.writeUTF(Item.Name.split("_")[2]); // shit for wallpapers
                    else if(Item.Name.contains("floor"))
                        PurchaseItem.writeUTF(Item.Name.split("_")[2]); // shit for wallpapers
                    else if(Item.Name.contains("landscape"))
                        PurchaseItem.writeUTF(Item.Name.split("_")[2]); // shit for wallpapers
                    else
                        PurchaseItem.writeUTF(Item.ExtraInformation); // shit for music and other shit                        
                    PurchaseItem.writeInt(1);
                    PurchaseItem.writeInt(0);
                    PurchaseItem.writeInt(0);               
                    PurchaseItem.Send(Socket);
                    
                    ServerMessage AlertPurchase = new ServerMessage(ServerEvents.InventoryWarn);
                    AlertPurchase.writeInt(1); // items alert!
                    int Type = 2;
                    if(furniData.Type.equals("s"))
                    {
                        if(furniData.Interactor.equals("pet"))
                            Type = 3;
                        else
                            Type = 1;
                    }
                    AlertPurchase.writeInt(Type);
                    int Amount = Item.Amount;
                    if(Item.ExtraAmounts.containsKey(furniId))
                        Amount = Item.ExtraAmounts.get(furniId);
                    AlertPurchase.writeInt(Amount);
                    
                    for(int i = 0; i != Amount; i++)
                    {
                    		Server.GetDatabase().executeUpdate("INSERT INTO users_items (id, userid, itemid, extradata) VALUES (NULL, '" + CurrentHabbo.Id + "', '" + furniId + "', '" + ExtraData + "');");
                            UserItem I = new UserItem();
                            I.Id = (UserItem.LastId + 1);
                            UserItem.LastId++;
                            I.UserId = CurrentHabbo.Id;
                            I.ItemId = furniId;
                            I.ExtraData = ExtraData;
                            UserItem.Items.put(I.Id, I);
                            AlertPurchase.writeInt(I.Id);
                    }
                    AlertPurchase.Send(Socket);
                    
                    ServerMessage UpdateInv = new ServerMessage(ServerEvents.UpdateInventory);
                    UpdateInv.Send(Socket);
            	}
                
            }		
        }
	}
}
