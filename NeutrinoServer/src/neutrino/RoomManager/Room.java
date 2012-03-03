/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.RoomManager;
import java.util.*;
import java.sql.*;
import neutrino.Environment;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.PetManager.Pet;
import neutrino.UserManager.Habbo;
/**
 *
 * @author Julián
 */
public class Room {
    public int Id;
    public int OwnerId;
    public String OwnerName;
    public String Name;
    public String Description;
    public int State;
    public String Password;
    public int MaxUsers;
    public int CurrentUsers;
    public int Score;
    public int Category;
    public String TagList;
    public List<String> Tags;
    public String Model;
    public boolean PublicRoom;
    public int IconBG;
    public int IconFG;
    public int Wall;
    public int Floor;
    public int VipFloors;
    public int VipWalls;
    public double Landscape;
    public boolean Hidewalls;
    public boolean Pets;
    public boolean PetsCanEat;
    public boolean CanWalkOnUsers;
    public List<Habbo> UserList;
    public List<RoomItem> FloorItems;
    public List<RoomItem> WallItems;
    public List<Pet> PetList;
    
    public RoomModel GetModel()
    {
        return RoomModel.GetModelByName(Model);
    }
    
    public static int LastId;
    public static Map<Integer, Room> Rooms;
    public static void Init(Environment Server) throws Exception
    {
        Rooms = new HashMap<Integer, Room>();
        LastId = 0;
        ResultSet Room = Server.GetDatabase().executeQuery("SELECT * FROM rooms");
        while(Room.next())
        {
            Room R = new Room();
            R.Id = Room.getInt("id");
            if(R.Id > LastId)
            	LastId = R.Id;
            R.OwnerId = Room.getInt("ownerid");
            R.OwnerName = ((Habbo)(Habbo.UsersbyId.get(R.OwnerId))).UserName;
            R.Name = Room.getString("name");
            R.Description = Room.getString("description");
            R.State = Room.getInt("state");
            R.Password = Room.getString("password");
            R.MaxUsers = Room.getInt("maxusers");
            R.CurrentUsers = 0;
            R.Score = Room.getInt("score");
            R.Category = Room.getInt("category");
            R.TagList = Room.getString("tags");
            R.Tags = new ArrayList<String>();
            R.Model = Room.getString("model");
            R.PublicRoom = Room.getBoolean("public_room");
            R.IconBG = Room.getInt("icon_bg");
            R.IconFG = Room.getInt("icon_fg");
            R.Wall = Room.getInt("wall");
            R.Floor = Room.getInt("floor");
            R.Landscape = Room.getDouble("landscape");
            R.Hidewalls = Room.getBoolean("hidewalls");
            R.Pets = Room.getBoolean("pets");
            R.PetsCanEat = Room.getBoolean("petseat");
            R.CanWalkOnUsers = Room.getBoolean("walkonusers");
            R.VipFloors = Room.getInt("vip_floors");
            R.VipWalls = Room.getInt("vip_walls");
            R.UserList = new ArrayList<Habbo>();
            R.FloorItems = new ArrayList<RoomItem>();
            R.WallItems = new ArrayList<RoomItem>();
            R.PetList = new ArrayList<Pet>();
            Rooms.put(R.Id, R);
        }
        
        Server.WriteLine("Loaded " + Rooms.size() + " room(s).");
    }
    
    public boolean HaveOwnerPowers(int UserId)
    {
    	if(UserId == this.OwnerId)
    		return true;
    	return false;
    }
    
    public boolean HavePowers(int UserId)
    {
    	if(HaveOwnerPowers(UserId))
    		return true;
    	else if(Habbo.UsersbyId.get(UserId).RankLevel >= 5)
    		return true;
    	return false;
    }
    
    public static Room AddRoom(int cId, String cName, String cModel, int cOwnerId, String cUserName)
    {
    	Room R = new Room();
        R.Id = cId;
        if(R.Id > LastId)
        	LastId = R.Id;
        R.OwnerId = cOwnerId;
        R.OwnerName = cUserName;
        R.Name = cName;
        R.Description = "";
        R.State = 0;
        R.Password = "";
        R.Model = cModel;
        R.MaxUsers = RoomModel.GetModelByName(cModel).MaxUsers;
        R.CurrentUsers = 0;
        R.Score = 0;
        R.Category = 0;
        R.TagList = "";
        R.Tags = new ArrayList<String>();
        R.PublicRoom = false;
        R.IconBG = 0;
        R.IconFG = 0;
        R.Wall = 0;
        R.Floor = 0;
        R.Landscape = 0.0;
        R.Hidewalls = false;
        R.Pets = false;
        R.PetsCanEat = false;
        R.CanWalkOnUsers = false;
        R.VipFloors = 0;
        R.VipWalls = 0;
        R.UserList = new ArrayList<Habbo>();
        R.FloorItems = new ArrayList<RoomItem>();
        R.WallItems = new ArrayList<RoomItem>();
        R.PetList = new ArrayList<Pet>();
        Rooms.put(R.Id, R);
        return R;
    }
    
