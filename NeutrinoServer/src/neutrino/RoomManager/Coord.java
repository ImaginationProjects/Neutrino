/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.RoomManager;

/**
 *
 * @author cb2z8eb
 */
public class Coord
    {
        public int X;
        public int Y;
        
        public int distanceToUserPos;

        public Coord(int x, int y)
        {
            X = x;
            Y = y;
            distanceToUserPos = 1000;
        }
    }
