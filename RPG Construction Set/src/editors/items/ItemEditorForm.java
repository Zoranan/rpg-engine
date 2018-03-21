package editors.items;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

import org.jdom2.Document;
import org.jdom2.Element;

import editors.XmlForm;
import editors.compoundObjects.Action;
import editors.compoundObjects.CheckBoxSelection;
import editors.compoundObjects.ComboBoxSelection;
import editors.compoundObjects.CompoundComponent;
import editors.compoundObjects.LabeledTextBox;
import editors.compoundObjects.XmlMultiSelection;
import editors.compoundObjects.XmlSelection;
import editors.subPanels.SpriteViewerPanel;
import gameObjects.SpriteSheet;
import util.Handler;
import util.ImageLoader;
import util.TextValidator;
import util.TxtLoader;
import util.XmlLoader;

public class ItemEditorForm extends XmlForm {
	private SpriteViewerPanel iconPreview;
	private XmlSelection compIconSelect;
	private Document itemsXml;
	/**
	 * Create the panel.
	 */
	public ItemEditorForm() {
		super ("Item Editor");
		
		ComboBoxSelection compType = new ComboBoxSelection("Type", "type", TxtLoader.getTxtAsArray("configFiles/itemTypes.txt"));
		LabeledTextBox compStackSize = new LabeledTextBox("Max Stack Size", "maxStackSize");
		LabeledTextBox compBaseValue = new LabeledTextBox("Base Value", "value");
		compIconSelect = new XmlSelection("Icon (Sprite)", "spriteIconID", "/sprites.xml", "Sprites", false);
		fields = new CompoundComponent[] {compIconSelect,
											new LabeledTextBox("Item Name", "name"),
											compType,
											new CheckBoxSelection("Consumable", "consumable", false),
											compStackSize,
											compBaseValue,
											new XmlMultiSelection("Models", "models", "/models.xml", "Models"),
											new XmlMultiSelection("Effects", "effects")//??????
											};
		
		compType.setEditable(true);
		compType.setMode(LabeledTextBox.CharacterMode.ALPHA);
		compStackSize.setMode(LabeledTextBox.CharacterMode.NUMERIC);
		compStackSize.setValue("1");
		compBaseValue.setMode(LabeledTextBox.CharacterMode.NUMERIC);
		compBaseValue.setValue("0");
		
		//This will make sure the value within the field is kept above 0
		compStackSize.getTextField().addFocusListener(new FocusAdapter(){
			@Override
			public void focusLost(FocusEvent e) {
				try
				{
					if (Integer.parseInt(compStackSize.getValue()) <= 0)
						throw new Exception("Minimum acceptable value is 1");
				}
				catch (Exception ex)
				{
					//If anything goes wrong, the stack size is set to 1
					compStackSize.setValue("1");
				}
			}
		});
		
		compBaseValue.getTextField().addFocusListener(new FocusAdapter(){
			@Override
			public void focusLost(FocusEvent e) {
				try
				{
					if (Integer.parseInt(compBaseValue.getValue()) <= 0)
						throw new Exception("Minimum acceptable value is 0");
				}
				catch (Exception ex)
				{
					//If anything goes wrong, the stack size is set to 1
					compBaseValue.setValue("0");
				}
			}
		});
		
		compIconSelect.addSelectAction(new Action(){
			@Override
			public void action() {
				loadSelectedSprite();
			}
		});
		
		//BUILD FORM
		this.buildForm();
		
		//Add our icon preview
		iconPreview = new SpriteViewerPanel();
		springLayout.putConstraint(SpringLayout.NORTH, iconPreview, 0, SpringLayout.NORTH, fields[0]);
		springLayout.putConstraint(SpringLayout.WEST, iconPreview, 0, SpringLayout.EAST, fields[0]);
		add(iconPreview);
		
		//Add a save button
		JButton btnSave = new JButton("Save");
		btnSave.setIcon(ImageLoader.loadResourceIcon("/Icons/save20px.png"));
		btnSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, VERT_PADDING, SpringLayout.SOUTH, fields[fields.length-1]);
		springLayout.putConstraint(SpringLayout.WEST, btnSave, 0, SpringLayout.WEST, fields[fields.length-1]);
		add(btnSave);
	}
	
	//FUNCTIONS
	
	private void loadSelectedSprite()
	{
		Element e = XmlLoader.readXML(Handler.getRootDirectory() + "/sprites.xml").getRootElement().getChild(compIconSelect.getValue());
		iconPreview.setSprite(new SpriteSheet(e));
	}
	
	//Process an element that is being loaded in
	@Override
	public void postLoad()
	{
		Element current;
		for (int i = 0; i < fields.length; i++)
		{
			fields[i].clear();
			current = editElement.getChild(fields[i].getNodeName());
			
			try
			{
				if (current.getContentSize() == 1)
					fields[i].setValue(current.getValue());
				else if (current.getContentSize() > 1)
				{
					XmlMultiSelection field = ((XmlMultiSelection)fields[i]); 
					List<Element> items = current.getChildren();

					if (items != null)
						for (Element e : items)
						{
							field.addValue(e.getValue());
						}
				}
			}
			catch(Exception e)	
			{
				//Do nothing
			}
		}
		loadSelectedSprite();
	}
	
	private void save()
	{
		itemsXml = XmlLoader.readXML(Handler.getRootDirectory() + "/items.xml");
		if (editElement == null)
			saveAs();
		else
			saveUpdate();
	}
	
	private void saveAs()
	{
		//Get the itemID from the user
		String itemID = "";
		boolean valid = false;
		
		//Validation loop
		while (!valid && itemID != null)
		{
			itemID = JOptionPane.showInputDialog(this, "Enter a unique ItemID", "Save Entity", JOptionPane.PLAIN_MESSAGE);
			
			if (itemID != null)
			{
				valid = TextValidator.isAlphaNumeric(itemID);
				if (!valid)
					JOptionPane.showMessageDialog(this, "Use only numbers and letters", "Invalid itemID", JOptionPane.WARNING_MESSAGE);
				
				else
				{
					valid &= itemsXml.getRootElement().getChild(itemID) == null;
					if (!valid)
						JOptionPane.showMessageDialog(this, "That itemID is taken", "ID in Use", JOptionPane.WARNING_MESSAGE);
				}
			}
		}	
		
		//Save if valid
		if (valid)
		{
			Element newItem = new Element(itemID);
			
			for (int i = 0; i < fields.length; i ++)
			{
				//Multiple item list saves
				if (fields[i] instanceof XmlMultiSelection)
				{
					Element listElement = new Element (fields[i].getNodeName());
					ArrayList<String> items = ((XmlMultiSelection) fields[i]).getListItems();
					for (int j = 0; j < items.size(); j++)
					{
						listElement.addContent(new Element ("list").addContent(items.get(j)));
					}
					newItem.addContent(listElement);
				}
				
				//Single item saves
				else
					newItem.addContent(new Element(fields[i].getNodeName()).addContent(fields[i].getValue()));
			}
			
			//the saving
			itemsXml.getRootElement().addContent(newItem);
			XmlLoader.writeXML(itemsXml);
			
			load(newItem);
			if (onSave != null)
				onSave.action();
		}
	}
	
	//Updates an element that is being edited
	private void saveUpdate()
	{
		Element current;
		for (int i = 0; i < fields.length; i++)
		{
			current =  editElement.getChild(fields[i].getNodeName());
			if (fields[i] instanceof XmlMultiSelection)
			{
				XmlMultiSelection field = (XmlMultiSelection) fields[i];
				//We need to delete the old list first
				current.detach();
				current = new Element(current.getName());
				//Now start adding elements
				ArrayList<String> list = field.getListItems();
				for (String s : list)
				{
					current.addContent(new Element("list").addContent(s));
				}
				editElement.addContent(current);
			}
			else
				current.setText(fields[i].getValue());
		}
		XmlLoader.writeXML(itemsXml);
	}
	
	@Override
	public void clearForm()
	{
		super.clearForm();
		iconPreview.setSprite(null);
	}
}
