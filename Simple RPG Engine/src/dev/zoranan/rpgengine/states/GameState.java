package dev.zoranan.rpgengine.states;

import java.awt.Graphics;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.Human;
import dev.zoranan.rpgengine.entities.Mob;
import dev.zoranan.rpgengine.entities.behaviors.Player;
import dev.zoranan.rpgengine.gui.GUIViewSwitch;
import dev.zoranan.rpgengine.worlds.World;

public class GameState extends State{

	//private Mob player;
	private World testWorld;
	private Mob player;
	private GUIViewSwitch guiViewSwitch;
	
	public GameState(Handler handler)
	{
		super (handler);
		player = new Human("Zor", handler, 300, 300, 50, 75);
		player.setBehavior(new Player(player, handler));
		handler.setPlayer(player);
		//Set the camera on the target
		handler.getGameCamera().setTarget(player);
		
		//World stuff
		testWorld = new World("testTown", handler);
		handler.setWorld(testWorld);
		
		//GUI setup
		guiViewSwitch = new GUIViewSwitch(handler);
		
	}
	
	public World getWorld()
	{
		return testWorld;
	}
	
	@Override
	public void update()
	{
		testWorld.update();
		guiViewSwitch.update();
	}
	
	@Override
	public void render(Graphics g)
	{
		testWorld.render(g);
		
		//GUI ITEMS RENDERED HERE
		guiViewSwitch.render(g);
	}

}
