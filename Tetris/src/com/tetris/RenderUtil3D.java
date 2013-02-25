package com.tetris;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
//import android.opengl.GLU;

public abstract class RenderUtil3D implements GLSurfaceView.Renderer {
	private static final int RENDER_MODE_2D = 1;
	private static final int RENDER_MODE_3D = 2;
	
	private int currentRenderMode;
	private int surfaceWidth;
	private int surfaceHeight;
	private float ratio, perspHeight;
	
	public abstract void renderAll();
	
	public RenderUtil3D()
	{
		currentRenderMode = RENDER_MODE_3D;
	}
	
	/**
	 * pripravy kontext na vykreslovanie 2D objektov
	 * @return vrati True ak bol rezim prepnuty
	 */
	public boolean switchTo2D()
	{
		if(currentRenderMode == RENDER_MODE_2D)
			return false;
		currentRenderMode = RENDER_MODE_2D;
		GLES11.glMatrixMode(GLES11.GL_PROJECTION);
		GLES11.glPushMatrix();
		GLES11.glLoadIdentity();
		GLES11.glOrthof(0.0f, surfaceWidth, surfaceHeight, 0.0f, -1.0f, 1.0f);
		return true;
	}
	
	/**
	 * Vystupi z 2D vykreslovacieho rezimu
	 * @return Vrati True ak sme boli v 3D rezime a vystupili sme
	 */
	public boolean exitFrom2D()
	{
		if(currentRenderMode == RENDER_MODE_3D)
			return false;
		currentRenderMode = RENDER_MODE_3D;
		GLES11.glMatrixMode(GLES11.GL_PROJECTION);
		GLES11.glPopMatrix();
		return true;
	}
	
	@Override
	public void onDrawFrame(GL10 unused) {
		GLES11.glClear(GLES11.GL_DEPTH_BUFFER_BIT | GLES11.GL_COLOR_BUFFER_BIT);
		this.renderAll();
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) {
		surfaceWidth = width;
		surfaceHeight = height;
		GLES11.glViewport(0, 0, width, height);
		// ak sme v 2D rezime vystupime a zaznamename do premennej ze sa don mame vratit
		if(exitFrom2D())
			currentRenderMode = RENDER_MODE_2D;
		/*
		 * perspektiva s fovy 45 stupnov
		 */
		GLES11.glLoadIdentity();
		ratio = (float)width/height;
		perspHeight = 0.41421f;
		GLES11.glFrustumf(-perspHeight*ratio, perspHeight*ratio, -perspHeight, perspHeight, 1.0f, 100.0f);
		
		if(currentRenderMode == RENDER_MODE_2D)
		{
			// ak by bol teraz render mode 2D tak by nepreplo, preto render mode 3D
			currentRenderMode = RENDER_MODE_3D;
			this.switchTo2D();
		}
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig egl_config) {
		GLES11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}
}
