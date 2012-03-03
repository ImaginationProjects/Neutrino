package Requests.Guilds;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.ItemManager.UserItem;
import neutrino.GuildManager.GuildItem;
import java.util.*;
/**
 *
 * @author Julián
 */
public class InitGuildCreate extends Handler implements Runnable {
    private ServerHandler Client;
    private Environment Server;
    private FutureTask T;
    @Override
    public void Load(ServerHandler Client, Environment Server, FutureTask T) throws Exception
    {
        this.Client = Client;
        this.Server = Server;
        this.T = T;
    }
    
    public void run() {
        try {
        	Habbo User = Client.GetSession();
        	ServerMessage MyData = new ServerMessage(ServerEvents.CreateGroup);
        	MyData.writeInt(10);
        	List<Room> myRooms = Room.GetItemsForUserId(User.Id);
        	MyData.writeInt(myRooms.size());
        	Iterator myRoomsReader = myRooms.iterator();
        	while(myRoomsReader.hasNext())
        	{
        		Room R = (Room)myRoomsReader.next();
        		MyData.writeInt(R.Id);
        		MyData.writeUTF(R.Name);
        		MyData.writeBoolean(false); // has group?
        	}
        	MyData.writeInt(5);
        	MyData.writeInt(10);
        	MyData.writeInt(3);
        	MyData.writeInt(4);
        	MyData.writeInt(25);
        	MyData.writeInt(17);
        	MyData.writeInt(5);
        	MyData.writeInt(25);
        	MyData.writeInt(17);
        	MyData.writeInt(3);
        	MyData.writeInt(29);
        	MyData.writeInt(11);
        	MyData.writeInt(4);
        	MyData.writeInt(0);
        	MyData.writeInt(0);
        	MyData.writeInt(0);
        	MyData.Send(Client.Socket);
        	
        	        	
        	// Now Items
        	ServerMessage GuildItems = new ServerMessage(ServerEvents.SetGuildItemsData);
        	GuildItems.writeInt(GuildItem.Bases.size());
        	Iterator bReader = GuildItem.Bases.iterator();
        	int i = 0;
        	while(bReader.hasNext())
        	{
        		GuildItem I = (GuildItem)bReader.next();
        		GuildItems.writeInt(i);
        		GuildItems.writeUTF(I.FirstValue);
        		GuildItems.writeUTF(I.SecondValue);
        		i++;
        	}
        	GuildItems.writeInt(GuildItem.Symbols.size());
        	Iterator sReader = GuildItem.Symbols.iterator();
        	i = 0;
        	while(sReader.hasNext())
        	{
        		GuildItem I = (GuildItem)sReader.next();
        		GuildItems.writeInt(i);
        		GuildItems.writeUTF(I.FirstValue);
        		GuildItems.writeUTF(I.SecondValue);
        		i++;
        	}
        	GuildItems.writeInt(GuildItem.ColorForBases.size());
        	Iterator cbReader = GuildItem.ColorForBases.iterator();
        	i = 0;
        	while(cbReader.hasNext())
        	{
        		GuildItem I = (GuildItem)cbReader.next();
        		GuildItems.writeInt(I.Identificator);
        		GuildItems.writeUTF(I.FirstValue);
        		i++;
        	}
        	GuildItems.writeInt(GuildItem.ColorForSymbols.size());
        	Iterator csReader = GuildItem.ColorForSymbols.iterator();
        	i = 0;
        	while(csReader.hasNext())
        	{
        		GuildItem I = (GuildItem)csReader.next();
        		GuildItems.writeInt(I.Identificator);
        		GuildItems.writeUTF(I.FirstValue);
        		i++;
        	}
        	GuildItems.writeInt(GuildItem.ColorForBG.size());
        	Iterator cbgReader = GuildItem.ColorForBG.iterator();
        	while(cbgReader.hasNext())
        	{
        		GuildItem I = (GuildItem)cbgReader.next();
        		GuildItems.writeInt(I.Identificator);
        		GuildItems.writeUTF(I.FirstValue);
        	}
        	GuildItems.Send(Client.Socket);
        	
        } catch (Exception e)
        {
        	
        }
    }
}
