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
public class AddItemToMyRoom extends Handler implements Runnable {
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
            String[] Data = Client.in.readUTF().split(" ");
            int ItemId = Integer.parseInt(Data[0]); 
            UserItem Item = UserItem.Items.get(ItemId);
            ItemInformation furniData = ItemInformation.Items.get(Item.ItemId);
            Habbo User = Client.GetSession();
            Room RoomData = Room.Rooms.get(User.CurrentRoomId);
            String ExtraData = Item.ExtraData;
            if(Data[1].startsWith(":"))
            {
                
            } else {
                // Add FloorItem
                int X = Integer.parseInt(Data[1]); 
                int Y = Integer.parseInt(Data[2]); 
                int Rot = Integer.parseInt(Data[3]); 
                UserItem.DeleteItem(ItemId, Server);
                ServerMessage Message = new ServerMessage(ServerEvents.UpdateInventory);
                Message.Send(Client.Socket);
                
                RoomItem I = new RoomItem();
                I.Id = RoomItem.LastId + 1;
                RoomItem.LastId++;
                I.RoomId = User.CurrentRoomId;
                I.FurniId = Item.ItemId;
                I.W = "";
                I.MoodLight = "";
                I.X = X;
                I.Y = Y;
                I.Rot = Rot;
                RoomItem.Items.put(I.Id, I);
                RoomData.FloorItems.add(I);
                Server.GetDatabase().executeUpdate("INSERT INTO rooms_items (id, room_id, furni_id, extradata, x, y, w, rot, moodlight, wired_string, wired_items) VALUES (" + I.Id + ", '" + User.CurrentRoomId + "', '" + Item.ItemId + "', '" + ExtraData + "', '" + X + "', '" + Y + "', '', '" + Rot + "','','','');");
                
                ServerMessage FloorItem = new ServerMessage(ServerEvents.AddFloorItem);
                FloorItem.writeInt(I.Id);
                FloorItem.writeInt(furniData.SpriteId);
                FloorItem.writeInt(X);
                FloorItem.writeInt(Y);
                FloorItem.writeInt(Rot);
                FloorItem.writeUTF("0.0");
                FloorItem.writeInt(0);
                FloorItem.writeInt(0);
                FloorItem.writeUTF(ExtraData);
                FloorItem.writeInt(-1);
                FloorItem.writeBoolean((furniData.Interactor.equals("default")) ? true : false);
                FloorItem.writeInt(RoomData.OwnerId);
                FloorItem.writeUTF(RoomData.OwnerName);
                FloorItem.Send(Client.Socket);
            }
        } catch (Exception e)
        {
            Server.WriteLine(e);
        }
    }
}
