package neutrino.SQL;

import neutrino.Environment;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.SQL.Database;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.sql.*;

public class DatabaseManager implements Runnable {
	// Neutrino's Control System: Luz
	public static List<String> SQLs;
	public static Environment Server;
	public static void init(Environment cServer)
	{
		Server = cServer;
		SQLs = new ArrayList<String>();
		ThreadPoolManager._myTimerPoolingThreads.scheduleAtFixedRate((new DatabaseManager()), 0L, 5L, TimeUnit.MINUTES);
	}
	
	public void run()
	{
		try {
		if(SQLs.size() > 0)
		{
			Iterator reader = SQLs.iterator();
			while(reader.hasNext())
			{
				String SQL = (String)reader.next();
				Database.executeUpdates(SQL);
			}
			//Server.WriteLine(SQLs.size() + " sql(s) executed (every 5 minutes!)");
			SQLs.clear();
		}
		
		if((Environment.UsersConnected / 100) >= 1 && Environment.UsersConnected >= 100)
		{
			int toUp = Environment.UsersConnected / 100;
			Database.ActualDBManager.getConfig().setPartitionCount(3 + toUp);
		}
		
		} catch (Exception e)
		{
			Server.WriteLine(e);
		}
	}
}
