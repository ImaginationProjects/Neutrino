package core.data.habbo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.SQLQuery;

import core.Environment;

public class Habbo {
	public static Map<Integer, Habbo> UsersbyId;
    public static int LastId;
    public int id;
    public String Username;
    public String Look;
    public String Gender;
    public String Motto;
    public String Authenticator;
    public int Credits;
    public int Pixels;
    public int SnowFlakes;
    public int Home;
    public int RankLevel;
    public int SnowWarScore;
    public int SnowWarWeeklyScore;
    
    public static void init() throws Exception
    {
        UsersbyId = new HashMap<Integer, Habbo>();
        LastId = 0;
        Iterator Users = Environment.GetDatabase().executeQuery("SELECT * FROM users").addEntity("user", Habbo.class).list().iterator();
        while(Users.hasNext())
        {
        	SQLQuery U = (SQLQuery)Users.next();
        	Environment.WriteDebug(U. + "");
        }
        /*while(Users.next())
        {
            Habbo U = new Habbo();
            U.Id = Users.getInt("id");
            if(LastId < U.Id)
            	LastId = U.Id;
            U.Username = Users.getString("username");
            U.Look = Users.getString("look");
            U.Gender = Users.getString("gender");
            U.Motto = Users.getString("motto");
            U.Authenticator = Users.getString("authenticator");
            U.Credits = Users.getInt("credits");
            U.Pixels = Users.getInt("pixels");
            U.SnowFlakes = Users.getInt("snowflakes");
            U.Home = Users.getInt("home");
            String Rank = Users.getString("level");
            U.RankLevel = 1;
            if(Rank.equals("BANNED"))
            	U.RankLevel = 0;
            else if(Rank.equals("GUIDE"))
            	U.RankLevel = 2;
            else if(Rank.equals("XTREME"))
            	U.RankLevel = 3;
            else if(Rank.equals("LINCE"))
            	U.RankLevel = 4;
            else if(Rank.equals("MODERATOR"))
            	U.RankLevel = 5;
            else if(Rank.equals("HEAD MODERATOR"))
            	U.RankLevel = 6;
            else if(Rank.equals("MANAGER"))
            	U.RankLevel = 7;
            else if(Rank.equals("ADMINISTRATOR"))
            	U.RankLevel = 8;
            U.SnowWarScore = Users.getInt("snowwar_points");
            U.SnowWarWeeklyScore = Users.getInt("snowwar_weeklypoints");
            UsersbyId.put(U.Id, U);
        }*/
        Environment.WriteStatus("Loaded " + UsersbyId.size() + " user(s).");
    }
    
    public static Habbo GetUserForIP(String IP)
    {
    	Habbo User = null;
    	Iterator<Map.Entry<Integer, Habbo>> reader = UsersbyId.entrySet().iterator();
        while (reader.hasNext())
        {
            Habbo H = (Habbo)(((Map.Entry<Integer, Habbo>)reader.next()).getValue());
            if(H.Authenticator.equals(IP))
            	return H;
        }        
        return User;
    }
    
    public static Habbo GetUserForName(String Name)
    {
    	Habbo User = null;
    	Iterator<Map.Entry<Integer, Habbo>> reader = UsersbyId.entrySet().iterator();
        while (reader.hasNext())
        {
            Habbo H = (Habbo)(((Map.Entry<Integer, Habbo>)reader.next()).getValue());
            if(H.Username.equals(Name))
            	return H;
        }        
        return User;
    }
}