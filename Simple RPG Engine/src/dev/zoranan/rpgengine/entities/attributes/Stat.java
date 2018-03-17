package dev.zoranan.rpgengine.entities.attributes;

public class Stat {
	private String name;
	private int current;
	private int maximum;
	//possibly add in for buffs / debuffs
	
	public Stat(String name, int level)
	{
		this.name = name;
		this.current = level;
		this.maximum = level;
	}
	
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
	
	public void increase(int i)
	{
		this.set(this.current + i);
	}
	
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
		this.increase(dif);
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
	
	public String name()
	{
		return this.name;
	}
}
