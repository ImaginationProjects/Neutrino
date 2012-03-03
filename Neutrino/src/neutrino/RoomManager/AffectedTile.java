/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.RoomManager;

/**
 *
 * @author cb2z8eb
 */
public class AffectedTile
    {
        int mX;
        int mY;
        int mI;

        public AffectedTile(int x, int y, int i)
        {
            mX = x;
            mY = y;
            mI = i;
        }

        public int X()
        {
            return mX;
        }

        public int Y()
        {
            return mY;
        }

        public int I()
        {
            return mI;
        }
    }
