package dev.zoranan.rpgengine.entities.attributes;

import java.util.HashMap;

public class StatSheet {
	private static final int STARTING_LEVEL = 1;
	
	//All stats
	private HashMap<String, Stat> statMap;
	
	private static final String[] statNames = {"Health", "Mana", 
												"Strength", "Toughness",
												"Agility", "Speed",
												"Intelligence", "Wit",
												"Stabbing", "Slashing", "Crushing",
												"Elemental", "Arcane", "Life",
												"Archery", "Throwing", "BeastTaming"};
	
	public StatSheet()
	{
		statMap = new HashMap<String, Stat>();
		//Derived Stats
		for (String s : statNames)
		{
			statMap.put(s, new Stat(s, STARTING_LEVEL));
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