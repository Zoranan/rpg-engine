package dev.zoranan.rpgengine;

import dev.zoranan.rpgengine.entities.EntityManager;
import dev.zoranan.rpgengine.entities.Mob;
import dev.zoranan.rpgengine.entities.combat.CombatManager;
import dev.zoranan.rpgengine.entities.containers.ItemTransferManager;
import dev.zoranan.rpgengine.gfx.GameCamera;
import dev.zoranan.rpgengine.input.KeyManager;
import dev.zoranan.rpgengine.input.MouseManager;
import dev.zoranan.rpgengine.worlds.World;

/////////////////////////////
//
//	This class makes variables accessible to any object in one place

public class Handler {
	
	private Game game;
	private World world;
	private Mob player;
	private EntityManager entityManager;
	private CombatManager combatManager;
	private ItemTransferManager itemTransferManager;
	
	public ItemTransferManager getItemTransferManager() {
		return itemTransferManager;
	}

	public void setItemTransferManager(ItemTransferManager itemTransferManager) {
		this.itemTransferManager = itemTransferManager;
	}

	public Handler(Game game)
	{
		this.game = game;
	}
	
	public KeyManager getKeyManager()
	{
		return game.getKeyManager();
	}
	
	public MouseManager getMouseManager(){
		return game.getMouseManager();
	}
	
	public GameCamera getGameCamera()
	{
		return game.getGameCamera();
	}
	
	public int getWidth()
	{
		return game.getWidth();
	}
	
	public int getHeight()
	{
		return game.getHeight();
	}

	public World getWorld() 
	{
		return world;
	}
	public void setWorld(World world) 
	{
		this.world = world;
	}
	public Game getGame()
	{
		return game;
	}

	public void setGame(Game game) 
	{
		this.game = game;
	}

	public Mob getPlayer() 
	{
		return player;
	}

	public void setPlayer(Mob player) 
	{
		this.player = player;
	}

	public EntityManager getEntityManager() 
	{
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) 
	{
		this.entityManager = entityManager;
	}

	public CombatManager getCombatManager() {
		return combatManager;
	}

	public void setCombatManager(CombatManager combatManager) {
		this.combatManager = combatManager;
	}
	
	

}
