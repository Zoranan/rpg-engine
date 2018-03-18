package dev.zoranan.rpgengine.entities.behaviors;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.Mob;

//Behavior classes define Mob behaviors. Types of AI, player controlled, etc...

public abstract class Behavior {
	protected Mob mob;
	protected Handler handler;
	protected int centerX, centerY;
	protected int radius;

	public Behavior (Mob mob, Handler handler)
	{
		this.handler = handler;
		this.mob = mob;
		centerX = (int) mob.getPosX() + (mob.getWidth()/2);
		centerY = (int) mob.getPosY() + (mob.getHeight()/2);
		radius = mob.getHeight()/2;
	}
	
	public Mob getMob()
	{
		return mob;
	}
	
	//Checks for map collision at a single point depending on player direction
	//Up for deletion when separate map collision is removed
	public boolean check(Mob.Direction dir)
	{
		centerX = (int) mob.getPosX() + (mob.getWidth()/2);
		centerY = (int) mob.getPosY() + (mob.getHeight()/2);
		int edgeX = centerX;
		int edgeY = centerY;
		
		//Check single direction bounds first
		if (dir == Mob.Direction.NORTH)
			edgeY -= radius;
		else if (dir == Mob.Direction.SOUTH)
			edgeY += radius;
		else if (dir == Mob.Direction.EAST)
			edgeX += radius;
		else if (dir == Mob.Direction.WEST)
			edgeX -= radius;
		
		//Now check diagonals
		if (dir == Mob.Direction.NORTH_EAST)
		{
			edgeX += radius/2;
			edgeY -= radius/2;
		}
		else if (dir == Mob.Direction.SOUTH_EAST)
		{
			edgeX += radius/2;
			edgeY += radius/2;
		}
		else if (dir == Mob.Direction.NORTH_WEST)
		{
			edgeX -= radius/2;
			edgeY -= radius/2;
		}
		else if (dir == Mob.Direction.SOUTH_WEST)
		{
			edgeX -= radius/2;
			edgeY += radius/2;
		}
		
		return handler.getWorld().isSolid(edgeX, edgeY);
			
	}
	
	public abstract void update();
}
