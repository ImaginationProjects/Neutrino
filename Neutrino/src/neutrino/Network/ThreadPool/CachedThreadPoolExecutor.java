/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package neutrino.Network.ThreadPool;
import java.util.concurrent.*;

/**
 * This is an advanced {@link java.util.concurrent.Executor} that uses a
 * {@link Thread}-pool. It is about the same as
 * {@link java.util.concurrent.Executors#newCachedThreadPool()} and inherits all
 * its implementation from {@link ThreadPoolExecutor}. The only reason was to
 * make it easier to be managed as component in an IoC-Container such as spring.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
public class CachedThreadPoolExecutor extends ThreadPoolExecutor {

  /**
   * The constructor.
   */
  public CachedThreadPoolExecutor() {

    super(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
  }

  /**
   * The constructor.
   * 
   * @param threadFactory is the {@link ThreadFactory} used to create new
   *        {@link Thread}s.
   */
  public CachedThreadPoolExecutor(int BasicThreads, int MaxValue, long Time, TimeUnit Conf, LinkedBlockingQueue<Runnable> Runner,ThreadFactory threadFactory) {

    super(BasicThreads, MaxValue, 5L, Conf, Runner,
        threadFactory);
  }

}
