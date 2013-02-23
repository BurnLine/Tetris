package com.tetris;

import android.opengl.GLES11;
import android.opengl.GLES10;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;


public class GameRender3D implements GLSurfaceView.Renderer {
	
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
	private FloatBuffer vertices;
	
	private int i;
	private float ratio;
	
	public GameRender3D(Board board)
	{
		gameBoard = board;
		vertices = ByteBuffer.allocateDirect(72*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertices.put(CUBE_VERTICES);
	}
	
	public void renderAll()
	{
		
	}
	
	public void renderShape()
	{
		activeShape = gameBoard.getActiveShape();
		for(i = 0; i < 16; ++i) {
			if(activeShape.getMapValue(i) == 1) {
				GLES11.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
				GLES11.glVertexPointer(24, GLES10.GL_FLOAT, 0, vertices);
				GLES11.glDrawArrays(GLES10.GL_TRIANGLES, 0, 8);
			}
		}
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		
		GLES11.glClear(GLES11.GL_DEPTH_BUFFER_BIT | GLES11.GL_COLOR_BUFFER_BIT);
		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
		GLES11.glLoadIdentity();
		GLES11.glTranslatef(0.0f, 0.0f, -15.0f);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		
		GLES11.glViewport(0, 0, width, height);
		ratio = (float)width/height;
		GLES11.glMatrixMode(GLES11.GL_PROJECTION);
		GLES11.glLoadIdentity();
		GLES11.glFrustumf(-ratio, ratio, -1, 1, 3, 7);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig egl_config) {
		
		GLES11.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	}
}
