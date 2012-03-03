package neutrino.AdministrationManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import neutrino.Environment;
import neutrino.SQL.Database;
import neutrino.UserManager.Habbo;

public class CallsForHelp {
		public int Id;
		public CallForHelpState State;
		public int Category;
	    public int Timestamp;
	    public int Priority;
	    public int ReporterId;
	    public int ReportedId;
	    public int RoomId;
	    public String Message;
	    public int PickedBy;
	    public int stateint;
	    
	    public static Map<Integer, CallsForHelp> Calls;
	    public static int LastId;

	    public static void Init(Environment Server) throws Exception
	    {
	        Calls = new HashMap<Integer, CallsForHelp>();
	        LastId = 0;
	        ResultSet Room = Server.GetDatabase().executeQuery("SELECT * FROM cfhs");
	        while(Room.next())
	        {
	        	CallsForHelp C = new CallsForHelp();
	            C.Id = Room.getInt("id");
	            if(C.Id > LastId)
	            	LastId = C.Id;
	            C.State = CallForHelpStateNumber.GetState(Room.getInt("state"));
	            C.stateint = Room.getInt("state");
	            C.Category = Room.getInt("category");
	            C.Priority = Room.getInt("priority");
	            C.ReporterId = Room.getInt("reporterid");
	            C.ReportedId = Room.getInt("reportedid");
	            C.RoomId = Room.getInt("roomid");
	            C.Timestamp = Room.getInt("timestamp");
	            C.Message = Room.getString("message");
	            C.PickedBy = Room.getInt("pickedby");
	            Calls.put(C.Id, C);
	        }
	        
	        Server.WriteLine("Loaded " + Calls.size() + " call(s) for help.");
	    }
	    
	    public static CallsForHelp Add(int aState, int aCategory, int aPriority, int aReporterId, int aReportedId, int aRoomId, String aMessage)
	    {
	    	CallsForHelp C = new CallsForHelp();
	    	C.Id = LastId + 1;
	    	LastId++;
	    	C.State = CallForHelpStateNumber.GetState(aState);
            C.Category = aCategory;
            C.Priority = aPriority;
            C.ReporterId = aReporterId;
            C.ReportedId = aReportedId;
            C.RoomId = aRoomId;
            C.Timestamp = Environment.getIntUnixTimestamp();
            C.Message = aMessage;
            C.PickedBy = 0;
            C.stateint = 1;
            Calls.put(C.Id, C);
            return C;
	    }
	    
	    public static List<CallsForHelp> GetNoPickedCalls()
	    {
	    	List<CallsForHelp> h = new ArrayList<CallsForHelp>();
	    	Iterator reader = Calls.entrySet().iterator();
	    	while(reader.hasNext())
	    	{
	    		CallsForHelp C = (CallsForHelp)(((Map.Entry)reader.next()).getValue());
	    		h.add(C);
	    	}
	    	return h;
	    }
}
