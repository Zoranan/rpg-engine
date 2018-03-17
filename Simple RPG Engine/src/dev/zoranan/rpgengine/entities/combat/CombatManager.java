package dev.zoranan.rpgengine.entities.combat;

import java.awt.Graphics;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map.Entry;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.EntityManager;
import dev.zoranan.rpgengine.entities.Mob;
import dev.zoranan.rpgengine.entities.attributes.TrackingStatusBar;

public class CombatManager {
	public static enum dmgType {PHYSICAL, ARCANE, FIRE, ICE, AIR, EARTH, ELECTRIC, POISON};
	
	private Handler handler;
	private EntityManager entityManager;
	
	private HashMap<Mob, TrackingStatusBar> healthBars;
	private static final int DELAY = 2;
	
	public CombatManager(Handler handler)
	{
		this.handler = handler;
		this.entityManager = handler.getEntityManager();
		healthBars = new HashMap<Mob, TrackingStatusBar>();
	}
	
	//Add an attack to be processed
	public void attack(Attack attack)
	{
		this.entityManager = handler.getEntityManager();
		//
		Mob target;
		
		//Look at all Mob objects for possible targets
		int i = 0;
		boolean attackComplete = false;
		
		//For Melee weapons, we draw a line from the dealer to the attack position to represent the range
		Line2D attackLine = new Line2D.Float(attack.getDealer().getCenterX(), attack.getDealer().getCenterY(), attack.getPosX(), attack.getPosY());
		
		while (!attackComplete && i < entityManager.size())
		{
			if(entityManager.get(i) instanceof Mob)
			{
				target = (Mob) (entityManager.get(i));
				
				//For single hitter attacks
				//Only damage temp if we hit them, and the attacker is not the temp Mob
				if (attack.getAreaAffected() == 0 && attack.getDealer() != target &&
					attackLine.intersects(target.getBodyBounds()))
				{
					//Cause damage to target
					damage(target, attack);
					attackComplete = true;
				}
				else;	//Move on to area attacks
				
			}
			i++; // on to the next one
		}
	}
	
	//Calculate damage done to a target
	private void damage(Mob target, Attack attack)
	{
		//Temporarily do RAW damage
		target.getStat("Health").decrease(attack.getDmgAmount());
		System.out.println(target.getStat("Health").get());
		
		//Add a health bar to our Damaged object, only if we have not already
		if (!healthBars.containsKey(target))
			healthBars.put(target, new TrackingStatusBar(target.getStat("Health"), target, handler, 50, 10));

		//if we have a health bar for that object, lets reset its timer
		else
			healthBars.get(target).resetTimer();
	}
	
	public void update()
	{
		//Lets start by updating all health bars on screen
		//if (!healthBars.isEmpty())
		for (Entry<Mob, TrackingStatusBar> e : healthBars.entrySet())
		{
			//Remove the health bar if we pass the delay
			if ((System.currentTimeMillis() - e.getValue().getCreationTime()) > (DELAY * 1000))
			{
				healthBars.remove(e.getKey());
				continue;
			}
			//Otherwise, update the tracking bar
			else
			{
				e.getValue().update();
			}
		}
	}
	
	public void render(Graphics g)
	{
		//Lets start by rendering all health bars on screen
		//if (!healthBars.isEmpty())
		for (TrackingStatusBar e : healthBars.values())
		{
			e.render(g);
		}
	}
	
	//Convert a string to a dmgType
	public static dmgType getDmgType(String s)
	{
		dmgType dmg = dmgType.PHYSICAL;
		s = s.toLowerCase();
		if (s.startsWith("ph"))
			dmg = dmgType.PHYSICAL;
		else if (s.startsWith("ar"))
			dmg = dmgType.ARCANE;
		else if (s.startsWith("fi"))
			dmg = dmgType.FIRE;
		else if (s.startsWith("ic"))
			dmg = dmgType.ICE;
		else if (s.startsWith("ai"))
			dmg = dmgType.AIR;
		else if (s.startsWith("ea"))
			dmg = dmgType.EARTH;
		else if (s.startsWith("el"))
			dmg = dmgType.ELECTRIC;
		else if (s.startsWith("po"))
			dmg = dmgType.POISON;
		System.out.println(s);
		return dmg;
	}
}
