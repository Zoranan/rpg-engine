package dev.zoranan.rpgengine.gui.menus;

import java.awt.Graphics;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.audio.SoundPlayer;
import dev.zoranan.rpgengine.gui.ClickListener;
import dev.zoranan.rpgengine.gui.GUIFrame;
import dev.zoranan.rpgengine.gui.GUIManager;
import dev.zoranan.rpgengine.gui.GUITextButton;
import dev.zoranan.rpgengine.states.State;

//Menu Layout creator class.
//Handles the main menu, and swapping to all the different portions of the main menu.

public class MainMenu {
	
	private Handler handler;
	private GUIManager guiManager;
	private SoundPlayer sound;
	
	//The first list of buttons on the menu
	private GUIFrame guiFrame1;
	
	public MainMenu(Handler handler)
	{
		this.handler = handler;
		guiManager = new GUIManager(handler);
		handler.getMouseManager().setGuiManager(guiManager);
		
		init();
	}
	
	//Initializes our main menu
	public void init()
	{
		sound = new SoundPlayer();
		int posX = (handler.getWidth() / 2) - 100;
		int posY = 200;
		guiFrame1 = new GUIFrame(posX, posY);
		//New Game Button
		guiFrame1.add(new GUITextButton(0, 0, 200, 75, "New Game", new ClickListener(){

			@Override
			public void onClick() {
				//sound.playOnce("swing1");
				//sound.playOnce("/sound/sfx/battle/sword-unsheathe.wav");
				//sword-unsheathe
				handler.getMouseManager().setGuiManager(null);
				State.setState(handler.getGame().gameState);
			}
		}));
		
		//Set our starting menu state
		guiManager.add(guiFrame1);
	}
	
	public void update()
	{
		guiManager.update();
	}
	
	public void render(Graphics g)
	{
		guiManager.render(g);
	}

}
