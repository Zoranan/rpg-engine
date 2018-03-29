package dev.zoranan.rpgengine.gfx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

/*
 * This class handles all of the image loading.
 * Currently, all loading is automatically done as a resource.
 */

public class ImageLoader {
	public static BufferedImage loadImage (String path)
	{
		Path res = Paths.get("res/");
		Path p = Paths.get(path);
		res.normalize();
		p.normalize();
		
		//Correct any improper file paths
		if (!p.startsWith(res))
			p = Paths.get(res.toString(), p.toString());
		
		File f = p.toFile();
		
		try 
		{
			if (f.exists())
			{
				return ImageIO.read(f);
			}
			else
			{
				return  ImageIO.read(ImageLoader.class.getResource(path));
			}
		} 
		//Cant access or load image
		catch (IOException e) 
		{
			System.err.println("Could not load img " + path);
			e.printStackTrace();
			//System.exit(1);
		}
		//Cant even find the file path
		catch (IllegalArgumentException e)
		{
			//Try to load the warning icon
			try 
			{
				System.err.println("Could not find " + path);
				return  ImageIO.read(ImageLoader.class.getResource("/warning.png"));
			}
			//Cant load the warning!?
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
		return new BufferedImage(255,255,0);
	}

}
