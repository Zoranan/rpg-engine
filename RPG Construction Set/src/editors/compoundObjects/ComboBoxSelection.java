package editors.compoundObjects;

import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SpringLayout;
import javax.swing.text.JTextComponent;

public class ComboBoxSelection extends LabeledTextBox {
	private JComboBox<String> comboBox;
	private DefaultComboBoxModel<String> model;
	/**
	 * Create the panel.
	 */
	public ComboBoxSelection(String labelTxt, String nodeName, String[] options)
	{
		super (labelTxt, nodeName);
		SpringLayout springLayout = (SpringLayout) getLayout();
		
		//replace the text box
		this.remove(txtSelectionField);
		
		comboBox = new JComboBox<String>();
		JTextComponent editor = ((JTextComponent)comboBox.getEditor().getEditorComponent());
		editor.addKeyListener(inputMask);
		springLayout.putConstraint(SpringLayout.NORTH, comboBox, -3, SpringLayout.NORTH, lblLabel);
		springLayout.putConstraint(SpringLayout.WEST, comboBox, 6, SpringLayout.EAST, lblLabel);
		springLayout.putConstraint(SpringLayout.EAST, comboBox, 120, SpringLayout.EAST, lblLabel);
		
		model = new DefaultComboBoxModel<String>();
		setOptions(options);
		comboBox.setModel(model);
		
		add(comboBox);
	}
	
	public ComboBoxSelection(String labelTxt, String nodeName)
	{
		this (labelTxt, nodeName, new String[0]);
	}
	
	public void setOptions(String[] options)
	{
		model.removeAllElements();
		
		for (int i=0; i<options.length; i++)
			model.addElement(options[i]);
	}
	
	public void setOptions(ArrayList<String> options)
	{
		model.removeAllElements();
		
		for (int i=0; i<options.size(); i++)
			model.addElement(options.get(i));
	}
	
	public void setEditable(boolean b)
	{
		comboBox.setEditable(b);
	}
	
	@Override
	public int getComponentHeight()
	{
		return 28;
	}
	
	@Override
	public String getValue()
	{
		return (String) this.comboBox.getSelectedItem();
	}

}
