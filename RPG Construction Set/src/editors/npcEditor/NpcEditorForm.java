package editors.npcEditor;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import dev.zoranan.utils.XmlLoader;
import editors.XmlForm;
import editors.compoundObjects.Action;
import editors.compoundObjects.ComboBoxSelection;
import editors.compoundObjects.CompoundComponent;
import editors.compoundObjects.LabeledTextBox;
import editors.compoundObjects.XmlSelection;
import util.Handler;

public class NpcEditorForm extends XmlForm {
	private ComboBoxSelection hairComboBox;
	private ComboBoxSelection headComboBox;
	private ComboBoxSelection sexComboBox;
	private XmlSelection raceSelect;
	private Document racesDoc;
	private Document modelsDoc;
	/**
	 * Create the panel.
	 */
	public NpcEditorForm() {
		super ("NPC Editor");
		
		hairComboBox = new ComboBoxSelection("Hair", "hairID");
		hairComboBox.setEditable(false);
		headComboBox = new ComboBoxSelection("Head", "headID");
		headComboBox.setEditable(false);
		raceSelect = new XmlSelection("Race", "raceID", "/races.xml", "Races", false);
		sexComboBox = new ComboBoxSelection ("Sex", "sex", new String[] {"male", "female"});
		
		raceSelect.addSelectAction(new Action() {
			@Override
			public void action() {
				hairComboBox.setOptions(getLimbOptions("hair"));
				headComboBox.setOptions(getLimbOptions("head"));
			}
		});
		
		this.fields = new CompoundComponent[] {new LabeledTextBox("Name", "name"),
												raceSelect,
												sexComboBox,
												hairComboBox, headComboBox
												};
		
		this.buildForm();
		this.loadDocs();
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
