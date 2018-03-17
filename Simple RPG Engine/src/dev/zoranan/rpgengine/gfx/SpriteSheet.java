package dev.zoranan.rpgengine.gfx;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jdom2.Element;

import dev.zoranan.rpgengine.util.FpsTimer;

/*
 * This class holds our in game images
 * If we load a sheet with many images, it chops them up into frames
 */

public class SpriteSheet {
	
	private BufferedImage sheet;
	private ArrayList<BufferedImage> frames;
	private int width = 0;
	private int height = 0;
	private int fps = 0;
	
	//CONSTRUCTORS
	//Pass in a loaded sheet
	public SpriteSheet (BufferedImage sheet)
	{
		this.sheet = sheet;
		frames = new ArrayList<BufferedImage>();
		frames.add(sheet);
	}
	
	//Pass in the path to a sheet
	public SpriteSheet (String sheetURL)
	{
		this (ImageLoader.loadImage(sheetURL));
	}
	
	//Finally, pass in an element from the sprites.xml
	public SpriteSheet (Element e)
	{
		frames = new ArrayList<BufferedImage>();
		
		//Get the information from our element
		String path = e.getChildText("src");
		int w = Integer.parseInt(e.getChild("width").getValue());
		int h = Integer.parseInt(e.getChild("height").getValue());
		this.fps = Integer.parseInt(e.getChild("fps").getValue());
		
		//Load the image
		this.sheet = ImageLoader.loadImage(path);
		
		frames.add(sheet);
		//Crop the frames
		this.cropFrames(w, h);
	}
	
	//Crop out any image you want
	public BufferedImage crop (int x, int y, int w, int h)
	{
		return sheet.getSubimage(x, y, w, h);
	}
	
	//Separates the sprite sheet into frames!
	public void cropFrames(int frameWidth, int frameHeight)
	{
		frames.clear();
		
		//Start at the top left corner of the sprite sheet
		int x = 0;
		int y = 0;
		
		//While we are within the Y image boundary...
		while ((y + frameHeight) <= sheet.getHeight())
		{
			//Begin reading all sprites in the row until we reach the end of the image
			while ((x + frameWidth) <= sheet.getWidth())
			{
				frames.add(this.crop(x, y, frameWidth, frameHeight));
				x += frameWidth;
			}
			//Go to the beginning of the next row
			y += frameHeight;
			x = 0;
		}	//If there is no row below the last one, END here.
		
		//Lets set the width and height variables
		this.width = frameWidth;
		this.height = frameHeight;
	}
	
	//Returns a specific frame
	public BufferedImage getFrame(int i)
	{
		if (i < frames.size())
			return frames.get(i);
		else if (frames.size() > 0)
			return frames.get(0);
		else
			return null;
	}
	
	//gets the number of frames
	public int size()
	{
		return frames.size();
	}
	
	public BufferedImage getImage()
	{
		return this.sheet;
	}

	public int getFps() {
		return fps;
	}

	public void setFps(int fps) {
		this.fps = fps;
	}
	
	public int getWidth()
	{
		if (width == 0)
			width = frames.get(0).getWidth();
		
		return width;
	}
	
	public int getHeight()
	{
		if (height == 0)
			height = frames.get(0).getHeight();
		
		return height;
	}
}

