
package neutrino.Network.ThreadPool;
import java.util.concurrent.*;
        public class PriorityThreadFactory implements ThreadFactory
        {
                /**
                 * Priority of new threads
                 */
                private int priority;
                private String name;
                private int threadNumber = 0;
                private ThreadGroup             group;
                public PriorityThreadFactory(String name, int prio)
                {
                        this.priority = prio;
                        this.name = name;
                        group = new ThreadGroup(this.name);
                }

                /**
                 * {@inheritDoc}
                 */
                @Override
                public Thread newThread(Runnable r)
                {
                        Thread t = new Thread(group, r);
                        t.setName(name + "-" + threadNumber);
                        t.setPriority(priority);
                        //t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
                        return t;
                }
        }