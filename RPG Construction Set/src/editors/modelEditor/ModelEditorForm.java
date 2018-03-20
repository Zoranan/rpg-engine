package editors.modelEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

import org.jdom2.Document;
import org.jdom2.Element;

import editors.XmlForm;
import editors.compoundObjects.ComboBoxSelection;
import editors.compoundObjects.CompoundComponent;
import editors.compoundObjects.XmlMultiSelection;
import editors.compoundObjects.XmlSelection;
import util.Handler;
import util.TextValidator;
import util.TxtLoader;
import util.XmlLoader;

public class ModelEditorForm extends XmlForm {
	private JButton btnSave;
	private Document modelsXml;
	/**
	 * Create the panel.
	 */
	public ModelEditorForm() {
		super ("Model Editor");
		
		fields = new CompoundComponent[] {new ComboBoxSelection("Limb", "limb", TxtLoader.getTxtAsArray("configFiles/limbs.txt")),
										new XmlSelection("Model (Front)", "front", "/sprites.xml", "Sprites", false),
										new XmlSelection("Model (Back)", "back", "/sprites.xml", "Sprites", false),
										new XmlSelection("Model (Left)", "left", "/sprites.xml", "Sprites", false),
										new XmlSelection("Model (Right)", "right", "/sprites.xml", "Sprites", false)};
		
		modelsXml = XmlLoader.readXML(Handler.getRootDirectory() + "/models.xml");
		btnSave = new JButton("Save");
		btnSave.setIcon(new ImageIcon(XmlMultiSelection.class.getResource("/Icons/save20px.png")));
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
	
	//Processes the editElement, loading its values into the form. Called by final super method "load"
	@Override
	protected void postLoad()
	{
		fields[0].setValue(editElement.getAttributeValue("limb"));
		
		for (int i = 1; i < this.fields.length; i++)
		{
			fields[i].setValue(editElement.getChildText(fields[i].getNodeName()));
		}
	}
	
	//Decides how to save the model (update or new)
	public void save()
	{
		//We need to check for all angles to be filled in in the future
		if (true) //<- temp (for now, just save it since we only have one angle animated)
		{
			if (editElement == null)
				saveAs();
			else
				saveUpdate();
		}
	}
	
	//Updates the file being edited
	private void saveUpdate()
	{
		editElement.setAttribute(fields[0].getNodeName(), fields[0].getValue());
		for (int i = 1; i < this.fields.length; i++)
		{
			XmlSelection current = ((XmlSelection) fields[i]);
			editElement.setContent(i-1, new Element(current.getNodeName()).addContent(current.getValue()));
		}
		XmlLoader.writeXML(modelsXml);
	}
	
	//Save the model as a new element
	private void saveAs()
	{
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
			load(newModel);//Sets the new model as the currently edited element
		}
	}

}