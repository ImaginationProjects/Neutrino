package neutrino.AdministrationManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import neutrino.Environment;

public class ModerationPreset {
	public int Id;
    public String Type;
    public String Message;
    
    public static List<ModerationPreset> Presets;
    public static int LastId;

    public static void Init(Environment Server) throws Exception
    {
        Presets = new ArrayList<ModerationPreset>();
        LastId = 0;
        ResultSet Room = Server.GetDatabase().executeQuery("SELECT * FROM moderation_presets");
        while(Room.next())
        {
        	ModerationPreset C = new ModerationPreset();
            C.Id = Room.getInt("id");
            if(C.Id > LastId)
            	LastId = C.Id;
            C.Type = Room.getString("type");
            C.Message = Room.getString("message");
            Presets.add(C);
        }
        
        Server.WriteLine("Loaded " + Presets.size() + " moderator preset(s).");
    }
    
    public static List<ModerationPreset> GetChatlogsForType(String cType)
    {
    	List<ModerationPreset> Chats = new ArrayList<ModerationPreset>();
    	Iterator reader = Presets.iterator();
    	while(reader.hasNext())
    	{
    		ModerationPreset C = (ModerationPreset)reader.next();
    		if(C.Type.equals(cType))
    		{
    			Chats.add(C);
    		}
    	}
    	return Chats;
    }
}
