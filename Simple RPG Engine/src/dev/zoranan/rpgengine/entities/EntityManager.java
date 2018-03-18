package dev.zoranan.rpgengine.entities;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.behaviors.Behavior;
import dev.zoranan.rpgengine.entities.behaviors.Player;
import dev.zoranan.rpgengine.items.Item;

/*
 * Handles all entities currently placed on the loaded map
 */

public class EntityManager {
	private Handler handler;
	private ArrayList<Entity> entities;
	private static final int ACTIVATION_RANGE = 60; //This, should be passed in or checked at entity level
	
	//Sorts the list by depth
	private Comparator<Entity> depth = new Comparator<Entity>() {

		@Override
		public int compare(Entity a, Entity b) {
			if (a.getDepthReference() < b.getDepthReference())
				return -1;
			else
				return 1;
	}};
	
	//CONSTRUCTOR
	public EntityManager (Handler handler)
	{
		this.handler = handler;
		entities = new ArrayList<Entity>();
	}
	
	//UPDATE
	public void update()
	{	
		for (int i = 0; i < entities.size(); i++)
		{
			entities.get(i).update();
		}
		entities.sort(depth);
	}
	
	//RENDER
	public void render(Graphics g)
	{
		for (int i = 0; i < entities.size(); i ++ )
			entities.get(i).render(g);
	}
	
	//Player activation and Item gets
	//Returns NULL is no item is found
	public Entity activateClosestEntity(float x, float y, Mob activator)
	{
		Entity closest = null;
		Entity e;
		for (int i = 0; i<entities.size(); i++)
		{
			e = entities.get(i);
			double distance = Math.sqrt(Math.pow(e.getCenterX() - x, 2) + Math.pow(e.getCenterY() - y, 2));
			//First, check if the current item is in range, can be picked up or activated, and isnt equal to the activating item
			if (e.hasAction && distance < ACTIVATION_RANGE && !e.equals(activator))
			{
				//If this is our first item found, select it
				if (closest == null) 
					closest = e;
	
				//If we found our first item, then we can begin looking for the closest
				else if (distance < Math.sqrt(Math.pow(closest.getCenterX() - x, 2) + Math.pow(closest.getCenterY() - y, 2)))
					closest = e;
			}
		}
		//Pass the item back to the caller. Will return null if no item was found.
		return closest;
	}
	
	
	//FUNCTIONS
	//Add an entity
	public void addEntity (Entity e)
	{
		if (e != null)
			entities.add(e);
	}
	
	//Converts behaviors to mobs for adding
	public void addEntity (Behavior b)
	{
		entities.add(b.getMob());
	}
	
	//Delete an entity
	public boolean removeEntity(Entity e)
	{
		return entities.remove(e);
	}
	
	public boolean removeEntity(Behavior b)
	{
		return entities.remove(b.getMob());
	}

	//get an entity
	public Entity get(int i) {
		return entities.get(i);
	}
	
	//get number of entities
	public int size() {
		return entities.size();
	}
	
	

}
