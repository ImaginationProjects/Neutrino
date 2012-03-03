package neutrino.Brain;

import java.util.*;
import neutrino.System.ServerMessage;
import org.jboss.netty.channel.Channel;

public class Neuron {
	// All neuron logic? to get information
	public String Identificator;
	public List<String> InformationFromUTF;
	public List<Integer> InformationFromInt;
	public List<ServerMessage> InformationFromServerMessages;
	public ServerMessage ReadedServerMessage;
	public List<Channel> InformationFromChannel;
	public Neuron()
	{
		this.Identificator = "NEURON:" + MotherNeuron.NeuronsByActivity.size();
		this.InformationFromUTF = new ArrayList<String>();
		this.InformationFromInt = new ArrayList<Integer>();
		this.InformationFromServerMessages = new ArrayList<ServerMessage>();
		this.InformationFromChannel = new ArrayList<Channel>();
		this.ReadedServerMessage = null;
	}
	
	public Neuron(String SERVERMESSAGECODE, ServerMessage Value)
	{
		this.Identificator = SERVERMESSAGECODE;
		this.InformationFromUTF = new ArrayList<String>();
		this.InformationFromInt = new ArrayList<Integer>();
		this.InformationFromServerMessages = new ArrayList<ServerMessage>();
		this.InformationFromChannel = new ArrayList<Channel>();
		this.ReadedServerMessage = Value;
	}
	
	public void SendMyPacketsAndClean(Channel Socket) throws Exception
	{
		Iterator reader = InformationFromServerMessages.iterator();
		while(reader.hasNext())
		{
			ServerMessage S = (ServerMessage)reader.next();
			S.Send(Socket);
		}
	}
	
	public void GiveAName(String SERVERMESSAGECODE)
	{
		this.Identificator = SERVERMESSAGECODE;
	}
	
	public void GiveAMessage(ServerMessage Value)
	{
		this.ReadedServerMessage = Value;
	}
	
	public void TeachAUTF(String UTF)
	{
		this.InformationFromUTF.add(UTF);
	}
	
	public void TeachAInt(int Int)
	{
		this.InformationFromInt.add(Int);
	}
	
	public void TeachAMessage(ServerMessage Message)
	{
		this.InformationFromServerMessages.add(Message);
	}
	
	public void TeachAChannel(Channel Socket)
	{
		this.InformationFromChannel.add(Socket);
	}
}
