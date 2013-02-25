package com.tetris;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Mesh3D {
	public FloatBuffer vertices;
	public FloatBuffer texCoords;
	private int textureId;
	
	/**
	 * default konstruktor
	 */
	public Mesh3D() {
		textureId = -1;
		vertices = null;
		texCoords = null;
	}
	
	/**
	 * Nacita vertexy do meshu, kazdy vertex sa sklada z 3 float x, y, z
	 * @param buffer je pole vertexov
	 */
	public void loadVertices(float[] buffer) {
		if(vertices != null) {
			vertices.clear();
			vertices = null;
		}
		vertices = ByteBuffer.allocateDirect(buffer.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}
	
	/**
	 * Nacita koordinaty textur, kazdy koordinat sa sklada z 2 float x, y
	 * @param buffer je pole koordinatov
	 */
	public void loadTexCoords2D(float [] buffer) {
		if(texCoords != null) {
			texCoords.clear();
			vertices = null;
		}
		texCoords = ByteBuffer.allocateDirect(buffer.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}
	
	/**
	 * Vrati opengl referenciu na texturu
	 * @return
	 */
	public int getTexture() {
		return textureId;
	}
	
	/**
	 * Nastavi opengl referenciu na texturu
	 * @param texId
	 */
	public void setTexture(int texId) {
		textureId = texId;
	}
}
