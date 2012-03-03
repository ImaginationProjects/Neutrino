/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Requests.Rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.UserItem;
import neutrino.RoomManager.CommandHandler;
import org.jboss.netty.channel.Channel;
/**
 *
 * @author Julián
 */
public class UpdatePapers extends Handler implements Runnable {
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
            int ItemId = Client.in.readInt();
            UserItem Item = UserItem.Items.get(ItemId);
            ItemInformation furniData = ItemInformation.Items.get(Item.ItemId);
            Habbo User = Client.GetSession();
            Room RoomData = Room.Rooms.get(User.CurrentRoomId);
            String ExtraData = Item.ExtraData;
            String type = "floor";
            String type2 = "floor";
            if(furniData.Name.contains("a2"))
            {
                RoomData.Floor = Integer.parseInt(ExtraData);
            }
            else if(furniData.Name.contains("wall"))
            {
                type = "wall";
                type2 = "wallpaper";
                RoomData.Wall = Integer.parseInt(ExtraData);
            }
            else if(furniData.Name.contains("land"))
            {
                type = "landscape";
                type2 = "landscape";
                RoomData.Landscape = Double.parseDouble(ExtraData);
            }
            
            Server.GetDatabase().executeUpdate("UPDATE rooms SET " + type + " = '" + ExtraData + "' WHERE id = " + User.CurrentRoomId);
            UserItem.DeleteItem(ItemId, Server);
            ServerMessage Message = new ServerMessage(ServerEvents.UpdateInventory);
            Message.Send(Client.Socket);
            
            ServerMessage UpdatePapers = new ServerMessage(ServerEvents.Papers);
            UpdatePapers.writeUTF(type2);
            UpdatePapers.writeUTF(ExtraData);
            UpdatePapers.Send(Client.Socket);
            
        } catch (Exception e)
        {
            Server.WriteLine(e);
        }
    }
        }
