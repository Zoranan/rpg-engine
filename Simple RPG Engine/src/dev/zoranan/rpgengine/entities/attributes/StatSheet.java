package dev.zoranan.rpgengine.entities.attributes;

import java.util.HashMap;

/*
 * This class holds our stats, and buffs for those stats (soon)
 * 
 */

public class StatSheet {
	private static final int STARTING_LEVEL = 1;
	private static final int MAXIMUM_LEVEL = 10;
	
	//All stats (Needs to be loaded from XML)
	private HashMap<String, Stat> statMap;
	private static final String[] statNames = {"Health", "Mana", 
												"Strength", "Toughness",
												"Agility", "Speed",
												"Intelligence", "Wit",
												"Stabbing", "Slashing", "Crushing",
												"Elemental", "Arcane", "Life",
												"Archery", "Throwing", "BeastTaming"};
	
	
	//This whole setup is temporary. We need to load this information from an XML
	public StatSheet()
	{
		statMap = new HashMap<String, Stat>();
		//Derived Stats
		for (String s : statNames)
		{
			statMap.put(s, new Stat(s, STARTING_LEVEL, MAXIMUM_LEVEL));
		}
		
		//Special Cases
		statMap.get("Health").setMax(20);
		statMap.get("Mana").setMax(5);
	}
	
	public Stat get(String key)
	{
		return statMap.get(key);
	}
}