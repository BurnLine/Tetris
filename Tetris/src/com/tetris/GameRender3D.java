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
			-0.45f, 0.45f, 0.0f,
			0.45f, 0.45f, 0.0f,
			-0.45f, -0.45f, 0.0f,
			0.45f, 0.45f, 0.0f,
			0.45f, -0.45f, 0.0f,
			-0.45f, -0.45f, 0.0f
			/*// back
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
			-0.45f, -0.45f, 0.45f,*/
	};
	
	private float limits[];

	private Board gameBoard;
	private Shape activeShape;
	private FloatBuffer vertices;
	private FloatBuffer lines;
	
	private int i;
	private float ratio, curX, curY;
	private Node renNode;
	
	public GameRender3D(Board board)
	{
		gameBoard = board;
		vertices = ByteBuffer.allocateDirect(18*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertices.put(CUBE_VERTICES);
		vertices.position(0);
		
		limits = new float[] {
				0.0f, 0.0f, 0.0f,
				gameBoard.getWidth(), 0.0f, 0.0f,
				gameBoard.getWidth(), gameBoard.getHeight(), 0.0f,
				0.0f, gameBoard.getHeight(), 0.0f,
				0.0f,0.0f,0.0f
		};
		
		lines = ByteBuffer.allocateDirect(15*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		lines.put(limits);
		lines.position(0);
	}
	
	public void renderAll()
	{
		renderLimitsOfBoard();
		renderShape();
		renderStaticNodes();
	}
	
	public void renderShape()
	{
		activeShape = gameBoard.getActiveShape();
		for(i = 0; i < 16; ++i) {
			if(activeShape.getMapValue(i) == 1) {
				curX = (float)((int)i%4);
				curY = (float)((int)i/4);
				System.out.println("X: " + curX);
				System.out.println("Y: " + curY);
				GLES11.glPushMatrix();
				GLES11.glTranslatef((float)activeShape.left + 0.5f, (float)(gameBoard.getHeight() - activeShape.top) + 0.5f, 0.0f);
				GLES11.glTranslatef(curX, -curY, 0.0f);
				GLES11.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
				GLES11.glVertexPointer(3, GLES10.GL_FLOAT, 0, vertices);
				GLES11.glDrawArrays(GLES10.GL_TRIANGLES, 0, 6);
				GLES11.glDisableClientState(GLES10.GL_VERTEX_ARRAY);
				GLES11.glPopMatrix();
			}
		}
	}
	
	public void renderLimitsOfBoard()
	{
		GLES11.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
		GLES11.glVertexPointer(3, GLES10.GL_FLOAT, 0, lines);
		GLES11.glDrawArrays(GLES10.GL_LINE_STRIP, 0, 5);
		GLES11.glDisableClientState(GLES10.GL_VERTEX_ARRAY);
	}
	
	public void renderStaticNodes()
	{
		for(i = 0; i < gameBoard.getWidth()*gameBoard.getHeight(); ++i) {
			renNode = gameBoard.getBoardNode(i);
			if(renNode != null) {
				GLES11.glPushMatrix();
				GLES11.glTranslatef(renNode.x, gameBoard.getHeight() - renNode.y, renNode.z);
				GLES11.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
				GLES11.glVertexPointer(3, GLES10.GL_FLOAT, 0, vertices);
				GLES11.glDrawArrays(GLES10.GL_TRIANGLES, 0, 6);
				GLES11.glDisableClientState(GLES10.GL_VERTEX_ARRAY);
				GLES11.glPopMatrix();
			}
		}
	}

	@Override
	public void onDrawFrame(GL10 unused) {
		
		GLES11.glClear(GLES11.GL_DEPTH_BUFFER_BIT | GLES11.GL_COLOR_BUFFER_BIT);
		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
		GLES11.glLoadIdentity();
		GLES11.glTranslatef((float)-gameBoard.getWidth()/2, (float)-gameBoard.getHeight()/2, -25.0f);
		this.renderAll();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		
		GLES11.glViewport(0, 0, width, height);
		ratio = (float)width/height;
		GLES11.glMatrixMode(GLES11.GL_PROJECTION);
		GLES11.glLoadIdentity();
		//GLES11.glFrustumf(-ratio, ratio, -1, 1, 3, 7);
		GLU.gluPerspective(unused, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig egl_config) {
		
		GLES11.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
	}
}
