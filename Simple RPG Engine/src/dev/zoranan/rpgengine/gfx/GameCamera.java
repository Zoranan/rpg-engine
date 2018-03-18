package dev.zoranan.rpgengine.gfx;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.Entity;

/*
 * The Game Camera class calculates and returns position data that controls
 * what the game screen is centered on. When a new target is set, the camera 
 * stays centered on that target
 */

public class GameCamera {
	private float xOffset, yOffset;
	private Entity target;
	private Handler handler;

	public GameCamera(Handler handler, float xOffset, float yOffset)
	{
		this.handler = handler;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	//Set target
	public void setTarget(Entity e)
	{
		target = e;
	}
	
	//Updates the cameras offset to keep the game centered
	public void update()
	{
		if (target == null)
		{
			xOffset = 0;
			yOffset = 0;
		}
		else
		{
			this.xOffset = (target.getPosX() - handler.getWidth() / 2) + (target.getWidth() /2);
			this.yOffset = (target.getPosY() - handler.getHeight() / 2) + (target.getHeight() / 2);
		}
	}
	
	
	public void move(float xAmt, float yAmt)
	{
		this.xOffset += xAmt;
		this.yOffset += yAmt;
	}

	public float getxOffset() {
		return xOffset;
	}

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}
	
	public int calcRenderX(float x)
	{
		return (int) (x - xOffset);
	}
	
	public int calcRenderY(float y)
	{
		return (int) (y - yOffset);
	}
}
