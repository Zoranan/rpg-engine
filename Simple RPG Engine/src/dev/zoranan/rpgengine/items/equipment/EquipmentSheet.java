package dev.zoranan.rpgengine.items.equipment;

import java.util.HashMap;

import dev.zoranan.rpgengine.items.Item;

/*
 * The equipment sheet class holds all equipped equipment for a mob.
 */

public class EquipmentSheet {
	private boolean changesMade = false;
	private HashMap<String, Item> equipment;

	public EquipmentSheet()
	{
		equipment = new HashMap<String, Item>();
		//The following needs to be loaded from a global variable XML file
		equipment.put("weapon", null);
		equipment.put("offHand", null);
		
		equipment.put("headGear", null);
		equipment.put("chestGear", null);
		equipment.put("handGear", null);
		equipment.put("legGear", null);
		equipment.put("footGear", null);
		
		equipment.put("ring1", null);
		equipment.put("ring2", null);
	}
	
	public Item setEquipped(Item i) {
		Item old = equipment.get(i.getGenericType());	//Gets the equipment that is currently equipped in the slot where the new equipment must go
		equipment.put(i.getGenericType(), i);	//Replace old equipment, with new
		changesMade = true;	//Flag that there have been changes to the equip sheet
		return old;	//give the old equipment back
	}
	
	public Item getEquipped(String type) {
		return equipment.get(type);
	}
	
	public Item unequip(String type)
	{
		Item old = equipment.get(type);
		equipment.put(type, null);
		changesMade = true;
		return old;
	}
	
	public boolean isEquipable(Item i)
	{
		return equipment.containsKey(i.getGenericType());
	}
	
	public final HashMap<String, Item> getEquipment()
	{
		return equipment;
	}
	
	public boolean checkForUpdates()
	{
		if (changesMade)
		{
			changesMade = false;
			return true;
		}
		else
			return false;
	}
	
}
