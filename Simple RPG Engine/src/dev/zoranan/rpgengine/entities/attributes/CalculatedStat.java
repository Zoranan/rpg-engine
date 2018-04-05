package dev.zoranan.rpgengine.entities.attributes;

import dev.zoranan.rpgengine.entities.Mob;
import dev.zoranan.rpgengine.util.ScriptExecuter;

public class CalculatedStat extends Stat{
	protected String equation;
	protected Mob mob;
	
	public CalculatedStat(String name, String equation, Mob mob) 
	{
		super(name, 1, 1);
		this.equation = equation;
		this.mob = mob;
		calculateStat();
		
		System.out.println(this);
	}
	
	public void calculateStat()
	{
		double percent = this.calcDecimal();
		this.setMax((int) mob.exec(equation));
		this.set((int) (this.getMax() * percent));
	}

}
