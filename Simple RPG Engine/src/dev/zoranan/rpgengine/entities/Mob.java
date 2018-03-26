package dev.zoranan.rpgengine.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.HashMap;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.attributes.Stat;
import dev.zoranan.rpgengine.entities.attributes.StatSheet;
import dev.zoranan.rpgengine.entities.behaviors.Behavior;
import dev.zoranan.rpgengine.entities.combat.Attack;
import dev.zoranan.rpgengine.entities.containers.Container;
import dev.zoranan.rpgengine.gfx.GameCamera;
import dev.zoranan.rpgengine.gfx.SpriteSheet;
import dev.zoranan.rpgengine.items.Item;
import dev.zoranan.rpgengine.items.equipment.EquipmentSheet;

public abstract class Mob extends Entity{
	//DEFAULTS
	public static enum Direction {STOP, NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST};
	private static final float DEFAULT_SPEED = 1.4f,
							DIAGONAL_ADJUSTMENT = 0.66f;
	public static final int DEFAULT_MOB_WIDTH = 50,
							DEFAULT_MOB_HEIGHT = 50;
	
	protected Behavior behavior;
	protected StatSheet statSheet;
	protected EquipmentSheet equipmentSheet;
	protected int xp;
	protected int level;
	protected float speed;
	protected float xMove, yMove;
	protected double attackAngle;
	protected Attack attack;
	
	//For animation
	protected boolean attacking = false;
	protected boolean walking = false;
	
	protected Direction direction;
	protected Direction facing = Direction.SOUTH;
	protected Rectangle body = new Rectangle(0, 0, 25, 50);
	
	
	//Graphics
	protected SpriteSheet icon;
	
	//We will need a variable that determine how the mob is drawn to the screen here.
	//Probably a custom class that holds the sprites for all actions the mob could do.
	
	//Basic constructor;
	public Mob (String name, Handler handler, float x, float y, int w, int h)
	{
		super (name, handler, x, y, w, h);
		statSheet = new StatSheet();
		equipmentSheet = new EquipmentSheet();
		
		xp = 0;
		level = 1;
		speed = DEFAULT_SPEED;
		xMove = 0;
		yMove = 0;
		inventory = new Container(this);
	}
	
	public HashMap<String, String> getSkin (String key)
	{
		return null;
	}
	
	//For use with skeletal animated mobs (humanoid)
	public HashMap<String, HashMap<String, String>> getSkinModels()
	{
		return null;
	}
	
	//RENDER and UPDATE
	@Override
	public void update() 
	{
		if (behavior != null)
			behavior.update();
	}
	@Override
	public void render(Graphics g) 
	{
		GameCamera cam = handler.getGameCamera();
		g.drawImage(icon.getFrame(0), cam.calcRenderX(posX), cam.calcRenderY(posY), width, height, null);
	}
	
	//COLLISION DETECTION
	//map collision (due to be removed)
	public boolean checkMapCollision(float xOffset, float yOffset)
	{
		float xCheck, yCheck;
		xCheck = this.posX + (this.width / 2) + xOffset;
		yCheck = this.getDepthReference() + yOffset;
		
		return (handler.getWorld().isSolid(xCheck, yCheck));
	}
	
	//Get ahold of our stats
	public StatSheet getStatSheet()
	{
		return statSheet;
	}
	
	public Stat getStat(String key)
	{
		return statSheet.get(key);
	}
	
	//MOVEMENT
	public void move()
	{	
		boolean xHit = checkMapCollision(xMove, 0f) || checkEntityCollision(xMove, 0f);
		boolean yHit = checkMapCollision(0f, yMove) || checkEntityCollision(0f, yMove);
		
			if (!xHit)
				this.posX += xMove;
			if (!yHit)
				this.posY += yMove;
	}
	
	//This move method sets the direction this mob will go in, and attempts to move them
	public void move (Direction dir)
	{
		this.xMove = 0;
		this.yMove = 0;
		this.direction = dir;
		
		if (dir == Direction.NORTH)
		{
			this.xMove = 0;
			this.yMove = -speed;
		}
		else if (dir == Direction.NORTH_EAST)
		{
			this.xMove = speed * DIAGONAL_ADJUSTMENT;
			this.yMove = -speed * DIAGONAL_ADJUSTMENT;
		}
		
		else if (dir == Direction.EAST)
		{
			this.xMove = speed;
			this.yMove = 0;
		}
		else if (dir == Direction.SOUTH_EAST)
		{
			this.xMove = speed * DIAGONAL_ADJUSTMENT;
			this.yMove = speed * DIAGONAL_ADJUSTMENT;
		}
		else if (dir == Direction.SOUTH)
		{
			this.xMove = 0;
			this.yMove = speed;
		}
		else if (dir == Direction.SOUTH_WEST)
		{
			this.xMove = -speed * DIAGONAL_ADJUSTMENT;
			this.yMove = speed * DIAGONAL_ADJUSTMENT;
		}
		else if (dir == Direction.WEST)
		{
			this.xMove = -speed;
			this.yMove = 0;
		}
		else if (dir == Direction.NORTH_WEST)
		{
			this.xMove = -speed * DIAGONAL_ADJUSTMENT;
			this.yMove = -speed * DIAGONAL_ADJUSTMENT;
		}
		
		//Try to move
		move();
		//Face the proper way (disabled until animations are finished)
		if (!attacking);
			//facing = dir;
	}

	//ITEMS and ACTIVATION
	public void activateEntity()
	{
		Entity activatedEntity = handler.getEntityManager().activateClosestEntity(this.getCenterX(), this.getDepthReference(), this);
		
		//Make sure an entity was found
		if (activatedEntity != null)
		{
			//What did we get?
			//If its an item...
			if (activatedEntity instanceof ItemEntity)
			{
				Item i = ((ItemEntity) activatedEntity).getItem(); //Get the raw item from the itemEntity
				int initialStackSize = i.getStackSize();			//Store how many items are in the stack before we loot any of them
				handler.getEntityManager().removeEntity(activatedEntity);	//Take the entity off the map.
				
				//Adds the item, which returns the amount left in the stack when done
				//If we still have items left in the stack, drop them
				if (inventory.add(i) < initialStackSize)
				{
					handler.getEntityManager().addEntity(activatedEntity);
				}
			}
			else; //<---------------------------------------------\/
				//Activate the item. Remember to remove that semicolon when implemented!!
					
		}
	}
	
	//COMBAT
	public void startAttack(double angle)
	{	
		//Once weapons and stats are implemented, they will determine the damage WITHIN the attack object
		
		if (!attacking && equipmentSheet.getEquipped("weapon") != null)
		{	
			//Melee attacks directly generate Attack objects
			attack = new Attack (this, angle);
			
			//Ranged weapons generate projectiles
			//Projectiles hold Attack objects generated here
			//Projectiles may alter Attack objects
			this.attacking = true;
		}
	}
	
	public void releaseAttack()
	{
		handler.getCombatManager().attack(attack);
		this.attack = null;
	}
	
	//Get Body Bounds for combat
	public Rectangle getBodyBounds()
	{
		this.body.setLocation((int)this.getCenterX() - (body.width / 2), (int)this.getCenterY() - (body.height / 2));
		return this.body;
	}
	
	//
	public void updateSkin()
	{
		//
	}
	
	//Get equipment
	public EquipmentSheet equipment()
	{
		return equipmentSheet;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isWalking() {
		return walking;
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}
	
	public Behavior getBehavior() {
		return behavior;
	}

	public void setBehavior(Behavior behavior) {
		this.behavior = behavior;
	}

	public Direction getDirectionFacing()
	{
		return this.facing;
	}
	
}
