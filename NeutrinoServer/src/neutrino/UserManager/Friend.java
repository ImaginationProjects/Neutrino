package neutrino.UserManager;

import java.util.*;
import java.sql.*;
import neutrino.Environment;

public class Friend {
	public int UserId;
	public int FriendId;
	public int Pending;
	public int CategoryId;
	
	public static List<Friend> Friends;
	public static void Init(Environment Server) throws Exception
	{
		Friends = new ArrayList<Friend>();
		ResultSet Friendz = Server.GetDatabase().executeQuery("SELECT * FROM users_friends");
		while(Friendz.next())
		{
			Friend F = new Friend();
			F.UserId = Friendz.getInt("userid");
			F.FriendId = Friendz.getInt("friendid");
			F.Pending = Friendz.getInt("pending");
			F.CategoryId = Friendz.getInt("category");
			Friends.add(F);
		}
		
		Server.WriteLine("Loaded " + Friends.size() + " friend(s).");
	}
	
	public static List<Friend> SelectFriends() throws Exception
	{
		List<Friend> Friendz = new ArrayList<Friend>();
		
		Iterator reader = Friends.iterator();
		while(reader.hasNext())
		{
			Friend F = (Friend)reader.next();
			if(F.Pending == 0)
				Friendz.add(F);
		}
		
		return Friendz;
	}
	
	public static List<Friend> SelectPendingFriends() throws Exception
	{
		List<Friend> Friendz = new ArrayList<Friend>();
		
		Iterator reader = Friends.iterator();
		while(reader.hasNext())
		{
			Friend F = (Friend)reader.next();
			if(F.Pending == 1)
				Friendz.add(F);
		}
		
		return Friendz;
	}
	
	public static void AddFriend(Friend F)
	{
		Friends.add(F);
	}
	
	public static Friend SelectFriendById(int uId, int fId)
	{
		Friend Friendz = null;
		Iterator reader = Friends.iterator();
		while(reader.hasNext())
		{
			Friend F = (Friend)reader.next();
			if(F.UserId == uId && F.FriendId == fId)
				Friendz = F;
		}
		
		return Friendz;
	}
	
	public static List<Friend> SelectFriendsForId(int uId) throws Exception
	{
		List<Friend> Friendz = new ArrayList<Friend>();
		
		Iterator reader = Friends.iterator();
		while(reader.hasNext())
		{
			Friend F = (Friend)reader.next();
			if(F.UserId == uId && F.Pending == 0)
			{
				if(!Friendz.contains(F))
					Friendz.add(F);
			}
		}
		
		return Friendz;
	}
	
	public static List<Friend> SelectPendingFriendsForId(int uId) throws Exception
	{
		List<Friend> Friendz = new ArrayList<Friend>();
		
		Iterator reader = SelectPendingFriends().iterator();
		while(reader.hasNext())
		{
			Friend F = (Friend)reader.next();
			if(F.UserId == uId && F.Pending == 1)
			{
				if(!Friendz.contains(F))
					Friendz.add(F);
			}
		}
		
		return Friendz;
	}
}
