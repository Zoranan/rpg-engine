package dev.zoranan.rpgengine.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

import org.jdom2.Element;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.behaviors.Behavior;
import dev.zoranan.rpgengine.entities.containers.Container;
import dev.zoranan.rpgengine.util.Assets;

public abstract class Entity {
	protected float posX;
	protected float posY;
	protected float depthReference;
	protected int width;
	protected int height;
	protected String name;
	protected Handler handler;
	
	protected boolean hasAction = false;
	protected boolean isSolid = false;
	
	protected Container inventory;
	
	protected Rectangle hitBounds;
	protected Rectangle touchBounds;

	public abstract void update();
	public abstract void render(Graphics g);
	
	public Entity(String name, Handler handler, float x, float y, int w, int h)
	{
		//defaults
		setHitBounds(0, 0, 0, 0);
		setTouchBounds(0,0,0,0);
		
		this.name = name;
		this.handler = handler;
		setPos(x, y);
		setWidthHeight(w, h);
		depthReference = height;
		setWidthHeight(w, h);
		hasAction = false;
	}
	
	//Takes an Entity element and creates it
	public Entity (Element e, Handler handler)
	{	
		setWidthHeight(0,0);
		hitBounds = new Rectangle();
		touchBounds = new Rectangle();
		this.handler = handler;
		
		this.name = e.getChildText("name");
		
		//Environmental Entity data
		if (e.getChildText("depthRegister") != null)
			this.setDepthReference(Integer.parseInt(e.getChildText("depthRegister")));
		if (e.getChildText("isSolid") != null)
			this.isSolid = Boolean.valueOf(e.getChildText("isSolid"));
		
		Element bounds = e.getChild("solidBounds");
		
		if (bounds != null)
			this.setHitBounds(Integer.parseInt(bounds.getAttributeValue("x")), Integer.parseInt(bounds.getAttributeValue("y")), 
							Integer.parseInt(bounds.getAttributeValue("w")), Integer.parseInt(bounds.getAttributeValue("h")));
		
		bounds = e.getChild("totalBounds");
		if (bounds != null)
			this.setTouchBounds(Integer.parseInt(bounds.getAttributeValue("x")), Integer.parseInt(bounds.getAttributeValue("y")), 
								Integer.parseInt(bounds.getAttributeValue("w")), Integer.parseInt(bounds.getAttributeValue("h")));
	}
	
	//COPY CONSTRUCTOR
	public Entity (Entity e, Handler handler)
	{
		this.handler = handler;
		this.posX = e.posX;
		this.posY = e.posY;
		this.depthReference = e.depthReference;
		this.width = e.width;
		this.height = e.height;
		this.name = e.name;
		this.hasAction = e.hasAction;
		this.isSolid = e.isSolid;
		this.hitBounds = e.hitBounds;
		this.touchBounds = e.touchBounds;
	}

	//DEPTH CHECK
	public float getDepthReference() 
	{
		return (posY + depthReference);
	}
	
	//COLLISION DETECTION
	//Between entities
	public boolean checkEntityCollision(float xOffset, float yOffset)
	{
		for (int i = 0; i < handler.getEntityManager().size(); i++)
		{
			Entity e = handler.getEntityManager().get(i);
			if (e.equals(this) || e.getHitBounds(0f, 0f) == null)
				continue;
			else if (e.getHitBounds(0f, 0f).intersects(this.getHitBounds(xOffset, yOffset)))
				return true;
			
		}
		return false;
	}
	
	//What happens when this item is activated
	public void onActivate()
	{
		//Nothing yet
	}
	
	//Getters
	public float getPosX() {
		return posX;
	}
	public float getPosY() {
		return posY;
	}
	//Get center position
	public float getCenterX() {
		return posX + (width / 2);
	}
	public float getCenterY() {
		return posY + (height / 2);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public String getName() {
		return name;
	}
	
	public boolean hasAction() {
		return hasAction;
	}
	
	public Container getInventory() 
	{
		return inventory;
	}
	
	public Rectangle getHitBounds(float xOffset, float yOffset)
	{
		return new Rectangle((int) (posX + hitBounds.x + xOffset), 
							 (int) (posY + hitBounds.y + yOffset),
							 hitBounds.width, hitBounds.height);
	}
	
	public Rectangle getTouchBounds(float xOffset, float yOffset)
	{
		return new Rectangle((int) (posX + touchBounds.x + xOffset), 
							 (int) (posY + touchBounds.y + yOffset),
							 touchBounds.width, touchBounds.height);
	}
	
	//SETTERS
	
	//Set the hit area
	public void setHitBounds(int x, int y, int width, int height)
	{
		if (hitBounds == null)
			hitBounds = new Rectangle();
		
		hitBounds.x = x;
		hitBounds.y = y;
		hitBounds.width = width;
		hitBounds.height = height;
	}
	
	//Set the the objects touch boundaries
	public void setTouchBounds(int x, int y, int width, int height)
	{
		if (touchBounds == null)
			touchBounds = new Rectangle();
		
		touchBounds.x = x;
		touchBounds.y = y;
		touchBounds.width = width;
		touchBounds.height = height;
	}
		
	public void setDepthReference(float depthReference) 
	{
		this.depthReference = depthReference;
	}
	
	public void setPos(float x, float y)
	{
		this.posX = x;
		this.posY = y;
	}
		
	public void setWidthHeight(int w, int h)
	{
		this.width = w;
		this.height = h;
	}
		
	public void setName(String name)
	{
		this.name = name;
	}
}
