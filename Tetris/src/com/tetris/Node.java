package com.tetris;

public class Node {
	public int color;
	public float x;
	public float y;
	public float z;
	
	public Node(int _color, float sX, float sY, float sZ, float sEdgeSize)
	{
		color = _color;
		x = sX;
		y = sY;
		z = sZ;
	}
}
