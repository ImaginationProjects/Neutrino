package core.game.users;

import org.jboss.netty.channel.Channel;
import java.util.*;

import core.Environment;
import core.data.habbo.Habbo;
import core.network.SocketHandler;

public class Session {
	// Habbo session
	public Channel Socket;
	public SocketHandler Client;
	public Habbo myHabbo;
	public int SessionId;
	public boolean IsOnline;
	
	public static Map<Channel, Session> SessionsByChannels = null;
	
	
	// Static actions
	public static void SetSession(Channel Socket, SocketHandler Client, int iSession)
	{
		if(SessionsByChannels == null)
			SessionsByChannels = new HashMap<Channel, Session>();
		Session currentSession = new Session();
		currentSession.Socket = Socket;
		currentSession.Client = Client;
		currentSession.SessionId = iSession;
		currentSession.myHabbo = Habbo.GetUserForIP(currentSession.Socket.getRemoteAddress().toString().split(":")[0].replace("/", ""));
		SessionsByChannels.put(currentSession.Socket, currentSession);
		
		Environment.WriteDebug("Created SESSION #" + currentSession.SessionId);
	}
}
