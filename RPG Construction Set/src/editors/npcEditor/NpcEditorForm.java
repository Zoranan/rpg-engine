package editors.npcEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

import org.jdom2.Document;
import org.jdom2.Element;

import dev.zoranan.utils.TextValidator;
import dev.zoranan.utils.XmlLoader;
import editors.XmlForm;
import editors.compoundObjects.Action;
import editors.compoundObjects.ComboBoxSelection;
import editors.compoundObjects.CompoundComponent;
import editors.compoundObjects.LabeledTextBox;
import editors.compoundObjects.XmlMultiSelection;
import editors.compoundObjects.XmlSelection;
import editors.compoundObjects.XmlSubform;
import util.Handler;
import util.ImageLoader;

public class NpcEditorForm extends XmlForm {
	private ComboBoxSelection hairComboBox;
	private ComboBoxSelection headComboBox;
	private ComboBoxSelection sexComboBox;
	private XmlSelection raceSelect;
	private ComboBoxSelection behaviorSelect;
	private Document racesDoc;
	private Document modelsDoc;
	private Document npcsDoc;
	private Document varsDoc;
	/**
	 * Create the panel.
	 */
	public NpcEditorForm() {
		super ("NPC Editor");
		varsDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/vars.xml", true);
		List<Element> statList = varsDoc.getRootElement().getChild("stats").getChildren();
		
		//Get desired array length
		for (int row = 0; row < statList.size(); row++)
		{
			if (TextValidator.isNumeric(statList.get(row).getAttributeValue("value")) == false)
				statList.remove(row);
		}
		//Initialize array
		ArrayList<CompoundComponent> inner = new ArrayList<CompoundComponent>();
		ArrayList<String> values = new ArrayList<String>(); 
		
		//Add array data
		for (int row = 0; row < statList.size(); row++)
		{
			if (TextValidator.isNumeric(statList.get(row).getAttributeValue("value")))
			{
				
				inner.add(new LabeledTextBox(statList.get(row).getAttributeValue("name"), statList.get(row).getName()));
				values.add(statList.get(row).getAttributeValue("value"));
			}
		}
		
		//Form components
		hairComboBox = new ComboBoxSelection("Hair", "hairID");
		hairComboBox.setEditable(false);
		headComboBox = new ComboBoxSelection("Head", "headID");
		headComboBox.setEditable(false);
		raceSelect = new XmlSelection("Race", "raceID", "/races.xml", "Races", false);
		sexComboBox = new ComboBoxSelection ("Sex", "sex", new String[] {"male", "female"});
		behaviorSelect = new ComboBoxSelection ("Behavior", "behavior");
		behaviorSelect.setEditable(false);
		
		raceSelect.addSelectAction(new Action() {
			@Override
			public void action() {
				hairComboBox.setOptions(getLimbOptions("hair"));
				headComboBox.setOptions(getLimbOptions("head"));
			}
		});
		
		sexComboBox.setSelectionChangeAction(new Action() {
			@Override
			public void action() {
				hairComboBox.setOptions(getLimbOptions("hair"));
				headComboBox.setOptions(getLimbOptions("head"));
			}
		});
		
		this.fields = new CompoundComponent[] {new LabeledTextBox("Name", "name"),
												raceSelect,
												sexComboBox,
												hairComboBox, headComboBox,
												new XmlSubform("Stats", "stats", inner, values),
												new XmlMultiSelection("Inventory", "inventory", "/items.xml", "Items"),
												new XmlMultiSelection("Equipment", "equipment", "/items.xml", "Items"),
												behaviorSelect};
		
		this.buildForm();
		this.loadDocs();
		
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
	
	//Load XML files
	public void loadDocs()
	{
		this.racesDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/races.xml");
		this.modelsDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/models.xml");
		this.npcsDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/npcs.xml");
	}
	
	//Get a list of body part models by limb category
	public ArrayList<String> getLimbOptions(String limbCat)
	{
		Element partsEle;
		ArrayList<String> options = new ArrayList<String>();
		
		if (raceSelect.getValue().length() > 0)
		{
			partsEle = racesDoc.getRootElement().getChild(raceSelect.getValue()).getChild(sexComboBox.getValue());
			
			if (partsEle != null)
			{
				List<Element> parts = partsEle.getChildren();
				for (Element e : parts)
				{
					//Element model = modelsDoc.getRootElement().getChild(e.getValue());
					if (modelsDoc.getRootElement().getChild(e.getValue()).getAttributeValue("limb").equals(limbCat))
						options.add(e.getValue());
				}
			}
		}
		return options;
	}//END getLimbOptions()
	
	//Save our NPC
	public void save()
	{
		if (editElement == null)
			saveAs();
		else
		{
			editElement.detach();
			saveNpc(editElement.getName());
		}
	}
	
	//Gets a save name, and initiate saving
	private void saveAs()
	{
		//Get the itemID from the user
		String npcID = "";
		boolean valid = false;

		//Validation loop
		while (!valid && npcID != null)
		{
			npcID = JOptionPane.showInputDialog(this, "Enter a unique npcID", "Save NPC", JOptionPane.PLAIN_MESSAGE);

			if (npcID != null)
			{
				valid = TextValidator.isAlphaNumeric(npcID);
				if (!valid)
					JOptionPane.showMessageDialog(this, "Use only numbers and letters", "Invalid npcID", JOptionPane.WARNING_MESSAGE);

				else
				{
					valid &= npcsDoc.getRootElement().getChild(npcID) == null;
					if (!valid)
						JOptionPane.showMessageDialog(this, "That npcID is taken", "ID in Use", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		
		if (npcID != null)
		{
			saveNpc(npcID);
		}
	}
	
	//Save the current form to the NPC xml file
	private void saveNpc(String ID)
	{
		Element newNpc = new Element(ID);
		for (int i = 0; i < fields.length; i++)
		{
			Element current = new Element(fields[i].getNodeName());
			if (fields[i] instanceof XmlMultiSelection)
			{
				ArrayList<String> items = ((XmlMultiSelection) fields[i]).getListItems();
				for (int j = 0; j < items.size(); j++)
				{
					current.addContent(new Element ("list").addContent(items.get(j)));
				}
			}
			else if (fields[i] instanceof XmlSubform)
			{
				ArrayList<String> values = ((XmlSubform) fields[i]).getValues();
				ArrayList<String> nodeNames = ((XmlSubform) fields[i]).getNodeNames();
				for (int j = 0; j < values.size(); j++)
				{
					Element stat = new Element (nodeNames.get(j));
					stat.setAttribute("value", values.get(j));
					
					//Only add the stat if it is not the default value
					if (values.get(j).isEmpty() == false && statHasChanged(stat))
						current.addContent(stat);
				}
			}
			else
			{
				current.addContent(fields[i].getValue());
			}
			
			newNpc.addContent(current);
		}
		
		this.npcsDoc.getRootElement().addContent(newNpc);
		XmlLoader.writeXML(npcsDoc);
		onSave.action();
		
		load(newNpc);
	}
	
	//Check if a stat is set different than the default
	private boolean statHasChanged(Element newStat)
	{
		boolean hasChanged = false;
		
		String origValue = this.varsDoc.getRootElement().getChild("stats").getChild(newStat.getName()).getAttributeValue("value");
		String newValue = newStat.getAttributeValue("value");
		if (!origValue.equals(newValue) && TextValidator.isNumeric(newValue))
		{
			hasChanged = true;
		}
		
		return hasChanged;
	}
	
	//
	@Override
	protected void postLoad()
	{
		
		for (CompoundComponent field : fields)
		{
			field.clear();
			
			//Set our stats values in the editor
			if (field instanceof XmlSubform)
			{
				List<Element> stats = editElement.getChild(field.getNodeName()).getChildren();
				for (Element e : stats)
				{
					((XmlSubform) field).setValueAtNode(e.getName(), e.getAttributeValue("value"));
				}
			}
			//Set our list items
			else if (field instanceof XmlMultiSelection)
			{
				List<Element> list = editElement.getChild(field.getNodeName()).getChildren();
				for (Element e : list)
				{
					((XmlMultiSelection) field).addValue(e.getText());
				}
			}
			//Set single field values
			else
			{
				//System.out.println(editElement);
				//System.out.println(editElement.getChildText("name"));
				field.setValue(editElement.getChildText(field.getNodeName()));
			}
		}
	}

}
