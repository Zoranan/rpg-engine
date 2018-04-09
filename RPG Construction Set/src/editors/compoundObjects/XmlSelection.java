package editors.compoundObjects;

import javax.swing.SpringLayout;
import editors.subPanels.XMLExplorerPanel;
import util.ImageLoader;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**A {@link CompoundComponent} consisting of a JLabel to the left, a JTextField in the middle, and one or two JButtons to the right.<br/>
 * Clicking the first JButton allows the user to select an element from an xml file (specified in the constructor).<br/>
 * If this Component is not editable, a second JButton is added (cancel / clear). When clicked, the JTextField is cleared.
 * @author Will
 *
 */
public class XmlSelection extends LabeledTextBox{
	//private JLabel lblLabel;
	private JButton btnSelect;
	private JButton btnClear;
	private String xmlPath;
	private String xmlTypeName;
	private Action selectAction;
	private Action clearAction;
	
	/**Creates a new XmlSelection from three parameters.
	 * @param labelTxt The text to be displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 * @param editable If true, the JTextField accepts user input. Otherwise, the JTextField is not editable.
	 * 
	 * @wbp.parser.constructor
	 */
	public XmlSelection(String labelTxt, String nodeName, boolean editable) {
		super (labelTxt, nodeName);
		this.setEditable(editable);
		height = 26;
		
		//Select btn
		btnSelect = new JButton("");
		springLayout.putConstraint(SpringLayout.EAST, btnSelect, 30, SpringLayout.EAST, txtSelectionField);
		btnSelect.setIcon(ImageLoader.loadResourceIcon("/Icons/choose.png"));
		springLayout.putConstraint(SpringLayout.WEST, btnSelect, 6, SpringLayout.EAST, txtSelectionField);
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnSelect, 0, SpringLayout.VERTICAL_CENTER, txtSelectionField);
		
		btnSelect.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				defaultOnClick();
			}
		});
		
		//Clear button: Only if the field is non editable
		if (editable == false)
		{	
			btnClear = new JButton("");
			springLayout.putConstraint(SpringLayout.EAST, btnClear, 30, SpringLayout.EAST, btnSelect);
			btnClear.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					txtSelectionField.setText("");
					if (clearAction != null)
						clearAction.action();
				}
			});
			btnClear.setIcon(ImageLoader.loadResourceIcon("/Icons/x.png"));
			springLayout.putConstraint(SpringLayout.WEST, btnClear, 6, SpringLayout.EAST, btnSelect);
			springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnClear, 0, SpringLayout.VERTICAL_CENTER, btnSelect);
			add(btnClear);
		}
		
		add(btnSelect);
	}
	
	//Constructor 2
	/**Creates a new XmlSelection from five parameters.
	 * @param labelTxt The text to be displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 * @param xmlPath The path to the xml file this Component can select elements from.
	 * @param xmlName The title displayed at the top of the selection window.
	 * @param editable If true, the JTextField accepts user input. Otherwise, the JTextField is not editable.
	 */
	public XmlSelection (String labelTxt, String nodeName, String xmlPath, String xmlName, boolean editable)
	{
		this(labelTxt, nodeName, editable);
		this.setXmlPath(xmlPath);
		this.setXmlName(xmlName);
		
		btnSelect.setToolTipText("Select from " + xmlTypeName);
		if (btnClear != null)
			btnClear.setToolTipText("Clear " + labelTxt);
	}
	
	//Remove all action listeners
	/**Removes the action listeners on the select button
	 */
	private void removeActionListeners()
	{
		ActionListener[] als = this.btnSelect.getActionListeners();
		for (int i = 0; i < als.length; i++)
			this.btnSelect.removeActionListener(als[i]);
	}
	
	//Add another action (ActionListener)
	/**Adds an additional ActionListener to the select button
	 * @param al The ActionListener to be added.
	 */
	public void addClickAction(ActionListener al)
	{
		this.btnSelect.addActionListener(al);
	}
	
	//Replaces all action listeners with a new passed in action listener
	/**Sets the ActionListener on the select button. (Replaces all other listeners)
	 * @param al The ActionListener to be added.
	 * @param al
	 */
	public void setClickAction(ActionListener al)
	{	
		removeActionListeners();
		addClickAction(al);
	}
	
	//The default action performed when the select button is pressed
	/**The default action for the select button. Is removed if {@link #removeActionListeners()} is called.<br/>
	 * Creates a selection window that loads elements from the xml file set in the constructor. 
	 * If no xmlPath was set in the constructor, nothing happens.
	 */
	public void defaultOnClick()
	{
		if (this.xmlPath != null)
		{
			XMLExplorerPanel chooser = XMLExplorerPanel.newXmlChooser(this.xmlPath, this.xmlTypeName, this);
			
			JButton btn = new JButton ("");
			btn.setIcon(ImageLoader.loadResourceIcon("/Icons/check.png"));
			btn.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						setValue(chooser.getSelectedName());
						if (selectAction != null)
							selectAction.action();
						
						chooser.dispose();
					}
				});
			
			chooser.addBtn(btn);
		}
	}
	
	//Adds an action to be performed when an xml element is selected
	/**Sets an Action Interface to be triggerd when an xml element is selected.
	 * @param action The Action interface to be triggered.
	 */
	public void addSelectAction(Action action)
	{
		this.selectAction = action;
	}
	
	//Adds an action to be performed when this component is cleared
	/**Sets an Action Interface to be triggered when this component is cleared
	 * @param action The Action interface to be triggered.
	 */
	public void setClearAction(Action action)
	{
		this.clearAction = action;
	}
	
	/**Sets the path to the xml file this Component can select elements from.
	 * @param p
	 */
	public void setXmlPath(String p)
	{
		this.xmlPath = p;
	}
	
	/**Sets the title displayed at the top of the Selection window.
	 * @param n The title displayed at the top of the element selection window.
	 */
	public void setXmlName (String n)
	{
		this.xmlTypeName = n;
	}
	
	@Override
	public void setEnabled(boolean b)
	{
		this.txtSelectionField.setEnabled(b);
		this.btnSelect.setEnabled(b);
		
		if (this.btnClear != null)
			this.btnClear.setEnabled(b);
	}
	
}
