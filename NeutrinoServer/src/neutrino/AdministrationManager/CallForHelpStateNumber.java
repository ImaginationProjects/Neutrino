package neutrino.AdministrationManager;

public class CallForHelpStateNumber {
	public static int GetNumber(CallForHelpState e)
	{
		if(e == CallForHelpState.NONE)
			return 0;
		else if(e == CallForHelpState.OPEN)
			return 1;
		else if(e == CallForHelpState.PICKED)
			return 2;
		else if(e == CallForHelpState.DELETED)
			return 2;
		else if(e == CallForHelpState.ABUSIVE)
			return 2;
		else if(e == CallForHelpState.RESOLVED)
			return 2;
		else if(e == CallForHelpState.INVALID)
			return 2;
		
		return 0;
	}
	
	public static CallForHelpState GetState(int Number)
	{
		if(Number == 0)
			return CallForHelpState.NONE;
		else if(Number == 1)
			return CallForHelpState.OPEN;
		else if(Number == 2)
			return CallForHelpState.PICKED;
		else if(Number == 3)
			return CallForHelpState.DELETED;
		else if(Number == 4)
			return CallForHelpState.ABUSIVE;
		else if(Number == 5)
			return CallForHelpState.RESOLVED;
		else if(Number == 6)
			return CallForHelpState.INVALID;
		
		return CallForHelpState.NONE;
	}
}
