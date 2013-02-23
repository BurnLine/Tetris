package com.tetris;

import android.os.Bundle;
import android.view.MotionEvent;
import android.app.Activity;
import android.content.Intent;

public class Main extends Activity {

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
		board = new Board(15, 30);
		board.attachNewShape(Shape.L_SHAPE);
		tetris_renderer = new GameRender3D(board);
		tetris_view.setRenderer(tetris_renderer);
		setContentView(tetris_view);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    switch (event.getAction()) {
	      case MotionEvent.ACTION_MOVE:
	        if(((int)event.getX() - x) > 0)
	        	board.moveRight();
	        else
	        	board.moveLeft();
	        x = (int)event.getX();
	        y = (int)event.getY();
	        tetris_view.requestRender();
	        return true;
	
	      case MotionEvent.ACTION_DOWN:
	        x = (int)event.getX();
	        y = (int)event.getY();
	        return true;
	
	      default:
	        return super.onTouchEvent(event);
	    }
	  }
	
}
