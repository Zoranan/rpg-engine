package dev.zoranan.rpgengine.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/*
 * This class exists to load font files
 */

public class FontLoader {
	
	public static Font loadFont(String path, int size)
	{
		//Attempt to load a font. Exit if we fail
		try 
		{
			//return (Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(Font.PLAIN, size));
			return (Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream(path)).deriveFont(Font.PLAIN, size));
		} 
		catch (FontFormatException | IOException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

}
