package com.sulake.habbo.communication.messages.outgoing.navigator;

import neutrino.Network.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;

public class LoadPublicRoomsMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User) throws Exception
	{
		ServerMessage Categorys = new ServerMessage(ServerEvents.NavigatorShowPublics);
        Categorys.writeInt(2);
        Categorys.writeInt(1);
        Categorys.writeUTF("Salas del Staff");
        Categorys.writeUTF("");
        Categorys.writeInt(1);
        Categorys.writeUTF("");
        Categorys.writeUTF("officialrooms_hq/staffpickfolder.gif");
        Categorys.writeInt(0);
        Categorys.writeInt(0);
        Categorys.writeInt(4);
        Categorys.writeBoolean(false);
        Categorys.writeInt(1);
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
