package com.tetris;

import java.util.Timer;
import java.util.TimerTask;

import com.tetris.Board.ActionListener;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.app.Activity;
import android.content.Intent;

public class Main extends Activity {

	private static final int step = 20;

	private boolean move;
	private float fx, fy;
	private int x, y;
	private TetrisGLSurfaceView tetris_view;
	private GameRender3D tetris_renderer;
	private Board board;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// dorobit menu

		/*
		 * Intent intent = new Intent(this, BoardOld.class);
		 * startActivity(intent);
		 */
		tetris_view = new TetrisGLSurfaceView(this);
		board = new Board(10, 18);
		board.attachNewShape(Shape.L_SHAPE);
		board.setActionListener(new ActionListener() {
			@Override
			public void onRedraw() {
				tetris_view.requestRender();
			}

			@Override
			public void onSmash() {
				
			}
		});
		tetris_renderer = new GameRender3D(board);
		tetris_view.setRenderer(tetris_renderer);
		tetris_view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		setContentView(tetris_view);
		
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(!board.pushShape())
					board.attachNewShape((int)(Math.round(Math.random() * 5)));
			}
		}, 500, 500);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			fx = event.getX();
			fy = event.getY();
			move = false;
			return true;
		case MotionEvent.ACTION_MOVE:
			if (Math.abs(event.getX() - fx) > step) {
				if (event.getX() > fx) {
					board.moveRight();
					fx += step;
				} else {
					board.moveLeft();
					fx -= step;
				}
				
				move = true; // nastal pohyb, pri ACTION_UP nenastane akcia
			}
			if (Math.abs(event.getY() - fy) > step) {
				if (event.getY() > fy) {
					fy += step;
				} else {
					fy -= step;
				}
				
				move = true; // nastal pohyb, pri ACTION_UP nenastane akcia
			}
			return true;
		case MotionEvent.ACTION_UP:
			if (!move)
				board.rotateShape();
			return true;
		}

		return false;
	}

}
