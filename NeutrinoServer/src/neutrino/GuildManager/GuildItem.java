package neutrino.GuildManager;
import java.sql.*;
import java.util.*;
import neutrino.Environment;
public class GuildItem {
	public int Identificator;
	public String Type;
	public String FirstValue;
	public String SecondValue;
	public static List<GuildItem> Bases;
	public static List<GuildItem> Symbols;
	public static List<GuildItem> ColorForBases;
	public static List<GuildItem> ColorForSymbols;
	public static List<GuildItem> ColorForBG;
	public static void init(Environment Server) throws Exception
	{
		Bases = new ArrayList<GuildItem>();
		Symbols = new ArrayList<GuildItem>();
		ColorForBases = new ArrayList<GuildItem>();
		ColorForSymbols = new ArrayList<GuildItem>();
		ColorForBG = new ArrayList<GuildItem>();
		ResultSet Items = Server.GetDatabase().executeQuery("SELECT * FROM groups_items");
		while(Items.next())
		{
			GuildItem I = new GuildItem();
			I.Identificator = Items.getInt("identificator");
			I.Type = Items.getString("type");
			I.FirstValue = Items.getString("firstvalue");
			I.SecondValue = Items.getString("secondvalue");
			
			if(I.Type.equals("base"))
				Bases.add(I);
			else if(I.Type.equals("symbol"))
				Symbols.add(I);
			else if(I.Type.equals("color"))
				ColorForBases.add(I);
			else if(I.Type.equals("color2"))
				ColorForSymbols.add(I);
			else if(I.Type.equals("color3"))
				ColorForBG.add(I);
		}
		
		Server.WriteLine("Loaded all guild (" + Bases.size() + " bases, " + Symbols.size() + " symbols, " + ColorForBases.size() + " Colors for bases, " + ColorForSymbols.size() + " colors for symbols, " + ColorForBG.size() + " colors for bg) items");
	}
	
	public static String GetColorBGForId1(int gId)
	{
		Iterator reader = ColorForBases.iterator();
		while(reader.hasNext())
		{
			GuildItem B = (GuildItem)reader.next();
			if(B.Identificator == gId)
			{
				return B.FirstValue;
			}
		}
		return null;
	}
	
	public static String GetColorBGForId2(int gId)
	{
		Iterator reader = ColorForSymbols.iterator();
		while(reader.hasNext())
		{
			GuildItem B = (GuildItem)reader.next();
			if(B.Identificator == gId)
			{
				return B.FirstValue;
			}
		}
		return null;
	}
	
	public static String GetColorBGForId3(int gId)
	{
		Iterator reader = ColorForBG.iterator();
		while(reader.hasNext())
		{
			GuildItem B = (GuildItem)reader.next();
			if(B.Identificator == gId)
			{
				return B.FirstValue;
			}
		}
		return null;
	}
}
