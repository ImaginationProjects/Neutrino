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
import neutrino.UserManager.SubscriptionManager;
/**
 *
 * @author Julián
 */
public class LoadClub extends Handler implements Runnable {
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
        Habbo CurrentHabbo = Client.GetSession();
        SubscriptionManager Sub = new SubscriptionManager(CurrentHabbo, Server);
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
            Club.writeInt(28022);
        }
        Club.Send(Client.Socket);
       /* Int(31) // Days left
Int(1) // Months Left
Int(0) // static
Int(2) // Vip State (1/2)
Bool(true) // static
Bool(true) // static
Int(0) // static
Int(31) // Days left
Int(0)
        */
        } catch (Exception e)
        {
            
        }
        
    }
}
