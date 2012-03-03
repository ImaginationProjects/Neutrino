package core.messages;

import core.game.users.Session;
import core.network.SocketHandler;

public abstract class MessageEvent {
	public abstract void Load(SocketHandler Main, Session Client) throws Exception;
}
