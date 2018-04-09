package editors.compoundObjects;

import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import editors.subPanels.XMLExplorerPanel;
import util.ImageLoader;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**A {@link CompoundComponent} consisting of a JLabel to the left, a JList in the middle, and a JButton to the right.<br/>
 * Clicking the JButton allows the user to select one or more elements from an xml file (specified in the constructor)
 * @author Will
 */
public class XmlMultiSelection extends CompoundComponent {
	private JList<String> list;
	private DefaultListModel<String> model;
	private String xmlPath;
	private String xmlName;
	private SpringLayout springLayout;
	private JScrollPane scrollPane;
	
	/**Creates a new XmlMultiSelection from two parameters
	 * @param labelTxt The text to be displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 */
	public XmlMultiSelection(String labelTxt, String nodeName) 
	{
		super (labelTxt, nodeName);
		SpringLayout springLayout = (SpringLayout) getLayout();
		
		scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, -3, SpringLayout.NORTH, lblLabel);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 6, SpringLayout.EAST, lblLabel);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 120, SpringLayout.EAST, lblLabel);
		//springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 80, SpringLayout.NORTH, lblLabel);
		this.setHeight(85);//Default height.
		
		add(scrollPane);
		
		list = new JList<String>();
		list.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent e) 
			{
				listKeyboardShortcut(e);
			}
		});
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		model = new DefaultListModel<String>();
		list.setModel(model);
		
		//Buttons
		JButton btnAdd = new JButton(ImageLoader.loadResourceIcon("/Icons/add.png"));
		springLayout.putConstraint(SpringLayout.NORTH, btnAdd, 0, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, btnAdd, 30, SpringLayout.EAST, scrollPane);
		btnAdd.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				defaultOnClick();
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, btnAdd, 6, SpringLayout.EAST, scrollPane);
		add(btnAdd);
		
		//height = 85;
	}
	
	/**
	 * @param labelTxt The text to be displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 * @param xmlPath The path to the xml file this Component can select elements from.
	 * @param xmlName The title displayed at the top of the selection window.
	 * @wbp.parser.constructor
	 */
	public XmlMultiSelection (String labelTxt, String nodeName, String xmlPath, String xmlName)
	{
		this (labelTxt, nodeName);
		this.xmlPath = xmlPath;
		this.xmlName = xmlName;
	}
	
	/**The default action for the JButton to the right of the JList.<br/>
	 * Creates a selection window that loads elements from the xml file set in the constructor. 
	 * If no xmlPath was set in the constructor, nothing happens.
	 */
	private void defaultOnClick()
	{
		if (this.xmlPath != null)
		{
			XMLExplorerPanel chooser = XMLExplorerPanel.newXmlChooser(this.xmlPath, this.xmlName, this);
			
			JButton btn = new JButton (ImageLoader.loadResourceIcon("/Icons/check.png"));
			btn.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						List<String> list = chooser.getSelectedList();
						for (int i = 0; i < list.size(); i++)
						{
							addValue(list.get(i));
						}
						chooser.dispose();
					}
				});
			
			chooser.addBtn(btn);
		}
	}
	
	/**Adds a String value to the JList
	 * @param value The List item to be added.
	 */
	public void addValue(String value)
	{
		model.addElement(value);
	}
	
	
	/**Processes key events from the JList.<br/>
	 * The 'DEL' key removes an item from the JList
	 * @param e The key event to be processed.
	 */
	private void listKeyboardShortcut(KeyEvent e)
	{
		if (e.getKeyChar() == KeyEvent.VK_DELETE && list.getSelectedIndex() >= 0)
		{
			model.remove(list.getSelectedIndex());
		}
	}
	
	/**Creates an ArrayList of all items contained in the JList.
	 * @return A list of values currently in the JList.
	 */
	public ArrayList<String> getValuesList()
	{
		ArrayList<String> listItems = new ArrayList<String>();
		
		for (int i = 0; i < model.size(); i++)
			listItems.add(model.get(i));
		
		return listItems;
	}
	
	@Override
	public void clear()
	{
		model.clear();
	}
	
	@Override
	public void setHeight(int h)
	{
		super.setHeight(h);
		if (springLayout == null)
		{
			springLayout = (SpringLayout) getLayout();
		}
		
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, h-5, SpringLayout.NORTH, lblLabel);
	}
}
