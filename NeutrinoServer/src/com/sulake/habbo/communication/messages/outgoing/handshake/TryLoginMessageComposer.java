package com.sulake.habbo.communication.messages.outgoing.handshake;

import java.util.*;

import neutrino.Network.*;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.UserManager.*;
import neutrino.Environment;
import neutrino.RoomManager.Room;
import neutrino.RoomManager.RoomEvent;
import neutrino.System.*;
import neutrino.AdministrationManager.CallForHelpState;
import neutrino.AdministrationManager.CallsForHelp;
import neutrino.AdministrationManager.ModerationPreset;
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
        if(CurrentHabbo.RankLevel == 8)
        	FuseRights.writeInt(7);
        else
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
        
        if(CurrentHabbo.RankLevel >= 5)
        {
            ServerMessage ModTools = new ServerMessage(ServerEvents.ModTools);
            List<CallsForHelp> FreeCalls = CallsForHelp.GetNoPickedCalls();
            if(FreeCalls.size() > 0)
            	ModTools.writeInt(FreeCalls.size()); // count issues
            else
            	ModTools.writeInt(-1); // no issues
            // serialize issues
            Iterator sreader = FreeCalls.iterator();
            while(sreader.hasNext())
            {
            	CallsForHelp C = (CallsForHelp)sreader.next();
            	ModTools.writeInt(C.Id); // id
                ModTools.writeInt(C.State); // state 
                ModTools.writeInt(1); // cat id
                ModTools.writeInt(C.Category); // reported cat id
                ModTools.writeInt((Environment.getIntUnixTimestamp() - C.Timestamp)); // timestamp
                ModTools.writeInt(C.Priority); // priority
                ModTools.writeInt(C.ReporterId); // reporter id
                ModTools.writeUTF(Habbo.UsersbyId.get(C.ReporterId).UserName); // reporter name
                ModTools.writeInt(C.ReportedId); // reported id
                ModTools.writeUTF((Habbo.UsersbyId.containsKey(C.ReportedId)) ? Habbo.UsersbyId.get(C.ReportedId).UserName : ""); // reported name
                ModTools.writeInt((C.stateint > 2) ? 0 : C.PickedBy); // mod id
                ModTools.writeUTF((Habbo.UsersbyId.containsKey(C.PickedBy)) ? Habbo.UsersbyId.get(C.PickedBy).UserName : ""); // mod name
                ModTools.writeUTF(C.Message); // issue message
                ModTools.writeInt(C.RoomId); // room id
                Room RoomData = Room.Rooms.get(C.RoomId);
                ModTools.writeUTF(RoomData.Name); // room name
                ModTools.writeInt(0); // room type: 0 private - 1 public
                // if private
                if(RoomEvent.GetEventForRoomId(C.RoomId)==null)
                	ModTools.writeUTF("-1");
                else {
                	RoomEvent E = RoomEvent.GetEventForRoomId(C.RoomId);
                	ModTools.writeUTF(E.OwnerId + "");
                    ModTools.writeUTF(Habbo.UsersbyId.get(E.OwnerId).UserName);
                    ModTools.writeUTF(E.RoomId + "");
                    ModTools.writeInt(E.Category);
                    ModTools.writeUTF(E.Title);
                    ModTools.writeUTF(E.Description);
                    ModTools.writeUTF(E.Created);
                    ModTools.writeInt(E.Tags.size());
                    Iterator reader = E.Tags.iterator();
                    while(reader.hasNext())
                    {
                    	String tag = (String)reader.next();
                    	ModTools.writeUTF(tag);
                    }
                }
                ModTools.writeInt(C.Category); // cat of room
                ModTools.writeInt(0); // not defined
            }
            
            List<ModerationPreset> UserPresets = ModerationPreset.GetChatlogsForType("users");
            ModTools.writeInt(UserPresets.size());
            Iterator reader = UserPresets.iterator();
            while(reader.hasNext())
            {
            	ModerationPreset M = (ModerationPreset)reader.next();
            	ModTools.writeUTF(M.Message);
            }
            ModTools.writeInt(6); //Mod Actions Count

            ModTools.writeUTF("Problemas en la sala"); // modaction Cata
            ModTools.writeInt(8); // ModAction Count
            ModTools.writeUTF("Bloqueando la puerta"); // mod action Cata
            ModTools.writeUTF("Deja de bloquear la puerta, por favor."); // Msg
            ModTools.writeUTF("Bans"); // Mod Action Cata
            ModTools.writeUTF("Esta es tu última advertencia, recivirás un baneo la próxima vez."); // Msg
            ModTools.writeUTF("Ayuda & Soporte");// Mod Action Cata
            ModTools.writeUTF("Por favor, contacta a los Administradores"); // Msg
            ModTools.writeUTF("Filtro"); // Mod Cata
            ModTools.writeUTF("Por favor, deja de jugar con el fltro"); // Msg
            ModTools.writeUTF("Kick Masivo"); // Mod Cata
            ModTools.writeUTF("Por favor, deja de kickear sin razón."); // Msg
            ModTools.writeUTF("Expulsiones Masivas"); // Mod Cata
            ModTools.writeUTF("Por favor, deja de expulsar gente sin razón."); // Msg
            ModTools.writeUTF("Nombre de Sala"); // Mod Cata
            ModTools.writeUTF("Cambia tu nombre o lo haremos nosotros."); // Msg
            ModTools.writeUTF("Furni no carga"); // Mod Cata
            ModTools.writeUTF("Contacta con un administrador respecto a tu problema."); // Msg

            ModTools.writeUTF("Soporte al Usuario");// modaction Cata
            ModTools.writeInt(8); // ModAction Count
            ModTools.writeUTF("Error"); // mod action Cata
            ModTools.writeUTF("Gracias por reportar el error."); // Msg
            ModTools.writeUTF("Problema de Conexión"); // Mod Action Cata
            ModTools.writeUTF("Vamos a contactar con soporte para solucionar tu problema."); // Msg
            ModTools.writeUTF("Ayuda");// Mod Action Cata
            ModTools.writeUTF("Contacta con los administradores para solucionar tu problema."); // Msg
            ModTools.writeUTF("CFH (Alertas de Ayuda)"); // Mod Cata
            ModTools.writeUTF("Deja de pedir ayuda sin sentido o serás baneado."); // Msg
            ModTools.writeUTF("Privacidad"); // Mod Cata
            ModTools.writeUTF("No debes dar tus datos a nadie."); // Msg
            ModTools.writeUTF("Alerta SWF."); // Mod Cata
            ModTools.writeUTF("Vamos a contactar con soporte para solucionar tu problema."); // Msg
            ModTools.writeUTF("Cache"); // Mod Cata
            ModTools.writeUTF("Por favor, reinicia tu caché."); // Msg
            ModTools.writeUTF("Archivos Temporales"); // Mod Cata
            ModTools.writeUTF("¡Borra tus arhivos temporales!"); // Msg

            ModTools.writeUTF("Problemas (Usuarios)");// modaction Cata
            ModTools.writeInt(8); // ModAction Count
            ModTools.writeUTF("Timos"); // mod action Cata
            ModTools.writeUTF("Revisaremos tu problema si nos das más información acerca de lo ocurrido."); // Msg
            ModTools.writeUTF("Timo en Cambios"); // Mod Action Cata
            ModTools.writeUTF("Explícanos qué y como ha pasado."); // Msg
            ModTools.writeUTF("Desconexión");// Mod Action Cata
            ModTools.writeUTF("Vamos a contactar con soporte para solucionar tu problema."); // Msg
            ModTools.writeUTF("Bloqueando Salas"); // Mod Cata
            ModTools.writeUTF("Dinos el nombre del usuario y lo que está pasando."); // Msg
            ModTools.writeUTF("Congelación"); // Mod Cata
            ModTools.writeUTF("¿Puedes explicarnos lo que pasa?"); // Msg
            ModTools.writeUTF("Error 404"); // Mod Cata
            ModTools.writeUTF("Explícanos detalladamente lo que te ha pasado."); // Msg
            ModTools.writeUTF("Imposible Conectar"); // Mod Cata
            ModTools.writeUTF("Dinos el nombre de usuario y lo que envía al conectarse."); // Msg
            ModTools.writeUTF("Actualizaciones"); // Mod Cata
            ModTools.writeUTF("Siempre hacemos lo mejor para actualizar todo."); // Msg

            ModTools.writeUTF("Problemas on-game");// modaction Cata
            ModTools.writeInt(8); // ModAction Count
            ModTools.writeUTF("Lag"); // mod action Cata
            ModTools.writeUTF("Intentaremos corregirlo en próximos mantenimientos."); // Msg
            ModTools.writeUTF("Desconexión"); // Mod Action Cata
            ModTools.writeUTF("¿Qué y cómo ha pasado?, explícanoslo detalladamente."); // Msg
            ModTools.writeUTF("Problema SSO");// Mod Action Cata
            ModTools.writeUTF("Vamos a contactar con soporte para solucionar tu problema."); // Msg
            ModTools.writeUTF("Scripting"); // Mod Cata
            ModTools.writeUTF("Dinos el nombre del usuario y lo que hacía."); // Msg
            ModTools.writeUTF("Aviso Scripting"); // Mod Cata
            ModTools.writeUTF("Vas a ser baneado por intentar hackear nuestro sistema."); // Msg
            ModTools.writeUTF("Error en el sistema"); // Mod Cata
            ModTools.writeUTF("Intentaremos corregirlo en próximos mantenimientos."); // Msg
            ModTools.writeUTF("Errores varios"); // Mod Cata
            ModTools.writeUTF("Intentaremos corregirlo en próximos mantenimientos."); // Msg
            ModTools.writeUTF("Error en Flash player"); // Mod Cata
            ModTools.writeUTF("Actualiza tu flash player, o intentaremos corregirlo en próximos mantenimientos."); // Msg

            ModTools.writeUTF("Problemas (Sistema)");// modaction Cata
            ModTools.writeInt(8); // ModAction Count
            ModTools.writeUTF("Versión"); // mod action Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("RELEASE de las SWF"); // Mod Action Cata
            ModTools.writeUTF("Puedes encontrarlo si dices :about"); // Msg
            ModTools.writeUTF("Servidor");// Mod Action Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Filtro de Usuarios"); // Mod Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Filtro de Nombres"); // Mod Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Filtros del Sistema"); // Mod Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Filtro de Palabras"); // Mod Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Añadir palabras al filtro"); // Mod Cata
            ModTools.writeUTF("Añadiremos esa palabra, gracias por reportarlo.."); // Msg

            ModTools.writeUTF("Problemas (SWF)");// modaction Cata
            ModTools.writeInt(8); // ModAction Count
            ModTools.writeUTF("Versión"); // mod action Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("RELEASE de las SWF"); // Mod Action Cata
            ModTools.writeUTF("Puedes encontrarlo si dices :about"); // Msg
            ModTools.writeUTF("Servidor");// Mod Action Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Filtro de Usuarios"); // Mod Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Filtro de Nombres"); // Mod Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Filtros del Sistema"); // Mod Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Filtro de Palabras"); // Mod Cata
            ModTools.writeUTF("No podemos darte esa información."); // Msg
            ModTools.writeUTF("Añadir palabras al filtro"); // Mod Cata
            ModTools.writeUTF("Añadiremos esa palabra, gracias por reportarlo.."); // Msg
            
            /*Boolean // ticket_queue_but 
        	Boolean // chatlog_but 
        	Boolean // message_but - modaction_but - send_caution_but 
        	Boolean // modaction_but - kick_but 
        	Boolean // modaction_but - ban_but 
        	Boolean // send_caution_but - send_message_but 
        	Boolean // kick_check */

            ModTools.writeBoolean(true); // ticket_queue fuse
            ModTools.writeBoolean(true); // chatlog fuse
            ModTools.writeBoolean(true); // message / caution fuse
            ModTools.writeBoolean(true); // kick fuse
            ModTools.writeBoolean(true); // band fuse
            ModTools.writeBoolean(true); // other alert fuse
            ModTools.writeBoolean(true); // kick check fuse

            List<ModerationPreset> RoomPresets = ModerationPreset.GetChatlogsForType("room");
            ModTools.writeInt(RoomPresets.size());
            Iterator Roomreader = RoomPresets.iterator();
            while(Roomreader.hasNext())
            {
            	ModerationPreset M = (ModerationPreset)Roomreader.next();
            	ModTools.writeUTF(M.Message);
            }
            ModTools.writeBoolean(true);
            ModTools.Send(Socket);
            
            /*ServerMessage Issue = new ServerMessage(ServerEvents.MOD_SHOWISSUE);
            Issue.writeInt(1); // id
            Issue.writeInt(CallForHelpState.OPEN); // state 
            Issue.writeInt(11); // cat id
            Issue.writeInt(1); // reported cat id
            Issue.writeInt(11); // timestamp
            Issue.writeInt(1); // priority
            Issue.writeInt(1); // reporter id
            Issue.writeUTF("Itachi"); // reporter name
            Issue.writeInt(3); // reported id
            Issue.writeUTF("Nacho"); // reported name
            Issue.writeInt(CurrentHabbo.Id); // mod id
            Issue.writeUTF(CurrentHabbo.UserName); // mod name
            Issue.writeUTF("Me quiere veolar D:"); // issue message
            Issue.writeInt(5); // room id
            Issue.writeUTF("OMG It's Itachi's room"); // room name
            Issue.writeInt(0); // room type: 0 private - 1 public
            // if private
            Issue.writeUTF("-1"); // event
            Issue.writeInt(1); // cat of room
            Issue.writeInt(0); // not defined*/
            // if public
            // Issue.writeUTF(""); // can't, so not defined
            // Issue.writeInt(1); // cat of room
            // Issue.writeUTF(""); // not defined */
         }
	}
}
