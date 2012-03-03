/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.MessageEvents;
import java.util.concurrent.FutureTask;

/**
 *
 * @author Julián
 */
import neutrino.Network.ServerHandler;
import neutrino.Environment;

public abstract class MessageEvent {
    public abstract void Load(ServerHandler Main, Environment Server, FutureTask T) throws Exception;
}
