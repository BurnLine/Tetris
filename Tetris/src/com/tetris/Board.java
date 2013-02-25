package com.tetris;

import java.util.ArrayList;
import java.util.List;

public class Board {
	private List<Node> boardMap;
	private ArrayList<Node> removedLine;
	private List<Node> nodesAbove; // pre buduce animacie, kocky nad odstranenym
									// riadkom
	private int boardWidth;
	private int boardHeight;
	private Shape activeShape;
	private ActionListener listener;

	private int i, j, tmp, count;

	public Board(int width, int height) {
		boardWidth = width;
		boardHeight = height;
		boardMap = new ArrayList<Node>(boardWidth * boardHeight);
		
		for (i = 0; i < boardWidth * boardHeight; ++i)
			boardMap.add(null);
		
		boardMap.set(17 * 10, new Node(0, 0.5f, 17.5f, 0.0f));
		boardMap.set(17 * 10 + 1, new Node(0, 1.5f, 17.5f, 0.0f));

		// vopred vytvorenie pola obsahujuce riadok pre vyhodenie
		removedLine = new ArrayList<Node>(boardWidth);
		
		for (i = 0; i < boardWidth; i++)
			removedLine.add(null);
	}

	/**
	 * create new active shape in map
	 * 
	 * @param shapeType
	 */
	public void attachNewShape(int shapeType) {
		activeShape = new Shape((boardWidth / 2) - 2, 0, shapeType);

		if (listener != null)
			listener.onRedraw();
	}

	/**
	 * push shape down until collide
	 * 
	 * @return true if success
	 */
	public boolean pushShape() {
		if (activeShape == null)
			return false;
		
		if (checkEmptyPosition(activeShape.getMap(), activeShape.left, activeShape.top + 1)) {
			activeShape.top += 1;

			if (listener != null)
				listener.onRedraw();
			
			return true;
		}

		try {
		smashShape();
		checkFullLines();
		}catch(Exception e) {
			e.printStackTrace();
		}

		if (listener != null)
			listener.onSmash();
		return false;
	}

	/**
	 * rotate shape clock wise
	 */
	public void rotateShape() {
		if (activeShape == null)
			return;
		
		byte[] newMap = activeShape.getRotatedShape();

		if (checkEmptyPosition(newMap, activeShape.left, activeShape.top)) {
			activeShape.rotateShape();

			if (listener != null)
				listener.onRedraw();
		}
	}

	/** 
	 * move shape right
	 */
	public void moveRight() {
		if (activeShape == null)
			return;
		
		if (checkEmptyPosition(activeShape.getMap(), activeShape.left + 1,
				activeShape.top)) {
			activeShape.left += 1;

			if (listener != null)
				listener.onRedraw();
		}
	}

	/**
	 * move shape left
	 */
	public void moveLeft() {
		if (activeShape == null)
			return;
		
		if (checkEmptyPosition(activeShape.getMap(), activeShape.left - 1,
				activeShape.top))
			activeShape.left -= 1;

		if (listener != null)
			listener.onRedraw();
	}

	public Shape getActiveShape() {
		return activeShape;
	}

	public int getWidth() {
		return boardWidth;
	}

	public int getHeight() {
		return boardHeight;
	}

	public Node getBoardNode(int num) {
		return boardMap.get(num);
	}

	/**
	 * odstrani riadok a automaticky vrati odstranene nody, pre buduce animacie
	 * 
	 * @param line Cislo riadku
	 * @return odstranene nody pre animaciu
	 */
	public ArrayList<Node> removeLine(int line) {
		for (i = 0; i < boardWidth; i++) {
			removedLine.set(i, boardMap.get(line * boardWidth + i));
			boardMap.set(line * boardWidth + i, null);
		}
		
		for (j = line; j > 0; j--) {
			for (i = 0; i < boardWidth; i++) {
				Node tmp = boardMap.get((j - 1) * boardWidth + i);
				
				if (tmp != null) {
					tmp.y = j + 0.5f;
					boardMap.set((j - 1) * boardWidth + i, null);
				}
				
				boardMap.set(j * boardWidth + i, tmp);
			}
		}

		return removedLine;
	}

	private boolean checkEmptyPosition(byte[] shape, int offsetX, int offsetY) {
		for (j = 0; j < 4; j++) {
			for (i = 0; i < 4; i++) {
				if (shape[i + j * 4] != 0) {
					if ((((j + offsetY) * boardWidth + i + offsetX) >= boardWidth * boardHeight)
							|| ((i + offsetX) < 0)
							|| ((i + offsetX) >= boardWidth)
							|| (boardMap.get((j + offsetY) * boardWidth + i + offsetX) != null)) {
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * rozhodi prvky shape (jednotkove) do pevnych blokov (nodov)
	 * pozicia nodu zavisi na pozicii shape-u plus k tomu pozicia v lokalnej
	 * mape shape-u
	 * 
	 */
	private void smashShape() {
		for (j = 0; j < 4; ++j) {
			for (i = 0; i < 4; ++i) {
				if (activeShape.getMap()[i + j * 4] != 0) {
					boardMap.set((j + activeShape.top) * boardWidth + i + activeShape.left,
							new Node(activeShape.getMap()[i + j * 4], activeShape.left + i + 0.5f, (float) activeShape.top + j + 0.5f, 0f));
				}
			}
		}
		
		activeShape = null;
	}

	/**
	 * najde a odstrani riadky, ktore su cele vyplnene
	 */
	private void checkFullLines() {
		count = 0;
		
		for (j = 0; j < boardHeight; j++) {
			tmp = 0;
			
			for (i = 0; i < boardWidth; i++) {
				if (boardMap.get(j * boardWidth + i) != null)
					tmp++;
			}
			
			if (tmp == boardWidth) {
				count++;
				
				if (listener != null)
					listener.onLineSmash(removeLine(j));
				else
					removeLine(j);
			}
		}
		
		if (listener != null)
			listener.onLinesSmash(count);
	}

	public void setActionListener(ActionListener listener) {
		this.listener = listener;
	}

	public interface ActionListener {
		/**
		 * trigger this function when redraw needed
		 */
		public void onRedraw();
		/**
		 * trigger this function when shape smash was done
		 */
		public void onSmash();
		
		/**
		 * trigger this function when full lines smash
		 * 
		 * @param count Count of smashed lines
		 */
		public void onLinesSmash(int count);
		
		/**
		 * vrati aktualne odstraneny riadok
		 * 
		 * @param line odstraneny riadok
		 */
		public void onLineSmash(ArrayList<Node> line);
	}
}
