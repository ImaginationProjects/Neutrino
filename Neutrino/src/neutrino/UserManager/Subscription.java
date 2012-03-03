/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.UserManager;
import neutrino.Environment;
import java.util.*;
import java.sql.*;

import neutrino.Network.*;

import org.jboss.netty.channel.Channel;
import neutrino.System.ServerMessage;
import neutrino.PacketsInformation.ServerEvents;
/**
 *
 * @author Julián
 */
public class Subscription {
    public int Id;
    public int UserId;
    public String SubscriptionId;
    public int ExpireTime;
    public int InitialTime;
    public int Gift;
    public int Days;
    public static int LastId;
    public static Map<Integer, Subscription> Subscriptions;
    
    public static void Init(Environment Server) throws Exception
    {
        LastId = 0;
        Subscriptions = new HashMap<Integer, Subscription>();
        ResultSet Subs = Server.GetDatabase().executeQuery("SELECT * FROM users_subscriptions");
        while(Subs.next())
        {
            Subscription S = new Subscription();
            S.Id = Subs.getInt("id");
            if(S.Id > LastId)
                LastId = S.Id;
            S.UserId = Subs.getInt("userid");
            S.SubscriptionId = Subs.getString("subscription_id");
            S.InitialTime = Subs.getInt("sub_activated");
            S.ExpireTime = Subs.getInt("sub_finished");
            S.Gift = Subs.getInt("usedgifts");
            S.Days = Subs.getInt("useddays");
            Subscriptions.put(S.Id, S);
        }
        
        Server.WriteLine("Loaded " + Subscriptions.size() + " subscription(s).");
    }
    
}
