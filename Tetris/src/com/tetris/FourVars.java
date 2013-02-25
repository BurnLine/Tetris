package com.tetris;

/**
 * 
 * @author Thomas1989
 *
 * mozu sa pouzivat obe sucasne, alebo len jedna sada premennych priamym pristupom
 */

public class FourVars {
	public float x, y, z, w;
	public float r, g, b, a;
	
	public FourVars(float _a, float _b, float c, float d) {
		x = r = _a;
		y = g = _b;
		z = b = c;
		w = a = d;
	}
	
	/**
	 * nastavi pristup premenych v dvoch menach
	 * @param _a
	 * @param _b
	 * @param c
	 * @param d
	 */
	public void set(float _a, float _b, float c, float d) {
		x = r = _a;
		y = g = _b;
		z = b = c;
		w = a = d;
	}
}
