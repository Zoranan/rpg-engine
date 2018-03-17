package dev.zoranan.rpgengine.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.gfx.GameCamera;

public class Building extends Entity{
	protected static final int INTERIOR_MAP_COLOR = -65536;
	protected static final int EXTERIOR_MAP_COLOR = -16776961;
	public static final int HOUSE_DEPTH = 100;
	
	protected boolean hideExterior = false;
	
	//TEMP
	protected int currentColor = 2;
	
	protected BufferedImage house;

	public Building(String name, Handler handler, float x, float y, BufferedImage house) 
	{
		super(name, handler, x, y, house.getWidth(), house.getHeight());
		this.house = house;
		
		calcBounds();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		GameCamera cam = handler.getGameCamera();
		g.drawImage(house, cam.calcRenderX(posX), cam.calcRenderY(posY), width, height, null);
	}
	
	//CALCULATE BOUNDS
	public void calcBounds()
	{
		
		int x = width / 2;
		int y = height-1;
		
		//find front and back
		while (house.getRGB(x, y) == 0)
		{
			y--;
		}
		
		hitBounds.y = y - HOUSE_DEPTH;
		hitBounds.height = HOUSE_DEPTH;
		
		//find left
		x = 0;
		while (house.getRGB(x, y) == 0)
		{
			x++;
		}
		
		hitBounds.x = x;
		
		//find right
		x = width-1;
		while (house.getRGB(x, y) == 0)
		{
			x--;
		}
		
		hitBounds.width = x - hitBounds.x;
	}

}
