package dev.zoranan.rpgengine.entities.containers;

import dev.zoranan.rpgengine.entities.Entity;
import dev.zoranan.rpgengine.items.Item;

////////////////////////////////////
//
//	This class holds an Item in transit, as well as 
//	its information and the context of the transfer.
//
public class ItemTransfer {
	private Entity from;
	private Container fromContainer;
	private int fromSlotNum;
	private Item item = null;
	private Context context;
	
	public static enum Context {LOOT, BUY, STEAL};

	public ItemTransfer(Item item, Entity from, Container fromContainer, int slotNum, Context context)
	{
		this.item = item;
		this.from = from;
		this.fromContainer = fromContainer;
		this.fromSlotNum = slotNum;
		this.context = context;
	}
	
	public void cancel()
	{
		//gives the item back
		fromContainer.add(this);
	}

	public Entity getFrom() {
		return from;
	}

	public int getFromSlotNum() {
		return fromSlotNum;
	}

	public Item getItem() {
		return item;
	}

	public Context getContext() {
		return context;
	}

	public Container getFromContainer() {
		return fromContainer;
	}
	
}
