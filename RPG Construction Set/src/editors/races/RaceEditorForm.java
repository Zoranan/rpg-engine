package editors.races;

import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;

import editors.XmlForm;
import editors.compoundObjects.ComboBoxSelection;
import editors.compoundObjects.CompoundComponent;
import editors.compoundObjects.XmlMultiSelection;
import editors.compoundObjects.XmlSelection;
import util.Handler;
import util.TextValidator;
import util.XmlLoader;

import javax.swing.SpringLayout;
import javax.swing.JButton;

public class RaceEditorForm extends XmlForm {
	private Document racesDoc;
	//private CompoundComponent[] 
	/**
	 * Create the panel.
	 */
	public RaceEditorForm() {
		super ("New Race");
		
		this.fields = new CompoundComponent[]{
				new ComboBoxSelection("Sex", "sex", new String[] {"male", "female"}),
				new XmlMultiSelection("Limbs", "models", "/models.xml", "Models")
		/*
				new XmlMultiSelection("Hair Styles", "hair", "/models.xml", "Models"),
				new XmlMultiSelection("Heads", "heads", "/models.xml", "Models"),
				new XmlSelection("Chest", "chest", "/models.xml", "Models", false),
				new XmlSelection("Left Arm", "leftArm", "/models.xml", "Models", false),
				new XmlSelection("Right Arm", "rightArm", "/models.xml", "Models", false),
				new XmlSelection("Left Hand", "leftHand", "/models.xml", "Models", false),
				new XmlSelection("Right Hand", "rightHand", "/models.xml", "Models", false),
				new XmlSelection("Left Thigh", "leftThigh", "/models.xml", "Models", false),
				new XmlSelection("Right Thigh", "rightThigh", "/models.xml", "Models", false),
				new XmlSelection("Left Foot", "leftFoot", "/models.xml", "Models", false),
				new XmlSelection("Right Foot", "rightFoot", "/models.xml", "Models", false),*/};
		fields[1].setHeight(300);
		
		this.buildForm();
		
		//Save button
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				openSaveDialog();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, VERT_PADDING, SpringLayout.SOUTH, fields[fields.length-1]);
		springLayout.putConstraint(SpringLayout.EAST, btnSave, -14, SpringLayout.EAST, fields[fields.length-1]);
		add (btnSave);
		h += 25;
		
		this.setPreferredSize(new Dimension(350, h));
		racesDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/races.xml", true);
	}
	
	//Saving
	public void openSaveDialog()
	{
		String raceNameID = JOptionPane.showInputDialog(this, "Enter a unique ItemID", "Save Entity", JOptionPane.PLAIN_MESSAGE);
		boolean valid = true;
		
		if (raceNameID != null && raceNameID.length() > 1)
			for (int i = 0; i < raceNameID.length(); i ++)
				if (TextValidator.isNotAlphaNumeric(raceNameID.charAt(i)) && raceNameID.charAt(i) != '_')
					valid = false;
		
		if (raceNameID != null && valid)
			saveRace(raceNameID);
		else if (raceNameID != null)
			JOptionPane.showMessageDialog(this, "That raceID is invalid\nUse only Alpha-Numeric or '_'", "Invalid Characters", JOptionPane.WARNING_MESSAGE);
	}
	
	
	//FUNCTION: Save Race / Sex
	public void saveRace(String raceNameID)
	{
		Element nameID;
		boolean save = true;
		//First, check if the race in question exists already
		//If it does not exist, create a new node with that raceNameID
		if (racesDoc.getRootElement().getChild(raceNameID) == null)
		{
			nameID = new Element (raceNameID);
		}
		//If it does exist, we need to look at the existing node
		else
		{
			nameID = racesDoc.getRootElement().getChild(raceNameID);
			//Now check if we are overwriting an existing gender of that race
			//If the gender we are making does not exist
			if (nameID.getChild(fields[0].getValue()) == null)
			{
				//Save the new gender into the existing race
			}
			//If we are about to overwrite a gender within the race, warn the user
			else
			{
				String message = raceNameID + " " + fields[0].getValue() + "s are already defined.\nDo you want to overwrite?";
				int i = JOptionPane.showOptionDialog(this, message, "Overwrite?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, 1);
				
				if (i == 0)
				{
					nameID.removeChild(fields[0].getValue());
				}
				else
				{
					save = false;
				}
			}
		}
		
		if (save)
		{
			racesDoc.getRootElement().removeChild(raceNameID);
			nameID.addContent(createGenderElement());
			racesDoc.getRootElement().addContent(nameID);
			XmlLoader.writeXML(racesDoc);
		}
	}
	
	private Element createGenderElement()
	{
		Element sex = new Element (fields[0].getValue());
		/*
		Element hair = new Element (fields[1].getNodeName());
		ArrayList<String> hairStyles = ((XmlMultiSelection) fields[1]).getListItems();
		for (int i = 0; i < hairStyles.size(); i++)
			hair.addContent(new Element ("style").addContent(hairStyles.get(i)));
		sex.addContent(hair);
		
		Element head = new Element (fields[2].getNodeName());
		ArrayList<String> heads = ((XmlMultiSelection) fields[2]).getListItems();
		for (int i = 0; i < heads.size(); i++)
			head.addContent(new Element ("head").addContent(heads.get(i)));
		sex.addContent(head);
		*/
		ArrayList<String> list = ((XmlMultiSelection) fields[1]).getListItems();
		for (String s : list)
		{
			sex.addContent(new Element("list").addContent(s));
		}
		
		return sex;
	}
}
