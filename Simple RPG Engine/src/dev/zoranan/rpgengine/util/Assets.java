package dev.zoranan.rpgengine.util;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import dev.zoranan.rpgengine.gfx.ImageLoader;
import dev.zoranan.rpgengine.gfx.SkeletonAnimation;
import dev.zoranan.rpgengine.gfx.SpriteSheet;
import dev.zoranan.utils.XmlLoader;

/*
 * The assets class handles all of our File/Resource IO
 * The assets class exists to keep the same resource from being loaded repeatedly.
 * Once all XML documents are loaded, they are kept here for reference.
 */

public class Assets {
	//FINAL
	public static final int BOUNDS_COLOR = -16777216;
	
	//WARNING
	public static BufferedImage warning;
	
	//XML DOCUMENTS
	public static Document varsXML;
	public static Document spriteXML;
	public static Document modelsXML;
	public static Document envEntityXML;
	public static Document itemsXML;
	public static Document soundsXML;
	public static Document racesXML;
	
	//GUI
	public static BufferedImage woodBtn;
	public static BufferedImage invGrid;
	public static BufferedImage skillPane;
	
	//STATUS / HUD (HP BAR, ETC)
	public static BufferedImage redBar;
	public static BufferedImage greenBar;
	public static BufferedImage blueBar;
	
	//FONTS
	public static Font fontImmortal28;
	public static Font fontRoboto14;
	
	//SKELETON ANIMATIONS
	public static HashMap<String, HashMap<String, SkeletonAnimation>> lowerSkeleAngles;
	public static HashMap<String, HashMap<String, SkeletonAnimation>> upperSkeleAngles;
	
	public static SkeletonAnimation walkFrontBottom;
	public static SkeletonAnimation walkFrontTop_0Hand;
	
	//////////////////
	//Asset Hashmaps
	//////////////////
	//Sprites
	//This holds all loaded sprite sheets
	private static HashMap<String, SpriteSheet> sprites;
	
	//Loads all assets
	public static void init ()
	{
		sprites = new HashMap<String, SpriteSheet>();
		
		//tree = ImageLoader.loadImage("/textures/tree1(sm).png");
		
		//GUI
		woodBtn = ImageLoader.loadImage("/GUI/WoodButton.png");
		invGrid = ImageLoader.loadImage("/GUI/Inventory Grid.png");
		skillPane = ImageLoader.loadImage("/GUI/Skills and Attributes.png");
		
		//STATUS / HUD (HP BAR, ETC)
		redBar = ImageLoader.loadImage("/GUI/Status Display/redBar.png");
		greenBar = ImageLoader.loadImage("/GUI/Status Display/greenBar.png");
		blueBar = ImageLoader.loadImage("/GUI/Status Display/blueBar.png");
		
		//FONTS
		fontImmortal28 = FontLoader.loadFont("/fonts/IMMORTAL.ttf", 28);
		fontRoboto14 = FontLoader.loadFont("/fonts/RobotoCondensed-Light.ttf", 14);
		
		//SKELETON ANIMATIONS
		Document animDoc = XmlLoader.readXML("res/skeleTest/skeleAnimation.xml");
		lowerSkeleAngles = new HashMap<String, HashMap<String, SkeletonAnimation>>();
		lowerSkeleAngles.put("front", new HashMap<String, SkeletonAnimation>());
		lowerSkeleAngles.put("back", new HashMap<String, SkeletonAnimation>());
		lowerSkeleAngles.put("left", new HashMap<String, SkeletonAnimation>());
		lowerSkeleAngles.put("right", new HashMap<String, SkeletonAnimation>());
		
		upperSkeleAngles = new HashMap<String, HashMap<String, SkeletonAnimation>>();
		upperSkeleAngles.put("front", new HashMap<String, SkeletonAnimation>());
		upperSkeleAngles.put("back", new HashMap<String, SkeletonAnimation>());
		upperSkeleAngles.put("left", new HashMap<String, SkeletonAnimation>());
		upperSkeleAngles.put("right", new HashMap<String, SkeletonAnimation>());
		
		//Legs
		List<Element> eles = animDoc.getRootElement().getChild("lower").getChildren();
		
		for (Element e : eles)
			lowerSkeleAngles.get(e.getAttributeValue("angle")).put(e.getName(), new SkeletonAnimation(e));
		
		//Upper
		eles = animDoc.getRootElement().getChild("upper").getChildren();
		
		for (Element e : eles)
			upperSkeleAngles.get(e.getAttributeValue("angle")).put(e.getName(), new SkeletonAnimation(e));
	}
	
	//Load external variables
	public static Element getVariables(String varGroup)
	{
		if (varsXML == null)
			varsXML = XmlLoader.readXML("res/vars.xml");
		
		return varsXML.getRootElement().getChild(varGroup);
	}
	
	//Get a sprite sheet by ID
	//This function checks for the sprite sheet in our HashMap
	//If it hasnt been loaded, we get the data from the xml file, and create it
	public static SpriteSheet getSpriteSheet(String spriteID)
	{
		SpriteSheet ss = sprites.get(spriteID);
		
		if (ss == null)
		{
			if (spriteXML == null)
				spriteXML = XmlLoader.readXML("res/sprites.xml");
			
			System.out.println("Trying to load " + spriteID);
			//Create the new sprite sheet and add it to the hashmap
			ss = new SpriteSheet(spriteXML.getRootElement().getChild(spriteID));
			sprites.put(spriteID, ss);
		}
		return ss;
	}
	
	//Get an EnvironmentalEntity element by ID
	public static Element getEnvironmentalEntity(String entityID)
	{	
		if (envEntityXML == null)
			envEntityXML = XmlLoader.readXML("res/environmentalObjects.xml");
		
		return envEntityXML.getRootElement().getChild(entityID);
	}
	
	//Get an item element by ID
	public static Element getItem(String entityID)
	{	
		if (itemsXML == null)
			itemsXML = XmlLoader.readXML("res/items.xml");
		
		return itemsXML.getRootElement().getChild(entityID);
	}
	
	//Get a model element by ID
	public static HashMap<String, HashMap<String, String>> getModel(String modelID)
	{	
		if (modelsXML == null)
			modelsXML = XmlLoader.readXML("res/models.xml");
		
		//		//Limb		//Angle		//spriteID
		HashMap<String, HashMap<String, String>> models = new HashMap<String, HashMap<String,String>>();
		
		try
		{
			List<Element> modelList = modelsXML.getRootElement().getChild(modelID).getChildren();
			String type = modelsXML.getRootElement().getChild(modelID).getAttributeValue("limb");	// Models xml
			HashMap<String, String> model = new HashMap<String, String>();;
			
			for (Element ele : modelList)
			{
				if (ele.getName() != "tags")
				{
					model.put(ele.getName(), ele.getText());
				}
			}
			models.put(type, model);
		}
		catch (Exception e)
		{
			//
		}
		return models;
	}
	
	//Get a race element by ID
	public static Element getRace(String raceID)
	{
		if (racesXML == null)
			racesXML = XmlLoader.readXML("res/races.xml");
		
		return racesXML.getRootElement().getChild(raceID);
	}
	
	//Get a sound element by ID
	public static Element getSound(String soundID)
	{	
		if (soundsXML == null)
			soundsXML = XmlLoader.readXML("res/sounds.xml");
		
		return soundsXML.getRootElement().getChild(soundID);
	}

}
