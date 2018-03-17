package editors.compoundObjects;

import javax.swing.SpringLayout;
import editors.subPanels.XMLExplorerPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.ImageIcon;

public class XmlSelection extends LabeledTextBox{
	//private JLabel lblLabel;
	private JButton btnSelect;
	private JButton btnClear;
	private String xmlPath;
	private String xmlTypeName;
	private Action selectAction;
	
	/**
	 * Create the panel.
	 * @wbp.parser.constructor
	 */
	public XmlSelection(String labelTxt, String nodeName, boolean editable) {
		super (labelTxt, nodeName);
		this.setEditable(editable);
		SpringLayout springLayout = (SpringLayout) getLayout();
		
		//Select btn
		btnSelect = new JButton("");
		springLayout.putConstraint(SpringLayout.EAST, btnSelect, 30, SpringLayout.EAST, txtSelectionField);
		btnSelect.setIcon(new ImageIcon(XmlSelection.class.getResource("/Icons/choose.png")));
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
				}
			});
			btnClear.setIcon(new ImageIcon(XmlSelection.class.getResource("/Icons/x.png")));
			springLayout.putConstraint(SpringLayout.WEST, btnClear, 6, SpringLayout.EAST, btnSelect);
			springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, btnClear, 0, SpringLayout.VERTICAL_CENTER, btnSelect);
			add(btnClear);
		}
		
		add(btnSelect);
	}
	
	//Constructor 2
	public XmlSelection (String labelTxt, String nodeName, String xmlPath, String xmlName, boolean editable)
	{
		this(labelTxt, nodeName, editable);
		this.setXmlPath(xmlPath);
		this.setXmlName(xmlName);
		
		btnSelect.setToolTipText("Select from " + xmlTypeName);
		btnClear.setToolTipText("Clear " + labelTxt);
	}
	
	//Remove all action listeners
	public void removeActionListeners()
	{
		ActionListener[] als = this.btnSelect.getActionListeners();
		for (int i = 0; i < als.length; i++)
			this.btnSelect.removeActionListener(als[i]);
	}
	
	//Add another action (ActionListener)
	public void addClickAction(ActionListener al)
	{
		this.btnSelect.addActionListener(al);
	}
	
	//Replaces all action listeners with a new passed in action listener
	public void setClickAction(ActionListener al)
	{	
		removeActionListeners();
		addClickAction(al);
	}
	
	//The default action performed when the select button is pressed
	public void defaultOnClick()
	{
		if (this.xmlPath != null)
		{
			XMLExplorerPanel chooser = XMLExplorerPanel.newXmlChooser(this.xmlPath, this.xmlTypeName, this);
			
			JButton btn = new JButton ("");
			btn.setIcon(new ImageIcon(XmlSelection.class.getResource("/Icons/check.png")));
			btn.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						txtSelectionField.setText(chooser.getSelectedName());
						
						if (selectAction != null)
							selectAction.action();
						
						chooser.dispose();
					}
				});
			
			chooser.addBtn(btn);
		}
	}
	
	//Adds an action to be performed when an xml element is selected
	public void addSelectAction(Action a)
	{
		this.selectAction = a;
	}
	
	public void setXmlPath(String p)
	{
		this.xmlPath = p;
	}
	
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
	
	@Override
	public int getComponentHeight()
	{
		return 26;
	}
	
}
