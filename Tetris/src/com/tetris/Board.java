package com.tetris;

import android.app.Activity;
import android.os.Bundle;

public class Board extends Activity {
	private BoardView view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new BoardView(this);
		setContentView(view);
	}
}
