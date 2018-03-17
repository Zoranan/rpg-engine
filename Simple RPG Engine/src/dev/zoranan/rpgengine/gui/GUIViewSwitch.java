package dev.zoranan.rpgengine.gui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.ItemEntity;
import dev.zoranan.rpgengine.entities.containers.Container;
import dev.zoranan.rpgengine.entities.containers.ItemTransfer;
import dev.zoranan.rpgengine.entities.containers.ItemTransferManager;
import dev.zoranan.rpgengine.input.KeyManager;
import dev.zoranan.rpgengine.input.MouseManager;
import dev.zoranan.rpgengine.items.Item;
import dev.zoranan.rpgengine.items.equipment.EquipmentSheet;
import dev.zoranan.rpgengine.util.Assets;

//This class allows different gui menu's to be selected easily, and switched between

public class GUIViewSwitch {
	private Handler handler;
	private KeyManager keyManager;
	private MouseManager mouseManager;
	private ItemTransferManager itemTransferManager;
	private boolean showLeft = false;
	private boolean showRight = false;
	
	private final int PANEL_WIDTH = 310;
	private final int PANEL_MOVE_SPEED = 10;
	private int leftX = -PANEL_WIDTH;
	private int leftXTarget = 0;
	private int rightX = 0;
	private int rightXTarget = 0;
	
	//EQUIPMENT VARS
	private EquipmentSheet equip;
	private static final int EQUIP_FRAME_WIDTH = 5;
	
	//INVENTORY VARS
	private Container inv;
	private static final int INV_COLUMNS = 5;
	private static final int INV_ROWS = 5;
	private static final int GRID_THICKNESS = 10;
	private static final int ICON_SIZE = 50;
	private static final int INV_Y_START = 410;
	private static final int INV_X_SIZE = 310;
	private static final int INV_Y_SIZE = 310;
	
	//BOUNDS FOR MOUSE SELECTION
	private Rectangle invArea;
	private Rectangle dropArea;
	private Rectangle equipArea;
	//Equipment bounds
	private Rectangle headSlot;
	private Rectangle chestSlot;
	private Rectangle handSlot;
	private Rectangle legSlot;
	private Rectangle footSlot;
	private Rectangle ring1Slot;
	private Rectangle ring2Slot;
	private Rectangle weaponSlot;
	private Rectangle offHandSlot;
	private HashMap<String, Rectangle> equipSlots;
	
	int leftView = 1;
	//The leftView integer decides what view we are on
	//	1: Inventory
	//	2: Attributes
	
	int rightView = 1;
	//The rightView integer decides what view we are on
	//	1: Buying / looting
	//	2: ?
	
	//CONSTRUCTOR
	public GUIViewSwitch (Handler handler)
	{
		this.handler = handler;
		this.keyManager = handler.getKeyManager();
		this.mouseManager = handler.getMouseManager();
		
		leftView = 0;
		rightView = 0;
		
		itemTransferManager = new ItemTransferManager();
		handler.setItemTransferManager(itemTransferManager);
		equip = handler.getPlayer().equipment();
		inv = handler.getPlayer().getInventory();
		//Set up bounds for mouse clicks
		invArea = new Rectangle(GRID_THICKNESS, INV_Y_START + GRID_THICKNESS, INV_X_SIZE -  2 * GRID_THICKNESS, INV_Y_SIZE - 2 * GRID_THICKNESS);
		dropArea = new Rectangle(PANEL_WIDTH, 0, handler.getWidth() - PANEL_WIDTH, handler.getHeight());
		equipArea = new Rectangle(0, 0, PANEL_WIDTH, INV_Y_START - 1);
		
		//Equip slots
		weaponSlot = new Rectangle(7, 222, 60, 60);
		offHandSlot = new Rectangle(241, 222, 60, 60);
		headSlot = new Rectangle(115, 6, 60, 60);
		chestSlot = new Rectangle(115, 78, 60, 60);
		handSlot = new Rectangle(115, 150, 60, 60);
		legSlot = new Rectangle(115, 222, 60, 60);
		footSlot = new Rectangle(115, 294, 60, 60);
		ring1Slot = new Rectangle(7, 150, 60, 60);
		ring2Slot = new Rectangle(241, 150, 60, 60);
		
		equipSlots = new HashMap<String, Rectangle>();
		equipSlots.put("weapon", new Rectangle(7, 222, 60, 60));
		equipSlots.put("offHand", new Rectangle(241, 222, 60, 60));
		equipSlots.put("headGear", new Rectangle(115, 6, 60, 60));
		equipSlots.put("chestGear", new Rectangle(115, 78, 60, 60));
		equipSlots.put("handGear", new Rectangle(115, 150, 60, 60));
		equipSlots.put("legGear", new Rectangle(115, 222, 60, 60));
		equipSlots.put("footGear", new Rectangle(115, 294, 60, 60));
		equipSlots.put("ring1", new Rectangle(7, 222, 60, 60));
		equipSlots.put("ring2", new Rectangle(7, 222, 60, 60));
	}
	
