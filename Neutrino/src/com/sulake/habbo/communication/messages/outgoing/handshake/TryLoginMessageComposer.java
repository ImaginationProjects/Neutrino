package com.sulake.habbo.communication.messages.outgoing.handshake;

import neutrino.Network.*;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.UserManager.*;
import neutrino.Environment;
import neutrino.System.*;
import neutrino.Brain.*;

import org.jboss.netty.channel.Channel;

public class TryLoginMessageComposer {
	public static void Compose(ServerHandler Client, Habbo CurrentHabbo, Environment Server) throws Exception
	{
		Channel Socket = Client.Socket;
		
		ServerMessage LoginUser = new ServerMessage(ServerEvents.InitSystem);
		LoginUser.Send(Socket);
        
        ServerMessage Home = new ServerMessage(ServerEvents.SetHome);
        Home.writeInt(CurrentHabbo.Home);
        Home.writeInt(CurrentHabbo.Home);
        Home.Send(Socket);
        
        SubscriptionManager Sub = new SubscriptionManager(CurrentHabbo, Server);
        if(Sub.HasSubscription("habbo_vip"))
        {
            // Gifts later
        }
        
        ServerMessage Favs = new ServerMessage(ServerEvents.SetFavs);
        Favs.writeInt(30);
        Favs.writeInt(0);
        Favs.Send(Socket);
        
        ServerMessage FuseRights = new ServerMessage(ServerEvents.FuseRights);
        if(Sub.HasSubscription("habbo_vip"))
            FuseRights.writeInt(2); // HC/VIP/SOMESHIT???? 
        else
            FuseRights.writeInt(0); // same
        FuseRights.writeInt(CurrentHabbo.RankLevel); // Haha admin level
        FuseRights.Send(Socket);
        
        ServerMessage MoreData = new ServerMessage(ServerEvents.MoreData);
        MoreData.writeBoolean(true);
        MoreData.writeBoolean(false);
        MoreData.Send(Socket);
        
        ServerMessage UnData = new ServerMessage(ServerEvents.UnData);
        UnData.writeBoolean(false);
        UnData.Send(Socket);
        
        ServerMessage Pixels = new ServerMessage(ServerEvents.Pixels);
        Pixels.writeInt(2); // Count
        Pixels.writeInt(0); // TYPE = PIXELS
        Pixels.writeInt(CurrentHabbo.Pixels); // Count of type (pixels)
        Pixels.writeInt(1); // TYPE = SNOWFLAKES
        Pixels.writeInt(CurrentHabbo.SnowFlakes); // blabla...
        Pixels.Send(Socket);
        
        ServerMessage Snow = new ServerMessage(ServerEvents.Snow);
        Snow.writeBoolean(true);
        Snow.Send(Socket);
        
        ServerMessage Alert = new ServerMessage(ServerEvents.Alert);
        Alert.writeUTF("Hola " + CurrentHabbo.UserName + ", ¡bienvenido a Habbo.ht!, ¿Qué tal estás hoy?");
        Alert.writeUTF("");
        Alert.Send(Socket);
        
        if(CurrentHabbo.RankLevel >= 4)
        {
            ServerMessage MOD_TOOLS = new ServerMessage(ServerEvents.ModTools);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.writeInt(0);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeBoolean(false);
            MOD_TOOLS.writeInt(0);
          //  MOD_TOOLS.Send(Socket);
        }
	}
}
