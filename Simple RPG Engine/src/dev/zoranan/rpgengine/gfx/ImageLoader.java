package dev.zoranan.rpgengine.gfx;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/*
 * This class handles all of the image loading.
 * Currently, all loading is automatically done as a resource.
 */

public class ImageLoader {
	public static BufferedImage loadImage (String path)
	{
		try 
		{
			System.out.println("Trying to read from " + path);
			return  ImageIO.read(ImageLoader.class.getResource(path));
			//return  ImageIO.read(new File(path));
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
