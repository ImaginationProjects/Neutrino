package neutrino.RoomManager;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import neutrino.SnowWarManager.SnowWar;
import neutrino.UserManager.*;

public class SnowPathFinder {
	private Point[] HabboMove;

    private int MapSizeX = 0;
    private int MapSizeY = 0;

    private SquareState[][] Squares;

    private Habbo User;
    private SnowWar War;
    public int GoX;
    public int GoY;
    public SnowPathFinder(SnowWar CurrentWar, Habbo User)
    {
        // make a easy acces to alls movements
        HabboMove = new Point[]
                {
                    new Point(0, -1),
                    new Point(0, 1),
                    new Point(1, 0),
                    new Point(-1, 0),
                    new Point(1, -1),
                    new Point(-1, 1),
                    new Point(1, 1),
                    new Point(-1, -1)
                };

        // define some values
        /*MapSizeX = Room.GetModel().MapSizeX;
        MapSizeY = Room.GetModel().MapSizeY;
        Squares = Room.GetModel().Squares;*/

        this.War = CurrentWar;
        this.User = User;
    }

    public List<Coord> MakeFinder()
    {
        List<Coord> NearSquares = new ArrayList<Coord>();
        
        int UserX = User.X;
        int UserY = User.Y;
        
        GoX = User.GoalX;
        GoY = User.GoalY;

        Coord finalCoord = new Coord(-200, -200);
        int Trys = 0;
        while(true)
        {
        	Trys++;
        int StepstoWalk = 10000;
        for(Point MovePoint: HabboMove)
        {
            int newX = MovePoint.x + UserX;
            int newY = MovePoint.y + UserY;

            // is valid?
            //System.out.println("Trying to..." + newX + ";" + newY + "; RESULT: " + (Room.IsItemOnXY(newX, newY) ? "TRUE" : "FALSE"));
           /* if(newX >= 0 && newY >= 0 && MapSizeX > newX && MapSizeY > newY && Squares[newX][newY] == SquareState.WALKABLE && !Room.IsItemOnXY(newX, newY) && !Room.IsUserOnXY(newX, newY))
            {*/
            	Coord gen = new Coord(newX, newY);
                gen.distanceToUserPos = DistanceBeetween(newX, newY, GoX, GoY);

            	//System.out.println("Trying to..." + gen.X + ";" + gen.Y + ";" + gen.distanceToUserPos);
                if(StepstoWalk > gen.distanceToUserPos)
                {
                	StepstoWalk = gen.distanceToUserPos;
                	finalCoord = gen;
                }
            //}
        }
        if(Trys >= 200)
        	return null;
        else if(finalCoord.X == -200 && finalCoord.Y == -200)
        	return null;
        //System.out.println("to: " + finalCoord.X + "; " + finalCoord.Y + "::" + GoX + ";" + GoY + "");
        UserX = finalCoord.X;
        UserY = finalCoord.Y;
        NearSquares.add(finalCoord);
        if(UserX == GoX && UserY == GoY)
        	break;
        }
        
        //OrderByNearly(NearSquares);

        return NearSquares;
    }
    private int DistanceBeetween(int currentX, int currentY, int goX, int goY)
    {
        return Math.abs(currentX - goX) + Math.abs(currentY - goY);
    }

    private void OrderByNearly(List<Coord> Squares)
    {
        Collections.sort(Squares, new Comparator()
                    {
                        @Override
                        public int compare(Object o1, Object o2) {
                        	Coord SquarePath1 = (Coord) o1;
                        	Coord SquarePath2 = (Coord) o2;

                            return SquarePath1.distanceToUserPos - SquarePath2.distanceToUserPos;
                        }
                    }
                );
    }

}
