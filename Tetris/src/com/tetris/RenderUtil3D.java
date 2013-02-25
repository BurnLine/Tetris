package com.tetris;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public abstract class RenderUtil3D implements GLSurfaceView.Renderer {
	private static final int RENDER_MODE_2D = 1;
	private static final int RENDER_MODE_3D = 2;
	
	private int currentRenderMode;
	
	public abstract void renderAll();
	
	public RenderUtil3D()
	{
		currentRenderMode = RENDER_MODE_3D;
	}
	
	public void switchTo2D()
	{
		currentRenderMode = RENDER_MODE_2D;
		GLES11.glMatrixMode(GLES11.GL_PROJECTION);
		GLES11.glPushMatrix();
		GLES11.glLoadIdentity();
		
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {
		this.renderAll();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {

	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig egl_config) {
		
	}
}
