package dev.zoranan.rpgengine.gfx;

import java.awt.image.BufferedImage;

import dev.zoranan.rpgengine.util.Assets;
import dev.zoranan.rpgengine.util.FpsTimer;

public class Sprite {
	private FpsTimer fpsTimer;
	private String spriteSheetID;
	private int frameNum = 0;
	private int size = 0;
	
	public Sprite(String spriteSheetID)
	{
		this.spriteSheetID = spriteSheetID;
		fpsTimer = new FpsTimer(Assets.getSpriteSheet(spriteSheetID).getFps());
		this.size = Assets.getSpriteSheet(spriteSheetID).size();
	}
	
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
