package dev.zoranan.rpgengine.entities.containers;

import dev.zoranan.rpgengine.entities.Entity;
import dev.zoranan.rpgengine.entities.containers.ItemTransfer.Context;
import dev.zoranan.rpgengine.items.Item;

/*
 * Container object are not physical Entities. They are attached to entities in order to store items
 * if that entity is meant to be a container
 */

public class Container {
	protected int capacity;	//Total storage space
	protected int length;	//Currently used slots
	protected Item[] contents;
	Entity owner;

	protected static final int DEFAULT_SIZE = 15;
	protected static final int MAX_SIZE = 25;
	protected static final int NUM_COLUMNS = 5;
	
	//Creates a default container
	public Container (Entity owner)
	{
		this(owner, DEFAULT_SIZE);
	}
	
	//Creates a larger container.
	public Container (Entity owner, int size)
	{
		this.owner = owner;
		contents = new Item[MAX_SIZE];
		length = 0;
		setSize(size);
		
		for (int i=0; i<MAX_SIZE; i++)
			contents[i] = null;
	}
	
	//Sets container size safely. Returns true if successful
	public boolean setSize(int size)
	{
		if (size >= length && size <= MAX_SIZE)
		{
			capacity = size;
			return true;
		}
		else
			return false;
	}
	
	//Calculates the length
	public void calcLength()
	{
		length = 0;
		for (int i=0; i<capacity; i++)
			if (contents[i] != null)
				length++;
	}
	
	//Adds an item if we have room. Returns the number of items added;
	//Adds an item to first available spot
	public int add(Item item)
	{
		int added = 0;
		
		if (item == null)
			return 0;
		
		//first, look for stacks that can hold the item
		if (item.getMaxStackSize() > 1)
		{
			for (int i=0; i<capacity; i++)
			{
				//add the item to the first stack we find with room
				//If we find a stack that has room
				
				if (contents[i] != null && item.getExactType().equals(contents[i].getExactType()) 
										&& contents[i].getStackSize() < contents[i].getMaxStackSize())
				{
					//begin adding to the stack in the container
					//if the item stack we are adding runs out, or
					//the inventory stack becomes full, we exit the loop
					added += add(item, i);					
				}
			}	
		}
		
		//If the item is non stackable or there is anything leftover in the stack, look for empty slots
		if (item.getStackSize() > 0)
		{
			//System.out.println("Make a new stack");
			boolean looking = true;
			for (int i=0; i<capacity; i++)
			{
				//add the item to the first empty space we find
				if (contents[i] == null && looking)
				{
					contents[i] = item;
					//item.setStackSize(0);
					added += item.getStackSize();
					looking = false;
				}
			}
		}
		
		calcLength();
		
		return added;
	}//END add
	
	//Add an item to a specific slot
	public int add(Item item, int slot)
	{
		int added = 0;
		//Start by checking if two similar items are about to be stacked
		if (contents[slot] != null && item.getExactType().equals(contents[slot].getExactType()))
		{
			while (contents[slot].getStackSize() < contents[slot].getMaxStackSize() && item.getStackSize() > 0)
			{
				contents[slot].addToStack();
				item.takeFromStack();
				added++;
			}
		}
		else if (slot < capacity)
		{
			contents[slot] = item;
			added++;
		}
		
		return added;
	}
	
	//These two functions need to charge the player accordingly
	public int add (ItemTransfer it, int slot)
	{
		if (slot >= capacity)
		{
			it.cancel();
			return 0;
		}
		
		else if (it == null || it.getItem() == null)
		{
			contents[slot] = null;
			return 0;
		}
		else
		{
			return add(it.getItem(), slot);
		}
	}
	
	public int add (ItemTransfer it)
	{		
		return add(it.getItem());
	}
	
	//Removes an Item
	//Returns That Item
	public Item remove(int i)
	{
		Item item = contents[i];
		contents[i] = null;
		return item;
	}
	
	//Remove an item from the container, and wrap it in an Item Transfer
	public ItemTransfer removeIT(int i)
	{
		ItemTransfer it = new ItemTransfer(this.remove(i), this.owner, this, i, Context.LOOT);
		return it;
	}
	
	
	//Remove an item (reduce stack size) by passing that item in
	public int remove(Item item)
	{
		int removed = 0;
		
		for (int i = 0; i < capacity; i++)
		{
			if (contents[i].equals(item) || contents[i] == item)
			{
				contents[i] = null;
				removed = contents[i].getStackSize();
			}
		}
		
		calcLength();
		
		return removed;
	}
	
	//GETTERS
	public Item get(int i)
	{
		return contents[i];
	}
	
	public int length()
	{
		calcLength();
		return length;
	}
	
	public Item[] getContents() {
		return contents;
	}

	public int getCapacity() {
		return capacity;
	}

	public Entity getOwner() {
		return owner;
	}
	
	
}
