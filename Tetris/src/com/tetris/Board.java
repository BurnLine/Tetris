package com.tetris;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private List<Node> boardMap;
	private int boardWidth;
	private int boardHeight;
	private Shape activeShape;
	private float edgeSize;
	
	private int i, j, maxColl;
	
	public Board(int width, int height)
	{
		boardWidth = width;
		boardHeight = height;
		boardMap = new ArrayList<Node>(boardWidth*boardHeight);
		for(i = 0; i < boardWidth*boardHeight; ++i)
			boardMap.add(null);
		boardMap.set(17*10, new Node(0, 0.5f, 17.5f, 0.0f));
		boardMap.set(17*10 + 1, new Node(0, 1.5f, 17.5f, 0.0f));
	}
	
	// create new active shape in map
	public void attachNewShape(int shapeType)
	{
		activeShape = new Shape((boardWidth / 2) - 2, 0, shapeType);
	}
	
	// push shape down until collide
	public boolean pushShape()
	{
		if(this.canMoveDown()) {
			activeShape.top += 1;
			return true;
		}
			
		this.smashShape();
		return false;
	}
	
	// rotate shape clock wise
	public void rotateShape()
	{
		activeShape.rotateShape();
	}
	
	// move shape right
	public void moveRight()
	{
		if(this.canMoveRight()) {
			activeShape.left += 1;
		}
	}
	
	// move shape left 
	public void moveLeft()
	{
		if(this.canMoveLeft()) {
			activeShape.left -= 1;
		}
	}
	
	public Shape getActiveShape()
	{
		return activeShape;
	}
	
	public int getWidth()
	{
		return boardWidth;
	}
	
	public int getHeight()
	{
		return boardHeight;
	}
	
	public Node getBoardNode(int num)
	{
		return boardMap.get(num);
	}
	
	private boolean canMoveLeft()
	{
		maxColl = 3;
		for(j = 0; j < 4; ++j) {
			i = 0; 
			while((activeShape.getMapValue(i++, j) == 0) && (i != 3));
			if(i < maxColl)
				maxColl = i;
		}
		maxColl += activeShape.left;
		if((maxColl - 1) > -1)
			return true;
		return false;
	}
	
	private boolean canMoveRight()
	{
		maxColl = 0;
		for(j = 0; j < 4; ++j) {
			i = 3; 
			while((activeShape.getMapValue(i--, j) == 0) && (i != 0));
			if(i > maxColl)
				maxColl = i;
		}
		maxColl += activeShape.left;
		if((maxColl + 1) < boardWidth)
			return true;
		return false;
	}
	
	private boolean canMoveDown()
	{
		/*for(i = 0; i < 4; ++i) {
			j = 3;
			while((activeShape.getMapValue(i, j--) == 0) && (j != 0));
			if()
		}*/
		if(activeShape.top < (boardHeight - 4))
			return true;
		return false;
	}
	
	// rozhodi prvky shape (jednotkove) do pevnych blokov (nodov)
	// pozicia nodu zavisi na pozicii shape-u plus k tomu pozicia v lokalnej mape shape-u
	private void smashShape()
	{
		for(j = 0; j < 4; ++j) {
			for(i = 0; i < 4; ++i) {
				if(activeShape.getMapValue(i) != 0) {
					boardMap.set((activeShape.top + j) * 4 + (activeShape.left + i),
							     new Node(0, (float)activeShape.left + i + 0.5f, (float)activeShape.top + j + 0.5f, 0.0f));
				}
			}
		}
		activeShape = null;
	}
}
