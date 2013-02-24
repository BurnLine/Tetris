package com.tetris;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private List<Node> boardMap;
	private ArrayList<Node> removedLine;
	private List<Node> nodesAbove; // pre buduce animacie, kocky nad odstranenym riadkom
	private int boardWidth;
	private int boardHeight;
	private Shape activeShape;
	private ActionListener listener;
	
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
		
		// vopred vytvorenie pola obsahujuce riadok pre vyhodenie
		removedLine = new ArrayList<Node>(boardWidth);
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
		if(checkEmptyPosition(activeShape.left + 1, activeShape.top)) {
			activeShape.left += 1;
			
			if (listener != null)
				listener.onRedraw();
		}
	}
	
	// move shape left 
	public void moveLeft()
	{
		if(checkEmptyPosition(activeShape.left - 1, activeShape.top))
			activeShape.left -= 1;
		
		if (listener != null)
			listener.onRedraw();
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
	
	// odstrani riadok a automaticky vrati odstranene nody, pre buduce animacie
	public ArrayList<Node> removeLine(int line)
	{
		if(line > (boardHeight - 1))
			return null;
		
		for(i = 0; i < boardWidth; ++i) {
			removedLine.set(i, boardMap.remove(line * boardWidth + i));
		}
		
		return removedLine;
	}
	
	public void setActionListener(ActionListener listener) {
		this.listener = listener;
	}
	
	private boolean canMoveDown()
	{
		// kokotina
		if(activeShape.top < (boardHeight - 4))
			return true;
		return false;
	}
	
	private boolean checkEmptyPosition(int offsetX, int offsetY) {
		for (j = 0; j < 4; j++) {
			for (i = 0; i < 4; i++) {
				if (activeShape.getMapValue(i + j * 4) != 0) {
					if ((((j + offsetY) * boardWidth + i + offsetX) >= boardWidth * boardHeight)
							|| ((i + offsetX) < 0) || ((i + offsetX) >= boardWidth)
							|| (boardMap.get((j + offsetY) * boardWidth + i + offsetX) != null)) {
						return false;
					}
				}
			}
		}

		return true;
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
	
	public interface ActionListener {
		public void onRedraw();
	}
}
