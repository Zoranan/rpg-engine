package dev.zoranan.rpgengine.entities.containers;

import dev.zoranan.rpgengine.items.Item;

public class ItemTransferManager {
	
	private ItemTransfer itemTransfer = null;
	
	//Holds onto the item
	public ItemTransfer holdItem(ItemTransfer it)
	{
		this.itemTransfer = it;
		ItemTransfer j = null;
		//If we are holding an item already...
		if (itemTransfer != null)
		{
			j = dropItem();		//Drop it (should be picked up by the calling object)
		}
		
		/////////////////////////////////////////////////////////////////
		//
		//	We need to check where this item came from. Do we pay for it?
		//
		
		itemTransfer = it;	//Set our held item to the new item
		return j;		//Return our old item (or null if empty)
	}
	
	//Drops the item held (returns the item, then clears heldItem variable)
	public ItemTransfer dropItem()
	{
		ItemTransfer it = this.itemTransfer;
		itemTransfer = null;
		return it;
	}
	
	//Return item being held to its original place
	public void returnItem()
	{
		//If we have an item, and a location for it to go, send it back
		itemTransfer.cancel();
		itemTransfer = null;
	}

	public ItemTransfer getItemTransfer() {
		return itemTransfer;
	}
	
	public Item getItem() {
		return itemTransfer.getItem();
	}
	
	public void checkEmpty()
	{
		if (itemTransfer.getItem().getStackSize() < 1)
			itemTransfer = null;
	}

}
