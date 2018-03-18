package dev.zoranan.rpgengine.states;

import java.awt.Graphics;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.gui.menus.MainMenu;

/*
 * This State renders our main menu object
 */

public class MenuState extends State{

	private MainMenu mainMenu;
	
	public MenuState(Handler handler) {
		super(handler);
		mainMenu = new MainMenu(handler);
	}

	@Override
	public void update() {
		mainMenu.update();
	}

	@Override
	public void render(Graphics g) {
		mainMenu.render(g);
	}

}
