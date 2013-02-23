package com.tetris;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Point {
	public int type = 0;
	
	private Bitmap[] images;
	private Paint paint;
	
	public Point(Bitmap[] images) {
		this.images = images;
		paint = new Paint();
	}
	
	public void draw(int x, int y, int size, Canvas canvas) {
		canvas.drawBitmap(images[type], new Rect(0, 0, images[type].getWidth(), images[type].getHeight()), 
				new Rect(x, y, x + size, y + size), paint);
	}
}
