/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino;

/**
 *
 * @author Julián
 */
public class Neutrino {

    /**
     * @param args the command line arguments
     */
    private static Environment Env;
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        Env = new Environment();
        
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            { 
               try { Env.CloseEmulation(); } catch (Exception e) { }
            }
        });
        
        Env.init();
    }
}
