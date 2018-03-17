package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
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
	
	public static BufferedImage loadResource(String path)
	{
		try
		{
			return ImageIO.read(ImageLoader.class.getResource(path));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

}
