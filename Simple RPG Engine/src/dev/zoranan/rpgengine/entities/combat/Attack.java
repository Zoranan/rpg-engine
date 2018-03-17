package dev.zoranan.rpgengine.entities.combat;

import dev.zoranan.rpgengine.entities.Mob;
import dev.zoranan.rpgengine.entities.attributes.StatusEffect;

public class Attack {
	private Mob dealer;						//Who dished out the attack
	//private Weapon weapon;					//What weapon was used
	private boolean friendlyFire = false;	//Can this hurt the dealer?
	private int dmgAmount;
	private float areaAffected = 0;			//AoE? 0 means single target.
	private float duration = 0;				//How long does the damage last? 0 means all in one frame.
	private int posX, posY;
	private StatusEffect statusEffect;		//Does the attack add any effects/////////////////////////////////////////////////!!!!
	private CombatManager.dmgType dmgType;	//What type of damage was it?
	
	//CONSTRUCTORS
	//Pass in the dealer and weapon, and let this object crunch the numbers
	public Attack (Mob dealer, double angle) //Need to pass in a way to calculate damage based on weapon
	{
		int range = 20;
		this.dealer = dealer;
		this.dmgAmount = 1;	//TEMP, we need more calculations
		
		//Calculate position
		float aX = (float)(range * Math.sin(angle));
		float aY = (float)(range * Math.cos(angle));
		this.setPos(dealer.getCenterX() + aX, dealer.getCenterY() - aY);
		
		//Get possible status effects (ex, poison, bleed, burn, etc)
		//this.statusEffect = weapon.getStatusEffect();
		
		////////////////////////////////////////////
		//
		//	Deal with AoE, multi-target, and DoT
		//
		////////////////////////////////////////////
		
		//Begin setting up our attack object variables
		//dmgType = weapon.getDmgType();
		
	}
	
	//GETTERS AND SETTERS
	public void setPos(float x, float y)
	{
		this.posX = (int) x;
		this.posY = (int) y;
	}
	
	public void setPos(int x, int y)
	{
		this.posX = x;
		this.posY = y;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public Mob getDealer() {
		return dealer;
	}

	public void setDealer(Mob dealer) {
		this.dealer = dealer;
	}

	public boolean friendlyFire() {
		return friendlyFire;
	}

	public void setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
	}

	public int getDmgAmount() {
		return dmgAmount;	//TEMP until STATS are implemented
	}

	public void setDmgAmount(int dmgAmount) {
		this.dmgAmount = dmgAmount;
	}

	public float getAreaAffected() {
		return areaAffected;
	}

	public void setAreaAffected(float areaAffected) {
		this.areaAffected = areaAffected;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public StatusEffect getStatusEffect() {
		return statusEffect;
	}

	public void setStatusEffect(StatusEffect statusEffect) {
		this.statusEffect = statusEffect;
	}

	public CombatManager.dmgType getDmgType() {
		return dmgType;
	}

	public void setDmgType(CombatManager.dmgType dmgType) {
		this.dmgType = dmgType;
	}
	
}
