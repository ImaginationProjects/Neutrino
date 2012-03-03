package neutrino.Network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Iterator;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import neutrino.Environment;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.RoomManager.Room;
import neutrino.SQL.DatabaseManager;
import neutrino.UserManager.Habbo;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class MUSServerHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger.getLogger(
            ServerHandler.class.getName());

    private Environment Server;
    public DataInputStream inreader;
    public ChannelBuffer in;
    public MUSServerHandler(Environment Env)
    {
        this.Server = Env;
    }
    private final AtomicLong transferredBytes = new AtomicLong();
    public Channel Socket;

    public long getTransferredBytes() {
        return transferredBytes.get();
    }
    
    public Habbo GetSession()
    {
  	  try {
        return Habbo.GetDataFromIP(Server.GetIPFromSocket(Socket));      
  	  } catch (Exception e) {
  		  return null;
  	  }
    }
    
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
    {
        this.Socket = e.getChannel();
    }
    
    @Override
  public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
  {
  }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
    {
  	  ChannelBuffer Buffer = (ChannelBuffer) e.getMessage();
  	  while(Buffer.readableBytes() != 0)
  	  {
  		 String IP = Server.GetIPFromSocket(Socket);
  		 if(IP.equals("176.31.111.150") || IP.equals("::1") || IP.equals("127.0.0.1") || IP.equals("localhost") || IP.equals("188.165.213.98"))
  			 this.ReadMessage(Buffer.readBytes(Buffer.readableBytes()), e);
  	  }
  	  
    }
    
    public void ReadMessage(ChannelBuffer Message, MessageEvent e) throws Exception
    {
  	  String Analized = "<No data lenght>";
  	  if(Message.readableBytes() > 0)
  	  {
  		  Analized = "";
  		  Message.markReaderIndex();
  		  while(Message.readableBytes() != 0)
  		  {
  			  byte[] b = new byte[] { Message.readByte() };
  			  Analized += (new String(b, "iso-8859-1")).toString();
  		  }
      	  Message.resetReaderIndex();
       }
  	   String[] Params = Analized.split(" ");
  	   Server.WriteLine("[" + Server.GetIPFromSocket(Socket) + "] [MUS] " + Analized + " / LEN: " + Message.readableBytes());
  	   if(Analized.startsWith("UPDATEIP"))
  	   {
  		   //Habbo.InitUsers(Server);
  		   int UserId = Integer.parseInt(Analized.split(" ")[1]);
  		   String IpInformation = Analized.split(" ")[2];
  		   Habbo User = Habbo.UsersbyId.get(UserId);
  		   if(User == null)
  			   return;
  		   User.Authenticator = IpInformation;
  		   Habbo.UsersbyId.remove(UserId);
  		   Habbo.UsersbyId.put(UserId, User);
  		   
  		   //Server.WriteLine(User.UserName + " : " + IpInformation);
  	   } else if(Params[0].equals("AlertToUser"))
  	   {
  		   //Habbo.InitUsers(Server);
  		   String Name = Analized.split(" ")[1];
  		   Habbo User = Habbo.GetUserForName(Name);
  		   if(User == null)
  			   return;
  		   String Alert = Analized.replace("AlertToUser " + Name + " ", "");
  		   if(User.IsOnline)
  			   User.SendAlert(Alert);  		   
  		   //Server.WriteLine(User.UserName + " : " + IpInformation);
  	   } else if(Params[0].equals("KickUser"))
  	   {
  		   //Habbo.InitUsers(Server);
  		   int Name = Integer.parseInt(Params[1]);
  		   Habbo User = Habbo.UsersbyId.get(Name);
  		   if(User == null)
  			   return;
  		   String Alert = Analized.replace("KickUser " + Name + " ", "");
  		   if(User.IsOnline)
  			   User.KickMe(Alert);  		   
  		   //Server.WriteLine(User.UserName + " : " + IpInformation);
  	   } else if(Params[0].equals("AlertToUserWithURL"))
  	   {
  		   //Habbo.InitUsers(Server);
  		   String Name = Analized.split(" ")[1];
  		   Habbo User = Habbo.GetUserForName(Name);
  		   if(User == null)
  			   return;
  		   String Url = Analized.split(" ")[2];
  		   String Alert = Analized.replace("AlertToUserWithURL " + Name + " " + Url + " ", "");
  		   if(User.IsOnline)
  			   User.SendAlert(Alert, Url);  		   
  		   //Server.WriteLine(User.UserName + " : " + IpInformation);
  	   } else if(Params[0].startsWith("RemoveUser"))
	   {
		   Habbo ToBan = Habbo.UsersbyId.get(Integer.parseInt(Params[1]));
		   if(ToBan != null && ToBan.IsOnline)
		   {
			   ToBan.RankLevel = 0;
			   ToBan.SendAlert("Has sido banead@");
			   Thread.sleep(5000);
			   ToBan.CurrentSocket.close();
		   }
	   } else if(Params[0].startsWith("ValidUser"))
	   {
		   Habbo ToBan = Habbo.UsersbyId.get(Integer.parseInt(Params[1]));
		   if(ToBan != null)
		   {
			   ToBan.RankLevel = 1;
		   }
	   }  else if(Analized.startsWith("UPDATEUSERS"))
  	   {
  		   if(DatabaseManager.SQLs.size() > 0)
 		   {
  			   Iterator reader = DatabaseManager.SQLs.iterator();
 			   while(reader.hasNext())
 			   {
 				   String SQL = (String)reader.next();
 			 	   Server.GetDatabase().executeUpdates(SQL);
 		 	   }
 		 	   DatabaseManager.SQLs.clear();
 		   }
  		   String Name = Params[1];
  		   Habbo.CreateUser(Name);
  	   }
  	  }

}
