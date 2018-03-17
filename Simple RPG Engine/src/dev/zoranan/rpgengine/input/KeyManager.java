package dev.zoranan.rpgengine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener{
	private boolean[] keys;
	private boolean up, down, left, right, space, F, I, K, ctrl;
	private boolean releasedMoveKey = false;
	private int[] toggleKeys = {KeyEvent.VK_F, KeyEvent.VK_I};
	
	//Constructor
	public KeyManager()
	{
		keys = new boolean[256];
	}
	
	//Update
	public void update()
	{
		up = keys[KeyEvent.VK_W];
		left = keys[KeyEvent.VK_A];
		down = keys[KeyEvent.VK_S];
		right = keys[KeyEvent.VK_D];
		space = keys[KeyEvent.VK_SPACE];
		F = keys[KeyEvent.VK_F];
		I = keys[KeyEvent.VK_I];
		K = keys[KeyEvent.VK_K];
		ctrl = keys[KeyEvent.VK_CONTROL];
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		if (!isToggleKey(e))
			keys[e.getKeyCode()] = true;
		
		else
			keys[e.getKeyCode()] = !keys[e.getKeyCode()];
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//Only release the key if its not a toggle key
		int key = e.getKeyCode();
		
		if (!isToggleKey(e))	
			keys[e.getKeyCode()] = false;
		
		if ((key == KeyEvent.VK_W || key == KeyEvent.VK_A || key == KeyEvent.VK_S || key == KeyEvent.VK_D)
				&& !up && !down && !left && !right)
			releasedMoveKey = true;
	}
	
	//Check to see if a key is meant to be toggled
	private boolean isToggleKey(KeyEvent e)
	{
		//look at all the toggle keys and see if our key matches
		for (int i = 0; i < toggleKeys.length; i++)
		{
			if (toggleKeys[i] == e.getKeyCode())
				return true;
		}
		return false;
		
	}
	
	//Reset the toggle key
	public void resetToggle()
	{
		for (int i = 0; i < toggleKeys.length; i++)
		{
			keys[toggleKeys[i]] = false;
		}
	}
	
	public void resetToggle(int e)
	{
			keys[e] = false;
	}
	
	//GET KEY PRESSES
	public boolean up()
	{
		return up;
	}
	
	public boolean down()
	{
		return down;
	}
	
	public boolean left()
	{
		return left;
	}
	
	public boolean right()
	{
		return right;
	}
	
	public boolean space()
	{
		return space;
	}
	
	public boolean ctrl()
	{
		return ctrl;
	}
	
	//A move key was released
	public boolean stoppedMoving()
	{
		boolean b = releasedMoveKey;
		releasedMoveKey = false;
		return b;
	}
	
	//I is toggled off by calling function
	public boolean F()
	{
		boolean f = F;
		resetToggle(KeyEvent.VK_F);
		return f;
	}
	
	//I is toggled off by calling function
	public boolean I()
	{
		boolean i = I;
		resetToggle(KeyEvent.VK_I);
		return i;
	}
	//K is toggled off by calling function
	public boolean K()
	{
		boolean k = K;
		resetToggle(KeyEvent.VK_K);
		return k;
	}

}
