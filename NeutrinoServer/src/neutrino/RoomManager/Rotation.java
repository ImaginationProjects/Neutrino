/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.RoomManager;

/**
 *
 * @author cb2z8eb
 */
/**
 *
 * @author Julián
 */
public class Rotation {
    public static int Calculate(int X1, int Y1, int X2, int Y2)
        {
            byte Rotation = 0;
            if (X1 > X2 && Y1 > Y2)
                Rotation = 7;
            else if (X1 < X2 && Y1 < Y2)
                Rotation = 3;
            else if (X1 > X2 && Y1 < Y2)
                Rotation = 5;
            else if (X1 < X2 && Y1 > Y2)
                Rotation = 1;
            else if (X1 > X2)
                Rotation = 6;
            else if (X1 < X2)
                Rotation = 2;
            else if (Y1 < Y2)
                Rotation = 4;
            else if (Y1 > Y2)
                Rotation = 0;

            return Rotation;
        }

        public int headRotation(byte headRot, int X, int Y, int toX, int toY)
        {
            if (headRot == 2)
            {
                if (X <= toX && Y < toY)
                    //return 3;
                    return 2;
                else if (X <= toX && Y > toY)
                    return 1;
                else if (X < toX && Y == toY)
                    // return 2;
                    return 2;
            }
            else if (headRot == 4)
            {
                if (X > toX && Y <= toY)
                    return 5;
                else if (X < toX && Y <= toY)
                    return 3;
                else if (X == toX && Y < toY)
                    return 4;
            }
            else if (headRot == 6)
            {
                if (X >= toX && Y > toY)
                    return 7;
                else if (X >= toX && Y < toY)
                    return 5;
                else if (X > toX && Y == toY)
                    return 6;
            }
            else if (headRot == 0)
            {
                if (X > toX && Y >= toY)
                    return 9;
                if (X < toX && Y >= toY)
                    return 1;
                if (X == toX && Y > toY)
                    return 0;
            }
            return headRot;
        }

        public int headRotation2(int oldx, int oldy, int newx, int newy)
        {
            if (oldx == newx)
            {
                if (oldy < newy)
                    return 4;
                else
                    return 0;

            } //Moved Left  
            else if (oldx > newx)
            {
                if (oldy == newy)
                    return 6;
                else if (oldy < newy)
                    return 5;
                else
                    return 7;

            } //Moved Right  
            else if (oldx < newx)
            {
                if (oldy == newy)
                    return 2;
                else if (oldy < newy)
                    return 3;
                else
                    return 1;
            }
            return 0;
        }
    }