    public List<RoomItem> GetItemOnXY(int X, int Y)
    {
        List<RoomItem> Result = new ArrayList<RoomItem>();
        Iterator reader = this.FloorItems.iterator();
        while(reader.hasNext())
        {
            RoomItem Item = (RoomItem)(reader.next());
            if(Item.X == X && Item.Y == Y)
                Result.add(Item);
            else {
                Map<Integer, AffectedTile> AffectedTiles = GetAffectedTiles(Item.GetBaseItem().Lenght, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rot);
                Iterator readertwo = AffectedTiles.entrySet().iterator();
                while(readertwo.hasNext())
                {
                    AffectedTile Tile = (AffectedTile)(((Map.Entry)readertwo.next()).getValue());
                    if(X == Tile.X && Y == Tile.Y)
                    {
                    	if(!Result.contains(Item))
                    		Result.add(Item);
                    }
                }
            }
        }
        
        return Result;
    }
    
    public boolean IsUserOnXY(int X, int Y)
    {
    	Iterator reader = this.UserList.iterator();
        while(reader.hasNext())
        {
            Habbo User = (Habbo)reader.next();
            if(User.X == X && User.Y == Y && !this.CanWalkOnUsers)
                return true;
        }
        
        return false;
    }
    
    public boolean IsItemOnXY(int X, int Y)
    {
        Iterator reader = this.FloorItems.iterator();
        while(reader.hasNext())
        {
            RoomItem Item = (RoomItem)reader.next();
            if(Item.GetBaseItem().Interactor.equals("gate") && Item.ExtraData.equals("1"))
                return false;
            else if(Item.X == X && Item.Y == Y && !Item.GetBaseItem().CanWalk)
                return true;
            else {
                Map<Integer, AffectedTile> AffectedTiles = GetAffectedTiles(Item.GetBaseItem().Lenght, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rot);
                Iterator reader2 = AffectedTiles.entrySet().iterator();
                while(reader2.hasNext())
                {
                    AffectedTile Tile = (AffectedTile)(((Map.Entry)reader2.next()).getValue());
                    if(Item.GetBaseItem().Interactor.equals("gate") && Item.ExtraData.equals("1"))
                        return false;
                    else if(X == Tile.X && Y == Tile.Y && !Item.GetBaseItem().CanWalk)
                        return true;
                }
            }
        }
        
        return false;
    }
    
    public double GetHForXY(int X, int Y, RoomItem Item)
    {
        double H = 0;
        int i = 0;
        List<RoomItem> HForXY = GetItemOnXY(X, Y);
        if (HForXY.size() > 1)
        {
        	Iterator reader = HForXY.iterator();
            while(reader.hasNext())
            {
            	RoomItem sItem = (RoomItem)reader.next();
                if (Item.Id == sItem.Id)
                    break;
                H += Item.GetBaseItem().Height;
                i++;
            }
        }
        return H;
    }

    public double SecondGetHForXY(int X, int Y)
    {
        double H = 0;
        int i = 0;
        List<RoomItem> HForXY = GetItemOnXY(X, Y);
        if (HForXY.size() > 0)
        {
        	Iterator reader = HForXY.iterator();
            while(reader.hasNext())
            {
            	RoomItem Item = (RoomItem)reader.next();
                i++;
                H += Item.GetBaseItem().Height;

            }
        }
        return H;
    }
    
