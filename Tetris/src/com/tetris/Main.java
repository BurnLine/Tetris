package com.tetris;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.tetris.Board.ActionListener;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.app.Activity;

public class Main extends Activity {

	private static final int step = 20;
	private static final int fastOffset = 60;

	private boolean move;
	private boolean fastPush, fastPushAgain;
	private float starty;
	private float fx, fy;
	private TetrisGLSurfaceView tetris_view;
	private GameRender3D tetris_renderer;
	private Board board;
	
	// pff pff

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// dorobit menu

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
				fastPush = false;
				fastPushAgain = false;
			}

			@Override
			public void onLinesSmash(int count) {
				// TODO pri odstraneni vsetkych riadkov ( pri 1 ticku)
			}

			@Override
			public void onLineSmash(ArrayList<Node> line) {
				// TODO pri odstraneni jedneho riadku (moze byt viackrat volane pocas 1 ticku)
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
			fy = starty = event.getY();
			move = false;
			fastPush = false;
			fastPushAgain = true;
			return true;
			
		case MotionEvent.ACTION_MOVE:
			if (Math.abs(event.getX() - fx) > step) {
				if (event.getX() > fx) {
					board.moveRight();
				} else {
					board.moveLeft();
				}

				fx = event.getX();
				move = true; // nastal pohyb, pri ACTION_UP nenastane akcia
			}
			
			if (Math.abs(event.getY() - fy) > step) {
				if (event.getY() > fy) {
					if (fastPush)
						board.pushShape();
					else {
						if (fastPushAgain && (event.getY() - starty > fastOffset))
							fastPush = true;
					}
					
					fy += step;
					fx = event.getX(); // aby pri fastPush nebolo mozne hybat do bokov
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
