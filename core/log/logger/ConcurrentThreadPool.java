package core.log.logger;

import core.log.conf.reloadable.ReloadableConfiguration;
import core.log.exception.InternalException;
import core.log.logger.resource.LogResource;
import core.log.util.PatternCheck;

import java.util.LinkedList;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 12. 4 ¿ÀÀü 8:28:50
 * Content  :
 * Usage    :
 */
public class ConcurrentThreadPool extends ThreadGroup {
    private boolean isAlive;
    private LinkedList taskQueue;
    private LinkedList threadQueue;
    private int threadID;
    private static int threadPoolID;
    private int defaultNumberThread=10;
    private static ConcurrentThreadPool concurrentThreadPool;
    public static int maxTask=0;
    public static int mt_thread=0;

    /**
     * Creates a new ThreadPool.
     *
     * @param numThreads
     *            The number of threads in the pool.
     */
    private ConcurrentThreadPool() {
        super("ThreadPool-"+(ConcurrentThreadPool.threadPoolID++));
        setDaemon(true);
        isAlive=true;
        taskQueue=new LinkedList();
        threadQueue=new LinkedList();
        for(int i=0; i<defaultNumberThread; i++) {
            PooledThread pooledThread=new PooledThread();
            threadQueue.add(pooledThread);
            pooledThread.start();
        }
    }

    private ConcurrentThreadPool(int initializedThreadCount) {
        super("ThreadPool-"+(ConcurrentThreadPool.threadPoolID++));
        setDaemon(true);
        isAlive=true;
        taskQueue=new LinkedList();
        threadQueue=new LinkedList();
        defaultNumberThread=initializedThreadCount;
        for(int i=0; i<defaultNumberThread; i++) {
            PooledThread pooledThread=new PooledThread();
            threadQueue.add(pooledThread);
            pooledThread.start();
        }
    }

    public static ConcurrentThreadPool getInstance() {
        if(concurrentThreadPool == null)
            return concurrentThreadPool=new ConcurrentThreadPool();
        return concurrentThreadPool;
    }

    public int getCurrentTaskCount() {
        return taskQueue.size();
    }

    public int getCurrentThreadCount() {
        return threadQueue.size();
    }

    public static ConcurrentThreadPool getInstance(int initializedThreadCount) {
        if(concurrentThreadPool == null)
            return concurrentThreadPool=new ConcurrentThreadPool(initializedThreadCount);
        return concurrentThreadPool;
    }

    private int resizeThreadCount(int resizedThreadCount) {
        if(defaultNumberThread>resizedThreadCount) {//decrease
            for(int i=0; i<threadQueue.size(); i++) {
                threadQueue.remove(i);
                if(threadQueue.size() == resizedThreadCount)
                    break;
            }
        } else//increase
            if(resizedThreadCount>defaultNumberThread)
                for(int i=0; i<resizedThreadCount; i++) {
                    PooledThread pooledThread=new PooledThread();
                    threadQueue.add(pooledThread);
                    pooledThread.start();
                }
        return defaultNumberThread=resizedThreadCount;
    }

    public synchronized void addTask(Object obj) {
        taskQueue.add(obj);
        notify();
    }

    private synchronized Object getTask() {
        try {
            while(taskQueue.size() == 0) {
                if(!isAlive)
                    return null;
                wait();
            }
            if(taskQueue.size()>100)//thread increase
                resizeThreadCount(taskQueue.size()/3);
            else
                resizeThreadCount(10);
        } catch(Throwable t) {
            new InternalException(t);
        }
        return taskQueue.removeFirst();
    }

    /**
     * Closes this ThreadPool and returns immediately. All threads are stopped,
     * and any waiting tasks are not executed. Once a ThreadPool is closed, no
     * more tasks can be run on this ThreadPool.
     */
    public synchronized void close() {
        if(isAlive) {
            isAlive=false;
            taskQueue.clear();
            interrupt();
        }
    }

    /**
     * Closes this ThreadPool and waits for all running threads to finish. Any
     * waiting tasks are executed.
     */
    public void join() {
        // notify all waiting threads that this ThreadPool is no
        // longer alive
        synchronized(this) {
            isAlive=false;
            notifyAll();
        }

        // wait for all threads to finish
        Thread[] threads=new Thread[activeCount()];
        int count=enumerate(threads);
        for(int i=0; i<count; i++) {
            try {
                threads[i].join();
            } catch(InterruptedException ex) {
            }
        }
    }

    /**
     * A PooledThread is a Thread in a ThreadPool group, designed to run tasks
     * (Runnables).
     */
    private class PooledThread extends Thread {
        public PooledThread() {
            super(ConcurrentThreadPool.this, "PooledThread-"+(threadID++));
        }

        public void run() {
            while(!isInterrupted()) {
                // get a task to run
                Object task=null;
                task=getTask();

                // if getTask() returned null or was interrupted,
                // close this thread by returning.
                if(task == null) {
                    return;
                }

                // run the task, and eat any exceptions it throws
                try {
                    if(new PatternCheck().doLog(ReloadableConfiguration.getInstance().getViewAppoint(),
                                                ((LogResource) task).getThrowable()))
                        SL.getInstance().logSql(((LogResource) task));
                } catch(Throwable t) {
                    if(((LogResource) task).isSelect())
                        new InternalException("LoggingDaoR Error!", t);
                    else
                        new InternalException("LoggingDaoCUD Error!", t);
                }
                ;
            }
        }
    }
}
