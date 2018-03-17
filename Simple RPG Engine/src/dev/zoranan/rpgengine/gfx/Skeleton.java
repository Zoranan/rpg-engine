package dev.zoranan.rpgengine.gfx;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jdom2.Document;
import org.jdom2.Element;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.Mob;
import dev.zoranan.rpgengine.entities.Mob.Direction;
import dev.zoranan.rpgengine.items.Item;
import dev.zoranan.rpgengine.util.Assets;
import dev.zoranan.rpgengine.util.FpsTimer;
import dev.zoranan.rpgengine.util.XmlLoader;

public class Skeleton {
	//POSSIBLE BONES
	//Head
	//Body
	
	//LEGS
	//left thigh
	//right thigh
	//left calf
	//right calf
	
	//ARMS
	//left arm
	//right arm
	//left forearm
	//right forearm
	
	//WEAPONS / OFFHAND
	//Left hand
	//right hand
	
	//ANIMATION STATE VARIABLES
	public static enum State {IDLE, ATTACKING, WALKING};
	
	private FpsTimer walkTimer;
	private FpsTimer torsoTimer;
	
	private HashMap <String, HashMap<String, SkeletonAnimation>> lowerSkeleAngles;
	private HashMap <String, HashMap<String, SkeletonAnimation>> upperSkeleAngles;
	
	//Store our equipment models / angles
	private HashMap<String, HashMap<String, String>> models;
	
	private Mob mob;
	private Handler handler;
	private String angle = "front";
	
	private SkeletonAnimation torso;
	private SkeletonAnimation legs;
	private boolean resetTorsoFrame = false;
	
	//Strings
	String weaponType = "EmptyHand";
	
	double torsoFrame = 0;
	double walkFrame = 0;
	
	public Skeleton(Mob mob, Handler handler)
	{
		this.mob = mob;
		this.handler = handler;
			//           Limb name			angle		spriteID
		models = new HashMap<String, HashMap<String, String>>();
		
		models.put("rightFoot", new HashMap<String, String>());
		models.put("leftFoot", new HashMap<String, String>());
		models.put("rightThigh", new HashMap<String, String>());
		models.put("leftThigh", new HashMap<String, String>());
		models.put("head", new HashMap<String, String>());
		models.put("chest", new HashMap<String, String>());
		models.put("rightArm", new HashMap<String, String>());
		models.put("rightHand", new HashMap<String, String>());
		models.put("leftArm", new HashMap<String, String>());
		models.put("leftHand", new HashMap<String, String>());
		models.put("weapon", new HashMap<String, String>());
		models.put("offHand", new HashMap<String, String>());	//NEW
		
		//TEMP
		walkTimer = new FpsTimer(16);
		torsoTimer = new FpsTimer(16);
		
		//models.get("rightFoot").put("front", ImageLoader.loadImage("/skeleTest/rightBoot.png"));
		//models.get("leftFoot").put("front", ImageLoader.loadImage("/skeleTest/leftBoot.png"));
		//models.get("rightThigh").put("front", ImageLoader.loadImage("/skeleTest/rightThigh.png"));
		//models.get("leftThigh").put("front", ImageLoader.loadImage("/skeleTest/leftThigh.png"));
		//models.get("head").put("front", ImageLoader.loadImage("/skeleTest/head.png"));
		//models.get("chest").put("front", ImageLoader.loadImage("/skeleTest/body.png"));
		//models.get("rightArm").put("front", ImageLoader.loadImage("/skeleTest/rightArm.png"));
		//models.get("rightHand").put("front", ImageLoader.loadImage("/skeleTest/rightHand.png"));
		//models.get("leftArm").put("front", ImageLoader.loadImage("/skeleTest/leftArm.png"));
		//models.get("leftHand").put("front", ImageLoader.loadImage("/skeleTest/leftHand.png"));
		
		
		//DEFAULT WALK ANIMATIONS
		
		lowerSkeleAngles = new  HashMap <String, HashMap<String, SkeletonAnimation>>();
		upperSkeleAngles = new  HashMap <String, HashMap<String, SkeletonAnimation>>();
		
		//Set up angle maps
		for (String key : Assets.lowerSkeleAngles.keySet())
		{
			lowerSkeleAngles.put(key, new HashMap<String, SkeletonAnimation>());
			upperSkeleAngles.put(key, new HashMap<String, SkeletonAnimation>());
		}
		
		//Copy lower animations
		for (Entry<String, HashMap<String, SkeletonAnimation>> angleEntry : Assets.lowerSkeleAngles.entrySet())
		{	
			for (Entry<String, SkeletonAnimation> e : angleEntry.getValue().entrySet())
			{
				this.lowerSkeleAngles.get(angleEntry.getKey()).put(e.getKey(), new SkeletonAnimation(e.getValue(), this));
			}
		}
		
		//Copy upper animations
		for (Entry<String, HashMap<String, SkeletonAnimation>> angleEntry : Assets.upperSkeleAngles.entrySet())
		{	
			for (Entry<String, SkeletonAnimation> e : angleEntry.getValue().entrySet())
			{
				this.upperSkeleAngles.get(angleEntry.getKey()).put(e.getKey(), new SkeletonAnimation(e.getValue(), this));
			}
		}
		
		legs = lowerSkeleAngles.get(angle).get("walk");
		torso = upperSkeleAngles.get(angle).get("walkEmptyHand");
		
		updateSkin();
	}
	
