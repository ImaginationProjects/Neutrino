package neutrino.RoomManager;
import java.util.*;
import java.util.concurrent.TimeUnit;

import neutrino.Environment;
import neutrino.RoomManager.Room;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.System.ServerMessage;
public class RoomManager implements Runnable {
	// Neutrino's Control System: Luz
	public static Map<Integer, RoomManager> Managers;
	public int RoomId;
	public Room RoomData;
	public int SecondsWithoutPersons;
	public static void Init()
	{
		Managers = new HashMap<Integer, RoomManager>();
	}
	
	public static void AddRoomToProcess(int RoomId) throws Exception
	{
		if(Managers.containsKey(RoomId))
			return;
		Room R = Room.Rooms.get(RoomId);
		RoomManager Manager = new RoomManager();
		Manager.RoomId = RoomId;
		Manager.RoomData = R;
		Manager.SecondsWithoutPersons = 0;
		Managers.put(Manager.RoomId, Manager);
		ThreadPoolManager._myTimerPoolingThreads.scheduleAtFixedRate(Manager, 0L, 1L, TimeUnit.SECONDS);
	}
	
	public void DestroyRoom()
	{
		Managers.remove(this.RoomId);
	}
	
	public void run()
	{
		// all things to do every second
		if(this.RoomData.CurrentUsers == 0)
		{
			// basic: to unload rooms
			SecondsWithoutPersons++;
			if(SecondsWithoutPersons == 5)
			{
				DestroyRoom();
				return;
			}
		}
		
	}
}
