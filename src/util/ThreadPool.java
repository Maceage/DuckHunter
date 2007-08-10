package util;

import java.util.LinkedList;

/**
    A thread pool is a group of a limited number of threads that
    are used to execute tasks.
    @author		David Brackeen - New Riders Developing Games In Java (ISBN 1-5927-3005-1)
    @version	August 20, 2003 - http://www.brackeen.com/javagamebook/
*/
public class ThreadPool extends ThreadGroup {

	private transient int threadID;
    private transient final LinkedList taskQueue;
	private transient boolean isAlive;
    private static int threadPoolID;

    /**
        Creates a new ThreadPool.
        @param numThreads The number of threads in the pool.
    */
    public ThreadPool(int numThreads) {
        super("ThreadPool-" + (threadPoolID++));
        setDaemon(true);

        isAlive = true;

        taskQueue = new LinkedList();
        for (int i=0; i<numThreads; i++) {
            new PooledThread().start();
        }
    }


    /**
        Requests a new task to run. This method returns
        immediately, and the task executes on the next available
        idle thread in this ThreadPool.
        <p>Tasks start execution in the order they are received.
        @param task The task to run. If null, no action is taken.
        @throws IllegalStateException if this ThreadPool is
        already closed.
    */
    public synchronized void runTask(final Runnable task) {
        if (!isAlive) {
            throw new IllegalStateException();
        }
        if (task != null) {
            taskQueue.add(task);
            notifyAll();
        }

    }


    protected Runnable getTask()
        throws InterruptedException
    {
    	synchronized(this) {
	        while (taskQueue.size() == 0) {
	            if (!isAlive) {
	                return null;
	            }
	            wait();
	        }
	        return (Runnable)taskQueue.removeFirst();
    	}
    }


    /**
        Closes this ThreadPool and returns immediately. All
        threads are stopped, and any waiting tasks are not
        executed. Once a ThreadPool is closed, no more tasks can
        be run on this ThreadPool.
    */
    public void close() {
    	synchronized(this) {
	        if (isAlive) {
	            isAlive = false;
	            taskQueue.clear();
	            interrupt();
	        }
    	}
    }


    /**
        Closes this ThreadPool and waits for all running threads
        to finish. Any waiting tasks are executed.
    */
    public void join() {
        // notify all waiting threads that this ThreadPool is no
        // longer alive
        synchronized (this) {
            isAlive = false;
            notifyAll();
        }

        // wait for all threads to finish
        final Thread[] threads = new Thread[activeCount()];
        final int count = enumerate(threads);
        for (int i=0; i<count; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException ex) { }
        }
    }



    /**
        Signals that a PooledThread has started. This method
        does nothing by default; subclasses should override to do
        any thread-specific startup tasks.
    */
    protected void threadStarted() {
        // do nothing
    }


    /**
        Signals that a PooledThread has stopped. This method
        does nothing by default; subclasses should override to do
        any thread-specific cleanup tasks.
    */
    protected void threadStopped() {
        // do nothing
    }


    /**
        A PooledThread is a Thread in a ThreadPool group, designed
        to run tasks (Runnables).
    */
    private class PooledThread extends Thread {
        public PooledThread() {
            super(ThreadPool.this,
                "PooledThread-" + (threadID++));
        }


        public void run() {
            // signal that this thread has started
            threadStarted();

            while (!isInterrupted()) {

                // get a task to run
                Runnable task = null;
                try {
                    task = getTask();
                }
                catch (InterruptedException ex) { }

                // if getTask() returned null or was interrupted,
                // close this thread.
                if (task == null) {
                    break;
                }

                // run the task, and eat any exceptions it throws
                try {
                    task.run();
                }
                catch (Throwable t) {
                    uncaughtException(this, t);
                }
            }
            // signal that this thread has stopped
            threadStopped();
        }
    }
}