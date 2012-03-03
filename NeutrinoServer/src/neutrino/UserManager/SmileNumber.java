package neutrino.UserManager;

public class SmileNumber {
	public static int GetSmileNum(SmileStates smile)
	{
		if (smile == SmileStates.SMILE)
	            return 1;
	   	 
	   	if (smile == SmileStates.ANGRY)
            return 2;
	   	
	   	if (smile == SmileStates.SAD)
            return 3;
	   	
	   	if (smile == SmileStates.SHOCKED)
            return 4;
	   	
	   	if (smile == SmileStates.NONE)
            return 0;
	   	 
	   	return 0;
    }
}
