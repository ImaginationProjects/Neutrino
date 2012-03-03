package neutrino.SnowWarManager;

import java.util.*;
import java.sql.*;
import neutrino.Environment;
import neutrino.CatalogManager.CatalogPage;

public class SnowWarItem {
	public int InternalId;
	public String ItemName;
	public int WarId;
	public int Initial;
	public int X;
	public int Y;
	public int Rot;
	public int Height;
	public int SpriteId;
	public boolean Walkable;
	public int Information;
	public String OneParam;
	public boolean HaveMoreThanOneParam;
	public Map<Integer, UnsortMap> Params;
	public static List <SnowWarItem> WarItems;
	
	public static void Init(Environment Server) throws Exception
	{
		WarItems = new ArrayList<SnowWarItem>();
		
		int o = 1;
		ResultSet Item = Server.GetDatabase().executeQuery("SELECT * FROM snowwar_items");
		while(Item.next())
		{
			SnowWarItem W = new SnowWarItem();
			W.InternalId = o;
			o++;
			W.ItemName = Item.getString("item_name");
			W.WarId = Item.getInt("war_id");
			W.Initial = Item.getInt("initial");
			W.X = Item.getInt("x");
			W.Y = Item.getInt("y");
			W.Rot = Item.getInt("rot");
			W.Height = Item.getInt("height");
			W.SpriteId = Item.getInt("sprite_id");
			W.Walkable = (Item.getInt("walkable") == 1);
			W.Information = Item.getInt("information");
			W.OneParam = Item.getString("oneparam");
			W.Params = new HashMap<Integer, UnsortMap>();
			String nullit = Item.getString("lotofparams");
			int w = 0;
			if(nullit.contains(";"))
			{
				W.HaveMoreThanOneParam = true;
				String[] first = nullit.split(";");
				for(int i = 0; i < first.length; i++)
				{
					w++;
					String separed = first[i];
					if(!separed.equals(""))
					{
						String[] separe = separed.split("=");
						UnsortMap M = new UnsortMap();
						M.Id = w;
						M.Set = separe[0];
						M.Value = separe[1];
						W.Params.put(M.Id, M);
					}
				}
				
			} else {
				W.HaveMoreThanOneParam = false;
			}
			WarItems.add(W);
		}
		
		
		
		Server.WriteLine("Loaded " + WarItems.size() + " snowwar item(s).");
	}
	
	public static Map sortByComparator(Map unsortMap) {

        List list = new LinkedList(unsortMap.entrySet());

        //sort list based on comparator
        Collections.sort(list, new Comparator() {
             public int compare(Object o1, Object o2) {
                 UnsortMap e1 = (UnsortMap) ((Map.Entry) (o1)).getValue();
                 UnsortMap e2 = (UnsortMap) ((Map.Entry) (o2)).getValue();
	           return ((Integer)e2.Id).compareTo(e1.Id);
             }
	});

        //put sorted list into map again
	Map sortedMap = new LinkedHashMap();
	for (Iterator it = list.iterator(); it.hasNext();) {
	     Map.Entry entry = (Map.Entry)it.next();
	     sortedMap.put(entry.getKey(), entry.getValue());
	}
	return sortedMap;
   }
	
	public static List<SnowWarItem> GetItemsForArena(int ArenaId)
	{
		List<SnowWarItem> Items = new ArrayList<SnowWarItem>();
		Iterator reader = WarItems.iterator();
		while(reader.hasNext())
		{
			SnowWarItem W = (SnowWarItem)reader.next();
			if(W.WarId == ArenaId)
				Items.add(W);
		}
		
		return Items;
	}
}

class UnsortMap
{
	public int Id;
	public String Set;
	public String Value;
}


