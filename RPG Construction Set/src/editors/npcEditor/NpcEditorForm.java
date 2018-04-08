package editors.npcEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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

/**Create and edit NPC's that can be placed within the game world
 * @author Will
 *
 */
public class NpcEditorForm extends XmlForm {
	private XmlSubform customLimbs;
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
	 * Create the NpcEditorForm
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
		customLimbs = new XmlSubform("Limbs", "limbs");
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
				buildLimbSelectionForm();
			}
		});
		raceSelect.setClearAction(new Action() {
			@Override
			public void action() {
				customLimbs.removeAll();
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
												customLimbs,
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
	
	
	/**Load the XML documents needed for the form
	 */
	public void loadDocs()
	{
		this.racesDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/races.xml");
		this.modelsDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/models.xml");
		this.npcsDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/npcs.xml");
	}
	
	/**Updates the limb selection subform with the appropriate options for the selected race and sex fields.<br/>
	 * Gets interchangeable LimbTypes with {@link #getDuplicateLimbs()}<br/>
	 * Creates a new combo box for the {@link #customLimbs} subform. <br/>
	 * Populates the combo box with values from the {@link #getLimbOptions(String)} function.<br/>
	 * Adds the combo box to the sub form
	 */
	private void buildLimbSelectionForm()
	{
		ArrayList<String> limbCategories = getDuplicateLimbs();
		loadDocs();
		//Iterate over each possible limb type selection
		for (String limbType : limbCategories)
		{
			//Create a new combo box for the limb type
			ComboBoxSelection c = new ComboBoxSelection(limbType, limbType);
			//Populate the combo box with all possible selections for that limb type
			c.setOptions(getLimbOptions(limbType));
			//Add the combo box to the Limb Selection subform
			customLimbs.addComponent(c);
		}
		//Once all items are added, build the Custom Limbs Subform
		customLimbs.buildForm();
	}
	
	/**Looks at the selected Race definition for limb types that have more than one possible selection.
	 * @return A list of customizable limb categories
	 */
	private ArrayList<String> getDuplicateLimbs()
	{
		ArrayList<String> finalLimbsList = new ArrayList<String>();
		
		if (!raceSelect.getValue().isEmpty())
		{
			HashMap<String, Boolean> duplicateLimbs = new HashMap<String, Boolean>();
			Element partsEle = racesDoc.getRootElement().getChild(raceSelect.getValue()).getChild(sexComboBox.getValue());
			
			//The first time through
			for (Element e : partsEle.getChildren())
			{
				String partType = modelsDoc.getRootElement().getChild(e.getText()).getAttributeValue("limb");
				
				if (duplicateLimbs.containsKey(partType))
					duplicateLimbs.put(partType, true);
				else
					duplicateLimbs.put(partType, false);
				
			}
			
			//Now we know which limb types appear more than once in the race definition.
			//Use this hashmap to create our final limb selection list
			for (Entry<String, Boolean> e : duplicateLimbs.entrySet())
			{
				if (e.getValue())
				{
					finalLimbsList.add(e.getKey());
				}
			}
		}
		
		return finalLimbsList;
	}
	
	
	/**Get a list of all possible body part models by limb category, for the currently selected race and sex
	 * @param limbCat The type of limbs we want to gather our options for
	 * @return A list of limbs in the limbCat category
	 */
	private ArrayList<String> getLimbOptions(String limbCat)
	{
		Element partsEle;
		ArrayList<String> options = new ArrayList<String>();
		
		if (!raceSelect.getValue().isEmpty())
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
	
	
	/**Initiates a save operation on the NPC information within the form. <br/>
	 * If this NPC has not been saved yet, a "Save As" dialog box will pop up.
	 */
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
	
	
	/**Prompts the user for an npcID and check it for validity before calling the {@link #saveNpc(String)} function.<br/> 
	 * The npcID can not contain spaces, must consist of alpha-numeric characters, and can not be in use already.
	 */
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
	
	
	/**Creates a new Element in the NPC's XML file with the npcID passed in. 
	 * Saves the NPC information in the form to the new element.
	 * @param ID the npcID we are saving the form information to
	 */
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
					Element listElement = new Element (nodeNames.get(j));
					listElement.setText(values.get(j));
					
					//Only add the stat if it is not the default value
					if (fields[i].getNodeName().equals("stats"))
					{
						if (values.get(j).isEmpty() == false && statHasChanged(listElement))
							current.addContent(listElement);
					}	
					else
						current.addContent(listElement);
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
	
	/**Checks if the passed in element is different than the original element it is modifying.
	 * @param newStat The element representing the newStat value.
	 * @return True if the value in the passed in element is different than the value in the original element.
	 */
	private boolean statHasChanged(Element newStat)
	{
		boolean hasChanged = false;
		
		String origValue = this.varsDoc.getRootElement().getChild("stats").getChild(newStat.getName()).getAttributeValue("value");
		String newValue = newStat.getText();
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
		//This form has to be rebuilt after the data is loaded
		customLimbs.removeAll();
		loadDocs();
		
		for (CompoundComponent field : fields)
		{
			field.clear();
			
			
			//Set our sub forms in the editor
			if (field instanceof XmlSubform)
			{
				//If we have reached our custom limb selection subform
				if (((XmlSubform) field).length() == 0 && !this.raceSelect.getValue().isEmpty())
				{
					buildLimbSelectionForm();
				}
				
				List<Element> subform = editElement.getChild(field.getNodeName()).getChildren();
				for (Element e : subform)
				{
					((XmlSubform) field).setValueAtNode(e.getName(), e.getText());
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
				field.setValue(editElement.getChildText(field.getNodeName()));
			}
		}
	}
	
	@Override
	public void clearForm()
	{
		super.clearForm();
		this.customLimbs.removeAll();
	}

}
