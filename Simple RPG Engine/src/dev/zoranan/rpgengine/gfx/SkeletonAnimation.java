package dev.zoranan.rpgengine.gfx;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jdom2.Element;

import dev.zoranan.rpgengine.util.Assets;

public class SkeletonAnimation {
	private HashMap<String, ArrayList<Bone>> limbs;
	private ArrayList<String> renderOrder;
	private Skeleton parent;
	private String angle;
	private int length = 0;
	
	private int triggerFrame = 0;
	private boolean triggered = false;

	//Constructor creates an object from an XML element
	public SkeletonAnimation(Element e)
	{
		limbs = new HashMap<String, ArrayList<Bone>>();
		renderOrder = new ArrayList<String>();
		loadAnim(e);
	}
	
	//Copy constructor
	public SkeletonAnimation (SkeletonAnimation skelAnim, Skeleton parent)
	{
		this.limbs = skelAnim.limbs;
		this.renderOrder = skelAnim.renderOrder;
		this.angle = skelAnim.angle;
		this.length = skelAnim.length;
		this.triggerFrame = skelAnim.triggerFrame;
		this.parent = parent;
	}
	
	
	//Render a specific frame
	public void render(Graphics2D g, int frame)
	{
		for (String s : renderOrder)
			if (parent.getModel(angle, s) != null)
				render(Assets.getSpriteSheet(parent.getModel(angle, s)).getFrame(0), limbs.get(s).get(frame), g);
	}
	
	//Render using information from a Bone object
	public void render(BufferedImage i, Bone b, Graphics2D g)
	{
		if (i != null)
		{
			AffineTransform at = new AffineTransform();
			at.translate((parent.getStartX() + b.x), (parent.getStartY() + b.y));
			at.scale(b.width / i.getWidth(), b.height / i.getHeight());
			at.rotate(Math.toRadians(b.rotation));
			
			g.drawImage(i, at, null);
		}
		//g.drawImage(i, (int)(parent.getStartX() + b.x), (int)(parent.getStartY() + b.y), 
		//			(int) b.width, (int) b.height, null);
	}
	
	//
	public void addLimb(String name, ArrayList<Bone> boneList)
	{
		limbs.put(name, boneList);
	}
	
	public void setParent (Skeleton p)
	{
		this.parent = p;
	}
	
	
	//LOADING AND CREATION
	public void loadAnim(Element e)
	{
		List<Element> frameElements = e.getChildren();
		length = 0;
		Element last = null;
		renderOrder.clear();
		
		this.angle = e.getAttributeValue("angle");
		
		//Check for a trigger frame
		try 
		{
			this.triggerFrame = Integer.parseInt(e.getAttributeValue("trigger"));
		}
		catch (Exception ex)
		{
			this.triggerFrame = 0;
		}
		
		/*
		//Get our key frames
		for (Element frame : frameElements)
		{
			List<Element> limbEle = frame.getChildren();
			
			//If its our first frame, or the last frame came directly before the current one, just add the frame
			if (last == null || Integer.parseInt(frame.getAttributeValue("n")) - Integer.parseInt(last.getAttributeValue("n")) == 1)
			{
				for (Element currentBone : limbEle)
				{
					if (!limbs.containsKey(currentBone.getName()))
						limbs.put(currentBone.getName(), new ArrayList<Bone>());
					
					limbs.get(currentBone.getName()).add(createBone(currentBone));
					length++;
				}
				
				//set the render order
				if (renderOrder.isEmpty())
					for (Element currentBone : limbEle)
						renderOrder.add(currentBone.getName());
			}
			//Otherwise, start tweening
			else
			{
				int frameGap = Integer.parseInt(frame.getAttributeValue("n")) - Integer.parseInt(last.getAttributeValue("n"));
				
				while (frameGap >= 1)
				{
					//TWEEN
					
					for (Element currentBone : limbEle)
					{	
						length = limbs.get(currentBone.getName()).size();
						
						limbs.get(currentBone.getName()).add(tween(limbs.get(currentBone.getName()).get(length -1),
								createBone(currentBone), frameGap));
						
						
					}
					frameGap--;
				}
			}
			last = frame;
		}*/
		
		//set the render order
		if (renderOrder.isEmpty())
			for (Element currentBone : frameElements.get(0).getChildren())
				renderOrder.add(currentBone.getName());
		
		for (String s : renderOrder)
		{
			last = null;
			
			for (Element frame : frameElements)
			{
				Element limbEle = frame.getChild(s);
				
				if (limbEle == null)
					continue;
			
			//If its our first frame, or the last frame came directly before the current one, just add the frame
				if (last == null || Integer.parseInt(frame.getAttributeValue("n")) - Integer.parseInt(last.getAttributeValue("n")) == 1)
				{
					if (!limbs.containsKey(limbEle.getName()))
						limbs.put(limbEle.getName(), new ArrayList<Bone>());
						
					limbs.get(limbEle.getName()).add(createBone(limbEle));
					length++;
				}
					
			
				//Otherwise, start tweening
				else
				{
					int frameGap = Integer.parseInt(frame.getAttributeValue("n")) - Integer.parseInt(last.getAttributeValue("n"));
					
					while (frameGap >= 1)
					{
						//TWEEN	
						length = limbs.get(limbEle.getName()).size();
							
						limbs.get(limbEle.getName()).add(tween(limbs.get(limbEle.getName()).get(length -1),
								createBone(limbEle), frameGap));
							
						frameGap--;
					}//End while
				}//End Else	
				last = frame;
			}//End frame for
		}//End renderOrder for
	}//END loadAnim function
	
	//Creates a bone from an element
	private Bone createBone(Element e)
	{
		return new Bone(Float.parseFloat(e.getAttributeValue("x")), Float.parseFloat(e.getAttributeValue("y")),
						Float.parseFloat(e.getAttributeValue("w")), Float.parseFloat(e.getAttributeValue("h")),
						Float.parseFloat(e.getAttributeValue("r")));
	}
	
	//Creates in-between frames 
	private Bone tween(Bone b1, Bone b2, int frames)
	{
		Bone tweenFrame = b1.difference(b2);
		tweenFrame = tweenFrame.divide((float) frames);
		return b1.add(tweenFrame);
	}
		
	public int length()
	{
		return length;
	}
	
	public int getTrigger()
	{
		return triggerFrame;
	}
	
	public boolean triggered()
	{
		return triggered;
	}
	
	public void setTrigger(boolean b)
	{
		triggered = b;
	}
}
