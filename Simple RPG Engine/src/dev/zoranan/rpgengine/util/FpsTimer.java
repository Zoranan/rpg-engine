package dev.zoranan.rpgengine.util;

/*
 * This class keeps track of the time elapsed since the last time it was checked
 * 
 * The check functions check to see if the proper amount of time has elapsed since 
 * the last successful call, and returns the result. 
 * 
 * The check percent function allows us to keep track of how many frames should 
 * have passed since the last successful check
 */

public class FpsTimer {
	private double timePerUpdate;
	private double delta;
	private long now;
	private long lastTime;
	private int fps;
	
	//Constructor sets the desired FPS to be targeted
	public FpsTimer(int fps)
	{
		setFps(fps);
	}
	
	//Checks if enough time has elapsed for another frame to have gone by.
	//If true, it resets the timer so we can check again.
	public boolean check()
	{
		now = System.nanoTime();
		delta = (now - lastTime) / timePerUpdate;
		
		if (delta >= 1)
		{
			lastTime = now;
			return true;
		}
		
		else return false;
	}
	
	//Gets the percentage of how many frames should have gone by since the last check
	public double checkPercent()
	{
		now = System.nanoTime();
		delta = (now - lastTime) / timePerUpdate;
		lastTime = now;
		
		double d = delta;
		delta = 0;
		return d;
	}
	
	public void setFps(int fps)
	{
		if (this.fps != fps)
		{
			timePerUpdate = 1e9 / fps;
			delta = 0;
			lastTime = System.nanoTime();
			this.fps = fps;
		}
	}

}
