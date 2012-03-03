/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.Requests;
import java.util.concurrent.FutureTask;

/**
 *
 * @author Julián
 */
import neutrino.Net.ServerHandler;
import neutrino.Environment;
public abstract class Handler {
    public abstract void Load(ServerHandler Main, Environment Server, FutureTask T) throws Exception;
}
