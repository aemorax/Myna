package com.nevergarden.myna.core;

/**
 * Myna main thread for handling updates and time.
 */
public class MynaThread extends Thread {
    private final Myna mina;
    private boolean isRunning;
    private long loopRunTime = 0;

    /**
     * Default Constructor
     * @param myna Myna instance of this thread
     */
    protected MynaThread(Myna myna) {
        this.mina = myna;
        this.isRunning = true;
    }

    /**
     * Running logic of thread.
     */
    @Override
    public void run() {
        while (isRunning) {
            long loopStart = System.currentTimeMillis();
            int FPS = (1000 / 60);
            if (loopRunTime < FPS) {
                try {
                    Thread.sleep(FPS - loopRunTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            long loopEnd = System.currentTimeMillis();
            loopRunTime = ((loopEnd - loopStart));
            this.mina.update(loopRunTime);
            this.mina.requestRender();
        }
    }

    /**
     * Pauses the update of current thread.
     */
    public void pause() {
        this.isRunning = false;
    }

    /**
     * Unpauses the update of current thread.
     */
    public void unpause() {
        this.isRunning = true;
    }
}
