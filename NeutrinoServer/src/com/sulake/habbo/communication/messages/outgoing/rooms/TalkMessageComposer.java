package com.sulake.habbo.communication.messages.outgoing.rooms;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.AdministrationManager.Chatlog;
import neutrino.MessageEvents.MessageEvent;
import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.PetManager.Pet;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.UserManager.UserManager;
import neutrino.RoomManager.CommandHandler;
import neutrino.RoomManager.Room;
import org.jboss.netty.channel.Channel;

public class TalkMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server, String Type) throws Exception
	{
		Habbo CurrentUser = Client.GetSession();
        int RoomId = CurrentUser.CurrentRoomId;
        Channel Socket = Client.Socket;
        Room R = Room.Rooms.get(RoomId);
        
        String Message = Client.inreader.readUTF();
        if(Type.equals( "snow"))
        {
        	// talk on snow
        	ServerMessage Talk = new ServerMessage(ServerEvents.TalkOnSnow);
            Talk.writeInt(CurrentUser.Id);
            Talk.writeUTF(Message);
            Talk.writeInt(0);
            UserManager.SendMessageToAllSnowPlayers(CurrentUser.CurrentWar, Talk);
        } else if(Type.equals("whisp"))
        {
        	String[] separe = Message.split(" ");
        	String Name = separe[0];
        	Habbo bUser = Habbo.GetUserForName(Name);
        	String ToSay = Message.replace(Name + " ", "");
        	
        	String MessageWithNoMods = ToSay;
            
            int SmileState = 0;
            if (ToSay.contains(":)") || ToSay.contains("=)") || ToSay.contains(":D") || ToSay.contains("=D"))
            {
                SmileState = 1; // Happy face
            }

            if (ToSay.contains(":@") || ToSay.contains(">:(") || ToSay.contains(">:@"))
            {
                SmileState = 2; // Angry face
            }

            if (ToSay.contains(":o") || ToSay.contains("D:"))
            {
                SmileState = 3; // Surprise face
            }

            if (ToSay.contains(":(") || ToSay.contains(":'(") || ToSay.contains("=(") || ToSay.contains("='("))
            {
                SmileState = 4; // Sad face
            }
            
            // Url System
            String Site = "";

            if (ToSay.contains("http://") || ToSay.contains("www."))
            {
                String[] Split = ToSay.split(" ");
                
                for (int i = 0; i < Split.length; i++)
                {
                	String Msg = Split[i];
                    if (Msg.startsWith("http://") || Msg.startsWith("https://") || Msg.startsWith("www."))
                    {
                        Site = Msg;
                    }
                }
                ToSay = ToSay.replace(Site, "{0}");
            }
        	
        	int PacketId = ServerEvents.Whisp;
            
            ServerMessage Talk = new ServerMessage(PacketId);
            Talk.writeInt(CurrentUser.SessionId);
            Talk.writeUTF(ToSay);
            Talk.writeInt(SmileState);
            Talk.writeInt(((!Site.equals("")) ? 1 : 0));
            if(!Site.equals(""))
            {
            	Talk.writeUTF(Site.replace("http://", ""));
            	Talk.writeUTF(Site);
                Talk.writeBoolean(true);
            }
            Talk.writeInt(0);
            Talk.Send(Socket);
            if(bUser.Id != User.Id)
            	Talk.Send(bUser.CurrentSocket);
            Chatlog.AddChatlog(CurrentUser, "[susurro] " + MessageWithNoMods);
        	
        }else {
            String MessageWithNoMods = Message;
            CommandHandler C = new CommandHandler();
            if(Message.startsWith(":") && C.CanSayCommand(Message, Socket, CurrentUser, Server))
            {
                return;
            }
            
            int SmileState = 0;
            if (Message.contains(":)") || Message.contains("=)") || Message.contains(":D") || Message.contains("=D"))
            {
                SmileState = 1; // Happy face
            }

            if (Message.contains(":@") || Message.contains(">:(") || Message.contains(">:@"))
            {
                SmileState = 2; // Angry face
            }

            if (Message.contains(":o") || Message.contains("D:"))
            {
                SmileState = 3; // Surprise face
            }

            if (Message.contains(":(") || Message.contains(":'(") || Message.contains("=(") || Message.contains("='("))
            {
                SmileState = 4; // Sad face
            }
            
            // Url System
            String Site = "";

            if (Message.contains("http://") || Message.contains("www.") || Message.contains("https://"))
            {
                String[] Split = Message.split(" ");
                
                for (int i = 0; i < Split.length; i++)
                {
                	String Msg = Split[i];
                    if (Msg.startsWith("http://") || Msg.startsWith("https://") || Msg.startsWith("www."))
                    {
                        Site = Msg;
                    }
                }
                Message = Message.replace(Site, "{0}");
            }
            
            int PacketId = ServerEvents.Shout;
            if(Type == "talk")
            	PacketId = ServerEvents.Talk;
            else
            	PacketId = ServerEvents.Shout;
            
            ServerMessage Talk = new ServerMessage(PacketId);
            Talk.writeInt(CurrentUser.SessionId);
            Talk.writeUTF(Message);
            Talk.writeInt(SmileState);
            Talk.writeInt(((!Site.equals("")) ? 1 : 0));
            if(!Site.equals(""))
            {
            	Talk.writeUTF(Site.replace("http://", ""));
            	Talk.writeUTF(Site);
                Talk.writeBoolean(true);
            }
            Talk.writeInt(0);
            UserManager.SendMessageToUsersOnRoomId(User.CurrentRoomId, Talk);
            Chatlog.AddChatlog(CurrentUser, MessageWithNoMods);
            Pet.ReadCommand(CurrentUser.Id, MessageWithNoMods);
        }
	}
}
