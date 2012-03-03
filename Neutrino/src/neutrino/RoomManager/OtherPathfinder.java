/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.RoomManager;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Julián
 */
public class OtherPathfinder {
    public List<Coord> GetPath(int ActualX, int ActualY, int GoalX, int GoalY)
        {
            /*if(!IsValidTile(GoalX, GoalY))
                return null;
             */
            int UserX = ActualX;
            int UserY = ActualY;

            List<Coord> Path = new ArrayList<Coord>();

            for (int i = 0; i < 20000; i++)
            {
                Coord Next = CalculateNextCoord(UserX, UserY, GoalX, GoalY);
                UserX = Next.X;
                UserY = Next.Y;

                if (Next.X == GoalX && Next.Y == GoalY || Next.X == -500 && Next.Y == -500)
                {
                    Path.add(Next);
                    i = 50000;
                }
                else
                {
                    Path.add(Next);
                }
            }

            return Path;

        }

        public Coord CalculateNextCoord(int UserX, int UserY, int GoalX, int GoalY)
        {
            Coord Next = new Coord(-1, -1);
            if (UserX > GoalX && UserY > GoalY && IsValidTile(UserX - 1, UserY - 1))
                Next = new Coord(UserX - 1, UserY - 1);
            else if (UserX < GoalX && UserY < GoalY && IsValidTile(UserX + 1, UserY + 1))
                Next = new Coord(UserX + 1, UserY + 1);
            else if (UserX > GoalX && UserY < GoalY && IsValidTile(UserX - 1, UserY + 1))
                Next = new Coord(UserX - 1, UserY + 1);
            else if (UserX < GoalX && UserY > GoalY && IsValidTile(UserX + 1, UserY - 1))
                Next = new Coord(UserX + 1, UserY - 1);
            else if (UserX > GoalX && IsValidTile(UserX - 1, UserY))
                Next = new Coord(UserX - 1, UserY);
            else if (UserX < GoalX && IsValidTile(UserX + 1, UserY))
                Next = new Coord(UserX + 1, UserY);
            else if (UserY < GoalY && IsValidTile(UserX, UserY + 1))
                Next = new Coord(UserX, UserY + 1);
            else if (UserY > GoalY && IsValidTile(UserX, UserY - 1))
                Next = new Coord(UserX, UserY - 1);
            else
                Next = new Coord(-500, -500);

            return Next;
        }
        
        public boolean IsValidTile(int x, int y)
        {
            return true;
        }
    
}
