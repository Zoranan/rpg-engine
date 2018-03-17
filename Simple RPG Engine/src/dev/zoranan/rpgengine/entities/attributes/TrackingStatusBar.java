package dev.zoranan.rpgengine.entities.attributes;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.Mob;

public class TrackingStatusBar extends StatusBar{

	private Mob target;
	private Handler handler;
	private static final int Y_MARGIN = 10;
	private long creationTime;
	
	public TrackingStatusBar(Stat stat, Mob mob, Handler handler, int w, int h) 
	{
		//Start off at (0,0)
		super(stat, 0, 0, w, h);
		this.target = mob;
		this.handler = handler;
		resetTimer();
		this.posX = handler.getGameCamera().calcRenderX((int) (target.getCenterX() - (this.width / 2)));
		this.posY = handler.getGameCamera().calcRenderX((int) (target.getPosY() - this.height - Y_MARGIN));
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
