package dev.zoranan.rpgengine.gui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import dev.zoranan.rpgengine.gfx.SpriteSheet;

public abstract class GUIObject {
	protected SpriteSheet UIStates;
	protected int posX, posY;
	protected int width, height;
	protected boolean hovering = false;
	protected Rectangle bounds;

	public GUIObject(int x, int y, int w, int h)
	{
		posX = x;
		posY = y;
		width = w;
		height = h;
		bounds = new Rectangle(x, y, w, h);
	}
	
	public abstract void update();
	public abstract void render (Graphics g);
	public abstract void onClick();
	
	public void onMouseMove(MouseEvent e)
	{
		hovering = bounds.contains(e.getPoint());
	}
	public void onMouseReleased(MouseEvent e)
	{
		if (hovering && e.getButton() == MouseEvent.BUTTON1)
			onClick();
	}

	//Getters and Setters

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isHovering() {
		return hovering;
	}

	public void setHovering(boolean hovering) {
		this.hovering = hovering;
	}
}
