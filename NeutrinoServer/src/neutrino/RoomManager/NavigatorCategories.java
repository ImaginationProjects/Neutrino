package neutrino.RoomManager;
import java.util.*;
import java.sql.*;
import neutrino.Environment;
public class NavigatorCategories {
	public int Id;
	public int MinRank;
	public String Caption;
	public static List<NavigatorCategories> Cats;
	public static void init(Environment Server) throws Exception
	{
		Cats = new ArrayList<NavigatorCategories>();
		ResultSet Cat = Server.GetDatabase().executeQuery("SELECT * FROM navigator_categories");
		while(Cat.next())
		{
			NavigatorCategories C = new NavigatorCategories();
			C.Id = Cat.getInt("id");
			C.MinRank = Cat.getInt("min_rank");
			C.Caption = Cat.getString("caption");
			Cats.add(C);
		}
		
		Server.WriteLine("Loaded " + Cats.size() + " navigator categorie(s).");
	}

}