	public void updateSkin()
	{
		models.putAll(mob.getSkinModels());
		
		for (Entry<String, Item> e : mob.equipment().getEquipment().entrySet())
		{
			if (e.getValue() != null)
			{
				models.putAll(e.getValue().getModel());
				System.out.println(e.getValue().getModel().toString());
			}//END if
		}//END for
	}//END updateSkin
	
	
	public void update()
	{	
		//update angle
		Mob.Direction dir = mob.getDirectionFacing();
		if (dir == Direction.SOUTH)
			this.angle = "front";
		else if (dir == Direction.NORTH)
			this.angle = "back";
		else if (dir == Direction.EAST)
			this.angle = "right";
		else if (dir == Direction.WEST)
			this.angle = "left";
		
		//Updating status
		//Weapon
		Item mobWeap = mob.equipment().getEquipped("weapon");
		if (mobWeap == null)
			weaponType = "EmptyHand";
		else
		//else if (mobWeap.isOneHand())
			weaponType = "OneHand";
		//else if (mob.equipment().getWeapon() instanceof rangedWeapon)
			//weaponType = "Bow";
		
		//WALKING
		if (mob.isWalking())
		{
			//LEGS
			legs = lowerSkeleAngles.get(angle).get("walk");
			
			walkFrame += walkTimer.checkPercent();
			walkFrame %= legs.length();	
			
			if (!mob.isAttacking()) //Or doing anything else
			{
				torsoFrame = walkFrame;
				torso = upperSkeleAngles.get(angle).get("walk" + weaponType);
			}
			
			//TORSO
		}
		//IDLE
		else if (!mob.isAttacking())//if not attacking or walking, set to IDLE
		{
			walkFrame = 0;
			torsoFrame = 0;
			legs = lowerSkeleAngles.get(angle).get("idle");
			torso = upperSkeleAngles.get(angle).get("idle" + weaponType);
		}
		
		
		//ATTACK
		if (mob.isAttacking())
		{
			//Change to attacking animation
			torso = upperSkeleAngles.get(angle).get("attack" + weaponType);
			
			if (!resetTorsoFrame)
			{
				torsoFrame = 0;
				torsoTimer.setFps(10);//(int) (torso.length() / mob.equipment().getEquipped("weapon").getSpeed()));
				
				resetTorsoFrame = true;
				torso.setTrigger(false);	//Must reset the trigger
			}
			
			
			//Increase the frame!
			torsoFrame += torsoTimer.checkPercent();
			
			//Deal with our trigger!
			if (!torso.triggered() && torsoFrame >= torso.getTrigger())
			{
				torso.setTrigger(true);	//Keeps trigger from happening more than once
				mob.releaseAttack();
			}
			
			//When attack ends, reset the mob.
			if (torsoFrame >= torso.length())
			{
				mob.setAttacking(false);
				torsoFrame = 0;
				resetTorsoFrame = false;
				torso.setTrigger(false);
			}
		}
		
		//When the torso is doing nothing (except maybe walking)
		else
			torsoTimer.check();
	}
	
	public void render(Graphics g)
	{	
		Graphics2D g2d = (Graphics2D) g;
		legs.render(g2d, (int)walkFrame);
		torso.render(g2d, (int)torsoFrame);
	}
	
	//
	
	public int getStartX()
	{
		return handler.getGameCamera().calcRenderX(mob.getPosX());
	}
	
	public int getStartY()
	{
		return handler.getGameCamera().calcRenderY(mob.getPosY());
	}
	
	public String getModel (String angle, String key)
	{
		try
		{
			return models.get(key).get(angle);
		}
		catch (NullPointerException e)
		{
			return null;
		}
	}
}
