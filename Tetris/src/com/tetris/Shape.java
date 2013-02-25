package com.tetris;

public class Shape {
	
	public static final int L_SHAPE = 0;
	public static final int SQR_SHAPE = 1;
	// ... and so on
	
	private final byte SHAPES[][] = {
		{
			0, 0, 0, 0,
			0, 1, 0, 0,
			0, 1, 0, 0,
			0, 1, 1, 0,
		},
		{
			0, 0, 0, 0,
			0, 0, 1, 0,
			0, 0, 1, 0,
			0, 1, 1, 0,
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
			1, 1, 0, 0,
			0, 1, 1, 0,
			0, 0, 0, 0,
		},
		{
			0, 1, 0, 0,
			0, 1, 0, 0,
			0, 1, 0, 0,
			0, 1, 0, 0,
		}
	};
	
	private byte map[] = new byte[16];
	
	public int left;
	public int top;
	
	private int i, j;
	
	public Shape(int sLeft, int sTop, int shapeType)
	{
		left = sLeft;
		top = sTop;
		System.arraycopy(SHAPES[shapeType], 0, map, 0, 16);
	}
	/*
	public byte getMapValue(int x, int y)
	{
		return map[y * 4 + x];
	}
	
	public byte getMapValue(int num)
	{
		return map[num];
	}
	*/
	public byte[] getMap() {
		return map;
	}
	
	/*
	public void setMap(byte[] newMap) {
		map = newMap;
	}*/
	
	/**
	 * rotate shape and return it, but do not update self
	 * 
	 * @return rotated shape
	 */
	public byte[] getRotatedShape() {
		byte newMap[] = map.clone();
		
		for(i = 0; i < 4; ++i) {
			for(j = 0; j < 4; ++j) {
				newMap[(3 - i) * 4 + j] = map[j * 4 + i];
			}
		}
		
		return newMap;
	}

	/**
	 * rotate self shape 
	 */
	public void rotateShape()
	{
		byte oldMap[] = map.clone();
		
		for(i = 0; i < 4; ++i) {
			for(j = 0; j < 4; ++j) {
				map[(3 - i) * 4 + j] = oldMap[j * 4 + i];
			}
		}
	}
}
