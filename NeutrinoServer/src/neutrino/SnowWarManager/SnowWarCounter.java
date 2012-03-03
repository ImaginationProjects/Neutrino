package neutrino.SnowWarManager;

import java.util.concurrent.ScheduledFuture;

public class SnowWarCounter implements Runnable {
	private SnowWar War;
	private ScheduledFuture future;
	public void GetData(SnowWar cWar)
	{
		War = cWar;
	}
	
	public void Set(ScheduledFuture Set) 
	{
		future = Set;
	}
	
	public void run()
	{
		try {
			War.RestTime = (War.RestTime - 1);
			if(War.RestTime == -2)
			{
				War.GoToLobby(War);
				future.cancel(true);
				return;
			}
		} catch (Exception e)
		{
			System.out.println(e.toString());
			future.cancel(true);
			return;
		}
	}
}
