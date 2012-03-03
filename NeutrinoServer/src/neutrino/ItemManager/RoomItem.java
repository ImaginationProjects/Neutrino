/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.ItemManager;
import java.util.*;
import java.sql.*;
import neutrino.Environment;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.UserManager;
/**
 *
 * @author Julián
 */
public class RoomItem {
    public int Id;
    public int RoomId;
    public int FurniId;
    public String ExtraData;
    public int X;
    public int Y;
    public String Z;
    public String W;
    public int Rot;
    public String MoodLight;
    public boolean IsUpdated;
    public static Map<Integer, RoomItem> Items;
    public static int LastId;
    
    public ItemInformation GetBaseItem()
    {
        return ItemInformation.Items.get(FurniId);
    }
    
    public static void Init(Environment Server) throws Exception
    {
        LastId = 0;
        Items = new HashMap<Integer, RoomItem>();
        ResultSet Item = Server.GetDatabase().executeQuery("SELECT * FROM rooms_items");
        while(Item.next())
        {
            RoomItem I = new RoomItem();
            I.Id = Item.getInt("id");
            if(I.Id > LastId)
                LastId = I.Id;
            I.RoomId = Item.getInt("room_id");
            I.FurniId = Item.getInt("furni_id");
            I.ExtraData = Item.getString("extradata");
            I.MoodLight = Item.getString("moodlight");
            I.X = Item.getInt("x");
            I.Y = Item.getInt("y");
            I.Rot = Item.getInt("rot");
            I.W = Item.getString("w");
            I.IsUpdated = false;
            Items.put(I.Id, I);
        }
        
        Server.WriteLine("Loaded " + Items.size() + " room item(s).");
    }
    
    public void UpdateState() throws Exception
    {
    	if(this.GetBaseItem().Type.toLowerCase().equals("s"))
    	{
    		ServerMessage Update = new ServerMessage(ServerEvents.UpdateFloorExtraData);
    		Update.writeUTF(this.Id + "");
    		Update.writeInt(0);
    		Update.writeUTF(this.ExtraData);
    		UserManager.SendMessageToUsersOnRoomId(this.RoomId, Update);
    		this.IsUpdated = true;
    	}
    }
    
    public static List<RoomItem> GetFloorItemsForRoom(int rId)
    {
        List<RoomItem> Item = new ArrayList<RoomItem>();
        
        Iterator reader = Items.entrySet().iterator();
        while(reader.hasNext())
        {
            RoomItem U = (RoomItem)(((Map.Entry)reader.next()).getValue());
            if(U.RoomId == rId && ((ItemInformation)(ItemInformation.Items.get(U.FurniId))).Type.equals("s"))
                Item.add(U);
        }
        
        return Item;
    }
    
    public static List<RoomItem> GetWallItemsForRoom(int rId)
    {
        List<RoomItem> Item = new ArrayList<RoomItem>();
        
        Iterator reader = Items.entrySet().iterator();
        while(reader.hasNext())
        {
            RoomItem U = (RoomItem)(((Map.Entry)reader.next()).getValue());
            if(U.RoomId == rId && !((ItemInformation)(ItemInformation.Items.get(U.FurniId))).Type.equals("s"))
                Item.add(U);
        }
        
        return Item;
    }
}