    public boolean cIsItemOnXY(int X, int Y)
    {
        Iterator reader = this.FloorItems.iterator();
        while(reader.hasNext())
        {
            RoomItem Item = (RoomItem)reader.next();
            if(Item.GetBaseItem().Interactor.equals("gate") && Item.ExtraData.equals("1"))
                return false;
            else if(Item.X == X && Item.Y == Y && !Item.GetBaseItem().CanWalk)
                return true;
            else {
                Map<Integer, AffectedTile> AffectedTiles = GetAffectedTiles(Item.GetBaseItem().Lenght, Item.GetBaseItem().Width, Item.X, Item.Y, Item.Rot);
                Iterator reader2 = AffectedTiles.entrySet().iterator();
                while(reader2.hasNext())
                {
                    AffectedTile Tile = (AffectedTile)(((Map.Entry)reader2.next()).getValue());
                    if(Item.GetBaseItem().Interactor.equals("gate") && Item.ExtraData.equals("1"))
                            return false;
                    else if(X == Tile.X && Y == Tile.Y && !Item.GetBaseItem().CanWalk)
                            return true;
                }
            }
        }
        
        return true;
    }
    
    public Map<Integer, AffectedTile> GetAffectedTiles(int Length, int Width, int PosX, int PosY, int Rotation)
        {
    	int x = 0;
        Map<Integer, AffectedTile> PointList = new HashMap<Integer, AffectedTile>();
        if (Length > 1)
        {
            if (Rotation == 0 || Rotation == 4)
            {
                for (int i = 1; i < Length; i++)
                {
                    PointList.put(x++, new AffectedTile(PosX, PosY + i, i));
                    for (int j = 1; j < Width; j++)
                    {
                        PointList.put(x++, new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
                    }
                }
            }
            else if (Rotation == 2 || Rotation == 6)
            {
                for (int i = 1; i < Length; i++)
                {
                    PointList.put(x++, new AffectedTile(PosX + i, PosY, i));

                    for (int j = 1; j < Width; j++)
                    {
                        PointList.put(x++, new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
                    }
                }
            }
        }

        if (Width > 1)
        {
            if (Rotation == 0 || Rotation == 4)
            {
                for (int i = 1; i < Width; i++)
                {
                    PointList.put(x++, new AffectedTile(PosX + i, PosY, i));

                    for (int j = 1; j < Length; j++)
                    {
                        PointList.put(x++, new AffectedTile(PosX + i, PosY + j, (i < j) ? j : i));
                    }
                }
            }
            else if (Rotation == 2 || Rotation == 6)
            {
                for (int i = 1; i < Width; i++)
                {
                    PointList.put(x++, new AffectedTile(PosX, PosY + i, i));

                    for (int j = 1; j < Length; j++)
                    {
                        PointList.put(x++, new AffectedTile(PosX + j, PosY + i, (i < j) ? j : i));
                    }
                }
            }
        }

        return PointList;
        }
    
    public static List<Room> GetRoomsForUserId(int Id)
    {
        List<Room> myRooms = new ArrayList<Room>();
        Iterator reader = Rooms.entrySet().iterator();
        while (reader.hasNext())
        {
            Room P = (Room)(((Map.Entry)reader.next()).getValue());
            if(P.OwnerId == Id)
            {
                myRooms.add(P);
            }
        }
        
        return myRooms;
    }
    
    public static List<Room> GetRoomsForRoomName(String sName)
    {
        List<Room> myRooms = new ArrayList<Room>();
        Iterator reader = Rooms.entrySet().iterator();
        while (reader.hasNext())
        {
            Room P = (Room)(((Map.Entry)reader.next()).getValue());
            if(P.Name.contains(sName))
            {
                myRooms.add(P);
            }
        }
        
        return myRooms;
    }
    
    public int GetNextSessionId()
    {
    	int i = 0;
    	while (true)
    	{
    		if(!UserHasSessionId(i))
    		{
    			break;
    		} else {
    			i += 1;
    		}
    	}    	
    	return i;
    }
    
    private boolean UserHasSessionId(int SessionId)
    {
    	Iterator reader = this.UserList.iterator();
    	while(reader.hasNext())
    	{
    		Habbo User = (Habbo)reader.next();
    		if(User.SessionId == SessionId)
    			return true;
    	}
    	Iterator reader2 = this.PetList.iterator();
    	while(reader2.hasNext())
    	{
    		Pet User = (Pet)reader2.next();
    		if(User.SessionId == SessionId)
    			return true;
    	}
    	return false;
    }
    
    
    public static List<Room> GetRoomsForCategory(int Id)
    {
        List<Room> myRooms = new ArrayList<Room>();
        Iterator reader = Rooms.entrySet().iterator();
        while (reader.hasNext())
        {
            Room P = (Room)(((Map.Entry)reader.next()).getValue());
            if(Id != -1)
            {
            if(P.Category == Id )
            {
                myRooms.add(P);
            }
            } else
            	myRooms.add(P);
        }
        
        return myRooms;
    }
}