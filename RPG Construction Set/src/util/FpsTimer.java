package util;

public class FpsTimer {
	
	private double timePerUpdate;
	private double delta;
	private long now;
	private long lastTime;
	private int fps;
	
	public FpsTimer(int fps)
	{
		setFps(fps);
	}
	
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
