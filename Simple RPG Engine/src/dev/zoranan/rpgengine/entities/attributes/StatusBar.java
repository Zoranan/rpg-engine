package dev.zoranan.rpgengine.entities.attributes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.zoranan.rpgengine.util.Assets;

public class StatusBar {
	protected int posX, posY;
	protected int width, height;
	private Stat stat;
	private BufferedImage bar = Assets.redBar;
	
	//CONSTRUCTOR
	//TAKES ALL PARAMETERS
	public StatusBar(Stat stat, int x, int y, int w, int h)
	{
		this.stat = stat;
		this.setPosition(x, y);
		this.setSize(w, h);
	}
	
	//SETTERS
	public void setPosition(int x, int y)
	{
		this.posX = x;
		this.posY = y;
	}
	
	public void setSize(int w, int h)
	{
		this.width = w;
		this.height = h;
	}
	
	public void setStat(Stat stat)
	{
		this.stat = stat;
	}
	
	///
	public void update()
	{
		
	}
	
	public void render(Graphics g)
	{
		//Background
		g.setColor(Color.DARK_GRAY);
		g.fillRect(posX, posY, width, height);
		
		//Fill bar
		//g.setColor(Color.RED);
		g.drawImage(bar, posX, posY, (int) (width * stat.calcDecimal()), height, null);
		//g.fillRect(posX, posY, (int) (width * stat.calcDecimal()), height);
		//Outline
		g.setColor(Color.BLACK);
		g.drawRect(posX, posY, width, height);
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	//GETTERS
	
}
