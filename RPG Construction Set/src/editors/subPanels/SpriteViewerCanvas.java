package editors.subPanels;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import gameObjects.SpriteSheet;

///////////////////////////////
//	SPRITE VIEWER CANVAS
//
/*	This class handles the graphical editing of Environmental entity properties
 *	Over top of the sprite tied to it.
 *
 *	When an edit mode is set, this canvas will allow the user to draw
 *	the entities hit box, total bounds box, and depth reference line. 
 * */

@SuppressWarnings("serial")
public class SpriteViewerCanvas extends Canvas
{
	public enum EditMode {NONE, DEPTH, HIT_BOUNDS, TOUCH_BOUNDS}
	public final int PADDING = 30;
	
	private EditMode editMode = EditMode.NONE;
	private SpriteSheet sprite;
	private int w = 0;
	private int h = 0;
	private Graphics2D g;
	//Stored values
	private int depthReference = 0;
	private boolean dragging = false;
	private Rectangle solidBounds;
	private Rectangle totalBounds;
	private Point mousePoint;
	
	//The constructor
	public SpriteViewerCanvas()
	{	
		//Initialize our entity properties
		solidBounds = new Rectangle (0,0,0,0);
		totalBounds = new Rectangle (0,0,0,0);
		mousePoint = new Point(0,0);
		
		//There are lots of listeners for this class
		
		this.addMouseListener(new MouseListener()
		{
			//Since some of our properties involve dragging the mouse
			//to draw boxes, we must set begin setting them up on mouse pressed
			@Override
			public void mousePressed(MouseEvent e) 
			{
				//on the initial mouse press (before release) we set our
				//bounds initial coordinates if we are in a bounds edit mode
				if (editMode == EditMode.HIT_BOUNDS)
				{
					solidBounds.x = e.getX();
					solidBounds.y = e.getY();
				}
				else if (editMode == EditMode.TOUCH_BOUNDS)
				{
					totalBounds.x = e.getX();
					totalBounds.y = e.getY();
				}
				dragging = true;
			}

			//All we have to do on the mouse release set our edit view to NONE
			//All edits end with either a click, or a drag and release.
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				//This function also sets dragging = false
				setEditMode(SpriteViewerCanvas.EditMode.NONE);
			}
			
			//These events aren't used
			@Override
			public void mouseClicked(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
			
		}); //End mouse listener (clicks)
		
		//This listener is used to get the mouse position relative to this element in real time
		this.addMouseMotionListener(new MouseMotionListener()
		{
			//During a drag event, we are probably trying to draw a boundary property
			//using this event allows us to see the bound as we draw it, if we are in its edit mode
			@Override
			public void mouseDragged(MouseEvent e) 
			{
				mousePoint = e.getPoint();
				
				//Calculate the width for the bound we are editing based on its mouse position
				if (editMode == EditMode.HIT_BOUNDS && dragging)
				{
					solidBounds.width = e.getX() - solidBounds.x;
					solidBounds.height = e.getY() - solidBounds.y;
				}
				else if (editMode == EditMode.TOUCH_BOUNDS && dragging)
				{
					totalBounds.width = e.getX() - totalBounds.x;
					totalBounds.height = e.getY() - totalBounds.y;
				}
			}

			//This is only used when we are selecting the depth reference, since dragging isnt involved
			@Override
			public void mouseMoved(MouseEvent e) 
			{
				mousePoint = e.getPoint();
				if (editMode == EditMode.DEPTH)
				{
					depthReference = mousePoint.y;
				}
			}
		});	//End mouse movement listener
	}
	
	public void render()
	{
		//Get our buffer strategy
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null)
		{
			this.createBufferStrategy(3);
			bs = this.getBufferStrategy();
		}
		
		//Get out graphics object. Using g2d for transparency (not sure if I need to)
		g = (Graphics2D) bs.getDrawGraphics();
		
		//Erase the canvas
		g.clearRect(0, 0, w, h);
		
		//Draw total bounds
		g.setColor(new Color(255, 253, 170, 200)); //Light yellow
		g.fillRect(totalBounds.x, totalBounds.y, totalBounds.width, totalBounds.height);
		g.setColor(Color.BLACK);
		g.drawRect(totalBounds.x, totalBounds.y, totalBounds.width, totalBounds.height);
		
