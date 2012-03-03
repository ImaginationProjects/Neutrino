package Requests.Guilds;

import java.util.concurrent.FutureTask;
import neutrino.Environment;
import neutrino.Net.ServerHandler;
import neutrino.PacketsInformation.ServerEvents;
import neutrino.Requests.Handler;
import neutrino.System.ServerMessage;
import neutrino.UserManager.Habbo;
import neutrino.RoomManager.Room;
import neutrino.ItemManager.ItemInformation;
import neutrino.ItemManager.RoomItem;
import neutrino.ItemManager.UserItem;
import neutrino.GuildManager.GuildItem;
import java.util.*;
/**
 *
 * @author Julián
 */
public class PurchaseGuild extends Handler implements Runnable {
    private ServerHandler Client;
    private Environment Server;
    private FutureTask T;
    @Override
    public void Load(ServerHandler Client, Environment Server, FutureTask T) throws Exception
    {
        this.Client = Client;
        this.Server = Server;
        this.T = T;
    }
    
    public void run() {
        try {
        	Habbo User = Client.GetSession();
        	User.UpdateCredits((User.Credits - 10), Client.Socket, Server);
        	// [0][0][0][0][0][0][10][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][0][1][0][0][0][0]
        	String Name = Client.in.readUTF();
        	String Description = Client.in.readUTF();
        	int RoomId = Client.in.readInt();
        	int BG1 = Client.in.readInt();
        	int BG2 = Client.in.readInt();
        	int CountItems = Client.in.readInt();
        	int BaseImage = Client.in.readInt();
        	int BaseColor = Client.in.readInt();
        	int Data3 = Client.in.readInt();
        	int FourthItem_Base = Client.in.readInt();
        	int FourthItem_Color = Client.in.readInt();
        	int FourthItem_Pos = Client.in.readInt();
        	int ThirdItem_Base = Client.in.readInt();
        	int ThirdItem_Color = Client.in.readInt();
        	int ThirdItem_Pos = Client.in.readInt();
        	int SecondItem_Base = Client.in.readInt();
        	int SecondItem_Color = Client.in.readInt();
        	int SecondItem_Pos = Client.in.readInt();
        	int FirstItem_Base = Client.in.readInt();
        	int FirstItem_Color = Client.in.readInt();
        	int FirstItem_Pos = Client.in.readInt();
        	//Server.WriteLine("Name: " + Name + "; Desc: " + Description + "; RoomId: " + RoomId + "; BG1_COLOR: " + GuildItem.GetColorBGForId2(BG1) + "; BG2_COLOR: " + GuildItem.GetColorBGForId3(BG2)  + " BASECOLOR " + GuildItem.GetColorBGForId1(BaseColor) + " BASEIMAGE " + BaseImage + " INT " + Data3+ " BASE " + FourthItem_Base + " COLOR " + GuildItem.GetColorBGForId1(FourthItem_Color)+ " POS " + FourthItem_Pos + " BASE " + ThirdItem_Base + " COLOR " + GuildItem.GetColorBGForId1(ThirdItem_Color)+ " POS " + ThirdItem_Pos+ " BASE " + SecondItem_Base + " COLOR " + GuildItem.GetColorBGForId1(SecondItem_Color)+ " POS " + SecondItem_Pos + " BASE " + FirstItem_Base + " COLOR " + GuildItem.GetColorBGForId1(FirstItem_Color)+ " POS " + FirstItem_Pos);
        	Server.WriteLine(GetStringForItem(BaseImage) + "" + GetStringForItem(BaseColor) + "s" + GetStringForItem(FirstItem_Base)  + GetStringForItem(FirstItem_Color) + GetStringForItem(FirstItem_Pos) + "s" + GetStringForItem(SecondItem_Base)  + GetStringForItem(SecondItem_Color) + GetStringForItem(SecondItem_Pos)+ "s" + GetStringForItem(ThirdItem_Base)  + GetStringForItem(ThirdItem_Color) + GetStringForItem(ThirdItem_Pos)+ "s" + GetStringForItem(FourthItem_Base)  + GetStringForItem(FourthItem_Color) + GetStringForItem(FourthItem_Pos));
        	//" / " + Client.in.readInt() + " / " + Client.in.readUTF() + " / " + Client.in.readUTF() + " / " + Client.in.readUTF());
        } catch (Exception e)
        {
        	
        }
    }
    
    public String GetStringForItem(int I)
    {
    	String IntReturn = "";
    	if(I == 1)
    		IntReturn = "01";
    	else if(I == 2)
    		IntReturn = "02";
    	else if(I == 3)
    		IntReturn = "03";
    	else if(I == 4)
    		IntReturn = "04";
    	else if(I == 5)
    		IntReturn = "05";
    	else if(I == 6)
    		IntReturn = "06";
    	else if(I == 7)
    		IntReturn = "07";
    	else if(I == 8)
    		IntReturn = "08";
    	else if(I == 9)
    		IntReturn = "09";
    	else
    		IntReturn = ((Integer)I).toString();
    	
    	
    	return IntReturn;
    }
}
