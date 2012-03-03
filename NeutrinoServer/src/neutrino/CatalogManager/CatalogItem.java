/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.CatalogManager;
import java.util.*;
import neutrino.Environment;
import java.sql.ResultSet;
/**
 *
 * @author Julián
 */
public class CatalogItem {
    public int Id;
    public int PageId;
    public String Name;
    public int CostCredits;
    public int CostPixels;
    public int Cost_AnythingQuestPoint;
    public int FinalPixels_orQuest;
    public int Amount;
    public String FurniId;
    public int SpriteId;
    public List<Integer> ItemIds;
    public String Type;
    public int IsClub;
    public String extraAm;
    public String ExtraInformation;
    public int TimesSelled;
    public Map<Integer, Integer> ExtraAmounts;
    public static Map<Integer, CatalogItem> Items;
    public static void Init(Environment Server) throws Exception
    {
        Items = new HashMap<Integer, CatalogItem>();
        ResultSet Item = Server.GetDatabase().executeQuery("SELECT * FROM catalog_items");
        while(Item.next())
        {
            CatalogItem I = new CatalogItem();
            I.Id = Item.getInt("id");
            I.PageId = Item.getInt("pageid");
            I.Name = Item.getString("name");
            I.CostCredits = Item.getInt("cost_credits");
            I.CostPixels = Item.getInt("cost_pixels");
            I.Cost_AnythingQuestPoint = Item.getInt("cost_quest");
            if(I.Cost_AnythingQuestPoint > 0)
                I.FinalPixels_orQuest = I.Cost_AnythingQuestPoint;
            else
                I.FinalPixels_orQuest = I.CostPixels;
            I.Amount = Item.getInt("amount");
            I.extraAm = Item.getString("extraamounts");
            I.ExtraAmounts = new HashMap<Integer, Integer>();
            if(I.extraAm.contains(";"))
            {
                String[] s = I.extraAm.split(";");
                int i = 0;
                while(i != s.length)
                {
                    String a = s[i].split(",")[0];
                    String b = s[i].split(",")[1];
                    if(a.equals(""))
                        break;
                    if(b.equals(""))
                        break;
                    I.ExtraAmounts.put(Integer.parseInt(a), Integer.parseInt(b));
                    i++;
                }
            }
            I.FurniId = Item.getString("furni_id");
            I.ItemIds = new ArrayList<Integer>();
            if(I.FurniId.contains(";"))
            {
                String[] s = I.FurniId.split(";");
                int i = 0;
                while(i != s.length)
                {
                    String a = s[i];
                    if(a.equals(""))
                        break;
                    I.ItemIds.add(Integer.parseInt(a));
                    i++;
                }
            } else if(!I.FurniId.equals("")) {
                I.ItemIds.add(Item.getInt("furni_id"));
            }
            I.IsClub = Item.getInt("is_club");
            I.ExtraInformation = Item.getString("extrainformation");
            Items.put(I.Id, I);
        }
        
        Server.WriteLine("Loaded " + Items.size() + " catalog item(s).");
    }
    
    public static Map<Integer, CatalogItem> GetItemsForPageId(int Id)
    {
        Map<Integer, CatalogItem> SubPages = new HashMap<Integer, CatalogItem>();
        Iterator reader = Items.entrySet().iterator();
        while (reader.hasNext())
        {
            CatalogItem P = (CatalogItem)(((Map.Entry)reader.next()).getValue());
            if(P.PageId == Id)
            {
                SubPages.put(P.Id, P);
            }
        }
        
        return SubPages;
    }
    
    public static Map<Integer, CatalogItem> Load25MostSelledIds() throws Exception
    {
        Map<Integer, CatalogItem> SubPages = new HashMap<Integer, CatalogItem>();
        
        for(int x = 0; x < 25; x++)
        {
        	int w = 0;
            Iterator reader = Items.entrySet().iterator();
            while (reader.hasNext())
            {
            	try {
                CatalogItem P = (CatalogItem)(((Map.Entry)reader.next()).getValue());
                boolean IsStaff = false;
                if(CatalogPage.Pages.get(P.PageId).CategoryId != -1 && (CatalogPage.Pages.get(CatalogPage.Pages.get(P.PageId).CategoryId).MinRank != 1))
                	IsStaff = true;
                else if (CatalogPage.Pages.get(P.PageId).MinRank != 1)
                	IsStaff = true;
                if(w < P.TimesSelled && P.TimesSelled != 0 && !(SubPages.containsKey(P.Id)) && !IsStaff)
                {
                    w = P.Id;
                }
            	} catch (Exception e) {
            		continue;
            	}
            }
            
            if(w != 0)
            	SubPages.put(w, Items.get(w));
        	w = 0;
        }
        
        return SubPages;
    }
    
    public static Map<Integer, CatalogItem> LoadNew25Ids() throws Exception
    {
        Map<Integer, CatalogItem> SubPages = new HashMap<Integer, CatalogItem>();
        
        for(int x = 0; x < 25; x++)
        {
        	int w = 0;
            Iterator reader = Items.entrySet().iterator();
            while (reader.hasNext())
            {
            	try {
                CatalogItem P = (CatalogItem)(((Map.Entry)reader.next()).getValue());
                if(w < P.Id && !(SubPages.containsKey(P.Id) && (CatalogPage.Pages.get(P.PageId).MinRank == 1)))
                {
                    w = P.Id;
                }
            	} catch (Exception e) {
            		continue;
            	}
            }
            SubPages.put(w, Items.get(w));
            w = 0;
        }
        
        return SubPages;
    }
}
