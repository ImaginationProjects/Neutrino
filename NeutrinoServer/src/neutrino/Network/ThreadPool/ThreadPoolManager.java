/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.Network.ThreadPool;
import neutrino.Environment;
import java.util.concurrent.*;

import neutrino.Network.ThreadPool.PriorityThreadFactory;
/**
 *
 * @author Julián
 */
public class ThreadPoolManager {
	// Neutrino Control System: Luz; #BRAIN ARCHIVE#
    public static CachedThreadPoolExecutor _myGeneralThreads;
    public static CachedThreadPoolExecutor _myPacketThreads;
    public static ThreadPoolExecutor _myInstantThreads;
    public static ScheduledThreadPoolExecutor _myAIPoolingThreads;
    public static ScheduledThreadPoolExecutor _EffectPoolingThreads;
    public static ScheduledThreadPoolExecutor _myTimerPoolingThreads;
    public static void WriteStatics(Environment Server)
    {
    	Server.WriteLine("Current thread pooling system have " + (_myAIPoolingThreads.getPoolSize() + _EffectPoolingThreads.getPoolSize() + _myTimerPoolingThreads.getPoolSize()) + " scheduled, " + (_myGeneralThreads.getPoolSize() + _myPacketThreads.getPoolSize()) + " long (cached) and " + _myInstantThreads.getPoolSize() + " instant threads on 6 thread pools.");
    }
    
    public static String ReadStatics()
    {
    	return ("Current thread pooling system have " + (_myAIPoolingThreads.getPoolSize() + _EffectPoolingThreads.getPoolSize() + _myTimerPoolingThreads.getPoolSize()) + " scheduled, " + (_myGeneralThreads.getPoolSize() + _myPacketThreads.getPoolSize()) + " long (cached) and " + _myInstantThreads.getPoolSize() + " instant threads on 6 thread pools.");
    }
    
    public static void Init(Environment Server)
    {
        Server.WriteLine("Starting thread pooling...");
        // general threadpool system
        _myGeneralThreads = new CachedThreadPoolExecutor(ThreadConfigurator.BASIC_THREADS,
                ThreadConfigurator.MAX_THREADS,
                ThreadConfigurator.MAX_TIME_WITHOUT_WARN,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new PriorityThreadFactory("GENERAL POOLING", Thread.MIN_PRIORITY));
        _myGeneralThreads.prestartAllCoreThreads();
        
        _myPacketThreads = new CachedThreadPoolExecutor(50, 2000, ThreadConfigurator.MAX_TIME_WITHOUT_WARN, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                new PriorityThreadFactory("PACKET POOLING", Thread.MIN_PRIORITY));
        _myPacketThreads.prestartAllCoreThreads();
        
        // long threadpool system
        _myInstantThreads = new ThreadPoolExecutor(ThreadConfigurator.BASIC_THREADS,
                ThreadConfigurator.MAX_THREADS,
                ThreadConfigurator.MAX_TIME_WITHOUT_WARN,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new PriorityThreadFactory("LONG THREADS POOLING", Thread.MIN_PRIORITY));
        _myInstantThreads.prestartAllCoreThreads();
        
        // Ai (bots and pets and some shit like this) threading pool
        _myAIPoolingThreads = new ScheduledThreadPoolExecutor(ThreadConfigurator.MAX_THREADS, 
                new PriorityThreadFactory("AI Threads", Thread.MIN_PRIORITY));
        
        // Efects and other periodical shits like this
        _EffectPoolingThreads = new ScheduledThreadPoolExecutor(ThreadConfigurator.MAX_THREADS, 
                new PriorityThreadFactory("Effects Threads", Thread.MIN_PRIORITY));
        
        // TimerPooling (general) and some things
        _myTimerPoolingThreads = new ScheduledThreadPoolExecutor(ThreadConfigurator.MAX_THREADS, 
                new PriorityThreadFactory("Timer Threads", Thread.MIN_PRIORITY));
        
        Server.WriteLine("Started thread pooling system with " + (_myAIPoolingThreads.getPoolSize() + _EffectPoolingThreads.getPoolSize() + _myTimerPoolingThreads.getPoolSize()) + " scheduled, " + (_myGeneralThreads.getPoolSize() + _myPacketThreads.getPoolSize()) + " long (cached) and " + _myInstantThreads.getPoolSize() + " instant threads on 6 thread pools.");
    }
    
}
