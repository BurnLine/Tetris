package com.tetris;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameBoard {
	private static final int[][] SHAPES = {
		{
			0, 0, 0, 0,
			0, 1, 0, 0,
			1, 1, 1, 0,
			0, 0, 0, 0,
		},
		{
			0, 0, 0, 0,
			0, 1, 1, 0,
			0, 1, 1, 0,
			0, 0, 0, 0,
		},
		{
			0, 0, 0, 0,
			0, 1, 1, 0,
			1, 1, 0, 0,
			0, 0, 0, 0,
		},
		{
			0, 0, 0, 0,
			0, 1, 1, 0,
			0, 0, 1, 1,
			0, 0, 0, 0,
		},
		{
			0, 0, 0, 0,
			0, 1, 1, 0,
			0, 1, 0, 0,
			0, 1, 0, 0,
		},
		{
			0, 0, 0, 0,
			0, 1, 1, 0,
			0, 0, 1, 0,
			0, 0, 1, 0,
		},
		{
			0, 1, 0, 0,
			0, 1, 0, 0,
			0, 1, 0, 0,
			0, 1, 0, 0,
		}
	};
	
	private int length;
	private int numW;
	private int numH;
	private Point[] data;
	private Point[] moveData = new Point[16];
	private int moveOffsetY;
	private int moveOffsetX;

	private int i, j, k, x, y;

	private Timer timer;
	private Paint pEmpty = new Paint();
	private Paint pFull = new Paint();
	private Bitmap[] images;
	private ActionListener actionListener = null;

	public GameBoard(Context context/* int x, int y, int width, int height, int numW, int numH */) {
		images = new Bitmap[2];
		images[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.green);
		images[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue);
		
		numW = 10;
		numH = 20;
		length = 15;
		data = new Point[numW * numH];
		for(i = 0; i < data.length; i++)
			data[i] = new Point(images);

		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				autoMove();

				if (actionListener != null)
					actionListener.onRedraw();
			}
		}, 5, 500);

		setFall(); // first item

		pEmpty.setColor(Color.DKGRAY);
		pFull.setColor(Color.rgb(64, 180, 64));
	}

	public void draw(Canvas canvas) {
		for (j = 0; j < numH; j++) {
			for (i = 0; i < numW; i++) {
				data[i + numW * j].draw(i * length, j * length, length, canvas);
			}
		}

		for (j = 0; j < 4; j++) {
			for (i = 0; i < 4; i++) {
				if (moveData[i + 4 * j].type == 0)
					continue;
				
				moveData[i + j * 4].draw((i + moveOffsetX) * length, (j + moveOffsetY) * length, length, canvas);
			}
		}
	}
	
	public void rotate() {
		Point[] clone = moveData.clone();
		
		for (j = 0; j < 4; j++) {
			for (i = 0; i < 4; i++) {
				clone[(3 - i) * 4 + j] = moveData[j * 4 + i];
			}
		}
		
		if (!checkColision(clone, moveOffsetX, moveOffsetY)) {
			moveData = clone;

			if (actionListener != null)
				actionListener.onRedraw();
		}
	}

	public void moveRight() {
		if (!checkColision(moveData, moveOffsetX + 1, moveOffsetY)) {
			moveOffsetX++;

			if (actionListener != null)
				actionListener.onRedraw();
		}
	}

	public void moveLeft() {
		if (!checkColision(moveData, moveOffsetX - 1, moveOffsetY)) {
			moveOffsetX--;

			if (actionListener != null)
				actionListener.onRedraw();
		}
	}

	private void autoMove() {
		if (checkColision(moveData, moveOffsetX, moveOffsetY + 1)) {
			copyData();
			checkFull();
			setFall();
		} else
			moveOffsetY++;
	}

	private void copyData() {
		for (j = 0; j < 4; j++) {
			for (i = 0; i < 4; i++) {
				if (moveData[i + j * 4].type != 0)
					data[(j + moveOffsetY) * numW + i + moveOffsetX] = moveData[i + j * 4];
			}
		}
	}
	
	private void checkFull() {
		y = 0;
		
		for (j = 0; j < numH; j++) {
			k = 0;
			
			for (i = 0; i < numW; i++) {
				if (data[j * numW + i].type != 0)
					k++;
			}
			
			if (k == numW) {
				y++;
				
				for (x = j; x > 0; x--) {
					for (i = 0; i < numW; i++)
						data[x * numW + i] = data[(x - 1) * numW + i];
				}
			}
		}
		
		if (actionListener != null)
			actionListener.onFullLine(y);
	}

	private void setFall() {
		moveOffsetY = 0;
		moveOffsetX = numW / 2 - 2;
		//moveData = SHAPES[(int)(Math.round(Math.random() * (SHAPES.length - 1)))].clone();
		moveData = new Point[16];
		for(i = 0; i < 16; i++) {
			moveData[i] = new Point(images);
		}
		moveData[4].type = 1;
	}

	private boolean checkColision(Point[] matrix, int offsetX, int offsetY) {
		for (j = 0; j < 4; j++) {
			for (i = 0; i < 4; i++) {
				if (matrix[i + j * 4].type != 0) {
					if ((((j + offsetY) * numW + i + offsetX) >= numW * numH)
							|| ((i + offsetX) < 0) || ((i + offsetX) >= numW)
							|| (data[(j + offsetY) * numW + i + offsetX].type != 0)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public void setActionListener(ActionListener listener) {
		actionListener = listener;
	}

	public interface ActionListener {
		public void onRedraw();
		public void onFullLine(int count);
	}
}
