package com.sulake.habbo.communication.messages.outgoing.catalog;

import java.util.*;

import neutrino.CatalogManager.CatalogPage;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadCatalogMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		ServerMessage CatalogPages = new ServerMessage(ServerEvents.SendCatalogPage);
        CatalogPages.writeBoolean(true);
        CatalogPages.writeInt(0);
        CatalogPages.writeInt(0);
        CatalogPages.writeInt(-1);
        CatalogPages.writeBoolean(false);
        CatalogPages.writeBoolean(false);
        CatalogPages.writeInt(CatalogPage.GetMapForId(-1, User).size());
        Iterator reader = CatalogPage.sortByComparator(CatalogPage.GetMapForId(-1, User)).entrySet().iterator();
        while(reader.hasNext())
        {
        	
            CatalogPage Page = (CatalogPage)(((Map.Entry)reader.next()).getValue());
            CatalogPages.writeBoolean(Page.PageOpen);
            CatalogPages.writeInt(Page.IconColor);
            CatalogPages.writeInt(Page.IconImage);
            CatalogPages.writeInt(Page.Id);
            CatalogPages.writeUTF(Page.Name);
            Map<Integer, CatalogPage> noCategorys = CatalogPage.sortByComparator(CatalogPage.GetMapForId(Page.Id, User));
            CatalogPages.writeInt(noCategorys.size());
            Iterator treader = noCategorys.entrySet().iterator();
            while(treader.hasNext())
            {
                CatalogPage sPage = (CatalogPage)(((Map.Entry)treader.next()).getValue());
                CatalogPages.writeBoolean(sPage.PageOpen);
                CatalogPages.writeInt(sPage.IconColor);
                CatalogPages.writeInt(sPage.IconImage);
                CatalogPages.writeInt(sPage.Id);
                CatalogPages.writeUTF(sPage.Name);
                CatalogPages.writeInt(0);
            }
        }
        CatalogPages.writeBoolean(false); // new items shit!
        CatalogPages.Send(Client.Socket);
	}
}
