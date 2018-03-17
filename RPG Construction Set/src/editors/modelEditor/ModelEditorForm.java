package editors.modelEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

import org.jdom2.Document;
import org.jdom2.Element;

import editors.XmlForm;
import editors.compoundObjects.ComboBoxSelection;
import editors.compoundObjects.CompoundComponent;
import editors.compoundObjects.XmlSelection;
import util.Handler;
import util.TextValidator;
import util.TxtLoader;
import util.XmlLoader;

public class ModelEditorForm extends XmlForm {
	private JButton btnSave;
	/**
	 * Create the panel.
	 */
	public ModelEditorForm() {
		super ("Model Editor");
		
		this.fields = new CompoundComponent[] {new ComboBoxSelection("Limb", "limb", TxtLoader.getTxtAsArray("configFiles/limbs.txt")),
										new XmlSelection("Model (Front)", "front", "/sprites.xml", "Sprites", false),
										new XmlSelection("Model (Back)", "back", "/sprites.xml", "Sprites", false),
										new XmlSelection("Model (Left)", "left", "/sprites.xml", "Sprites", false),
										new XmlSelection("Model (Right)", "right", "/sprites.xml", "Sprites", false)};
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, btnSave, VERT_PADDING, SpringLayout.SOUTH, fields[fields.length - 1]);
		springLayout.putConstraint(SpringLayout.WEST, btnSave, 0, SpringLayout.WEST, fields[fields.length - 1]);
		
		//Build the form
		this.buildForm();
		add(btnSave);

	}
	
	public void save()
	{
		///////
		//We need to check for all angles to be filled in in the future
		if (true) //<- temp
		{
			Document modelsXml = XmlLoader.readXML(Handler.getRootDirectory() + "/models.xml"); //Get the document
			//Get the itemID from the user
			boolean valid = false;
			String modelID = "";
			while (!valid && modelID != null)
			{
				modelID = JOptionPane.showInputDialog(this, "Enter a unique modelID", "Save Model", JOptionPane.PLAIN_MESSAGE);
				
				if (modelID != null)
				{
					valid = (TextValidator.isAlphaNumeric(modelID) && modelsXml.getRootElement().getChild(modelID) == null);
					
					if (!valid)
						JOptionPane.showMessageDialog(this, "Invalid ID, or that ID is taken", "Invalid Name", JOptionPane.WARNING_MESSAGE);
				}
			}
			//If we entered a save name, and it was unique...
			if (valid)
			{
				Element newModel = new Element(modelID);
				newModel.setAttribute(fields[0].getNodeName(), fields[0].getValue());
				
				for (int i = 1; i < fields.length; i++)
				{
					newModel.addContent(new Element (fields[i].getNodeName()).addContent(fields[i].getValue()));
				}
				
				modelsXml.getRootElement().addContent(newModel);
				
				XmlLoader.writeXML(modelsXml);
			}
		}
	}

}