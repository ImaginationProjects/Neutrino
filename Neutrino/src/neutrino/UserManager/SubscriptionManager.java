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
public class SubscriptionManager {
    private Habbo User;
    private Environment Server;
    public int Id;
    public int UserId;
    public String SubscriptionId;
    public int ExpireTime;
    public int InitialTime;
    public int Gift;
    public int Days;
    public SubscriptionManager(Habbo cUser, Environment cServer)
    {
        User = cUser;
        Server = cServer;
    }
    
    public boolean HasSubscription(String sId)
    {
        Iterator reader = Subscription.Subscriptions.entrySet().iterator();
        while (reader.hasNext())
        {
            Subscription S = (Subscription)(((Map.Entry)reader.next()).getValue());
            if(sId.equals(S.SubscriptionId) && User.Id == S.UserId)
                return true;
        }
        return false;
    }
    
    public String GetActualSubscriptionName()
    {
        Iterator reader = Subscription.Subscriptions.entrySet().iterator();
        while (reader.hasNext())
        {
            Subscription S = (Subscription)(((Map.Entry)reader.next()).getValue());
            if(User.Id == S.UserId)
                return S.SubscriptionId;
        }
        return null;
    }
    
    public void AddOrExtendSubscription(String SubscriptionId, int DurationSeconds) throws Exception
        {
            SubscriptionId = SubscriptionId.toLowerCase();
            if (!this.HasSubscription(SubscriptionId))
            {
                int TimeCreated = Environment.getIntUnixTimestamp();
                int TimeExpire = ((int)Environment.getIntUnixTimestamp() + DurationSeconds);
                Server.GetDatabase().executeUpdate("INSERT INTO users_subscriptions (id, userid, subscription_id, sub_activated, sub_finished, usedgifts) VALUES (NULL, '" + User.Id + "', '" + SubscriptionId + "', '" + TimeCreated + "', '" + TimeExpire + "', '0');");

                Subscription Dat = new Subscription();
                Dat.Id = Subscription.Subscriptions.size() + 1;
                Dat.SubscriptionId = SubscriptionId;
                Dat.InitialTime = TimeCreated;
                Dat.ExpireTime = TimeExpire;
                Dat.Gift = 0;
                Dat.Days = 1;
                Dat.UserId = User.Id;
                Subscription.Subscriptions.put(Dat.Id, Dat);
            }
            else
            {
                Iterator reader = Subscription.Subscriptions.entrySet().iterator();
                while (reader.hasNext())
                {
                    Subscription S = (Subscription)(((Map.Entry)reader.next()).getValue());
                    if (S.UserId == User.Id && S.SubscriptionId.equals(SubscriptionId))
                    {
                        int NewTimeExpire = S.ExpireTime + DurationSeconds;
                        S.ExpireTime = NewTimeExpire;
                        Server.GetDatabase().executeUpdate("UPDATE users_subscriptions SET sub_finished = '" + NewTimeExpire + "' WHERE userid = '" + User.Id + "'");
                    }
                }
            }
        }
    
    public void GetSubscriptionData(String Id)
    {
        Iterator reader = Subscription.Subscriptions.entrySet().iterator();
        while (reader.hasNext())
        {
            Subscription S = (Subscription)(((Map.Entry)reader.next()).getValue());
            if(Id.equals(S.SubscriptionId) && User.Id == S.UserId)
            {
                this.Id = S.Id;
                this.UserId = S.UserId;
                this.ExpireTime = S.ExpireTime;
                this.InitialTime = S.InitialTime;
                this.SubscriptionId = S.SubscriptionId;
                this.Gift = S.Gift;
                this.Days = S.Days;
            }
        }
    }
    
    public int GetExpireTime()
    {
        return this.ExpireTime;
    }
}
