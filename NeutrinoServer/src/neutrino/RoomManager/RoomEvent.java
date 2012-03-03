package neutrino.RoomManager;

import java.util.*;

public class RoomEvent {
	public int Id;
	public int OwnerId;
	public int RoomId;
	public int Category;
	public String Title;
	public String Description;
	public String Created;
	public List<String> Tags;
	
	public static int LastId;
	public static Map<Integer, RoomEvent> Events;
	
	public static void Init()
	{
		LastId = 0;
		Events = new HashMap<Integer, RoomEvent>();
	}
	
	public static void Add(int aCategory, String aTitle, String aDescription, int Own, int rId, List<String> aTags)
	{
		RoomEvent E = new RoomEvent();
		LastId++;
		E.Id = LastId;
		E.OwnerId = Own;
		E.RoomId = rId;
		E.Category = aCategory;
		E.Title = aTitle;
		E.Description = aDescription;
		Calendar calendario = new GregorianCalendar();
		String Minute = "";
		int Minuter = Calendar.MINUTE;
		if(Minuter < 10)
			Minute += "0" + Minuter;
		else
			Minute += Minuter;
        E.Created = calendario.get(Calendar.HOUR_OF_DAY) + ":" + Minute;
        E.Tags = aTags;
        Events.put(E.Id, E);
	}
	
	public Room GetRoom()
	{
		return Room.Rooms.get(this.RoomId);
	}
	
	public static List<RoomEvent> GetRoomsForCategory(int CategoryId)
	{
		List<RoomEvent> allRooms = new ArrayList<RoomEvent>();
		Iterator reader = Events.entrySet().iterator();
		while(reader.hasNext())
		{
			RoomEvent E = (RoomEvent)(((Map.Entry)reader.next()).getValue());
			if(CategoryId == -1)
				allRooms.add(E);
			else if(E.Category == CategoryId)
				allRooms.add(E);
		}
		return allRooms;
	}
	
	public static RoomEvent GetEventForRoomId(int aRoomId)
	{
		Iterator reader = Events.entrySet().iterator();
		while(reader.hasNext())
		{
			RoomEvent E = (RoomEvent)(((Map.Entry)reader.next()).getValue());
			if(E.RoomId == aRoomId)
				return E;
		}
		return null;
	}
	
}
