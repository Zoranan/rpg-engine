package dev.zoranan.rpgengine.util;

import java.util.HashMap;
import java.util.Map.Entry;

import dev.zoranan.utils.mathString.MathString;
import dev.zoranan.utils.TextValidator;

public interface ScriptExecuter {
	public default double exec(String code)
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
		for (Entry<String, String[]> e : vars.entrySet())
		{	
			//Replace the fully qualified name of each variable with whatever we find in our lookup function
			code = code.replaceAll(e.getKey(), getVar(e.getValue()));
		}
		
		return MathString.eval(code);
	}
	
	//Everything defaults to 0
	public default String getVar(String[] parts)
	{
		return "0";
	}
}
