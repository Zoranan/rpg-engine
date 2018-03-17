package dev.zoranan.rpgengine.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import dev.zoranan.rpgengine.Handler;

public class GUIManager {
	
	private Handler handler;
	private ArrayList<GUIObject> gui;
	
	public GUIManager(Handler handler)
	{
		this.handler = handler;
		gui = new ArrayList<GUIObject>();
	}
	
	//Pass events on to all objects
	
	public void onMouseMove (MouseEvent e)
	{
		for (GUIObject o : gui)
			o.onMouseMove(e);
	}
	
	public void onMouseReleased (MouseEvent e)
	{
		for (GUIObject o : gui)
			o.onMouseReleased(e);
	}
	
	//
	
	public void add(GUIObject e)
	{
		gui.add(e);
	}
	
	public void remove(GUIObject e)
	{
		gui.remove(e);
	}
	
	public void replace(GUIObject e)
	{
		gui.clear();
		add(e);
	}
	
	public int size()
	{
		return gui.size();
	}
	
	//UPDATE and RENDER all
	public void update()
	{
		for (GUIObject e : gui)
			e.update();
	}
	
	public void render(Graphics g)
	{
		for (GUIObject e : gui)
			e.render(g);
	}

}
