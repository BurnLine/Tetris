package com.tetris;

import java.lang.Thread.State;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.tetris.GameBoard.ActionListener;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnKeyListener;

public class BoardView extends SurfaceView implements Runnable, SurfaceHolder.Callback, OnKeyListener {
	private static final int THREAD_DRAW = 1;
	private static final int THREAD_STOP = 2;
	
	private Thread thread;
	private BlockingQueue<Integer> queue;
	
	private GameBoard board;
	
	private int score = 0;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

	public BoardView(Context context) {
		super(context);
		getHolder().addCallback(this);
		requestFocus();
		setFocusableInTouchMode(true);
		setOnKeyListener(this);
		queue = new LinkedBlockingQueue<Integer>(1);
		
		board = new GameBoard(context);
		board.setActionListener(new ActionListener() {
			@Override
			public void onRedraw() {
				redraw();
			}

			@Override
			public void onFullLine(int count) {
				score += count * 2;
			}
		});
		
		paint.setColor(Color.DKGRAY);
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
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(Color.LTGRAY);
		board.draw(canvas);
		canvas.drawText("SCORE: " + score, 200, 10, paint);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				board.moveLeft();
				break;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				board.moveRight();
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				board.rotate();
				break;
			}
		}
		return false;
	}
}
