package editors.races;

import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;

import dev.zoranan.utils.TextValidator;
import dev.zoranan.utils.XmlLoader;
import editors.XmlForm;
import editors.compoundObjects.ComboBoxSelection;
import editors.compoundObjects.CompoundComponent;
import editors.compoundObjects.XmlMultiSelection;
import util.Handler;
import util.ImageLoader;

import javax.swing.SpringLayout;
import javax.swing.JButton;

public class RaceEditorForm extends XmlForm {
	private Document racesDoc;
	//private CompoundComponent[] 
	/**
	 * Create the panel.
	 */
	public RaceEditorForm() {
		super ("Race Editor");
		
		this.fields = new CompoundComponent[]{
				new ComboBoxSelection("Sex", "sex", new String[] {"male", "female"}),
				new XmlMultiSelection("Limbs", "models", "/models.xml", "Models")
				}
		;
		fields[1].setHeight(300);
		
		this.buildForm();
		
		//Save button
		JButton btnSave = new JButton("Save");
		btnSave.setIcon(ImageLoader.loadResourceIcon("/Icons/save20px.png"));
		btnSave.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				save();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, VERT_PADDING, SpringLayout.SOUTH, fields[fields.length-1]);
		springLayout.putConstraint(SpringLayout.WEST, btnSave, 0, SpringLayout.WEST, fields[fields.length-1]);
		add (btnSave);
	}
	
	//Saving
	public void openSaveDialog()
	{
		String raceNameID = JOptionPane.showInputDialog(this, "Enter a unique RaceID", "Save Race", JOptionPane.PLAIN_MESSAGE);
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
	
	private void save()
	{
		racesDoc = XmlLoader.readXML(Handler.getRootDirectory() + "/races.xml", true);
		
		if (editElement == null)
			openSaveDialog();
		else
			saveUpdate();
	}
	
	//FUNCTION: Save Race / Sex
	private void saveRace(String raceNameID)
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
			
			this.clearForm();
			if (onSave != null)
				onSave.action();
		}
	}
	
	//Gets the element that specifies the sex, and all limbs associated with that sex for this race
	private Element createGenderElement()
	{
		Element sex = new Element (fields[0].getValue());
		
		ArrayList<String> list = ((XmlMultiSelection) fields[1]).getListItems();
		for (String s : list)
		{
			sex.addContent(new Element("list").addContent(s));
		}
		
		return sex;
	}
	
	//Saves an element being edited
	private void saveUpdate()
	{
		editElement = racesDoc.getRootElement().getChild(editElement.getName()); //Should not need this, but its the only way it worked?
		editElement.removeChild(fields[0].getValue());
		editElement.addContent(createGenderElement());
		
		XmlLoader.writeXML(racesDoc);
	}
	
	@Override
	public void postLoad()
	{
		String[] options = {"male", "female"};
		int choice = JOptionPane.showOptionDialog(this, "Which sex would you like to edit?", "Choose Sex", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		boolean found = editElement.getChild(options[choice]) != null;
		
		if (!found)
		{
			clearForm();
			JOptionPane.showMessageDialog(this, "The chosen sex is undefined for this race");
		}
		
		//Finally, load the race-sex
		else
		{
			fields[0].setValue(options[choice]);
			fields[1].clear();
			List<Element> limbs = editElement.getChild(options[choice]).getChildren();
			
			for (Element e : limbs)
				((XmlMultiSelection)fields[1]).addValue(e.getValue());
			
			fields[0].setEnabled(false);
		}
	}
	
	@Override
	public void clearForm()
	{
		super.clearForm();
		fields[0].setEnabled(true);
	}
}
