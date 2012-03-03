/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.System;
import neutrino.AdministrationManager.CallForHelpState;
import neutrino.AdministrationManager.CallForHelpStateNumber;
import neutrino.UserManager.DanceNumber;
import neutrino.UserManager.Dances;
import neutrino.UserManager.EffectNumber;
import neutrino.UserManager.Effects;
import neutrino.UserManager.SmileNumber;
import neutrino.UserManager.SmileStates;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
public class ServerMessage {
    public ChannelBuffer buffer;
    public ChannelBufferOutputStream out;
    private int cHeader; // current header lol
    String FinalPacket;
    public ServerMessage(int Header) throws Exception
    {
        cHeader = Header;
        FinalPacket = "<id> " + Header;
        buffer = ChannelBuffers.buffer(20000);
        out = new ChannelBufferOutputStream(buffer);
        out.writeInt(0); // Lenght (to update)
        out.writeShort(Header);
    }
    
    public void writeChars(byte[] c) throws Exception
    {
    	out.write(c);
    }
    
    public void writePacket(String S) throws Exception
    {
    	S = S.replace("[0]", (char)0 + "");
    	S = S.replace("[1]", (char)1 + "");
    	S = S.replace("[2]", (char)2 + "");
    	S = S.replace("[3]", (char)3 + "");
    	S = S.replace("[4]", (char)4 + "");
    	S = S.replace("[5]", (char)5 + "");
    	S = S.replace("[6]", (char)6 + "");
    	S = S.replace("[7]", (char)7 + "");
    	S = S.replace("[8]", (char)8 + "");
    	S = S.replace("[9]", (char)9 + "");
    	S = S.replace("[10]", (char)10 + "");
    	S = S.replace("[11]", (char)11 + "");
    	S = S.replace("[12]", (char)12 + "");
    	S = S.replace("[13]", (char)13 + "");
    	S = S.replace("[14]", (char)14 + "");
    	S = S.replace("[15]", (char)15 + "");
    	writeChars(S.getBytes());
    }
    
    public void writeInt(int i) throws Exception
    {
        out.writeInt(i);
        FinalPacket += " <int> " + i;
    }
    
    public void writeInt(Effects o) throws Exception
    {
    	int i = EffectNumber.GetEffectNum(o);
        out.writeInt(i);
        FinalPacket += " <int> " + i;
    }
    
    public void writeInt(Dances o) throws Exception
    {
    	int i = DanceNumber.GetDanceNum(o);
        out.writeInt(i);
        FinalPacket += " <int> " + i;
    }
    
    public void writeInt(SmileStates o) throws Exception
    {
    	int i = SmileNumber.GetSmileNum(o);
        out.writeInt(i);
        FinalPacket += " <int> " + i;
    }
    
    public void writeInt(CallForHelpState o) throws Exception
    {
    	int i = CallForHelpStateNumber.GetNumber(o);
        out.writeInt(i);
        FinalPacket += " <int> " + i;
    }
    
    public void writeShort(short i) throws Exception
    {
        out.writeShort(i);
        FinalPacket += " <shoor> " + i;
    }
    
    public void writeUTF(String i) throws Exception
    {
        out.writeUTF(i);
        FinalPacket += " <utf> " + i;
    }
    
    public void writeBoolean(boolean i) throws Exception
    {
        out.writeBoolean(i);
        FinalPacket += " <bool> " + i;
    }
    
    public void Send(Channel Socket) throws Exception
    {
        buffer.setInt(0, buffer.writerIndex() - 4);
        Socket.write(buffer);
        System.out.println("Sended -> [" + cHeader +"]: " + FinalPacket);
    }
}
