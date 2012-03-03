package neutrino.AdministrationManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.Iterator;

import neutrino.Environment;
import neutrino.CatalogManager.CatalogPage;
import neutrino.ItemManager.RoomItem;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomModel;
import neutrino.SQL.Database;
import neutrino.UserManager.Habbo;

public class Chatlog {
	public int Id;
    public int UserId;
    public int RoomId;
    public int Timestamp;
    public int Hour;
    public int Minute;
    public String Message;
    
    public static List<Chatlog> Chatlogs;
    public static int LastId;

    public static void Init(Environment Server) throws Exception
    {
        Chatlogs = new ArrayList<Chatlog>();
        LastId = 0;
        ResultSet Room = Server.GetDatabase().executeQuery("SELECT * FROM chatlogs");
        while(Room.next())
        {
            Chatlog C = new Chatlog();
            C.Id = Room.getInt("id");
            if(C.Id > LastId)
            	LastId = C.Id;
            C.UserId = Room.getInt("userid");
            C.RoomId = Room.getInt("roomid");
            C.Timestamp = Room.getInt("timestamp");
            C.Hour = Room.getInt("hour");
            C.Minute = Room.getInt("minute");
            C.Message = Room.getString("message");
            Chatlogs.add(C);
        }
        
        Server.WriteLine("Loaded " + Chatlogs.size() + " chatlogs(s).");
    }
    
    public static void AddChatlog(Habbo User, String Message) throws Exception
    {
    	Chatlog C = new Chatlog();
        C.Id = LastId + 1;
        LastId++;
        C.UserId = User.Id;
        C.RoomId = User.CurrentRoomId;
        C.Timestamp = Environment.getIntUnixTimestamp();
        Calendar calendario = new GregorianCalendar();
        C.Hour = calendario.get(Calendar.HOUR_OF_DAY);
        C.Minute = calendario.get(Calendar.MINUTE);
        C.Message = Message;
        Database.executeUpdate("INSERT INTO chatlogs (id, userid, roomid, timestamp, hour, minute, message) VALUES (NULL,'" + C.UserId + "','" + C.RoomId + "','" + C.Timestamp + "','" + C.Hour + "','" + C.Minute + "','" + C.Message + "');");
        Chatlogs.add(C);
    }
    
    public static List<Integer> GetRoomsOfChatlogsForUserId(int cUserId)
    {
    	List<Integer> Chats = new ArrayList<Integer>();
    	Iterator reader = Chatlogs.iterator();
    	while(reader.hasNext())
    	{
    		Chatlog C = (Chatlog)reader.next();
    		if(C.UserId == cUserId)
    		{
    			if(!Chats.contains(C.RoomId))
    				Chats.add(C.RoomId);
    		}
    	}
    	return Chats;
    }
    
    public static List<Chatlog> GetChatlogsForRoomId(int cRoomId)
    {
    	List<Chatlog> Chats = new ArrayList<Chatlog>();
    	Iterator reader = Chatlogs.iterator();
    	while(reader.hasNext())
    	{
    		Chatlog C = (Chatlog)reader.next();
    		if(C.RoomId == cRoomId)
    		{
    			Chats.add(C);
    		}
    	}
    	Collections.sort(Chats, new Comparator<Chatlog>() {
    		public int compare(Chatlog e1, Chatlog e2)
    		{
    			return Integer.valueOf(e2.Id).compareTo(e1.Id);
    		}
    	});
    	return Chats;
    }
}
