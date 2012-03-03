/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.System;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
public class ServerMessage {
    public ChannelBuffer buffer;
    public ChannelBufferOutputStream out;
    private int cHeader; // current header lol
        
    public ServerMessage(int Header) throws Exception
    {
        cHeader = Header;
        buffer = ChannelBuffers.buffer(20000);
        out = new ChannelBufferOutputStream(buffer);
        out.writeInt(0); // Lenght (to update)
        out.writeShort(Header);
    }
    
    public void writeChars(byte[] c) throws Exception
    {
    	out.write(c);
    }
    
    public void writeInt(int i) throws Exception
    {
        out.writeInt(i);
    }
    
    public void writeShort(short i) throws Exception
    {
        out.writeShort(i);
    }
    
    public void writeUTF(String i) throws Exception
    {
        out.writeUTF(i);
    }
    
    public void writeBoolean(boolean i) throws Exception
    {
        out.writeBoolean(i);
    }
    
    public void Send(Channel Socket) throws Exception
    {
        buffer.setInt(0, buffer.writerIndex() - 4);
        Socket.write(buffer);
        System.out.println("Sended -> [" + cHeader +"]");
    }
}
