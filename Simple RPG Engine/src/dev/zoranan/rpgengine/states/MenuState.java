package dev.zoranan.rpgengine.states;

import java.awt.Graphics;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.gui.menus.MainMenu;

public class MenuState extends State{

	private MainMenu mainMenu;
	
	public MenuState(Handler handler) {
		super(handler);
		//guiManager = new GUIManager(handler);
		//handler.getMouseManager().setGuiManager(guiManager);
		mainMenu = new MainMenu(handler);
		
		/*
		guiManager.add(new GUIImageButton(0, 0, 200, 75, new ClickListener(){

			@Override
			public void onClick() {
				handler.getMouseManager().setGuiManager(null);
				State.setState(handler.getGame().gameState);
			}
		}));*/
		
	}

	@Override
	public void update() {
		mainMenu.update();
	}

	@Override
	public void render(Graphics g) {
		mainMenu.render(g);
		//Text.drawText(g, "Welcome", 640, 50, true, Color.BLACK, Assets.fontImmortal28);
	}

}
