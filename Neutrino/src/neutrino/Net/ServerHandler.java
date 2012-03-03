/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.Net;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import neutrino.Net.ThreadPool.ThreadPoolManager;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import neutrino.RoomManager.Room;
import neutrino.UserManager.Habbo;
/**
 *
 * @author Julián
 */
import neutrino.Environment;
public class ServerHandler extends SimpleChannelUpstreamHandler {  
      private static final Logger logger = Logger.getLogger(
              ServerHandler.class.getName());

      private Environment Server;
      public DataInputStream in;
      public ServerHandler(Environment Env)
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
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e)
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
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
    {
        try
        {
            ChannelBuffer bufferin = (ChannelBuffer) e.getMessage();
            
            if (bufferin.readableBytes() < 6) // 4 bytes of int32 len + 2 bytes of short header
            {
                Socket.disconnect();
                return;
            }
            
            //Server.WriteLine("Load data... Currents thread on use " + ThreadPoolManager._myGeneralThreads.getPoolSize());
            
            if(bufferin.getByte(0) == 60)
            {
                //if(Crypto == null || Crypto.RC4Decode == null)
                //{
                    byte Bytes[] = "<?xml version=\"1.0\"?>\r\n<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\r\n<cross-domain-policy>\r\n   <allow-access-from domain=\"*\" to-ports=\"1-65535\" />\r\n</cross-domain-policy>\0".getBytes();
                    ChannelBuffer buffer2 = ChannelBuffers.wrappedBuffer(Bytes);
                    ChannelFuture future = Socket.write(buffer2);
                    future.addListener(ChannelFutureListener.CLOSE);
                    return;
                //}
            }
            
            List<DataInputStream> Messages = new ArrayList<DataInputStream>();
            
            do
            {
                int readerindex = bufferin.readerIndex();
                byte[] byt = new byte[4];
                bufferin.readBytes(byt);
                /*if(Crypto != null && Crypto.RC4Decode != null)
                {
                    Crypto.RC4Decode.parse(byt);
                }*/
                
                // int32
                int size = ((byt[0] & 0xff) << 24) + ((byt[1] & 0xff) << 16) + ((byt[2] & 0xff) << 8) + (byt[3] & 0xff);
                
                if (size < 2 || size > 1024 * 5)
                {
                    Server.WriteLine("Bad Size Packet!! "+size);
                    Socket.disconnect();
                    return;
                }

                if (bufferin.readableBytes() < size)
                {
                    bufferin.readerIndex(readerindex);
                    break;
                }

                byt = new byte[size];
                bufferin.readBytes(byt);
                /*if(Crypto != null && Crypto.RC4Decode != null)
                {
                    Crypto.RC4Decode.parse(byt);
                }*/
                
                Messages.add(new DataInputStream(new ByteArrayInputStream(byt)));
            }
            while(bufferin.readableBytes() >= 6);
            
            for(DataInputStream i : Messages)
            {
                int header = i.readShort();
                DataInputStream o;
                o = i;
                i.mark(10000000);
                String Analized = "<no more data readed>";
                try {
                if(o.available() > 0)
                {
                	Analized = "";
                	while(o.available() != 0)
                	{
                		byte[] b = new byte[] { o.readByte() };
                		Analized += (new String(b, "UTF-8")).toString();
                	}
                	
                	Analized = Analized.replace((char)0 + "", "[0]");
                	Analized = Analized.replace((char)1 + "", "[1]");
                	Analized = Analized.replace((char)2 + "", "[2]");
                	Analized = Analized.replace((char)10 + "", "[10]");
                	Analized = Analized.replace((char)13 + "", "[13]");
                }
                } catch (Exception u)
                {
                	Analized = "<can't read>";
                }
                i.reset();
                    /*Analized = "";
                    while(i.available() > 0)
                    {
                        try {
                    if(i.readUTF() != null && i.readUTF() != "")
                        Analized += " " + i.available() +" <string>" + i.readUTF();
                    else
                        Analized += "have no string or string('')";
                        } catch (Exception u) {
                        Analized += i.available();
                        }
                    }*/
                    /*<while(i.read()!= -1 && i.available() > 0)
                    {
                        try {
                        if(i.read()!= -1 && !"".equals(i.readUTF()))
                            Analized += " <string> " + i.readUTF();
                        else if(i.read()!= -1 && i.readInt() >= 0)
                            Analized += " <int> " + i.readInt();
                        else if(i.read()!= -1 && i.readBoolean())
                            Analized += " <boolean> true";
                        else if(i.read() != -1)
                            Analized += " <boolean> false";
                        } catch (Exception u)
                        {
                            Analized += " <dato> " + i.read();
                        }
                    }*/
               // }
                
                //Server.WriteLine("Received Packet from IP: (" + Server.GetIPFromSocket(Socket) + ")");
                try
                {
                    if(Environment.Request[header]!=null)
                    {
                        Server.WriteLine("Received <- [" + header + "]"  + Analized + " / LEN: " + i.available());
                    
                        in = i;
                        this.GetSession().CurrentSocket = this.Socket;
                        FutureTask T = new FutureTask((Runnable)Environment.Request[header], null);
                        Environment.Request[header].Load(this, Server, T);
                        ThreadPoolManager._myPacketThreads.execute(T);
                    }
                    else
                    {
                        Server.WriteLine("Unassigned <- [" + header + "]" + Analized + " / LEN: " + i.available());
                    }

                }
                catch(Exception ex)
                {
                    Server.WriteLine(ex);
                }
                
                i.close();
            }
        }
        catch (Exception ex)
        {
            Server.WriteLine(ex);
        }
    }
  
      @Override
      public void exceptionCaught(
              ChannelHandlerContext ctx, ExceptionEvent e) {
          // Close the connection when an exception is raised.
          logger.log(
                  Level.WARNING,
                  "Unexpected exception from downstream.",
                  e.getCause());
          e.getChannel().close();
      }
  }
