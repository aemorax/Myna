package com.nevergarden.myna.core;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MynaThread extends Thread {
    Boolean isRunning = true;
    Boolean cLock = true;

    final Myna mynaInstance;
    final SurfaceHolder surfaceHolder;
    final Context context;


    public MynaThread(Myna myna) {
        this(myna.getHolder(), myna.getContext(), myna);
    }

    private MynaThread(SurfaceHolder surfaceHolder, Context context, Myna myna) {
        this.surfaceHolder = surfaceHolder;
        this.context = context;
        this.mynaInstance = myna;
    }

    @Override
    public void run() {
        while (this.isRunning) {
            if(!surfaceHolder.getSurface().isValid())
                continue;

            // Update
            mynaInstance.step();

            // Render
            if(!cLock) {
                Canvas canvas = this.surfaceHolder.lockCanvas(null);
                cLock = true;
                if (canvas != null) {
                    synchronized (this.surfaceHolder) {
                        mynaInstance.requestRender();
                    }
                    if(cLock) {
                        this.surfaceHolder.unlockCanvasAndPost(canvas);
                        this.cLock = false;
                    }
                }
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}