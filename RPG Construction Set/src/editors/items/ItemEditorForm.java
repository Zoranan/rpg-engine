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
import util.TextValidator;
import util.TxtLoader;
import util.XmlLoader;

public class ItemEditorForm extends XmlForm {
	private SpriteViewerPanel iconPreview;
	/**
	 * Create the panel.
	 */
	public ItemEditorForm() {
		super ("Item Editor");
		
		ComboBoxSelection compType = new ComboBoxSelection("Type", "type", TxtLoader.getTxtAsArray("configFiles/itemTypes.txt"));
		LabeledTextBox compStackSize = new LabeledTextBox("Max Stack Size", "maxStackSize");
		LabeledTextBox compBaseValue = new LabeledTextBox("Base Value", "value");
		XmlSelection compIconSelect = new XmlSelection("Icon (Sprite)", "spriteIconID", "/sprites.xml", "Sprites", false);
		fields = new CompoundComponent[] {compIconSelect,
											new LabeledTextBox("Item Name", "name"),
											compType,
											new CheckBoxSelection("Consumable", "consumable", false),
											compStackSize,
											compBaseValue,
											new XmlMultiSelection("Models", "models", "/models.xml", "Models"),//???
											new XmlMultiSelection("Effects", "effects")//??????
											};
		
		compType.setEditable(true);
		compType.setMode(LabeledTextBox.CharacterMode.ALPHA);
		compStackSize.setMode(LabeledTextBox.CharacterMode.NUMERIC);
		compStackSize.setText("1");
		compBaseValue.setMode(LabeledTextBox.CharacterMode.NUMERIC);
		compBaseValue.setText("0");
		
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
					compStackSize.setText("1");
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
					compBaseValue.setText("0");
				}
			}
		});
		
		compIconSelect.addSelectAction(new Action(){
			@Override
			public void action() {
				Element e = XmlLoader.readXML(Handler.getRootDirectory() + "/sprites.xml").getRootElement().getChild(compIconSelect.getValue());
				iconPreview.setSprite(new SpriteSheet(e));
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
		btnSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAs();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, VERT_PADDING, SpringLayout.SOUTH, fields[fields.length-1]);
		springLayout.putConstraint(SpringLayout.WEST, btnSave, 0, SpringLayout.WEST, fields[fields.length-1]);
		add(btnSave);
	}
	
	//FUNCTIONS
	
	public void saveAs()
	{
		Document itemsXml = XmlLoader.readXML(Handler.getRootDirectory() + "/items.xml"); //Get the document
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
			
			//Element effects = new Element("effects");
			
			//the saving
			itemsXml.getRootElement().addContent(newItem);
			XmlLoader.writeXML(itemsXml);
		}
	}
	
	public void save(String itemID)
	{
		
	}

}
