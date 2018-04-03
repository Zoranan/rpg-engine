package dev.zoranan.rpgengine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import dev.zoranan.rpgengine.entities.Mob;
import dev.zoranan.utils.mathString.MathString;
import dev.zoranan.utils.TextValidator;

public class ScriptInterpreterClass {
	
	public static double interpret(String code, Object caller)
	{
		code = code.replaceAll("\\s", ""); //Remove whitespace
		//Lets pull any variable names out of our code
		HashMap<String, String[]> vars = new HashMap<String, String[]>();
		String varName = "";
		char c;
		char last = code.charAt(0);
		
		for (int i = 0; i < code.length(); i++)
		{
			c = code.charAt(i);	//Current character
			
			//Add to the current variable name
			if (!MathString.isOperator(c) && (TextValidator.isNotNumericDecimal(last) && 
				!MathString.isOperator(last) || TextValidator.isNotNumericDecimal(c)))
			{
				varName += c;
			}
			//Store the completed variable name, and its split parts
			else if (!varName.isEmpty())
			{
				if (!vars.containsKey(varName))
					vars.put(varName, varName.split("\\."));
				
				varName = "";
			}
			
			last = c;
		}
		
		//Now we need to replace our variables with numbers
		//Interpreter functions different depending on where the code is called from
		if (caller instanceof Mob)
		{	
			String actualValue;
			for (Entry<String, String[]> e : vars.entrySet())
			{
				String[] parts = e.getValue();
				if (parts[0].equals("stats"))
				{
					actualValue = Integer.toString(((Mob) caller).getStatValue(parts[1]));
					code = code.replaceAll(e.getKey(), actualValue);
				}
			}
		}
		return MathString.eval(code);
	}
}
