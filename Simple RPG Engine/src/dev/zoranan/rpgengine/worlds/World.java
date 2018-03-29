package dev.zoranan.rpgengine.worlds;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import dev.zoranan.rpgengine.Handler;
import dev.zoranan.rpgengine.entities.Entity;
import dev.zoranan.rpgengine.entities.EntityManager;
import dev.zoranan.rpgengine.entities.EnvironmentalEntity;
import dev.zoranan.rpgengine.entities.Human;
import dev.zoranan.rpgengine.entities.ItemEntity;
import dev.zoranan.rpgengine.entities.Mob;
import dev.zoranan.rpgengine.entities.combat.CombatManager;
import dev.zoranan.rpgengine.gfx.GameCamera;
import dev.zoranan.rpgengine.gfx.ImageLoader;
import dev.zoranan.rpgengine.util.Assets;
import dev.zoranan.utils.XmlLoader;

/*
 * World Objects load world (map) data, and create objects for that world dynamically 
 */

public class World {
	public static final String MAP_DIR = "/maps/";
	
	private BufferedImage hitAreaMap;
	private BufferedImage groundMap;
	private BufferedImage buildingsMap;
	
	private Handler handler;
	private Mob player;
	//ENTITIES
	private EntityManager entityManager;
	
	//COMBAT
	private CombatManager combatManager;

	public World (String name, Handler handler)
	{	
		this.handler = handler;
		entityManager = new EntityManager(handler);
		combatManager = new CombatManager(handler);
		handler.setEntityManager(entityManager);
		handler.setCombatManager(combatManager);
		player = handler.getPlayer();
		entityManager.addEntity(player);
		player.setPos(1100f, 1400f);	//NEEDS to be determined by WHERE the player was loaded in from
	
		this.loadWorld(name);
	}
	
	//LOAD THE WORLD
	public void loadWorld(String name)
	{
		String baseURL = MAP_DIR + name + "/";
		//Document map = XmlLoader.readXML((baseURL + name + ".xml"));
		Document map = XmlLoader.readXML("res/maps/testTown/testTown.xml");
		Element root = map.getRootElement();
		
		//Start by loading the base images in (Due for removal)
		groundMap = ImageLoader.loadImage(baseURL + root.getChild("ground").getAttributeValue("src"));
		hitAreaMap = ImageLoader.loadImage(baseURL + root.getChild("bounds").getAttributeValue("src"));
		buildingsMap = ImageLoader.loadImage(baseURL + root.getChild("upper").getAttributeValue("src"));
		
		//Now for Entities
		root = root.getChild("entities");
		List<Element>entityGroup = root.getChildren();
		
		for (Element g : entityGroup)
		{
			entityManager.addEntity(this.createNew(g));
		}
	}
	
	//Get entity based on name
	public Entity createNew(Element ele)
	{
		Entity e = null;
		String type = ele.getName();
		int x = Integer.parseInt(ele.getChild("position").getAttributeValue("x"));
		int y = Integer.parseInt(ele.getChild("position").getAttributeValue("y"));
		
		if (type == "envEnt")
		{
			//e = new Tree(Integer.parseInt(ele.getAttributeValue("treeType")), handler, x, y);
			e = new EnvironmentalEntity(ele, handler);
		}
		
		//ITEMS
		else if (type == "itemEnt")
		{
			e = new ItemEntity(ele, handler);
		}
		
		//MOBS
		else if (type == "humanoid")
		{
			String nameID = ele.getChildText("nameID");
			e = new Human(ele.getChildText("name"), handler, x, y, 50, 75);
		}
			
		return e;
	}
	
	//UPDATE AND RENDER METHODS
	public void update()
	{
		entityManager.update();
		handler.getGameCamera().update();
		combatManager.update();
	}
	
	public void render(Graphics g)
	{
		GameCamera cam = handler.getGameCamera();
		
		g.drawImage(groundMap, cam.calcRenderX(0), cam.calcRenderY(0), null);
		entityManager.render(g);
		g.drawImage(buildingsMap, cam.calcRenderX(0), cam.calcRenderY(0), null);
		
		//Combat stuff
		combatManager.render(g);
	}
	
	//GET SOLID AREA? (Due for removal when separate map collision is removed)
	public boolean isSolid (int x, int y)
	{
		if (hitAreaMap != null)
			return hitAreaMap.getRGB(x, y) == Assets.BOUNDS_COLOR;
		else return true;
	}
	public boolean isSolid (float x, float y)
	{
		return isSolid((int) x, (int) y);
	}
}
