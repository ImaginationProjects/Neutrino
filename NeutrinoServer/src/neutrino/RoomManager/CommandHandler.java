/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.RoomManager;
import java.util.*;

import neutrino.Environment;
import neutrino.CatalogManager.CatalogItem;
import neutrino.CatalogManager.CatalogPage;
import neutrino.ItemManager.ItemInformation;
import neutrino.Network.ServerHandler;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.SQL.Database;
import neutrino.SnowWarManager.SnowWar;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.Room;
import neutrino.PacketsInformation.ServerEvents;
import org.jboss.netty.channel.Channel;
/**
 *
 * @author cb2z8eb
 */
public class CommandHandler {
    private String CurrentCommand;
    private String[] Params;
    
    public boolean CanSayCommand(String Message, Channel Socket, Habbo User, Environment Server) throws Exception
    {
        CurrentCommand = Message.replace(":", "");
        Params = Message.replace(":", "").split(" ");
        
        if (Params[0].equals("send"))
        {
            // SEND A PACKET, LOL!
            /*ServerMessage mess = null;
            for(int i = 1; i != Params.length; i++)
            {
                String[] Data = Params[i].split("=>");
                String Type = Data[0];
                String Content = Data[1];
                if(Type == "id")
                    mess = new ServerMessage(Integer.parseInt(Content));
                else if(Type == "string")
                    mess.writeUTF(Content);
                else if(Type == "int")
                    mess.writeInt(Integer.parseInt(Content));
                else if(Type == "bool")
                    mess.writeBoolean((Content == "1"));
                
            }
            System.out.println("lol send packet");
            if(mess != null)
                mess.Send(Socket);*/
        } else if(Params[0].equals("adminabout"))
        {
        	if(User.RankLevel != 8)
        		return false;
            ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
            String AlertContent = "";
            
            AlertContent += "         Neutrino // Rendimiento\n\n" +
                    "#System: RELEASE63-201201190922-6332255033\n" +
                    "#ThreadPool: " + ThreadPoolManager.ReadStatics() +"\n" +
                    "#Database: " + Database.Readtatics() + "\n" +
                    "#SnowStorm: " + SnowWar.Wars.size() + " War(s) active.";
            
            Alert.writeUTF(AlertContent);
            Alert.writeUTF("");
            Alert.Send(Socket);
            return true;
        }
            else if(Params[0].equals("update_catalog"))
            {
            	if(User.RankLevel != 8)
            		return false;
                CatalogPage.Init(Server);
                CatalogItem.Init(Server);
                return true;
            }
            else if(Params[0].equals("update_furniture"))
            {
            	if(User.RankLevel != 8)
            		return false;
                ItemInformation.Init(Server);
                return true;
            } else if(Params[0].equals("massalert"))
        {
        	if(User.RankLevel < 6)
        		return false;
            ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
            String AlertContent = "Alerta General: \n";
            
            AlertContent += CurrentCommand.replace(Params[0] + " ", "");
            
            Alert.writeUTF(AlertContent);
            Alert.writeUTF("");
            UserManager.SendMessageToAllUsers(Alert);
            return true;
        } else if(Params[0].equals("teleport"))
        {
        	if(User.RankLevel < 5)
        		return false;
            String Type = Params[1];
            
            User.IsTeleportOn = (Type.equals("on"));
            return true;
        } else if(Params[0].equals("move"))
        {
        	if(User.RankLevel < 5)
        		return false;
            String UserName = Params[1];
            int X = Integer.parseInt(Params[2]);
            int Y = Integer.parseInt(Params[3]);
            Habbo cUser = Habbo.GetUserForName(UserName);
            if(cUser.IsOnline && cUser.IsOnRoom)
            {
            cUser.X = X;
            cUser.Y = Y;
            cUser.Z = ((Double)(Room.Rooms.get(cUser.CurrentRoomId)).SecondGetHForXY(X, Y)).toString().replace(',', '.');
            if (!cUser.Z.contains("."))
            	cUser.Z += ".0";
            cUser.UpdateState("", cUser.CurrentSocket, Server);
            }
            return true;
        } else if(Params[0].equals("roomalert"))
        {
        	if(User.RankLevel < 6)
        		return false;
        	int RoomId = User.CurrentRoomId;
            Room RoomData = Room.Rooms.get(RoomId);
            String cMessage = CurrentCommand.replace(Params[0] + " ", "");
            
            Iterator reader = RoomData.UserList.iterator();
            while(reader.hasNext())
            {
            	Habbo cUser = (Habbo)reader.next();
            	cUser.SendAlert(cMessage);
            }
            return true;
        } else if(Params[0].equals("roomalertto"))
        {
        	if(User.RankLevel < 6)
        		return false;
        	int RoomId = Integer.parseInt(Params[1]);
            Room RoomData = Room.Rooms.get(RoomId);
            String cMessage = CurrentCommand.replace(Params[0] + " ", "").replace(Params[1] + " ", "");
            
            Iterator reader = RoomData.UserList.iterator();
            while(reader.hasNext())
            {
            	Habbo cUser = (Habbo)reader.next();
            	cUser.SendAlert(cMessage);
            }
            return true;
        }
        else if(Params[0].equals("alert"))
        {
        	if(User.RankLevel < 5)
        		return false;
            String UserName = Params[1];
            String cMessage = "Has recibido una alerta\n";
            Habbo cUser = Habbo.GetUserForName(UserName);
            cMessage += CurrentCommand.replace(Params[0] + " ", "").replace(Params[1] + " ", "");
            cUser.SendAlert(cMessage);
            return true;
        } else if(Params[0].equals("about"))
        {
            ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
            String AlertContent = "";
            
            AlertContent += "         Habbo.ht // Sobre nosotros\n\n" +
                    "#Sistema: RELEASE63-201201190922-6332255033\n" +
                    "#Usuarios Conectados: " + Environment.UsersConnected + "/2500";
            
            Alert.writeUTF(AlertContent);
            Alert.writeUTF("");
            Alert.Send(Socket);
            return true;
        } else if(Params[0].equals("commands"))
        {
            ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
            String AlertContent = "";
            
            AlertContent += "         Habbo.ht // Tus Comandos\n\n";
            if(User.RankLevel == 8)
            {
            	AlertContent += ":adminabout\n" +
                        ":update_furniture\n" +
                        ":update_catalog\n";
            }
            if(User.RankLevel >= 6)
            {
            	AlertContent += ":massalert <mensaje>\n" +
            			":roomalert <mensaje>\n" +
            			":roomalertto <id> <mensaje>";
            }
            if(User.RankLevel >= 5)
            {
            	AlertContent += ":teleport <on/off>\n" +
                        ":move <user> <x> <y>\n" +
                        ":alert <nombre> <mensaje>\n";
            }
            AlertContent += ":commands\n" +
                    ":about";
            
            Alert.writeUTF(AlertContent);
            Alert.writeUTF("");
            Alert.Send(Socket);
            return true;
        } else if(Params[0].equals("whoisonline"))
        {
        	if(User.RankLevel < 4)
        		return false;
            ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
            String AlertContent = "";
            
            AlertContent += "         Usuarios conectados (" + Habbo.GetUsersOnline().size() + ")\n\n";
            List<Habbo> Users = Habbo.GetUsersOnline();
            Iterator reader = Users.iterator();
            while(reader.hasNext())
            {
            	Habbo cUser = (Habbo)reader.next();
            	AlertContent += cUser.UserName + "\n";
            }
            
            Alert.writeUTF(AlertContent);
            Alert.writeUTF("");
            Alert.Send(Socket);
            return true;
        }
        return false;
    }
    
    public boolean ThereIsACommand()
    {
        if(Params[0].equals("send") || Params[0].equals("about"))
            return true;
        else
            return false;
    }
}
