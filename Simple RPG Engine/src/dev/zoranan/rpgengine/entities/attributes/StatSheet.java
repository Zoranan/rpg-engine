package dev.zoranan.rpgengine.entities.attributes;

import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

import dev.zoranan.rpgengine.util.Assets;

/*
 * This class holds our stats, and buffs for those stats (soon)
 * 
 */

public class StatSheet {
	private static HashMap<String, Stat> defaultStats;
	private HashMap<String, Stat> statMap;
	/*
	private static final String[] statNames = {"Health", "Mana", 
												"Strength", "Toughness",
												"Agility", "Speed",
												"Intelligence", "Wit",
												"Stabbing", "Slashing", "Crushing",
												"Elemental", "Arcane", "Life",
												"Archery", "Throwing", "BeastTaming"};*/
	
	
	//This whole setup is temporary. We need to load this information from an XML
	public StatSheet()
	{
		if (defaultStats == null)
			loadDefaults();
		
		statMap = new HashMap<String, Stat>();
		statMap.putAll(defaultStats);
	}
	
	public void loadDefaults()
	{
		defaultStats = new HashMap<String, Stat>();
		List<Element> stats = Assets.getVariables("stats").getChildren();
		int value;
		String name, type;
		
		for (Element e : stats)
		{
			name = e.getAttributeValue("name");
			type = e.getName();
			try {
				value = Integer.parseInt(e.getAttributeValue("value"));
			}
			catch(Exception ex)
			{
				value = 0;
			}
			defaultStats.put(name, new Stat ("", value, value));
		}
	}
	
	public Stat get(String key)
	{
		return statMap.get(key);
	}
}