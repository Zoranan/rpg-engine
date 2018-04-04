package dev.zoranan.rpgengine.entities;

import java.awt.Graphics;
import java.util.HashMap;

import org.jdom2.Element;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.gfx.Skeleton;
import dev.zoranan.rpgengine.util.Assets;
import dev.zoranan.utils.FpsTimer;

//A skeletal animated mob

public class Human extends Mob{
	protected HashMap <String, HashMap<String, String>> skinModels;
	protected Skeleton skeleton;
	
	protected FpsTimer walkTimer;
	
	//default constructor
	public Human (String name, Handler handler, float x, float y, int w, int h)
	{
		super(name, handler, x, y, w, h);
		init("white", "male");
	}
	
	public Human (Element mapEle, Handler handler)
	{
		super (mapEle, Assets.getNpc(mapEle.getChildText("npcID")), handler, 50, 75);
		Element npcEle = Assets.getNpc(mapEle.getChildText("npcID"));
		System.out.println(npcEle);
		this.setName(npcEle.getChildText("name"));
		init(npcEle.getChildText("raceID"), npcEle.getChildText("sex"));
		
		//Head and Hair
		skinModels.putAll(Assets.getModel(npcEle.getChildText("headID")));
		skinModels.putAll(Assets.getModel(npcEle.getChildText("hairID")));
		
	}
	
	private void init (String race, String sex)
	{
		this.setHitBounds(18, 64, 14, 7);	//This needs to be loaded eventually
		
		skinModels = new HashMap <String, HashMap<String, String>>();
		Element skin = Assets.getRace(race).getChild(sex);	//Races are loading XML. YAY!
		
		//Get the mob's skin
		for (Element e : skin.getChildren())
		{
			skinModels.putAll(Assets.getModel(e.getValue()));
		}
		
		skeleton = new Skeleton(this, handler);
	}
	
	@Override
	public HashMap<String, String> getSkin (String key)
	{
		System.out.println("Updating " + key + " to " + skinModels.get(key).get("front"));
		return skinModels.get(key);
	}
	
	@Override
	public final HashMap<String, HashMap<String, String>> getSkinModels()
	{
		return skinModels;
	}
	
	@Override
	public void update() 
	{
		super.update();
		
		skeleton.update();
		
		if (this.direction != Mob.Direction.STOP)
			this.walking = true;
		else
			this.walking = false;
		
		//Skin update
		if (this.equipmentSheet.checkForUpdates())
			this.updateSkin();
	}
	
	@Override
	public void render(Graphics g) 
	{
		skeleton.render(g);
	}
	
	@Override
	public void updateSkin()
	{
		System.out.println("Skin update");
		skeleton.updateSkin();
	}
	
}