		//sprite render
		if (sprite != null)
			g.drawImage(sprite.getFrame(0), PADDING / 2, PADDING / 2, sprite.getWidth(), sprite.getHeight(), null);
		
		
		//Draw solid bounds
		g.setColor(new Color(255, 214, 214, 200));	//Light red / pink
		g.fillRect(solidBounds.x, solidBounds.y, solidBounds.width, solidBounds.height);
		g.setColor(Color.BLACK);
		g.drawRect(solidBounds.x, solidBounds.y, solidBounds.width, solidBounds.height);
		
		g.setColor(Color.RED);
		//Draw depthLine (horizontal)
		g.drawLine(0, depthReference, w, depthReference);
		
		//Draw Canvas border
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, w-1, h-1);
		
		//END draw
		bs.show();
		g.dispose();
		
	}
	
	//Sets the sprite, and resizes the object
	public void setSprite(SpriteSheet ss)
	{
		this.sprite = ss;
		
		if (this.sprite != null)
		{
			this.w = ss.getWidth() + PADDING;
			this.h = ss.getHeight() + PADDING;
			
			this.setPreferredSize(new Dimension(w,h));
			this.setSize(w, h);
			
			this.validate();
			this.repaint();
			
			this.render();
		}
	}
	
	/////////////////
	//GETTERS AND SETTERS
	
	//Helper method for the rectangles
	private String rectToString(Rectangle r)
	{
		String s = "";
		s += (r.x - (PADDING / 2)) + "," + (r.y - (PADDING / 2)) + "," + r.width + "," + r.height;
		return s;
	}
	
	//Takes in a string and creates a rectangle
	private Rectangle stringToRect(String s)
	{
		Rectangle r = new Rectangle(0,0,0,0);
		String[] stringSplit = s.split(",");
		ArrayList<Integer> rectDim = new ArrayList<Integer>(4);
		
		if (stringSplit.length == 4)
		{
			for (int i = 0; i < stringSplit.length; i++)
			{
				try{
					rectDim.add(Integer.parseInt(stringSplit[i]));
				}
				catch (Exception e)
				{
					rectDim.add(0);
				}
			}
			
			r.setBounds(rectDim.get(0) + (PADDING / 2), rectDim.get(1) + (PADDING / 2), rectDim.get(2), rectDim.get(3));
		}
		
		return r;
	}
	
	//Set the edit mode we are in
	public void setEditMode(EditMode em)
	{
		//Null values are accepted as NONE
		if (em == null)
			em = EditMode.NONE;
		
		editMode = em;
		dragging = false;
		
		//Reset values we are editing
		if (editMode == EditMode.HIT_BOUNDS)
		{
			this.solidBounds.setBounds(0, 0, 0, 0);
		}
		else if (editMode == EditMode.TOUCH_BOUNDS)
		{
			this.totalBounds.setBounds(0, 0, 0, 0);
		}
	}
	
	public void setDepthRegister(int i)
	{
		this.depthReference = i + (PADDING / 2);
	}
	
	public void setSolidBounds(String s)
	{
		this.solidBounds = this.stringToRect(s);
	}
	
	public void setTotalBounds(String s)
	{
		this.totalBounds = this.stringToRect(s);
	}
	
	
	public EditMode getEditMode()
	{
		return editMode;
	}

	public int getDepthReference() {
		return depthReference - (PADDING / 2);
	}

	public String getSolidBounds() {
		return this.rectToString(solidBounds);
	}

	public String getTotalBounds() {
		return this.rectToString(totalBounds);
	}
	
	public Rectangle getSolidBoundsRect() {
		return new Rectangle (solidBounds.x - (PADDING / 2), solidBounds.y - (PADDING / 2), solidBounds.width, solidBounds.height);
	}

	public Rectangle getTotalBoundsRect() {
		return new Rectangle (totalBounds.x - (PADDING / 2), totalBounds.y - (PADDING / 2), totalBounds.width, totalBounds.height);
	}
	
}
