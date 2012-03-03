/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.SQL;


import neutrino.Environment;
import java.sql.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.jolbox.bonecp.*;
import com.jolbox.bonecp.hooks.*;

/**
 *
 * @author Julián
 */
public class Database {
    private static Connection ActualConn;
    public static BoneCP ActualDBManager;
    private static Environment Server;
    public static void WriteStatics() throws Exception
    {
    	 Server.WriteLine("Database pooling have " + ActualDBManager.getTotalCreatedConnections() + " created, " + ActualDBManager.getTotalLeased() + " leased and " + ActualDBManager.getTotalFree() + " free connections.");
    }
    
    public static String Readtatics() throws Exception
    {
    	 return ("Database pooling have " + ActualDBManager.getTotalCreatedConnections() + " created, " + ActualDBManager.getTotalLeased() + " leased and " + ActualDBManager.getTotalFree() + " free connections.");
    }
    
    public static void sendinit() throws Exception
    {
    	Server.WriteLine("Database pooling have " + ActualDBManager.getTotalCreatedConnections() + " created, " + ActualDBManager.getTotalLeased() + " leased and " + ActualDBManager.getTotalFree() + " free connections.");
    }
    
    public static void Init (String Host, String User, String Password, String DB, Environment cServer) throws Exception
    {
    	Server = cServer;
    	try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e)
        {
            Server.WriteLine(e);                    
        }
    	BoneCPConfig config = new BoneCPConfig();
    	config.setJdbcUrl("jdbc:mysql://" + Host + "/" + DB);
    	config.setUsername(User);
    	config.setPassword(Password);
    	config.setMinConnectionsPerPartition(100);
    	config.setMaxConnectionsPerPartition(500);
    	config.setPartitionCount(2);
    	config.setCloseConnectionWatch(true);
    	config.setLazyInit(true);
    	
    	BoneCP connectionPool = new BoneCP(config);
    	ActualDBManager = connectionPool;
    	/*Logger.getLogger("com.mchange.v2").setLevel(Level.OFF);
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e)
        {
            Server.WriteLine(e);                    
        }
        ActualDBManager = new ComboPooledDataSource();
        ActualDBManager.setDriverClass("com.mysql.jdbc.Driver");
        ActualDBManager.setJdbcUrl("jdbc:mysql://" + Host + "/" + DB);
        ActualDBManager.setUser(User);
        ActualDBManager.setPassword(Password);
        ActualDBManager.setMaxPoolSize(500);
        ActualDBManager.setMinPoolSize(5);
        ActualDBManager.setNumHelperThreads(20); // current threads*/
        //Connection conn = DriverManager.getConnection("jdbc:mysql://" + Host + "/" + DB,User,Password);
        //ActualConn = conn;
    }
    
    public static ResultSet executeQuery(String Query) throws Exception
    {
        Statement s = ActualDBManager.getConnection().createStatement();
        ResultSet r = s.executeQuery(Query);
        //ActualDBManager.getConnection().close();
        return r;        
    }
    
    public static void executeUpdate(String Query) throws Exception
    {
    	DatabaseManager.SQLs.add(Query);
    }
    
    public static void executeUpdates(String Query) throws Exception
    {
        Statement s = ActualDBManager.getConnection().createStatement();
        s.executeUpdate(Query);
        //ActualDBManager.getConnection().close();
    }
}