	//RENDER The Inventory Panel
	public void renderInventory(Graphics g)
	{
		//The background first
		g.drawImage(Assets.invGrid, leftX, 0, null);
		
		//Draw all elements of the inventory
		for (int i=0; i < inv.getCapacity(); i++)
		{
			if (inv.get(i) != null)
				inv.get(i).renderIcon(g, leftX + GRID_THICKNESS + (i % INV_COLUMNS)* (GRID_THICKNESS + ICON_SIZE), 
								(INV_Y_START + GRID_THICKNESS + (i / INV_ROWS) * (GRID_THICKNESS + ICON_SIZE)));
		}
		
		//Draw all equipped items
		for (Entry<String, Rectangle> e : equipSlots.entrySet())
		{
			if (equip.getEquipped(e.getKey()) != null)
				equip.getEquipped(e.getKey()).renderIcon(g, leftX + e.getValue().x + EQUIP_FRAME_WIDTH,  + e.getValue().y + EQUIP_FRAME_WIDTH);
		}
		
	}
	
	//RENDER the Skills and Attributes Panel
	public void renderAttributes(Graphics g)
	{
		//The background first
		g.drawImage(Assets.skillPane, leftX, 0, null);
	}
	
	//Main render method
	public void render(Graphics g)
	{
		//if the panel is on screen at all, we need to render it
		if (leftX > -PANEL_WIDTH)
		{
			//Inventory screen
			if (leftView == 1)
				renderInventory(g);
			
			//Attributes screen
			else if (leftView == 2)
				renderAttributes(g);
			
		}
		
		//RENDER Items held by mouse
		if (itemTransferManager.getItemTransfer() != null)
			itemTransferManager.getItem().renderIcon(g, mouseManager.getMouseX() - ICON_SIZE / 2, mouseManager.getMouseY() - ICON_SIZE / 2);
	}
	
	//CHECK FOR KEYBOARD INPUT
	private void checkKeys()
	{
		//close inventory pane (only if we are on the inventory pane)
		if (leftView == 1 && showLeft == true && keyManager.I())
		{
			showLeft = false;
		}
		//Inventory toggle
		else if (keyManager.I())
		{
			leftView = 1;
			showLeft = true;
			//keyManager.resetToggle();
		}
		
		//close skill panel (only if we are on that panel)
		if (leftView == 2 && showLeft == true && keyManager.K())
		{
			showLeft = false;
		}
		//Skill / Attribute Toggle
		else if (keyManager.K())
		{
			showLeft = true;
			leftView = 2;
		}
	}
	
