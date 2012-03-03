/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.ItemManager;
import java.util.*;
import java.sql.*;
import neutrino.Environment;
/**
 *
 * @author Julián
 */
public class ItemInformation {
    public int Id;
    public int SpriteId;
    public String Type;
    public String Name;
    public String Interactor;
    public int Width;
    public double Height;
    public int Lenght;
    public boolean CanStack;
    public boolean CanWalk;
    public boolean CanGift;
    public boolean CanSit;
    public boolean CanTrade;
    public boolean CanRecycle;
    public boolean CanSell;
    public static Map<Integer, ItemInformation> Items;
    public static void Init(Environment Server) throws Exception
    {
        Items = new HashMap<Integer, ItemInformation>();
        ResultSet Item = Server.GetDatabase().executeQuery("SELECT * FROM items_information");
        while(Item.next())
        {
            ItemInformation I = new ItemInformation();
            I.Id = Item.getInt("id");
            I.SpriteId = Item.getInt("sprite_id");
            I.Type = Item.getString("type");
            I.Name = Item.getString("name");
            I.Interactor = Item.getString("furni_type");
            I.Width = Item.getInt("width");
            I.Height = Item.getDouble("height");
            I.Lenght = Item.getInt("length");
            I.CanSell = Item.getBoolean("can_sell");
            I.CanWalk = Item.getBoolean("can_walk");
            I.CanGift = Item.getBoolean("can_gift");
            I.CanSit = Item.getBoolean("can_sit");
            if(I.CanSit && !I.CanWalk)
            	I.CanWalk = true;
            I.CanStack = Item.getBoolean("can_stack");
            I.CanRecycle = Item.getBoolean("can_recycle");
            I.CanTrade = Item.getBoolean("can_trade");
            Items.put(I.Id, I);
        }
        
        Server.WriteLine("Loaded " + Items.size() + " item(s) information.");
    }
}
