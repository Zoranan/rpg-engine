package dev.zoranan.rpgengine.gfx;

import java.awt.image.BufferedImage;

import dev.zoranan.rpgengine.util.Assets;
import dev.zoranan.rpgengine.util.FpsTimer;

/*
 * The Sprite class animates a spritesheet. This separation was necessary to allow each
 * sprite-sheet to only be loaded once, but allow several instances of the same sprite to 
 * be on different frames, with different timings
 */

public class Sprite {
	private FpsTimer fpsTimer;
	private String spriteSheetID;
	private int frameNum = 0;
	private int size = 0;
	
	//Creates a sprite by passing in a sprite-sheet id
	public Sprite(String spriteSheetID)
	{
		this.spriteSheetID = spriteSheetID;
		fpsTimer = new FpsTimer(Assets.getSpriteSheet(spriteSheetID).getFps());
		this.size = Assets.getSpriteSheet(spriteSheetID).size();
	}
	
	//Gets the current frame of our animation
	public BufferedImage get()
	{
		if (size > 0 && fpsTimer.check())
		{
			frameNum++;
			frameNum %= size;
		}
		return Assets.getSpriteSheet(spriteSheetID).getFrame(frameNum);
	}
	
	public SpriteSheet getSpriteSheet()
	{
		return Assets.getSpriteSheet(spriteSheetID);
	}
	
	public String getSpriteSheetID()
	{
		return spriteSheetID;
	}
}
