package com.tetris;

import android.opengl.GLES11;
import android.opengl.GLES10;

public class GameRender3D {
	
	private final float CUBE_VERTICES[] = {
			// front
			-0.45f, 0.45f, 0.45f,
			0.45f, 0.45f, 0.45f,
			-0.45f, -0.45f, 0.45f,
			0.45f, 0.45f, 0.45f,
			0.45f, -0.45f, 0.45f,
			-0.45f, -0.45f, 0.45f
			// back
			-0.45f, 0.45f, -0.45f,
			0.45f, 0.45f, -0.45f,
			-0.45f, -0.45f, -0.45f,
			0.45f, 0.45f, -0.45f,
			0.45f, -0.45f, -0.45f,
			-0.45f, -0.45f, -0.45f
			//top
			-0.45f, 0.45f, -0.45f,
			-0.45f, 0.45f, 0.45f,
			0.45f, 0.45f, -0.45f,
			0.45f, 0.45f, -0.45f,
			0.45f, 0.45f, 0.45f,
			-0.45f, 0.45f, 0.45f,
			//bottom
			-0.45f, -0.45f, -0.45f,
			-0.45f, -0.45f, 0.45f,
			0.45f, -0.45f, -0.45f,
			0.45f, -0.45f, -0.45f,
			0.45f, -0.45f, 0.45f,
			-0.45f, -0.45f, 0.45f,
	};

	private Board gameBoard;
	private Shape activeShape;
	
	private int i;
	
	public GameRender3D(Board board)
	{
		gameBoard = board;
	}
	
	public void renderAll()
	{
		
	}
	
	public void renderShape()
	{
		activeShape = gameBoard.getActiveShape();
		for(i = 0; i < 16; ++i) {
			if(activeShape.getMapValue(i) == 1) {
				GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
				//GLES10.glVertexPointer(24, GLES10.GL_FLOAT, 0, );
			}
		}
	}
}
