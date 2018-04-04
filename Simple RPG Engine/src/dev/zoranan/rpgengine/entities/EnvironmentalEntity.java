package dev.zoranan.rpgengine.entities;

import java.awt.Color;
import java.awt.Graphics;
import org.jdom2.Element;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.gfx.GameCamera;
import dev.zoranan.rpgengine.gfx.Sprite;
import dev.zoranan.rpgengine.util.Assets;

/*
 * Any non moving object used to make up the game map
 */

public class EnvironmentalEntity extends Entity{
	
	protected Sprite sprite;	//The sprite used to render this object

	public EnvironmentalEntity(String name, Handler handler, float x, float y, int w, int h) 
	{
		super(name, handler, x, y, w, h);
	}
	
	//Constructor that takes an XML element but no handler (Why? Testing.. To be removed)
	private EnvironmentalEntity(Element envEntityElement)
	{
		super (envEntityElement, null);	//we must set the handler here!
		sprite = new Sprite (envEntityElement.getChildText("spriteID"));
	}
	
	//This constructor is used when loading map data
	public EnvironmentalEntity (Element mapElement, Handler handler)
	{
		/*Take the map element we have, and get the "envID" node
		 * this node stores the ID for the entity we need to create
		 * Request the element for this entity from our assets class,
		 * and pass it to our other constructor
		 */
		this(Assets.getEnvironmentalEntity(mapElement.getChildText("envID")));
		
		//position
		int x = Integer.parseInt(mapElement.getChild("position").getAttributeValue("x"));
		int y = Integer.parseInt(mapElement.getChild("position").getAttributeValue("y"));
		this.setPos(x, y);
		
		this.setWidthHeight(sprite.getSpriteSheet().getWidth(), sprite.getSpriteSheet().getHeight());
		this.handler = handler;
	}

	@Override
	public void update() 
	{
		
	}

	@Override
	public void render(Graphics g) 
	{	
		GameCamera cam = handler.getGameCamera();
		g.drawImage(sprite.get(), cam.calcRenderX(posX), cam.calcRenderY(posY), width, height, null);
		//g.setColor(Color.RED);
		//g.fillRect(cam.calcRenderX(getHitBounds(0,0).x), cam.calcRenderY(getHitBounds(0,0).y), this.hitBounds.width, this.hitBounds.height);
	}

}
