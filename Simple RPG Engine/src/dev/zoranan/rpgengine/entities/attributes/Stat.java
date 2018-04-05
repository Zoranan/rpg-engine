package dev.zoranan.rpgengine.entities.attributes;

/*
 * Simple objects that store the name, current, and maximum values for a stat.
 */

public class Stat {
	protected String name;
	protected int current;
	protected int maximum;
	//possibly add in for buffs / debuffs
	
	public Stat(String name, int level, int max)
	{
		this.name = name;
		this.current = level;
		this.maximum = max;
	}
	
	//Returns a decimal value (percent) representing this stat's percentage to max value.
	public double calcDecimal()
	{
		if (current < 0)
			current = 0;
		else if (current > maximum)
			current = maximum;
		return (double)current / (double)maximum;
	}
	
	//Change stat level
	public void set(int c)
	{
		if (c > maximum)
			current = maximum;
		else if (c < 0)
			current = 0;
		else
			current = c;
	}
	
	//Increase the stat by a given amount
	public void increase(int i)
	{
		this.set(this.current + i);
	}
	
	//Decrease the stat by a given amount
	public void decrease(int i)
	{
		this.set(this.current - i);
	}
	
	//Change Maximum stat level
	public void setMax(int c)
	{
		if (c < 1)
			c = 1;
		
		int dif = c - maximum;
		maximum = c;
		this.increase(dif); //Drops or lowers our current value by the same amount we changed our max value.
	}
	
	public void increaseMax(int i)
	{
		this.setMax(this.maximum + i);
	}
	
	public void decreaseMax(int i)
	{
		this.setMax(this.maximum - i);
	}
	
	//get stat level
	public int get()
	{
		return current;
	}
	
	public int getMax()
	{
		return this.maximum;
	}
	
	public String name()
	{
		return this.name;
	}
	
	@Override
	public String toString()
	{
		return (name() + ": " + this.get() + "/" + this.getMax());
	}
}
