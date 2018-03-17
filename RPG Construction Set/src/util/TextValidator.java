package util;

public class TextValidator {
	
	
	//Check for an alpha character
	public static boolean isNotAlpha(char c)
	{
		//if ((e.getKeyChar() < '0' || e.getKeyChar() > '9') && 
		return (c < 'a' || c > 'z') && (c < 'A' || c > 'Z'); 
	}
	
	public static boolean isAlpha(char c)
	{
		return (!isNotAlpha(c));
	}
	
	//Check for a numeric character
	public static boolean isNotNumeric(char c)
	{
		return (c < '0' || c > '9');
	}
	
	public static boolean isNumeric(char c)
	{
		return (!isNotNumeric(c));
	}
	
	//Check for alpha numeric
	public static boolean isNotAlphaNumeric(char c)
	{
		return (isNotAlpha(c) && isNotNumeric(c));
	}
	
	public static boolean isAlphaNumeric(char c)
	{
		return (!isNotAlphaNumeric(c));
	}
	
	//Strings
	public static boolean isNumeric(String s)
	{
		boolean valid = true;
		
		for (int i = 0; i < s.length() && valid; i ++)
		{
			valid &= isNumeric(s.charAt(i));
		}
		
		return valid;
	}
	
	public static boolean isAlpha(String s)
	{
		boolean valid = true;
		
		for (int i = 0; i < s.length() && valid; i ++)
		{
			valid &= isAlpha(s.charAt(i));
		}
		
		return valid;
	}
	
	public static boolean isAlphaNumeric(String s)
	{	
		boolean valid = true;
		
		for (int i = 0; i < s.length() && valid; i ++)
		{
			valid &= isAlphaNumeric(s.charAt(i));
		}
		
		return valid;
	}
	
	//Eater function to eat unwanted characters
	
	public static String eatChars(String orig, char c)
	{
		String newString = "";
		
		for (int i = 0; i < orig.length(); i++)
		{
			if (orig.charAt(i) != c)
				newString += orig.charAt(i);
		}
		
		return newString;
	}
	
	public static String eatDoubleChars(String orig, char c)
	{
		String newString = "";
		char current;
		
		for (int i = 0; i < orig.length(); i++)
		{
			current = orig.charAt(i);
			if (current != c || (!newString.isEmpty() && orig.charAt(i-1) != c))
				newString += current;			
		}
		
		return newString;
	}

}
