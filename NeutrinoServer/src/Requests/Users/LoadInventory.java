/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Requests.Users;
import java.util.*;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.*;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import org.jboss.netty.channel.Channel;
/**
 *
 * @author Julián
 */
public class LoadInventory extends Handler implements Runnable {
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
        Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        List<UserItem> AllItems = UserItem.GetItemsbyUserId(CurrentUser.Id);
        List<UserItem> WallItems = new ArrayList<UserItem>();
        List<UserItem> FloorItems = new ArrayList<UserItem>();
        Iterator reader = AllItems.iterator();
        while(reader.hasNext())
        {
            UserItem I = (UserItem)(reader.next());
            ItemInformation furniData = ItemInformation.Items.get(I.ItemId);
            if(furniData.Type.equals("s"))
                FloorItems.add(I);
            else
            {
                WallItems.add(I);
            }
        }
        
        ServerMessage FloorInventory = new ServerMessage(ServerEvents.SendInventory);
        FloorInventory.writeUTF("S");
        FloorInventory.writeInt(1);
        FloorInventory.writeInt(1);
        FloorInventory.writeInt(FloorItems.size());
        Iterator Rreader = FloorItems.iterator();
        while(Rreader.hasNext())
        {
            UserItem I = (UserItem)(Rreader.next());
            ItemInformation furniData = ItemInformation.Items.get(I.ItemId);
            FloorInventory.writeInt(I.Id);
            FloorInventory.writeUTF(furniData.Type.toUpperCase());
            FloorInventory.writeInt(furniData.Id);
            FloorInventory.writeInt(furniData.SpriteId);
            FloorInventory.writeInt(1);
            FloorInventory.writeUTF(I.ExtraData);
            FloorInventory.writeInt(0);
            FloorInventory.writeBoolean(furniData.CanRecycle);
            FloorInventory.writeBoolean(furniData.CanTrade);
            FloorInventory.writeBoolean(furniData.CanStack);
            FloorInventory.writeBoolean(furniData.CanSell);
            FloorInventory.writeInt(-1);
            FloorInventory.writeUTF("");
            FloorInventory.writeInt(0);
        }
        FloorInventory.Send(Socket);
        
        ServerMessage WallInventory = new ServerMessage(ServerEvents.SendInventory);
        WallInventory.writeUTF("I");
        WallInventory.writeInt(1);
        WallInventory.writeInt(1);
        WallInventory.writeInt(WallItems.size());
        Iterator Wreader = WallItems.iterator();
        while(Wreader.hasNext())
        {
            UserItem I = (UserItem)(Wreader.next());
            ItemInformation furniData = ItemInformation.Items.get(I.ItemId);
            WallInventory.writeInt(I.Id);
            WallInventory.writeUTF(furniData.Type.toUpperCase());
            WallInventory.writeInt(furniData.Id);
            WallInventory.writeInt(furniData.SpriteId);
            if(furniData.Name.contains("a2"))
                WallInventory.writeInt(3);
            else if(furniData.Name.contains("wall"))
                WallInventory.writeInt(2);
            else if(furniData.Name.contains("land"))
                WallInventory.writeInt(4);
            else
                WallInventory.writeInt(1);
            WallInventory.writeInt(0);
            WallInventory.writeUTF(I.ExtraData);
            WallInventory.writeBoolean(furniData.CanRecycle);
            WallInventory.writeBoolean(furniData.CanTrade);
            WallInventory.writeBoolean(furniData.CanStack);
            WallInventory.writeBoolean(furniData.CanSell);
            WallInventory.writeInt(-1);
        }
        WallInventory.Send(Socket);
        } catch (Exception e)
        {
            
        }
        
    }
}
