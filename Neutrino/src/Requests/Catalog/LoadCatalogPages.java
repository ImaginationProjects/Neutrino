/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Requests.Catalog;
import java.util.*;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.CatalogManager.CatalogPage;

/**
 *
 * @author Julián
 */
public class LoadCatalogPages extends Handler implements Runnable {
    private ServerHandler Client;
    private Environment Server;
    private FutureTask T;
    @Override
    public void Load(ServerHandler Client, Environment Server, FutureTask T) throws Exception
    {
        this.Client = Client;
        this.Server = Server;
        this.T = T;
    }
    
    public void run() {
        try {
        ServerMessage CatalogPages = new ServerMessage(ServerEvents.SendCatalogPage);
        CatalogPages.writeBoolean(true);
        CatalogPages.writeInt(0);
        CatalogPages.writeInt(0);
        CatalogPages.writeInt(-1);
        CatalogPages.writeBoolean(false);
        CatalogPages.writeBoolean(false);
        CatalogPages.writeInt(CatalogPage.GetMapForId(-1).size());
        Iterator reader = CatalogPage.sortByComparator(CatalogPage.GetMapForId(-1)).entrySet().iterator();
        while(reader.hasNext())
        {
            CatalogPage Page = (CatalogPage)(((Map.Entry)reader.next()).getValue());
            CatalogPages.writeBoolean(Page.PageOpen);
            CatalogPages.writeInt(Page.IconColor);
            CatalogPages.writeInt(Page.IconImage);
            CatalogPages.writeInt(Page.Id);
            CatalogPages.writeUTF(Page.Name);
            Map<Integer, CatalogPage> noCategorys = CatalogPage.sortByComparator(CatalogPage.GetMapForId(Page.Id));
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
        } catch (Exception e)
        {
            
        }
        
    }
}
