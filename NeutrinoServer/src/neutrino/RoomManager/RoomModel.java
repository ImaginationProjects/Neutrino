/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.RoomManager;
import java.util.*;
import java.sql.*;
import neutrino.Environment;
/**
 *
 * @author Julián
 */
public class RoomModel {
    public final int OPEN = 0;
    public final int CLOSED = 1;

    public String Id;

    public int DoorX;
    public int DoorY;
    public int DoorZ;
    public int DoorDir;
    public boolean ClubOnly;
    public int MaxUsers;

    public String Map;
    public String SerializeMap = "";
    public String SerializeRelativeMap = "";
    public int MapSizeX;
    public int MapSizeY;
    public int MapSize;
    public List<String> Lines;

    public int[][] SqState;
    public double[][] SqFloorHeight;
    public SquareState[][] Squares;
    public static Map<String, RoomModel> Models;
    private static Environment Server;
    public static void Init(Environment cServer) throws Exception
    {
        Server = cServer;
        Models = new HashMap<String, RoomModel>();
        ResultSet Model = Server.GetDatabase().executeQuery("SELECT * FROM rooms_models");
        while(Model.next())
        {
            RoomModel M = new RoomModel(Model.getString("model_name"), Model.getInt("door_x"), Model.getInt("door_y"), Model.getInt("door_z"), Model.getInt("door_dir"), Model.getString("heightmap"), Model.getBoolean("club_only"), Model.getInt("limit_users"));
            Models.put(M.Id, M);
        }
        Server.WriteLine("Loaded " + Models.size() + " model(s).");
    }
    
    public static RoomModel GetModelByName(String Id)
    {
        return (RoomModel)Models.get(Id);
    }
    
    private static boolean isNumeric(String cadena){
        try
        {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public RoomModel(String Id, int DoorX, int DoorY, int DoorZ, int DoorDir, String Map, boolean ClubOnly, int MaxUsers)
    {
        this.Id = Id;

        this.DoorX = (DoorX - 1);
        this.DoorY = (DoorY);
        this.DoorZ = DoorZ;
        this.DoorDir = DoorDir;
        this.ClubOnly = ClubOnly;
        this.MaxUsers = MaxUsers;

        this.Map = Map.toLowerCase();

        String[] tmpHeightmap = this.Map.split(""+(char)13);

        this.MapSizeX = tmpHeightmap[0].length();
        this.MapSizeY = tmpHeightmap.length;
        this.Lines = new ArrayList<String>();
        Lines = new ArrayList(Arrays.asList(this.Map.split(""+(char)13)));

        this.SqState = new int[MapSizeX][MapSizeY];
        this.SqFloorHeight = new double[MapSizeX][MapSizeY];
        this.Squares = new SquareState[MapSizeX][MapSizeY];
        

        for (int y = 0; y < MapSizeY; y++)
        {
            if (y > 0)
            {
                tmpHeightmap[y] = tmpHeightmap[y].substring(1);
            }

            for (int x = 0; x < MapSizeX; x++)
            {
                String Square = tmpHeightmap[y].substring(x,x + 1).trim().toLowerCase();

                if (Square.equals("x"))
                {
                	Squares[x][y] = SquareState.UNWALKABLE;
                    SqState[x][y] = CLOSED;
                }
                else if(isNumeric(Square))
                {
                	Squares[x][y] = SquareState.WALKABLE;
                    SqState[x][y] = OPEN;
                    SqFloorHeight[x][y] = Double.parseDouble(Square);
                    MapSize++;
                }

                if (this.DoorX == x && this.DoorY == y)
                {
                	Squares[x][y] = SquareState.WALKABLE;
                    SerializeRelativeMap += (int)DoorZ + "";
                }
                else
                {
                    if(Square.isEmpty() || Square == null)
                    {
                        continue;
                    }
                    SerializeRelativeMap += Square;
                }
            }
            SerializeRelativeMap += (char)13;
        }

        for(String MapLine: this.Map.split("\r\n"))
        {
            if(MapLine.isEmpty() || MapLine == null)
            {
                continue;
            }
            SerializeMap += MapLine + (char)13;
        }

    }   
}
