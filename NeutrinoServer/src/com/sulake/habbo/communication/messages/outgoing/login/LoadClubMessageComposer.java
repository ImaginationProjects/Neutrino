package com.sulake.habbo.communication.messages.outgoing.login;

import neutrino.Network.*;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.UserManager.*;
import neutrino.Environment;
import neutrino.System.*;

import org.jboss.netty.channel.Channel;
public class LoadClubMessageComposer {
	public static void Compose(ServerHandler Client, Habbo User, Environment Server) throws Exception
	{
		SubscriptionManager Sub = new SubscriptionManager(User, Server);
        ServerMessage Club = new ServerMessage(ServerEvents.ClubData);
        Club.writeUTF("club_habbo");
        if(!Sub.HasSubscription("habbo_vip"))
        {
            Club.writeInt(0); // DaysLeft
            Club.writeInt(0); // Months Left
            Club.writeInt(0); // Years left wtf?
            Club.writeInt(1); // VIP (1 = no / 2 = yes)
            Club.writeBoolean(false);
            Club.writeBoolean(true);
            Club.writeInt(0);
            Club.writeInt(0); // Days I have
            Club.writeInt(0);
        } else {
            Sub.GetSubscriptionData("habbo_vip");
            int Expire = Sub.GetExpireTime();
            double TimeLeft = Expire - Environment.getIntUnixTimestamp();
            int DaysLeft = (int)Math.ceil(TimeLeft / 86400);
            int MonthsLeft;
            if (DaysLeft < 31)
                MonthsLeft = 0;
            else
                MonthsLeft = DaysLeft / 31;
            int Initial = Sub.InitialTime;
            double TimeLeft2 = Environment.getIntUnixTimestamp() - Initial;
            int TotalDaysLeft2 = (int)Math.ceil(TimeLeft2 / 86400);
            int MonthsLeft2 = TotalDaysLeft2 / 31;
            if (DaysLeft - (MonthsLeft * 31) < 0)
                MonthsLeft = MonthsLeft - 1;
            Club.writeInt(DaysLeft); // DaysLeft
            Club.writeInt(MonthsLeft); // Months Left
            Club.writeInt(0); // Years left wtf?
            Club.writeInt(1); // VIP (0 = no / 1 = yes)
            Club.writeBoolean(true);
            Club.writeBoolean(true);
            Club.writeInt(0);
            Club.writeInt(TotalDaysLeft2); // Days I have
            Club.writeInt(17629);
        }
        Club.Send(Client.Socket);
	}
}
