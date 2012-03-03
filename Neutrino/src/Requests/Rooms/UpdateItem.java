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
import neutrino.ItemManager.RoomItem;
import neutrino.ItemManager.UserItem;
/**
 *
 * @author Julián
 */
public class UpdateItem extends Handler implements Runnable {
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
            Habbo User = Client.GetSession();
            Room RoomData = Room.Rooms.get(User.CurrentRoomId);
            int ItemId = Client.in.readInt();
            int X = Client.in.readInt();
            int Y = Client.in.readInt();
            int Rot = Client.in.readInt();
            RoomItem Item = RoomItem.Items.get(ItemId);
            ItemInformation furniData = ItemInformation.Items.get(Item.FurniId);
            Item.X = X;
            Item.Y = Y;
            Item.Rot = Rot;
            RoomData.FloorItems.remove(Item);
            RoomData.FloorItems.add(Item);
            
            ServerMessage Update = new ServerMessage(ServerEvents.UpdateFloorItem);
            Update.writeInt(Item.Id);
            Update.writeInt(furniData.SpriteId);
            Update.writeInt(X);
            Update.writeInt(Y);
            Update.writeInt(Rot);
            Update.writeUTF("0.0");
            Update.writeInt(0);
            Update.writeInt(0);
            Update.writeUTF(Item.ExtraData);
            Update.writeInt(-1);
            Update.writeBoolean((furniData.Interactor.equals("default")) ? true : false);
            Update.writeInt(RoomData.OwnerId);
            Update.Send(Client.Socket);
            
        } catch (Exception e)
        {
            
        }
    }
}
