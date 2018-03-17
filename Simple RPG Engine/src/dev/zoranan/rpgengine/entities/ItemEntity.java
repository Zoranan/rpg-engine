package dev.zoranan.rpgengine.entities;

import java.awt.Graphics;

import org.jdom2.Element;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.gfx.GameCamera;
import dev.zoranan.rpgengine.items.Item;
import dev.zoranan.rpgengine.util.Assets;

public class ItemEntity extends Entity{
	public static final int ITEM_SIZE = 25;	//The item's size on the map
	public Item item;

	public ItemEntity (Item i, Handler handler, float x, float y)
	{
		super(i.getName(), handler, x, y, ITEM_SIZE, ITEM_SIZE);
		item = i;
		hasAction = true;
	}
	
	public ItemEntity (Element mapElement, Handler handler)
	{
		super (mapElement, handler);
		/*The element we have is from the map file
		 * We need to get the proper item from the items XML file and create it
		 * So we pass the itemID text content from the map element into the getItem
		 * function in our assets class, and create a new item with it.
		 */
		item = new Item (Assets.getItem(mapElement.getChildText("itemID")));
		isSolid = false;
		hasAction = true;
		
		//position
		int x = Integer.parseInt(mapElement.getChild("position").getAttributeValue("x"));
		int y = Integer.parseInt(mapElement.getChild("position").getAttributeValue("y"));
		this.setPos(x, y);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		GameCamera cam = handler.getGameCamera();
		g.drawImage(item.getIcon(), cam.calcRenderX(this.posX), cam.calcRenderY(this.posY), ITEM_SIZE, ITEM_SIZE, null);
	}
	
	@Override
	public String getName()
	{
		return item.getName();
	}
	
	public Item getItem()
	{
		return this.item;
	}

}
