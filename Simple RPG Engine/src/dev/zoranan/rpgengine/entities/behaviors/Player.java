package dev.zoranan.rpgengine.entities.behaviors;


import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.*;
import dev.zoranan.rpgengine.input.KeyManager;
import dev.zoranan.rpgengine.input.MouseManager;

///////////////////////////////
//
//	Behavior: Player
//	This behavior is player controlled
//	Any mob placed into this behavior will be controllable
//
///////////////////////////////

public class Player extends Behavior{
	private KeyManager keyManager;
	private MouseManager mouseManager;
	//private World world;
	
	public Player(Mob mob, Handler handler)
	{
		super (mob, handler);
		this.keyManager = handler.getKeyManager();
		this.mouseManager = handler.getMouseManager();
	}

	@Override
	public void update() {
		//MOVEMENT CONTROL
		Mob.Direction dir;
		dir = Mob.Direction.STOP;
		
		//Check for a single direction first!
		if (keyManager.up())
			dir = Mob.Direction.NORTH;
		
		else if (keyManager.down())
			dir = Mob.Direction.SOUTH;
		
		else if (keyManager.right())
			dir = Mob.Direction.EAST;
		
		else if (keyManager.left())
			dir = Mob.Direction.WEST;
		
		//Now lets check for diagonal directions
		if (keyManager.up() && keyManager.right())
			dir = Mob.Direction.NORTH_EAST;
		
		else if (keyManager.down() && keyManager.right())
			dir = Mob.Direction.SOUTH_EAST;

		else if (keyManager.down() && keyManager.left())
			dir = Mob.Direction.SOUTH_WEST;

		else if (keyManager.up() && keyManager.left())
			dir = Mob.Direction.NORTH_WEST;
		
		//Attempt to move player in 'dir' direction
		mob.move(dir);
		
		//ACTIVATE AN ITEM
		if (keyManager.F())
		{
			mob.activateEntity();
		}
		
		//Attacking
		if (keyManager.space())
		{
			//Since the player is in the center of the screen, we will find our angle based on that
			//our angle is equal to the arctan of the difference in Y / the difference in X
			double dX = (handler.getWidth() / 2) - mouseManager.getMouseX();
			double dY = (handler.getHeight() / 2) - mouseManager.getMouseY();
			
			mob.startAttack(Math.atan2(dY, dX) - Math.PI / 2);
		}
	}
}
