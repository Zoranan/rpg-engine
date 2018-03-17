package editors.environmentalEditor;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Rectangle;

import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdom2.Document;
import org.jdom2.Element;

import editors.XmlForm;
import editors.compoundObjects.Action;
import editors.compoundObjects.CheckBoxSelection;
import editors.compoundObjects.CompoundComponent;
import editors.compoundObjects.LabeledTextBox;
import editors.compoundObjects.XmlSelection;
import editors.subPanels.SpriteViewerCanvas;
import gameObjects.SpriteSheet;
import util.FpsTimer;
import util.Handler;
import util.TextValidator;
import util.XmlLoader;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EnvironmentalEntityForm extends XmlForm implements Runnable {
	private XmlSelection compDepthReg;
	private XmlSelection compSpriteIcon;
	private XmlSelection compSolidBounds;
	private XmlSelection compTotalBounds;
	private CheckBoxSelection compIsSolid;
	private JButton btnSave;
	
	private boolean saveAsNew = true;
	//XML files
	private Document spriteDoc;
	private Document envObjDoc;
	
	//Graphical
	private SpriteSheet sprite;
	private SpriteViewerCanvas spriteFrame;
	
	//Thread
	private Thread thread;
	private boolean running = false;

	/**
	 * Create the panel.
	 */
	public EnvironmentalEntityForm () 
	{
		super("Environmental Entity Editor");	//Form title
		
		//form components
		compDepthReg = new XmlSelection("Depth Register", "depthRegister", true);
		compSpriteIcon = new XmlSelection("Sprite", "spriteID", "/sprites.xml", "Sprites", false);
		compIsSolid = new CheckBoxSelection("Is Solid", "isSolid", "Yes it is", "No its not", true);
		compSolidBounds = new XmlSelection("Solid Bounds", null, true);	//We set these to null nodes because we 
		compTotalBounds = new XmlSelection("Total Bounds", null, true);//will manually calculate them
		
		fields = new CompoundComponent[] {
				new LabeledTextBox("Name", "name"),
				compSpriteIcon,
				compDepthReg,
				compIsSolid,
				compSolidBounds,
				compTotalBounds};
		
		////////////////
		//Build the form
		this.buildForm();
		
		//Viewer for the sprite	icon	
		spriteFrame = new SpriteViewerCanvas();
		springLayout.putConstraint(SpringLayout.NORTH, spriteFrame, 0, SpringLayout.NORTH, fields[0]);
		springLayout.putConstraint(SpringLayout.WEST, spriteFrame, VERT_PADDING, SpringLayout.EAST, fields[0]);
		add(spriteFrame);
		spriteFrame.setVisible(true);
		
		this.setPreferredSize(springLayout.preferredLayoutSize(this));//?? Not sure if I need this
		
		//Save Button
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				saveEntityAs();//Needs to save as only if we are creating a new entity
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, VERT_PADDING, SpringLayout.SOUTH, fields[fields.length-1]);
		springLayout.putConstraint(SpringLayout.WEST, btnSave, 0, SpringLayout.WEST, fields[fields.length-1]);
		add (btnSave);
		
		//START: Setting up our selection buttons
		//Depth register
		compDepthReg.setClickAction(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectDepthRegister();
				setEnableAll(false);
			}
		});
		
		//Solid Bounds
		compSolidBounds.setClickAction(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectHitArea();
				setEnableAll(false);
			}
		});
		
		//Solid Bounds
		compTotalBounds.setClickAction(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				selectTotalArea();
				setEnableAll(false);
			}
		});
		//END button setup
		
		//Set up our text boxes
		//Sprite icon selection
		compSpriteIcon.getTextField().getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void insertUpdate(DocumentEvent e) {
				loadSelectedSprite();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				loadSelectedSprite();
			}
			
		});
		
		//Depth register
		this.compDepthReg.setMode(LabeledTextBox.CharacterMode.NUMERIC);
		this.compDepthReg.setKeyTypedAction(new Action(){
			@Override
			public void action() {
				try
				{
					spriteFrame.setDepthRegister(Integer.parseInt(compDepthReg.getValue()));
				}
				catch (Exception e)
				{
					//Do nothing
				}
			}
		});
		//Depth register
		this.compSolidBounds.setMode(LabeledTextBox.CharacterMode.NUMERIC);
		this.compSolidBounds.setValidChars(",");
		this.compSolidBounds.setKeyTypedAction(new Action(){
			@Override
			public void action() {
				spriteFrame.setSolidBounds(compSolidBounds.getValue());
			}
		});
		//Depth register
		this.compTotalBounds.setMode(LabeledTextBox.CharacterMode.NUMERIC);
		this.compTotalBounds.setValidChars(",");
		this.compTotalBounds.setKeyTypedAction(new Action(){
			@Override
			public void action() {
				spriteFrame.setTotalBounds(compTotalBounds.getValue());
			}
		});
		
		this.setEnableAll(false);
		this.compSpriteIcon.setEnabled(true);
		//Set up our thread
		thread = new Thread(this);
		
	}//END constructor
	
	//////////////
	//FUNCTIONS
	//////////////
	
	//Set depthRegister
	//Allows the user to click the image, getting where it wants the depth register to be
	public void selectDepthRegister()
	{
		spriteFrame.setEditMode(SpriteViewerCanvas.EditMode.DEPTH);
	}
	
	public void selectHitArea()
	{
		spriteFrame.setEditMode(SpriteViewerCanvas.EditMode.HIT_BOUNDS);
	}
	
	public void selectTotalArea()
	{
		spriteFrame.setEditMode(SpriteViewerCanvas.EditMode.TOUCH_BOUNDS);
	}
	
	//Load sprite element
	public void loadSelectedSprite()
	{
		//Load the sprite document if necessary
		if (spriteDoc == null)
			spriteDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/sprites.xml");
		//Attempt to find the sprite node
		Element spriteNode = spriteDoc.getRootElement().getChild(compSpriteIcon.getValue());
		//If the sprite doesn't exist, tell the user, and clear the field
		if (spriteNode == null)
		{
			JOptionPane.showMessageDialog(this, "Sprite '" + compSpriteIcon.getValue() + "' does not exist", "Input Error", JOptionPane.ERROR_MESSAGE);
		}
		//if it does exist, create the sprite sheet object
		else
		{
			//Create the sheet, and add it to our preview frame
			sprite = new SpriteSheet(spriteNode);
			spriteFrame.setSprite(sprite);
			
			this.start();
		}
	}
	
	//Save the Entity
	public void saveEntityAs()
	{
		boolean valid = false;
		String nameID = "";
		
		while (valid == false)
		{
			//Get our current document
			envObjDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/environmentalObjects.xml");
			Element root = envObjDoc.getRootElement();
			
			//Get the desired save name
			nameID = JOptionPane.showInputDialog(this, "Enter a unique EntityID", "Save Entity", JOptionPane.PLAIN_MESSAGE);
			
			if (nameID == null)
				nameID = "";
			
			valid = TextValidator.isAlphaNumeric(nameID);
			
			//If our input was not valid, show a message
			if (valid == false)
				JOptionPane.showMessageDialog(this, "Invalid ID\nUse only numbers, \nand letters", "Invalid Input", JOptionPane.ERROR_MESSAGE);
			
			//Part two: name conflict check
			//We only check this if our input characters were valid, and we didnt cancel the save
			else if (nameID.length() > 0)
			{
				valid &= (root.getChild(nameID) == null);	//The nameID must not be in use
				
				//If we found a match, its invalid
				if (valid == false)
					JOptionPane.showMessageDialog(this, "Invalid ID\nThat ID is already in use", "Invalid Input", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (nameID.length() > 0)
			saveEntity(nameID, false);
	}
	
	//Save entity with a specified name
	public void saveEntity(String nameID, boolean reload)
	{
		if (reload)
			envObjDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/environmentalObjects.xml");
		
		//Delete existing if we are editing
		if (saveAsNew == false)
			envObjDoc.getRootElement().removeChild(nameID);
		
		//Now, we create our node
		Element newEntity = new Element(nameID);
		
		for (int i=0; i < fields.length; i++)
			if (fields[i].getNodeName() != null)
			{
				newEntity.addContent(new Element(fields[i].getNodeName()).addContent(fields[i].getValue()));
			}
		
		//Manually set the boundary objects (unique case)
		Element solidArea = new Element ("solidBounds");
		Rectangle r = spriteFrame.getSolidBoundsRect();
		solidArea.setAttribute("x", Integer.toString(r.x));
		solidArea.setAttribute("y", Integer.toString(r.y));
		solidArea.setAttribute("w", Integer.toString(r.width));
		solidArea.setAttribute("h", Integer.toString(r.height));
		newEntity.addContent(solidArea);
		
		Element totalArea = new Element ("totalBounds");
		r = spriteFrame.getTotalBoundsRect();
		totalArea.setAttribute("x", Integer.toString(r.x));
		totalArea.setAttribute("y", Integer.toString(r.y));
		totalArea.setAttribute("w", Integer.toString(r.width));
		totalArea.setAttribute("h", Integer.toString(r.height));
		newEntity.addContent(totalArea);
		
		newEntity.addContent(new Element("script").addContent(""));
		
		//Finally, add the new node to our root, and save the doc
		envObjDoc.getRootElement().addContent(newEntity);
		XmlLoader.writeXML(envObjDoc);
	}
	
	//Disable all buttons / fields
	public void setEnableAll(boolean b)
	{	
		for (int i=0; i < fields.length; i++)
			if (b == true && fields[i].equals(compSolidBounds))
				compSolidBounds.setEnabled(this.compIsSolid.getBoolValue());
			else
				fields[i].setEnabled(b);
	}
	
	///////////
	//RUNNABLE
	//////////
	
	@Override
	public void run() {
		FpsTimer timer = new FpsTimer(60);
		while (running)
		{
			if (timer.check())
			{
				spriteFrame.render();
			
				//Grab our depth data from the drawing
				if (spriteFrame.getEditMode() == SpriteViewerCanvas.EditMode.DEPTH)
				{
					this.compDepthReg.getTextField().setText(Integer.toString(spriteFrame.getDepthReference()));
				}
				else if (spriteFrame.getEditMode() == SpriteViewerCanvas.EditMode.HIT_BOUNDS)
				{
					this.compSolidBounds.getTextField().setText(spriteFrame.getSolidBounds());
				}
				else if (spriteFrame.getEditMode() == SpriteViewerCanvas.EditMode.TOUCH_BOUNDS)
				{
					this.compTotalBounds.getTextField().setText(spriteFrame.getTotalBounds());
				}
				else
				{
					setEnableAll(true);
				}
				
				//Enable / disable buttons
				
				//Stop this thread if we have no sprite selected
				if (compSpriteIcon.getValue() == "")
					this.stop();
			}
		}
	}
	
	//Start the thread
	public void start()
	{	
		if (running == false && thread.isAlive() == false)
		{
			thread = new Thread(this);
			running = true;
			thread.start();
		}
	}
	
	//Stop the thread
	public void stop()
	{
		if (running && thread.isAlive())
		{
			running = false;
			thread.interrupt();
			while (thread.isAlive()); //Wait for thread to die
		}
	}
	
	//Setters and getters
	public void setSaveAsNew(boolean saveAsNew) {
		this.saveAsNew = saveAsNew;
	}
}