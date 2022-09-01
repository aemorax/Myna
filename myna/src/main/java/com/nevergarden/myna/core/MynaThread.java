package com.nevergarden.myna.core;

public class MynaThread extends Thread {
    private final Myna mina;
    public boolean isRunning = false;
    private long loopRunTime = 0;

    public MynaThread(Myna myna) {
        this.mina = myna;
        this.isRunning = true;
    }

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
}
