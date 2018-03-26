package dev.zoranan.rpgengine.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import dev.zoranan.rpgengine.gfx.Text;
import dev.zoranan.rpgengine.util.Assets;

public class GUITextButton extends GUIObject {
	protected ClickListener clicker;
	//private SpriteSheet btnStates;
	private BufferedImage image = Assets.woodBtn;
	private String text;
	
	private static final Color HOVER_COLOR =  Color.GRAY;
	private static final Color REG_COLOR = Color.BLACK;
	
	public GUITextButton(int x, int y, int w, int h, String text, ClickListener clicker) {
		super(x, y, w, h);
		this.text = text;
		this.clicker = clicker;
		//btnStates = new SpriteSheet("/textures/textBtn.png");
		
		//btnStates.cropFrames(200, 75);
	}
	

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		
		g.drawImage(this.image, this.posX, this.posY, null);
		
		if (hovering)
		{
			//g.drawImage(btnStates.getFrame(1), posX, posY, null);
			Text.drawText(g, text, this.posX + (this.width / 2), this.posY + (this.height / 2), true, HOVER_COLOR, Assets.fontImmortal28);
		}
		else
		{
			//g.drawImage(btnStates.getFrame(0), posX, posY, null);
			Text.drawText(g, text, this.posX + (this.width / 2), this.posY + (this.height / 2), true, REG_COLOR, Assets.fontImmortal28);
		}
	}

	@Override
	public void onClick() {
		clicker.onClick();
	}
}
