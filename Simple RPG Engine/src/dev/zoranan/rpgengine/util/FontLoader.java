package dev.zoranan.rpgengine.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * This class exists to load font files
 */

public class FontLoader {
	
	public static Font loadFont(String path, int size)
	{
		//Attempt to load a font. Exit if we fail
		try 
		{
			Path p = Paths.get(path);
			Path res = Paths.get("res/");
			p.normalize();
			res.normalize();
			if (!p.startsWith(res))
				p = Paths.get(res.toString(), p.toString());
			
			File f = p.toFile();
			if (f.exists())
			{
				System.out.println("Loading font from file");
				return (Font.createFont(Font.TRUETYPE_FONT, f).deriveFont(Font.PLAIN, size));
			}
			else
			{
				System.out.println("Loading font from resource");
				return (Font.createFont(Font.TRUETYPE_FONT, FontLoader.class.getResourceAsStream(path)).deriveFont(Font.PLAIN, size));
			}
		} 
		catch (FontFormatException | IOException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

}
