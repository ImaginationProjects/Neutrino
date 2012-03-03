package com.sulake.habbo.communication.messages.outgoing.catalog;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import neutrino.CatalogManager.CatalogItem;
import neutrino.CatalogManager.CatalogPage;
import neutrino.ItemManager.ItemInformation;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadCatalogPageMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		Integer I = Client.inreader.readInt();
        CatalogPage PageData = CatalogPage.Pages.get(I);
        if (!PageData.PageEnabled) {
            return;
        }
        
        if(PageData.MinRank > User.RankLevel)
        	return;

        ServerMessage Catalog = new ServerMessage(ServerEvents.SendCatalogPageData);
        Catalog.writeInt(PageData.Id);
        if ("frontpage".equals(PageData.Type)) {
            Catalog.writeUTF("frontpage3");
            Catalog.writeInt(3);
            Catalog.writeUTF("catalog_frontpage_headline_shop_DKCOMNLDE_02");
            Catalog.writeUTF("topstory_xmas11_03");
            Catalog.writeUTF("");
            Catalog.writeInt(11);
            Catalog.writeUTF("OMG ITACHI RULES!");
            Catalog.writeUTF("Hey Habbo, you can see that :O!");
            Catalog.writeUTF("Wow! Nacha marica");
            Catalog.writeUTF("Â¿CÃ³mo conseguir Habbo CrÃ©ditos?");
            Catalog.writeUTF("You can get Habbo Credits via Prepaid Cards, Home Phone, Credit Card, Mobile, completing offers and more!\n" + (char) 10 + "" + (char) 10 + " To redeem your Habbo Credits, enter your voucher code below.");
            Catalog.writeUTF("Voucheras aquÃ­ nena:");
            Catalog.writeUTF("Rares");
            Catalog.writeUTF("#FEFEFE");
            Catalog.writeUTF("#FEFEFE");
            Catalog.writeUTF("Bla bla bla!");
            Catalog.writeUTF("magic.credits");
        } else if ("petpage".equals(PageData.Type)) {
        	Catalog.writeUTF("pets");
            Catalog.writeInt(2);
            Catalog.writeUTF(PageData.HeadLine);
            Catalog.writeUTF(PageData.Teaser);
            Catalog.writeInt(4);
            Catalog.writeUTF(PageData.Text);
            Catalog.writeUTF("Dale un nombre:");
            if (PageData.Name.contains("Pollit"))
            {
                Catalog.writeUTF("");
                Catalog.writeUTF("");
            }
            else
            {
                if (PageData.Name.contains("Tortug") || PageData.Name.contains("Ara�") || PageData.Name.contains("Rana") || PageData.Name.contains("Drag") || PageData.Name.contains("Mono"))
                {
                	Catalog.writeUTF("Dale un nombre:");
                	Catalog.writeUTF("Selecciona una raza:");
                }
                else
                {
                	Catalog.writeUTF("Escoje un color:");
                	Catalog.writeUTF("Selecciona una raza:");
                }
            }
        } else if("hs_saddles".equals(PageData.Type))
        {
        	//Catalog.writeUTF("hs_saddles");
        	Catalog.writeUTF("petcustomization");
            Catalog.writeInt(2);
            Catalog.writeUTF(PageData.HeadLine);
            Catalog.writeUTF(PageData.Teaser);
            Catalog.writeInt(3);
            Catalog.writeUTF(PageData.Text);
            Catalog.writeUTF("");
            Catalog.writeUTF("");
        	//[0][10]hs_saddles[0]petcustomization[0][0][0][2][0]catalog_saddles_header1_es
        }else if ("guild_frontpage".equals(PageData.Type)) {
            Catalog.writeUTF("guild_frontpage");
            Catalog.writeInt(2);
            Catalog.writeUTF(PageData.HeadLine);
            Catalog.writeUTF(PageData.Teaser);
            Catalog.writeInt(3);
            Catalog.writeUTF("lololol");
            Catalog.writeUTF("* YES!\n* blahblahblah\n* :O!\n* SOY LOCA CON MI TIGRE (8)");
            Catalog.writeUTF("ITACHI RULES?");
        } else if("spacepage".equals(PageData.Type))
        {
            Catalog.writeUTF("spaces_new");
            Catalog.writeInt(1);
            Catalog.writeUTF(PageData.HeadLine);
            Catalog.writeInt(1);
            Catalog.writeUTF(PageData.Text);
        } else if("musicshop".equals(PageData.Type))
        {
            Catalog.writeUTF("soundmachine");
            Catalog.writeInt(2);
            Catalog.writeUTF(PageData.HeadLine);
            Catalog.writeUTF(PageData.Teaser);
            Catalog.writeInt(2);
            Catalog.writeUTF(PageData.Text);
            Catalog.writeUTF(PageData.TextDetails);
        } else if("club_buy".equals(PageData.Type))
        {
            Catalog.writeUTF("vip_buy");
            Catalog.writeInt(2);
            Catalog.writeUTF("ctlg_buy_vip_header");
            Catalog.writeUTF("ctlg_buy_vip_picture");
            Catalog.writeInt(0);                
        } else if("instructions".equals(PageData.Type))
        {
        	Catalog.writeUTF("recycler_info");
            Catalog.writeInt(2);
            Catalog.writeUTF(PageData.HeadLine);
            Catalog.writeUTF(PageData.Teaser);
            Catalog.writeInt(3);
            Catalog.writeUTF(PageData.Text);
            String TextDetails = PageData.TextDetails.replace("{separe}", ";:;");
            String[] Details = TextDetails.split(";:;");
            Catalog.writeUTF(Details[0].replace("|", '\n' + ""));
            Catalog.writeUTF(Details[1].replace("|", '\n' + "") + '\n' + '\n' + PageData.AnotherText);
            Catalog.writeInt(0);                
        } else if("marketplace_offers".equals(PageData.Type))
        {
        	Catalog.writeUTF("marketplace");
            Catalog.writeInt(1);
            Catalog.writeUTF(PageData.HeadLine);
            Catalog.writeInt(0);                
            Catalog.writeInt(0);                
        } else if("marketplace_mine".equals(PageData.Type))
        {
        	Catalog.writeUTF("marketplace_own_items");
        	Catalog.writeInt(1);
            Catalog.writeUTF(PageData.HeadLine);
            Catalog.writeInt(0);                
            Catalog.writeInt(0);              
        } else {
            Catalog.writeUTF("default_3x3");
            Catalog.writeInt(3);
            Catalog.writeUTF(PageData.HeadLine);
            Catalog.writeUTF(PageData.Teaser);
            Catalog.writeUTF(PageData.TextSpecial);
            Catalog.writeInt(3);
            Catalog.writeUTF(PageData.Text);
            Catalog.writeUTF(PageData.TextDetails);
            Catalog.writeUTF(PageData.TextTeaser);
        }
        Map<Integer, CatalogItem> Items = new HashMap<Integer, CatalogItem>();
        if(PageData.Type.equals("news"))
        {
            Items = CatalogItem.LoadNew25Ids();
        } else if(PageData.Type.equals("bestsellers"))
        {
        	Items = CatalogItem.Load25MostSelledIds();
        }
        else
            Items = CatalogItem.GetItemsForPageId(PageData.Id);
        if (PageData.Id != 2) {
            Catalog.writeInt(Items.size()); // Count Items
            Iterator reader = Items.entrySet().iterator();
            while (reader.hasNext()) {
                CatalogItem ItemData = (CatalogItem) (((Map.Entry) reader.next()).getValue());
                Catalog.writeInt(ItemData.Id); // ItemId
                Catalog.writeUTF(ItemData.Name); // Name
                Catalog.writeInt(ItemData.CostCredits); // Cost Credits
                Catalog.writeInt(ItemData.CostPixels); // Activity points
                Catalog.writeInt(0); // quest shit
                Catalog.writeBoolean(false); // Can gift?
                Catalog.writeInt(ItemData.ItemIds.size()); // count of items (for deals)
                Iterator treader = ItemData.ItemIds.iterator();
                while (treader.hasNext()) {
                    int ItemId = (Integer)treader.next();
                    if(!ItemInformation.Items.containsKey(ItemId))
                    	continue;
                    ItemInformation furniData = ItemInformation.Items.get(ItemId);
                    Catalog.writeUTF(furniData.Type); // type: s, i, h, etc
                    Catalog.writeInt(furniData.SpriteId); // spriteid
                    if (ItemData.Name.contains("wallpaper"))
                        Catalog.writeUTF(ItemData.Name.split("_")[2]); // shit for wallpapers
                    else if(ItemData.Name.contains("floor"))
                        Catalog.writeUTF(ItemData.Name.split("_")[2]); // shit for wallpapers
                    else if(ItemData.Name.contains("landscape"))
                        Catalog.writeUTF(ItemData.Name.split("_")[2]); // shit for wallpapers
                    else
                        Catalog.writeUTF(ItemData.ExtraInformation); // shit for music and other shit
                    int Amount = ItemData.Amount;
                    if(ItemData.ExtraAmounts.containsKey(ItemId))
                    Amount = ItemData.ExtraAmounts.get(ItemId);
                    Catalog.writeInt(Amount); // amount of items
                    Catalog.writeInt(-1); // separe
                }
                Catalog.writeInt(ItemData.IsClub); // is club/vip/etc shit
            }
        } else {
            Catalog.writeInt(0);
        }
        Catalog.writeInt(-1); // Final Shit
        Catalog.Send(Client.Socket);
        
        // Extra packets
        if("club_buy".equals(PageData.Type))
        {
            ServerMessage Club = new ServerMessage(ServerEvents.ClubBuy);
            Map<Integer, CatalogItem> cItems = CatalogItem.GetItemsForPageId(PageData.Id);
            Club.writeInt(cItems.size()); // Count Items
            Iterator reader = cItems.entrySet().iterator();
            while (reader.hasNext()) {
                CatalogItem ItemData = (CatalogItem) (((Map.Entry) reader.next()).getValue());
                Club.writeInt(ItemData.Id); // Id
                Club.writeUTF(ItemData.Name); // Name
                Club.writeInt(ItemData.CostCredits); // Creidts
                Club.writeBoolean(true); // ? VIP Maybe?
                int MonthsorDays = Integer.parseInt(ItemData.Name.split("_")[3]);
                if(ItemData.Name.split("_")[4].contains("MONTH"))
                {
                    Club.writeInt(MonthsorDays); // Months Left
                    Club.writeInt(3 * MonthsorDays); // Days Left
                    Club.writeInt(3 * MonthsorDays); // ??
                } else {
                    Club.writeInt(0); // Months Left
                    Club.writeInt(MonthsorDays); // Days left
                    Club.writeInt(MonthsorDays); // ??
                }
                Club.writeInt(2012); //  // futureyear
                Club.writeInt(3); // future month
                Club.writeInt(27); // future day                    
            }
            Club.writeInt(1);
            Club.Send(Client.Socket);
        }

	}
}
