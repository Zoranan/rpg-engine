package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/*
 * This class handles all of the image loading.
 */

public class ImageLoader {
	private static HashMap<String, BufferedImage> loadedImages = new HashMap<String, BufferedImage>();
	
	public static BufferedImage loadImage (String path)
	{
		int attempt = 0;
		final int MAX_ATTEMPTS = 2;
		BufferedImage bi = null;
		
		while (bi == null && attempt < MAX_ATTEMPTS)
		{
			try 
			{
				bi = ImageIO.read(new File(path));
			} 
			catch (Exception e) 
			{
				path = Handler.getRootDirectory() + path;
				attempt++;
			}
		}
		return bi;
	}
	
	public static BufferedImage loadImageFullPath (String path)
	{
		try 
		{
			File imgFile = new File(path);
			return ImageIO.read(imgFile);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	//Stores loaded resources for later use
	public static BufferedImage loadResource(String path)
	{
		try
		{
			if (!loadedImages.containsKey(path))
				loadedImages.put(path, ImageIO.read(ImageLoader.class.getResource(path)));
			
			return loadedImages.get(path);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static ImageIcon loadResourceIcon(String path)
	{
		return new ImageIcon(loadResource(path));
	}

}
