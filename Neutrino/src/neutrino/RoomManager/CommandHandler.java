/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.RoomManager;
import java.util.*;
import neutrino.Environment;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
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
    
    public CommandHandler(String Message, Channel Socket) throws Exception
    {
        CurrentCommand = Message;
        Params = Message.replace(":", "").split(" ");
        
        if (Params[0].equals("send"))
        {
            // SEND A PACKET, LOL!
            ServerMessage mess = null;
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
                mess.Send(Socket);
        } else if(Params[0].equals("about"))
        {
            ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
            String AlertContent = "";
            
            AlertContent += "         Habbo.ht // Sobre nosotros\n\n" +
                    "#Sistema: RELEASE63-201201041334-994259463\n" +
                    "#Usuarios Conectados: " + Environment.SessionIds + "/2500";
            
            Alert.writeUTF(AlertContent);
            Alert.writeUTF("");
            Alert.Send(Socket);
        }
    }
    
    public boolean ThereIsACommand()
    {
        if(Params[0].equals("send") || Params[0].equals("about"))
            return true;
        else
            return false;
    }
}