	//CHECK FOR MOUSE INPUT
	private void checkMouse()
	{
		
		//INVENTORY ITEMS CLICK
		//If we have the left pane open, and we are viewing the inventory, 
		//and our mouse is in the inv area, and we click the mouse...
		
		//All inventory and Equip screen functions
		if (showLeft && leftView == 1 && mouseManager.isLeftToggled())
		{
			Point mouseXY = new Point (mouseManager.getMouseX(), mouseManager.getMouseY());
			
			//INVENTORY
			if (invArea.contains(mouseXY))
			{
				int slot = getInvSlot();	//Get the slot we are hovering over
				
				//If the slot is empty
				if (inv.get(slot) == null)
					inv.add(itemTransferManager.dropItem(), slot);
					
				//////////////////////
				//If the slot has an item of the same type
				else if (itemTransferManager.getItemTransfer() != null &&
						inv.get(slot).getExactType().equals(itemTransferManager.getItem().getExactType()))
				{
					inv.add(itemTransferManager.getItemTransfer(), slot);
					itemTransferManager.checkEmpty();	//Check to see if the item was depleted
				}
					
				//If something non stackable is in that slot, swap items
				else
				{
					ItemTransfer newItem = inv.removeIT(slot);		//Get the item in the slot we clicked as an ItemTransfer
					inv.add(itemTransferManager.dropItem(), slot);	//Drop the item we are currently holding in the slot
					itemTransferManager.holdItem(newItem);			//hold onto the item we originally took out of the slot
				}
			}
			
			//EQUIPPING
			else if (equipArea.contains(mouseXY))
			{
				//If we have an item to equip, equip it
				if (itemTransferManager.getItemTransfer() != null)
					equip(itemTransferManager.dropItem());
				
				//If we dont have an item in hand, check to see if we are UN-Equipping something
				//Player can't remove items while attacking!
				else if (!handler.getPlayer().isAttacking())
				{
					//Check through every slot to see if one of them was clicked
					for (Entry<String, Rectangle> e : equipSlots.entrySet())
					{
						/*
						 * If the slot we are checking has an item equipped, and we click that slot remove it
						 * from the slot, and place it on our cursor.
						 */
						if (equip.getEquipped(e.getKey()) != null && e.getValue().contains(mouseManager.getMouseX(), mouseManager.getMouseY()))
						{
							itemTransferManager.holdItem(new ItemTransfer(equip.getEquipped(e.getKey()), handler.getPlayer(), inv, 0, ItemTransfer.Context.LOOT));
							equip.unequip(e.getKey());
							
							break;
						}//END IF clicked valid
					}//END FOR loop
				}//END Attack Check ELSE IF
			}//END Unequip ELSE IF
			
			//DROPPING ITEMS
			else if (dropArea.contains(mouseXY)
					 && itemTransferManager.getItemTransfer() != null)
			{
				float x = handler.getPlayer().getCenterX() - ItemEntity.ITEM_SIZE / 2;
				float y = handler.getPlayer().getDepthReference() - ItemEntity.ITEM_SIZE / 2;
				handler.getEntityManager().addEntity(new ItemEntity(itemTransferManager.getItem(), handler, x, y));
				//itemTransferManager.getItem().setPos(handler.getPlayer().getCenterX(), handler.getPlayer().getDepthReference());
				//handler.getEntityManager().addEntity(itemTransferManager.getItem());
				itemTransferManager.dropItem();
			}
		}
	}
	
	public void update()
	{
		//Check for Keyboard and mouse input
		checkKeys();
		checkMouse();
		
		//Make sure mouse objects arent stored after inventory exit
		checkReturnMouseItem();
		
		updateView(); //Updates the panel target x to animate them
		
		slidePanes(); //Handles panel slide animation
		
	}
	
	//Resolve current view.
	//This function decides weather to render the left and right
	//It also sets the position that the panels will move to accordingly
	public void updateView()
	{
		//if the left view needs to be expanded
		if (!showLeft)
			leftXTarget = -PANEL_WIDTH;
		else
			leftXTarget = 0;
		
		if (!showRight)
			rightXTarget = handler.getWidth() + PANEL_WIDTH;
		else
			rightXTarget = handler.getWidth();
			
	}
	
	//Handles the panel movement
	public void slidePanes()
	{
		//update positions
		//left frame
		//move right
		if (leftX < leftXTarget)
		{
			leftX += PANEL_MOVE_SPEED;
				
			if (leftX > leftXTarget)
				leftX = leftXTarget;
		}
		//Move left
		if (leftX > leftXTarget)
		{
			leftX -= PANEL_MOVE_SPEED;
			
			if (leftX < leftXTarget)
				leftX = leftXTarget;
		}
			
		//Right frame
		//move right
		if (rightX < rightXTarget)
		{
			rightX += PANEL_MOVE_SPEED;
			
			if (rightX > rightXTarget)
				rightX = rightXTarget;
		}
		//Move left
		if (rightX > rightXTarget)
		{
			rightX -= PANEL_MOVE_SPEED;
				
			if (rightX < rightXTarget)
				rightX = rightXTarget;
		}
	}
	
	//FIGURE OUT WHAT INV SLOT WE CLICKED ON
	public int getInvSlot()
	{
		int x = mouseManager.getMouseX();
		int y = mouseManager.getMouseY();
		
		y -= INV_Y_START + (GRID_THICKNESS / 2);
		x -= (GRID_THICKNESS / 2);
		
		x = x / (ICON_SIZE + GRID_THICKNESS);
		y = y / (ICON_SIZE + GRID_THICKNESS);
		
		int a = (y * INV_COLUMNS + x);
		if (a > 24) a = 24;
		
		return (y * INV_COLUMNS + x);
	}
	
	public void checkReturnMouseItem()
	{
		if (!showLeft && itemTransferManager.getItemTransfer() != null)
			itemTransferManager.returnItem();
	}
	
	//EQUIP AN ITEM
	public void equip(ItemTransfer it)
	{
		Item i = it.getItem();
		
		if (equip.isEquipable(i))
		{
			inv.add(equip.setEquipped(i));
		}
		
		//NECKLACES HERE
		//RINGS
		
		else
			it.cancel();
	}
}
