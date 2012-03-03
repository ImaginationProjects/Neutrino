package neutrino.UserManager;

public class DanceNumber {	
	public static int GetDanceNum(Dances dance)
	{
	   	 if (dance == Dances.DANCE)
	            return 1;
	   	 
	   	if (dance == Dances.POGO_MOGO)
            return 2;
	   	
	   	if (dance == Dances.DUCK_FUNK)
            return 3;
	   	
	   	if (dance == Dances.THE_ROLLIE)
            return 4;
	   	
	   	if (dance == Dances.NONE)
            return 0;
	   	 
	   	return 0;
    }
	
	public static Dances GetDanceFromNum(int d)
	{
		if(d == 0)
			return Dances.DANCE;
		
	    if(d == 1)
		    return Dances.DANCE;
	    
	    if(d == 2)
			return Dances.POGO_MOGO;
		
	    if(d == 3)
		    return Dances.DUCK_FUNK;
	    
	    if(d == 4)
			return Dances.THE_ROLLIE;
	   	 
	   	return Dances.NONE;
    }
}
