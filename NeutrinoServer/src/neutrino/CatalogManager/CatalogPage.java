/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.CatalogManager;
import neutrino.Environment;
import neutrino.UserManager.Habbo;

import java.util.*;
import java.sql.ResultSet;
/**
 *
 * @author Julián
 */
public class CatalogPage {
    public static Map<Integer, CatalogPage> Pages;
    public int Id;
    public int CategoryId;
    public String Name;
    public boolean PageOpen;
    public boolean ClubPage;
    public boolean PageEnabled;
    public int MinRank;
    public int IconImage;
    public int IconColor;
    public String Type;
    public String HeadLine;
    public String Teaser;
    public String TextSpecial;
    public String Text;
    public String TextDetails;
    public String TextTeaser;
    public String AnotherText;
    public int OrderNum;
    public static void Init(Environment Server) throws Exception
    {
        Pages = new HashMap<Integer, CatalogPage>();
        ResultSet Page = Server.GetDatabase().executeQuery("SELECT * FROM catalog_pages ORDER BY order_num ASC");
        while(Page.next())
        {
            CatalogPage C = new CatalogPage();
            C.Id = Page.getInt("id");
            C.CategoryId = Page.getInt("categoryid");
            C.Name = Page.getString("name");
            C.PageOpen = Page.getBoolean("page_open");
            C.ClubPage = Page.getBoolean("club_page");
            C.PageEnabled = Page.getBoolean("page_enabled");
            C.MinRank = Page.getInt("min_rank");
            C.IconColor = Page.getInt("icon_color");
            C.IconImage = Page.getInt("icon_image");
            C.Type = Page.getString("page_extra");
            C.HeadLine = Page.getString("page_headline");
            C.Teaser = Page.getString("page_teaser");
            C.Text = Page.getString("page_text");
            C.TextSpecial = Page.getString("page_special");
            C.TextDetails = Page.getString("page_text_details");
            C.TextTeaser = Page.getString("page_text_teaser");
            C.AnotherText = Page.getString("page_othertext");
            C.OrderNum = Page.getInt("order_num");
            Pages.put(C.Id, C);
        }
        Collection <CatalogPage> Order = Pages.values();
        Pages = sortByComparator(Pages);
        Server.WriteLine("Loaded " + Pages.size() + " catalog page(s).");
    }
    
    public Integer getOrderNum()
    {
        return OrderNum;
    }
    
    public static Map sortByComparator(Map unsortMap) {

        List list = new LinkedList(unsortMap.entrySet());

        //sort list based on comparator
        Collections.sort(list, new Comparator() {
             public int compare(Object o1, Object o2) {
                 CatalogPage e1 = (CatalogPage) ((Map.Entry) (o1)).getValue();
                 CatalogPage e2 = (CatalogPage) ((Map.Entry) (o2)).getValue();
	           return e1.getOrderNum().compareTo(e2.getOrderNum());
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
    
    public static Map<Integer, CatalogPage> GetMapForId(int Id, Habbo User)
    {
        Map<Integer, CatalogPage> SubPages = new HashMap<Integer, CatalogPage>();
        Iterator reader = Pages.entrySet().iterator();
        while (reader.hasNext())
        {
            CatalogPage P = (CatalogPage)(((Map.Entry)reader.next()).getValue());
            if(P.CategoryId == Id && User.RankLevel >= P.MinRank)
            {
                SubPages.put(P.Id, P);
            }
        }
        
        return SubPages;
    }
}
