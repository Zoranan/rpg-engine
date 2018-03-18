package dev.zoranan.rpgengine.entities.attributes;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.Mob;

/*
 * This is a statusBar that tracks a Mob object (Think, floating health bars above Mobs during combat)
 * Keeps track of when it was created, allowing it to expire if it goes unused for too long
 */

public class TrackingStatusBar extends StatusBar{
	private Mob target;
	private Handler handler;
	private static final int Y_MARGIN = 10;
	private long creationTime;	//The exact time this item was show
	
	public TrackingStatusBar(Stat stat, Mob mob, Handler handler, int w, int h) 
	{
		//Start off at (0,0)
		super(stat,0, 0, w, h);
		this.target = mob;
		this.handler = handler;
		resetTimer();
		update();	//Call update immediately to set our position
	}
	
	public void resetTimer()
	{
		this.creationTime = System.currentTimeMillis();
	}
	
	public long getCreationTime() {
		return creationTime;
	}

	@Override
	public void update()
	{
		//Track the position of our target
		this.posX = handler.getGameCamera().calcRenderX((int) (target.getCenterX() - (this.width / 2)));
		this.posY = handler.getGameCamera().calcRenderY((int) (target.getPosY() - this.height - Y_MARGIN));
	}

}
