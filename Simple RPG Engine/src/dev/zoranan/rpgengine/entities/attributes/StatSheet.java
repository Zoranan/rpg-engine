package dev.zoranan.rpgengine.entities.attributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jdom2.Element;

import dev.zoranan.rpgengine.entities.Mob;
import dev.zoranan.rpgengine.util.Assets;
import dev.zoranan.utils.TextValidator;

/*
 * This class holds our stats, and buffs for those stats (soon)
 * 
 */

public class StatSheet {
	private HashMap<String, String> calculatedStats;
	private HashMap<String, Stat> statMap;
	private Mob mob;
	
	/*
	 * We pass in our stats element from our xml file, along with a reference to our parent mob.
	 * This gives us access to script execution which we use for calculated stats
	 */
	public StatSheet(Element stats, Mob mob)
	{
		this.mob = mob;
		statMap = new HashMap<String, Stat>();
		calculatedStats = new HashMap<String, String>(); 
		
		if (stats == null)
			loadStats(Assets.getVariables("stats"));
		else
			loadStats(stats);
	}
	
	//In this case, there are no customized stats, everything is the default values found in the vars.xml file
	public StatSheet (Mob mob)
	{
		this(null, mob);
	}
	
	//Load stats from our xml file
	public void loadStats(Element statsEle)
	{
		statMap.clear();
		calculatedStats.clear();
		
		List<Element> stats = statsEle.getChildren();
		int value;
		String name;
		
		for (Element e : stats)
		{
			name = e.getAttributeValue("name");
			
			if (TextValidator.isNumeric(e.getAttributeValue("value")))
			{
				try {
					value = Integer.parseInt(e.getAttributeValue("value"));
				}
				catch(Exception ex)
				{
					value = 0;
				}
				statMap.put(name, new Stat (name, value, value));
			}
			//Create a calculated stat
			else
			{
				calculatedStats.put(name, e.getAttributeValue("value"));
				statMap.put(name, new Stat (name, 1, 1));
			}
		}
	}
	
	//Re-calculate the values of any stats that are determined by other variables
	public void calculateStats()
	{
		double percent;
		
		for (Entry<String, String> e : calculatedStats.entrySet())
		{
			Stat s = statMap.get(e.getKey());
			percent = s.calcDecimal();
			s.setMax((int) mob.exec(e.getValue()));
			s.set((int) (s.getMax() * percent));
		}
	}
	
	public Stat get(String key)
	{
		return statMap.get(key);
	}
}