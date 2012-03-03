/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.ItemManager;
import neutrino.Environment;
import java.sql.*;
import java.util.*;
/**
 *
 * @author Julián
 */
public class UserItem {
    public int Id;
    public int UserId;
    public int ItemId;
    public String ExtraData;
    public static int LastId;
    public static Map<Integer, UserItem> Items;
    public static void init(Environment Server) throws Exception
    {
        LastId = 0;
        Items = new HashMap<Integer, UserItem>();
        ResultSet Item = Server.GetDatabase().executeQuery("SELECT * FROM users_items");
        while(Item.next())
        {
            UserItem I = new UserItem();
            I.Id = Item.getInt("id");
            if(I.Id > LastId)
                LastId = I.Id;
            I.UserId = Item.getInt("userid");
            I.ItemId = Item.getInt("itemid");
            I.ExtraData = Item.getString("extradata");
            Items.put(I.Id, I);
        }
        
        Server.WriteLine("Loaded " + Items.size() + " item(s).");
    }
    
    public static List<UserItem> GetItemsbyUserId(int UserOwn)
    {
        List<UserItem> Item = new ArrayList<UserItem>();
        
        Iterator reader = Items.entrySet().iterator();
        while(reader.hasNext())
        {
            UserItem U = (UserItem)(((Map.Entry)reader.next()).getValue());
            if(U.UserId == UserOwn)
                Item.add(U);
        }
        
        return Item;
    }
    
    public static void DeleteItem(int ItemId, Environment Server) throws Exception
    {
        Server.GetDatabase().executeUpdate("DELETE FROM users_items WHERE id = '" + ItemId + "'");
        UserItem Us = UserItem.Items.get(ItemId);
        Items.remove(ItemId);
   }
}
