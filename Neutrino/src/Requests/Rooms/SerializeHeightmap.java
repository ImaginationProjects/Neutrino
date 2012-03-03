/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Requests.Rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;
/**
 *
 * @author cb2z8eb
 */
public class SerializeHeightmap extends Handler implements Runnable {
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
        Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        ServerMessage HeightMap = new ServerMessage(ServerEvents.HeightMap1);
        HeightMap.writeUTF(R.GetModel().SerializeMap);
        HeightMap.Send(Socket);
        
        ServerMessage RelativeMap = new ServerMessage(ServerEvents.HeightMap2);
        String Map = R.GetModel().SerializeRelativeMap;
        RelativeMap.writeUTF(Map);
        RelativeMap.Send(Socket);
        } catch (Exception e)
        {
            
        }
        
    }
}
