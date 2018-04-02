package editors.npcEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import dev.zoranan.utils.TextValidator;
import dev.zoranan.utils.XmlLoader;
import editors.XmlForm;
import editors.compoundObjects.Action;
import editors.compoundObjects.ComboBoxSelection;
import editors.compoundObjects.CompoundComponent;
import editors.compoundObjects.LabeledTextBox;
import editors.compoundObjects.PropertiesList;
import editors.compoundObjects.XmlMultiSelection;
import editors.compoundObjects.XmlSelection;
import util.Handler;

public class NpcEditorForm extends XmlForm {
	private ComboBoxSelection hairComboBox;
	private ComboBoxSelection headComboBox;
	private ComboBoxSelection sexComboBox;
	private XmlSelection raceSelect;
	private PropertiesList statsPropertyList;
	private ComboBoxSelection behaviorSelect;
	private Document racesDoc;
	private Document modelsDoc;
	private Document varsDoc;
	/**
	 * Create the panel.
	 */
	public NpcEditorForm() {
		super ("NPC Editor");
		varsDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/vars.xml", true);
		List<Element> statList = varsDoc.getRootElement().getChild("stats").getChildren();
		
		//Create our stat table data
		String[][] data;
		
		//Get desired array length
		for (int row = 0; row < statList.size(); row++)
		{
			if (TextValidator.isNumeric(statList.get(row).getAttributeValue("value")) == false)
				statList.remove(row);
		}
		//Initialize array
		data = new String[statList.size()][2];
		
		//Add array data
		for (int row = 0; row < data.length; row++)
		{
			if (TextValidator.isNumeric(statList.get(row).getAttributeValue("value")))
			{
				data[row][0] = statList.get(row).getAttributeValue("name");
				data[row][1] = statList.get(row).getAttributeValue("value");
			}
		}
		
		
		
		hairComboBox = new ComboBoxSelection("Hair", "hairID");
		hairComboBox.setEditable(false);
		headComboBox = new ComboBoxSelection("Head", "headID");
		headComboBox.setEditable(false);
		raceSelect = new XmlSelection("Race", "raceID", "/races.xml", "Races", false);
		sexComboBox = new ComboBoxSelection ("Sex", "sex", new String[] {"male", "female"});
		statsPropertyList = new PropertiesList("Stats", "stats", new String[]{"Name", "Value"}, data);
		behaviorSelect = new ComboBoxSelection ("Behavior", "behavior");
		behaviorSelect.setEditable(false);
		
		raceSelect.addSelectAction(new Action() {
			@Override
			public void action() {
				hairComboBox.setOptions(getLimbOptions("hair"));
				headComboBox.setOptions(getLimbOptions("head"));
			}
		});
		
		sexComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				hairComboBox.setOptions(getLimbOptions("hair"));
				headComboBox.setOptions(getLimbOptions("head"));
			}
		});
		
		this.fields = new CompoundComponent[] {new LabeledTextBox("Name", "name"),
												raceSelect,
												sexComboBox,
												hairComboBox, headComboBox,
												statsPropertyList,
												new XmlMultiSelection("Inventory", "inventory", "/items.xml", "Items"),
												new XmlMultiSelection("Equipment", "equipment", "/items.xml", "Items"),
												behaviorSelect};
		
		this.buildForm();
		this.loadDocs();
		
		statsPropertyList.getList();
	}
	
	//Load XML files
	public void loadDocs()
	{
		this.racesDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/races.xml");
		this.modelsDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/models.xml");
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

}
