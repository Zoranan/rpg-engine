package dev.zoranan.rpgengine.gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GUIFrame extends GUIObject{
	private ArrayList<GUIObject> elements;

	public GUIFrame(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	
	public GUIFrame(int x, int y) {
		super(x, y, 10, 10);
	}
	
	//Pass events on to all objects
	
	public void onMouseMove (MouseEvent e)
	{
		for (GUIObject o : elements)
			o.onMouseMove(e);
	}
		
	public void onMouseReleased (MouseEvent e)
	{
		for (GUIObject o : elements)
			o.onMouseReleased(e);
	}
		
		//
	
	//This function adds an object to our group of elements,
	//adjusting its position relative to this layers position.
	
	public void add(GUIObject o)
	{
		if (elements == null)
			elements = new ArrayList<GUIObject>();
		
		if (o.width > this.width)
			this.width = o.width;
		
		if (o.height > this.height)
			this.height = o.height;
		
		o.setPosX(this.posX + o.posX);
		o.bounds.x = o.posX;
		o.setPosY(this.posY + o.posY);
		o.bounds.y = o.posY;
		
		elements.add(o);
	}

	@Override
	public void update() {
		for (GUIObject e : elements)
		{
			e.update();
		}
	}

	@Override
	public void render(Graphics g) {
		for (GUIObject e : elements)
		{
			e.render(g);
		}
		
	}

	@Override
	public void onClick() {
		// TODO Auto-generated method stub
		
	}

}
