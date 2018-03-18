package dev.zoranan.rpgengine.entities.attributes;

import java.awt.Graphics;

import dev.zoranan.rpgengine.entities.Mob;

//Status effects are added to mob objects. They cover everything from DoT attacks, to weaknesses

/*
 * This class may be up for removal. When stat effects are implemented, we will see
 */

public abstract class StatusEffect {
	protected float duration;
	protected Mob effected;		//A reference to the effected mob
	
	public StatusEffect(Mob effected)
	{
		this.effected = effected;
	}
	
	public abstract void update();	//Used to update the amount of time left until the effect expires as well as other variables
	
	public void render(Graphics g)	//Only used for effects like DoT attacks (Burning, bleeding, poison, etc)
	{
		
	}
	
	//Getters and setters
	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}
	
}
