package neutrino.Network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import neutrino.Environment;
import neutrino.Network.ThreadPool.ThreadPoolManager;
import neutrino.RoomManager.Room;
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
  	  Habbo User = this.GetSession();
  	  if(User != null)
  	  {
  		  Environment.UsersConnected--;
  		  if(User.IsOnRoom)
  		  {
  			  Room RoomData = Room.Rooms.get(User.CurrentRoomId);
  	          RoomData.UserList.remove(User);
  	          RoomData.CurrentUsers--;
  		  }
  		  
  		  Server.WriteLine(User.UserName +" has been disconnected under IP " + User.Authenticator);
  	  }
  }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
    {
  	  ChannelBuffer Buffer = (ChannelBuffer) e.getMessage();
  	  while(Buffer.readableBytes() != 0)
  	  {
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
  	   Server.WriteLine("[MUS]" + Analized + " / LEN: " + Message.readableBytes());
  	   if(Analized.startsWith("UPDATEIP"))
  	   {
  		   int UserId = Integer.parseInt(Analized.split(" ")[1]);
  		   String IP = Analized.split(" ")[2];
  		   Habbo User = Habbo.UsersbyId.get(IP);
  		   String oldIP = User.Authenticator;
  		   User.Authenticator = IP;
  		   Habbo.Users.remove(User);
  		   Habbo.Users.add(User);
  		   Habbo.UsersLogged.remove(oldIP);
  		   Habbo.UsersLogged.put(IP, User);
  	   }
  	  }

}
