package com.tetris;

import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BoardView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
	private static final int THREAD_DRAW = 1;
	private static final int THREAD_STOP = 2;
	
	private Thread thread;
	private BlockingQueue<Integer> queue;

	public BoardView(Context context) {
		super(context);
		getHolder().addCallback(this);
		queue = new LinkedBlockingQueue<Integer>(1);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		if (thread.getState() == State.NEW)
			thread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		thread = new Thread(this);
		queue.offer(THREAD_DRAW);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		while (true) {
			try {
				queue.put(THREAD_STOP);
				thread.join();
				break;
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (queue.take() == THREAD_STOP)
					break;
				
				SurfaceHolder holder = getHolder();
				Canvas canvas = holder.lockCanvas();

				synchronized (canvas) {
					draw(canvas);
				}
				
				holder.unlockCanvasAndPost(canvas);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}

	public void redraw() {
		queue.offer(THREAD_DRAW);
	}
}
