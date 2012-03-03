/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Requests.Users;
import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.*;
import org.jboss.netty.channel.Channel;
/**
 *
 * @author Julián
 */
public class ChangeLooks extends Handler implements Runnable  {
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
        
        String Gender = Client.in.readUTF();
        String Look = Client.in.readUTF();
        Server.WriteLine("Gender:  " + Gender + "; Look: " + Look);
        Server.GetDatabase().executeUpdate("UPDATE users SET look = '" + Look + "' WHERE id = " + CurrentUser.Id);
        Server.GetDatabase().executeUpdate("UPDATE users SET gender = '" + Gender + "' WHERE id = " + CurrentUser.Id);
        CurrentUser.Look = Look;
        CurrentUser.Gender = Gender;        
        
        ServerMessage UpdateInfo = new ServerMessage(ServerEvents.UpdateInfo);
        UpdateInfo.writeInt(-1);
        UpdateInfo.writeUTF(Look);
        UpdateInfo.writeUTF(Gender.toLowerCase());
        UpdateInfo.writeUTF(CurrentUser.Motto);
        UpdateInfo.writeInt(525); // achv points
        UpdateInfo.Send(Socket);
        
        ServerMessage UpdateGInfo = new ServerMessage(ServerEvents.UpdateInfo);
        UpdateGInfo.writeInt(CurrentUser.SessionId);
        UpdateGInfo.writeUTF(Look);
        UpdateGInfo.writeUTF(Gender.toLowerCase());
        UpdateGInfo.writeUTF(CurrentUser.Motto);
        UpdateGInfo.writeInt(525); // achv points
        UpdateGInfo.Send(Socket); // send to all room (soon)
        } catch (Exception e)
        {
            
        }        
        
    }
}
