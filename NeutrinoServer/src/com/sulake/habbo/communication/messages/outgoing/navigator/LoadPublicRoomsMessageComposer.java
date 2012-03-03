package com.sulake.habbo.communication.messages.outgoing.navigator;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadPublicRoomsMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		ServerMessage Categorys = new ServerMessage(ServerEvents.NavigatorShowPublics);
        Categorys.writeInt(2); // count Categorys
        
        // serialize Categorys
        Categorys.writeInt(1); // id 
        Categorys.writeUTF("Salas del Staff"); // name
        Categorys.writeUTF(""); // desc?
        Categorys.writeInt(1); // ?
        Categorys.writeUTF(""); // ?
        Categorys.writeUTF("officialrooms_hq/staffpickfolder.gif"); // image
        Categorys.writeInt(0); // ?
        Categorys.writeInt(0); // ?
        Categorys.writeInt(4); // ?
        Categorys.writeBoolean(false); // ?
        
        /*Categorys.writeInt(5); // roomid?
        Categorys.writeInt(0);
        Categorys.writeInt(1);
        Categorys.writeInt(0);
        Categorys.writeInt(1); // parent id
        Categorys.writeInt(7);
        Categorys.writeInt(2);
        Categorys.writeInt(5); // roomid
        Categorys.writeBoolean(false); // is event?
        Categorys.writeUTF("Uh yea!");
        Categorys.writeInt(1); // owner id?
        Categorys.writeUTF("Itachi"); // owner Name
        Categorys.writeInt(0); // state
        Categorys.writeInt(7); // current users
        Categorys.writeInt(50); // max users
        Categorys.writeUTF("OMG It's Itachi's room picked by Staff!"); // description
        Categorys.writeInt(0);
        Categorys.writeInt(0); // 
        Categorys.writeInt(0); // 
        Categorys.writeInt(0); // 
        Categorys.writeInt(0); // group id
        Categorys.writeUTF(""); // group name
        Categorys.writeUTF(""); // group badge
        Categorys.writeUTF(""); // group badge
        Categorys.writeInt(0); // tags?
     // Icons
        Categorys.writeInt(0);
        Categorys.writeInt(0);
        Categorys.writeInt(0);
        // bools
        Categorys.writeBoolean(false);
        Categorys.writeBoolean(true);*/      
        
        
        Categorys.writeInt(2);
        Categorys.writeUTF("este es diferente?");
        Categorys.writeUTF("");
        Categorys.writeInt(1);
        Categorys.writeUTF("");
        Categorys.writeUTF("officialrooms_hq/staffpickfolder.gif");
        Categorys.writeInt(0);
        Categorys.writeInt(0);
        Categorys.writeInt(4);
        Categorys.writeBoolean(false);
        Categorys.writeInt(-1);
        Categorys.Send(Client.Socket);
	}
}
