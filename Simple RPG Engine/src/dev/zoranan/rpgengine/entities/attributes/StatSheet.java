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
	public StatSheet(Element stats)
	{
		statMap = new HashMap<String, Stat>();
		if (stats == null)
			loadStats(Assets.getVariables("stats"));
		else
			loadStats(stats);
	}
	
	public StatSheet ()
	{
		this(null);
	}
	
	public void loadStats(Element statsEle)
	{
		statMap.clear();
		List<Element> stats = statsEle.getChildren();
		int value;
		String name;
		
		for (Element e : stats)
		{
			name = e.getAttributeValue("name");
			try {
				value = Integer.parseInt(e.getAttributeValue("value"));
			}
			catch(Exception ex)
			{
				value = 0;
			}
			statMap.put(name, new Stat (name, value, value));
		}
	}
	
	public Stat get(String key)
	{
		return statMap.get(key);
	}
}