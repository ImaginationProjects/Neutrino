package neutrino.PetManager;

import java.util.*;
import java.sql.*;
import neutrino.Environment;

public class PetRace {
	public int RaceId;
	public int Color1;
	public int Color2;
	public boolean Has1Color;
	public boolean Has2Color;
	
	public static List<PetRace> Races;
	
	public static void Init(Environment Server) throws Exception
	{
		Races = new ArrayList<PetRace>();
		ResultSet Race = Server.GetDatabase().executeQuery("SELECT * FROM pets_racesoncatalogue");
		while(Race.next())
		{
			PetRace R = new PetRace();
			R.RaceId = Race.getInt("raceid");
			R.Color1 = Race.getInt("color1");
			R.Color2 = Race.getInt("color2");
			R.Has1Color = (Race.getInt("has1color") == 1);
			R.Has2Color = (Race.getInt("has2color") == 1);
			Races.add(R);
		}
		
		Server.WriteLine("Loaded "+ Races.size() + " pet race(s) to catalog.");
	}
	
	public static List<PetRace> GetRacesForRaceId(int sRaceId)
	{
		List<PetRace> sRaces = new ArrayList<PetRace>();
		Iterator reader = Races.iterator();
		while(reader.hasNext())
		{
			PetRace R = (PetRace)reader.next();
			if(R.RaceId == sRaceId)
				sRaces.add(R);
		}
		
		return sRaces;
	}
	
	public static boolean RaceGotRaces(int sRaceId)
	{
		if(GetRacesForRaceId(sRaceId).size() > 0)
			return true;
		else
			return false;
	}
}
