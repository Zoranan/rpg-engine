package dev.zoranan.rpgengine.gui;

import java.awt.Graphics;

import dev.zoranan.rpgengine.gfx.SpriteSheet;

public class GUIImageButton extends GUIObject {
	protected ClickListener clicker;
	
	public GUIImageButton(int x, int y, int w, int h, ClickListener clicker) {
		super(x, y, w, h);
		this.clicker = clicker;
		btnStates = new SpriteSheet("/textures/StartBtn.png");
		btnStates.cropFrames(200, 75);
	}

	private SpriteSheet btnStates;
	

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		if (hovering)
			g.drawImage(btnStates.getFrame(1), posX, posY, null);
		else
			g.drawImage(btnStates.getFrame(0), posX, posY, null);
	}

	@Override
	public void onClick() {
		clicker.onClick();
	}

}
