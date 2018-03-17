package dev.zoranan.rpgengine.items;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jdom2.Element;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.Entity;
import dev.zoranan.rpgengine.gfx.GameCamera;
import dev.zoranan.rpgengine.gfx.ImageLoader;
import dev.zoranan.rpgengine.gfx.Sprite;
import dev.zoranan.rpgengine.gfx.Text;
import dev.zoranan.rpgengine.util.Assets;

public class Item{
	//Item stats
	public static final int ICON_SIZE = 50;
	protected String itemID;
	protected Sprite spriteIcon;
	protected String name;
	protected String type;
	protected boolean consumable;
	protected int stackSize = 1;
	protected int maxStackSize = 1;
	protected int value;
					//Limb			//Angle 	//spriteID
	protected HashMap<String, HashMap<String, String>> models;
	protected HashMap<String, String> effects;
	
	public Item(String name, BufferedImage icon, Handler handler, float x, float y)
	{
		
	}
	
	//From XML constructor
	public Item (Element e)
	{		
		this.itemID = e.getName();	//The name of the element node, which is a unique ID
		spriteIcon = new Sprite (e.getChildText("spriteIconID"));
		this.name = e.getChildText("name");
		this.type = e.getChildText("type");
		this.consumable = Boolean.parseBoolean(e.getChildText("consumable"));
		this.maxStackSize = Integer.parseInt(e.getChildText("maxStackSize"));
		this.value = Integer.parseInt(e.getChildText("value"));
		
		//Load our models
		try
		{
			List<Element> modelEles = e.getChild("models").getChildren();
			System.out.println(e.getName() + " has " + modelEles.size() + " models");
			for (Element m : modelEles)
			{
				System.out.println("Loading " + m.getName() + " --> " + Assets.getModel(m.getValue()).toString());
				if (models == null)
					models = Assets.getModel(m.getValue());
				else
				{
					models.putAll(Assets.getModel(m.getValue()));
				}
			}
		}
		catch(Exception ex)
		{
			//DO NOTHING
		}
		
		//Load all effects (magic or otherwise)
		effects = new HashMap<String, String>();
		List<Element> effectList = e.getChild("effects").getChildren();
		for (Element ele : effectList)
		{
			effects.put(ele.getName(), ele.getText());
		}
	}
	
	//Copy constructor
	public Item (Item i)
	{
		this.itemID = i.itemID;
		this.spriteIcon = new Sprite (i.spriteIcon.getSpriteSheetID());
		this.name = i.name;
		this.type = i.type;
		this.consumable = i.consumable;
		this.maxStackSize = i.maxStackSize;
		this.value = i.value;
		this.models = i.models;
		this.effects = i.effects;
	}

	public void update()
	{
		
	}
	
	//For rendering the item within a container
	public void renderIcon(Graphics g, int x, int y)
	{
		//Draws the image
		g.drawImage(spriteIcon.get(), x, y, null);
		
		//Draws the stack count
		if (stackSize > 1)
		{
			Text.setAlign(Text.H_align.RIGHT, Text.V_align.ABOVE);
			Text.drawText(g, Integer.toString(stackSize), x + ICON_SIZE, y + ICON_SIZE , false, Color.YELLOW, Assets.fontRoboto14);
		}
	}
	
	//ADD AND REMOVE FROM STACK
	public boolean takeFromStack()
	{
		//it IS possible for stacks to be empty.
		//It is up to the container object to remove empty stacks
		if (stackSize > 0)
		{
			stackSize--;
			return true;
		}
		else return false;
	}
	
	public boolean addToStack()
	{
		//
		if (stackSize < maxStackSize)
		{
			stackSize++;
			return true;
		}
		else return false;
	}
	
	//GETTERS AND SETTERS
	public int getStackSize() {
		return stackSize;
	}

	public void setStackSize(int stackSize) {
		if (stackSize <= maxStackSize)
			this.stackSize = stackSize;
		else
			this.stackSize = maxStackSize;
	}

	public int getValue() {
		return value;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}
	
	public String getGenericType() {
		return type;
	}
	
	public String getExactType() {
		return itemID;
	}
	
	public BufferedImage getIcon()
	{
		return spriteIcon.get();
	}
	
	public HashMap<String, HashMap<String, String>> getModel()
	{
		return this.models;
	}
	
	public String getName()
	{
		return name;
	}
}
