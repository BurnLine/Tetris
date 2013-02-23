package com.tetris;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class TetrisGLSurfaceView extends GLSurfaceView {
	
	public TetrisGLSurfaceView(Context context){
        super(context);

        // Set the Renderer for drawing on the GLSurfaceView
        //setRenderer(new GameRender3D(new Board(15, 30)));
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
