package com.tetris;

import com.tetris.Board.ActionListener;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.app.Activity;
import android.content.Intent;

public class Main extends Activity {

	private float fx, fy;
	private int x, y;
	private TetrisGLSurfaceView tetris_view;
	private GameRender3D tetris_renderer;
	private Board board;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// dorobit menu

		/*Intent intent = new Intent(this, BoardOld.class);
		startActivity(intent);*/
		tetris_view = new TetrisGLSurfaceView(this);
		board = new Board(10, 18);
		board.attachNewShape(Shape.L_SHAPE);
		board.setActionListener(new ActionListener() {
			@Override
			public void onRedraw() {
				tetris_view.requestRender();
			}
		});
		tetris_renderer = new GameRender3D(board);
		tetris_view.setRenderer(tetris_renderer);
		tetris_view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		setContentView(tetris_view);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*
	    switch (event.getAction()) {
	      case MotionEvent.ACTION_MOVE:
	    	  
	        if(((int)(event.getX()/100.0f) - x) > 1)
	        	board.moveRight();
	        
	        if (((int)(event.getX()/100.0f) - x) < -1)
	        	board.moveLeft();
	        
	        x = (int)(event.getX()/100.0f);
	        y = (int)(event.getY()/100.0f);
	        tetris_view.requestRender();
	        return true;
	
	      case MotionEvent.ACTION_DOWN:
	        x = (int)(event.getX()/100.0f);
	        y = (int)(event.getY()/100.0f);
	        return true;
	
	      default:
	        return super.onTouchEvent(event);
	    }
	    */
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			return true;
		case MotionEvent.ACTION_MOVE:
			return true;
		}
		
		return false;
	  }
	
}
