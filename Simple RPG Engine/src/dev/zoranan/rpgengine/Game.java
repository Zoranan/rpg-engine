package dev.zoranan.rpgengine;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import dev.zoranan.rpgengine.display.Display;
import dev.zoranan.rpgengine.gfx.GameCamera;
import dev.zoranan.rpgengine.input.KeyManager;
import dev.zoranan.rpgengine.input.MouseManager;
import dev.zoranan.rpgengine.states.GameState;
import dev.zoranan.rpgengine.states.MenuState;
import dev.zoranan.rpgengine.states.State;
import dev.zoranan.rpgengine.util.Assets;
import dev.zoranan.utils.FpsTimer;

public class Game implements Runnable {
	private Display display;
	private Thread thread;
	private boolean running = false;
	
	//Graphics
	private BufferStrategy bs;
	private Graphics g;
	
	//STATES
	public State gameState;
	public State menuState;
	
	//INPUT 
	private KeyManager keyManager;
	private MouseManager mouseManager;
	
	//CAMERA
	private GameCamera gameCamera;
	
	//HANDLER
	private Handler handler;
	
	//Window properties
	private int width, height;
	private String title;
	
	public Game (String title, int width, int height)
	{
		this.width = width;
		this.height = height;
		this.title = title;
		keyManager = new KeyManager();
		mouseManager = new MouseManager();
	}
	
	//Initial setup
	private void init()
	{
		display = new Display(title, width, height);
		
		//Set up input
		display.getFrame().addKeyListener(keyManager);
		display.getFrame().addMouseListener(mouseManager);
		display.getFrame().addMouseMotionListener(mouseManager);
		display.getCanvas().addMouseListener(mouseManager);
		display.getCanvas().addMouseMotionListener(mouseManager);
		
		//LOAD EXTERNAL FILES
		Assets.init();
		
		handler = new Handler(this);
		gameCamera = new GameCamera(handler, 0,0);
		
		//States
		gameState = new GameState(handler);
		menuState = new MenuState(handler);
		State.setState(menuState);
	}
	
	//Updates all values in all objects of the state
	private void update()
	{
		keyManager.update();
		
		if (State.getState() != null)
		{
			State.getState().update();
		}
	}
	
	//Redraws all graphics
	private void render()
	{
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null)
		{
			display.getCanvas().createBufferStrategy(3);
		}
		else
		{
			g = bs.getDrawGraphics();
			//Clear screen
			g.clearRect(0, 0, width, height);
			
			if (State.getState() != null)
			{
				State.getState().render(g);
			}
			
			//End draw.
			bs.show();
			g.dispose();
		}
	}
	
	//Our main game loop, in its own thread
	public void run()
	{
		init();
		FpsTimer timer = new FpsTimer(60);
		
		while (running)
		{
			
			if (timer.check())
			{
				update();
				render();
			}
		}
		
		stop();
	}
	
	//Starts out game loop
	public synchronized void start()
	{
		if (!running)
		{
			running = true;
			thread = new Thread(this);
			thread.start();
		}
	}
	
	//Stops the game loop as safe as possible
	public synchronized void stop()
	{
		if (running)
		{
			running = false;
			try 
			{
				thread.join();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	
	//GETTERS
	//Get the keyManager for control
	public KeyManager getKeyManager()
	{
		return keyManager;
	}
	
	//Get the MouseManager for control
	public MouseManager getMouseManager()
	{
		return mouseManager;
	}
	
	//Get the keyManager for control
	public GameCamera getGameCamera()
	{
		return gameCamera;
	}

	//Get game window width
	public int getWidth() {
		return width;
	}

	//get game window height
	public int getHeight() {
		return height;
	}

}
