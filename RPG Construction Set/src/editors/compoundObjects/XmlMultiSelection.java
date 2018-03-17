package editors.compoundObjects;

import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import editors.subPanels.XMLExplorerPanel;

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

import javax.swing.ImageIcon;

public class XmlMultiSelection extends CompoundComponent {
	private JList<String> list;
	private DefaultListModel<String> model;
	private String xmlPath;
	private String xmlName;
	private SpringLayout springLayout;
	private JScrollPane scrollPane;
	/**
	 * Create the panel.
	 */
	public XmlMultiSelection(String labelTxt, String nodeName) 
	{
		super (labelTxt, nodeName);
		springLayout = (SpringLayout) getLayout();
		
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
		JButton btnAdd = new JButton("");
		springLayout.putConstraint(SpringLayout.NORTH, btnAdd, 0, SpringLayout.NORTH, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, btnAdd, 30, SpringLayout.EAST, scrollPane);
		btnAdd.setIcon(new ImageIcon(XmlMultiSelection.class.getResource("/Icons/add.png")));
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
	 * @wbp.parser.constructor
	 */
	public XmlMultiSelection (String labelTxt, String nodeName, String xmlPath, String xmlName)
	{
		this (labelTxt, nodeName);
		this.xmlPath = xmlPath;
		this.xmlName = xmlName;
		
		
	}
	
	public void defaultOnClick()
	{
		if (this.xmlPath != null)
		{
			XMLExplorerPanel chooser = XMLExplorerPanel.newXmlChooser(this.xmlPath, this.xmlName, this);
			
			JButton btn = new JButton ("");
			btn.setIcon(new ImageIcon(XmlMultiSelection.class.getResource("/Icons/check.png")));
			btn.addActionListener(
				new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						List<String> list = chooser.getSelectedList();
						for (int i = 0; i < list.size(); i++)
						{
							model.addElement(list.get(i));
						}
						chooser.dispose();
					}
				});
			
			chooser.addBtn(btn);
		}
	}
	
	public void listKeyboardShortcut(KeyEvent e)
	{
		if (e.getKeyChar() == KeyEvent.VK_DELETE && list.getSelectedIndex() >= 0)
		{
			model.remove(list.getSelectedIndex());
		}
	}
	
	public ArrayList<String> getListItems()
	{
		ArrayList<String> listItems = new ArrayList<String>();
		
		for (int i = 0; i < model.size(); i++)
			listItems.add(model.get(i));
		
		return listItems;
	}
	
	@Override
	public int getComponentHeight()
	{
		return height;	
	}
	
	@Override
	public void setHeight(int h)
	{
		super.setHeight(h);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, h-5, SpringLayout.NORTH, lblLabel);
	}
}
