package editors.subPanels;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.jdom2.Document;
import org.jdom2.Element;

import util.Handler;
import util.TextValidator;
import util.XmlLoader;
import window.Display;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;

public class XMLExplorerPanel extends JPanel {

	/**
	 * This class handles the loading and viewing of all XML lists.
	 * Custom buttons can be added depending on the panel's application.
	 */
	private JToolBar toolBar;
	private JList<String> list;
	private DefaultListModel<String> listModel;
	private JPopupMenu rcMenu;
	private Display frame;
	
	private String type = "";
	private String fileName;
	private Document doc;
	private static HashMap<String, String> filters = new HashMap<String, String>();
	
	public XMLExplorerPanel(String fn, String title) {
		setLayout(new BorderLayout(0, 0));
		this.fileName = fn;
		
		type = title;
		JLabel lblTitle = new JLabel(title);
		add(lblTitle, BorderLayout.NORTH);
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		list = new JList<String>();
		listModel = new DefaultListModel<String>();
		list.setModel(listModel);
		list.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyReleased(KeyEvent e) 
			{
				listShortcutKeyPressed(e);
			}
		});
		
		list.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				mouseActions(e);
			}	
		});
		scrollPane.setViewportView(list);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.SOUTH);
		
		JButton btnReload = new JButton("");
		btnReload.setIcon(new ImageIcon(XMLExplorerPanel.class.getResource("/Icons/refresh2.png")));
		btnReload.setToolTipText("Reload");
		btnReload.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				loadXML(true);
			}
		});
		toolBar.add(btnReload);
		
		JButton btnDel = new JButton("");
		btnDel.setIcon(new ImageIcon(XMLExplorerPanel.class.getResource("/Icons/delete.png")));
		btnDel.setToolTipText("Delete");
		btnDel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				deleteSelected();
			}
		});
		toolBar.add(btnDel);
		
		JButton btnSearch = new JButton("");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				searchFilter();
			}
		});
		btnSearch.setIcon(new ImageIcon(XMLExplorerPanel.class.getResource("/Icons/search.png")));
		toolBar.add(btnSearch);
		
		//Create our right click menu
		rcMenu = new JPopupMenu();
		JMenuItem reload = new JMenuItem("Reload");
		reload.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				loadXML(true);
			}
		});
		
		JMenuItem delete = new JMenuItem("Delete");
		delete.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				deleteSelected();
			}
		});
		
		JMenuItem search = new JMenuItem("Search");
		search.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				searchFilter();
			}
		});
		
		JMenuItem clearSearch = new JMenuItem("Clear Search");
		clearSearch.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				filters.put(doc.getBaseURI(), "");
				searchFilter("");
			}
		});
		
		JMenuItem editTags = new JMenuItem("Edit Tags");
		editTags.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				openTagEditor();
			}
		});
		rcMenu.add(reload);
		rcMenu.add(delete);
		rcMenu.add(search);
		rcMenu.add(clearSearch);
		rcMenu.add(editTags);
		
		//Load!
		this.loadXML(false);

	}//END CONSTRUCTOR
	
	//FUNCTION: handles mouse actions
	private void mouseActions(MouseEvent e)
	{
		if (e.getButton() == MouseEvent.BUTTON3)
			rightClick(e);
	}
	
	//FUNCTION: Creates our right click menu
	private void rightClick(MouseEvent e)
	{
		int row = list.locationToIndex(e.getPoint());
		
		int[] rows = list.getSelectedIndices();
		boolean keepSelected = false;
		for (int i = 0; i < rows.length; i++)
		{
			keepSelected |= (row == rows[i]);
		}
		
		if (!keepSelected)
			list.setSelectedIndex(row);
		
		rcMenu.show(list, e.getX(), e.getY());
	}
	
	//FUNCTION: handles key shortcuts
	private void listShortcutKeyPressed(KeyEvent e)
	{
		if (e.getKeyChar() == KeyEvent.VK_DELETE)
			deleteSelected();
		
		else if (e.getKeyCode() == KeyEvent.VK_F5)
			loadXML(true);
		
		else if (e.getKeyCode() == KeyEvent.VK_F2)
			renameSelected();
		
		else if (e.getKeyCode() == KeyEvent.VK_F && (e.getModifiers() == KeyEvent.CTRL_MASK))
			searchFilter();
		
		//System.out.println(e.getKeyCode() + " : " + e.getModifiers());
	}
	
	//FUNCTION: Allows buttons to be added to the toolbar
	public void addBtn(JButton btn)
	{
		toolBar.add(btn);
	}
	
	//FUNCTION: Removes a button from the toolbar
	public void removeBtn(JButton btn)
	{
		toolBar.remove(btn);
	}
	
	//FUNCTION: Load the xml list (all children within the root node)
	public void loadXML() {loadXML(false);}
	public void loadXML(boolean reload)
	{
		//Clear the existing list
		listModel.clear();
		
		//Load the doc
		doc = XmlLoader.readXML(Handler.getRootDirectory() + "/" + this.fileName, reload);
		
		//Apply a search filter if one exists for the document
		if (filters.get(doc.getBaseURI()) == null)
			searchFilter("");
		else
			searchFilter(filters.get(doc.getBaseURI()));
		System.out.println(filters.get(doc.getBaseURI()));
		
		list.validate();
		list.repaint();
	}
	
	//FUNCTION: Delete an entity
	public void deleteSelected()
	{
		//String cname = list.getSelectedValue();
		List<String> selectedList = this.getSelectedList();
		//System.out.println(this.getSelectedList());
		if (!selectedList.isEmpty())
			{
			String options[] = {"Delete forever", "Cancel"};
			String message = "Are you sure you want to delete " + selectedList.get(0);
			
			if (selectedList.size() > 1)
				message += " and " + (selectedList.size()-1) + " other " + this.type;
			
			message += "?\nThis can't be undone.";
			
			int yn = JOptionPane.showOptionDialog(this, 
					message, "Delete?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			
			if (yn == 0)
			{	
				for (int i = 0; i < selectedList.size(); i++)
				{
					//Delete and Overwrite
					doc.getRootElement().removeChild(selectedList.get(i));
					
					//Remove this entity from the list displayed
					//This does so without reloading...
					listModel.removeElement(selectedList.get(i));
				}
				XmlLoader.writeXML(doc);
				list.validate();
				list.repaint();
			}
		}
	}
	
	//FUNCTION: search / filter
	public void searchFilter()
	{
		String search = JOptionPane.showInputDialog(this, "Search:", "Search", JOptionPane.PLAIN_MESSAGE);
		searchFilter(search);
		
		if (search != null)
			filters.put(doc.getBaseURI(), search);
	}
	
	public void searchFilter(String search)
	{	
		if (search != null)
		{
			//Clear our list first
			listModel.clear();
			
			search = TextValidator.eatDoubleChars(search, ' '); //Remove all but one space between words
			search = search.toLowerCase();
			ArrayList<String> searchTerms = new ArrayList<String>(Arrays.asList(search.split(" "))); //Separate terms by space
			System.out.println(search);
			
			//look for element tags
			List<Element> eleList = doc.getRootElement().getChildren();
			for (Element e : eleList)
			{
				boolean match = false;
				for (String term : searchTerms)
				{
					char mode = '|';
					boolean found = false;
					
					if (!term.isEmpty())
					{
						mode = term.charAt(0);
						if (mode == '+' || mode == '-')//If the term has a mode
							term = term.substring(1);
						
						else
							mode = '|';
					}
					
					System.out.println(term);
					
					//If we enter a blank search, pull all elements 
					if (search.isEmpty() || search == "")
					{
						match = true;
					}
					//If we entered a search, look through the item's name for our search query
					else if (e.getName().toLowerCase().contains(term))
					{
						found = true;
					}
					
					//If we entered a search, but the item's name did not contain our query,
					//Check the element's tags
					else if (e.getChild("tags") != null && (e.getChild("tags").getChildren().isEmpty() == false))
					{
						List<Element> tagList = e.getChild("tags").getChildren();	//Get the tags
						for (Element t : tagList)
						{
							if (t.getValue().toLowerCase().contains(term))		//if any tag matches our search
								found = true;										//Signal that it is a match
						}
					}
					
					//Adjust for the mode
					if (mode == '|')
						match |= found;
					else if (mode == '+')
						match &= found;
					else // only mode left is NOT mode (!)
						match &= !found;
				}
				//if we have found that the Element is a match, add it
				if (match)
				{
					listModel.addElement(e.getName());
				}
			}
		}
	}
	
	//private boolean searchLogic (boolean match, boolean)
	
	//FUNCTION: Rename selected
	public void renameSelected()
	{
		String oldNameID = list.getSelectedValue();		
		String newNameID = JOptionPane.showInputDialog(this, "Rename " + oldNameID + " to...", "Rename", JOptionPane.PLAIN_MESSAGE);
		
		//If the name is not taken...
		if (doc.getRootElement().getChild(newNameID) == null)
		{
			Element oldEle = doc.getRootElement().getChild(oldNameID);
			doc.getRootElement().removeChild(oldNameID);
			
			Element newEle = new Element(newNameID);
			List<Element> children = oldEle.getChildren();
			for (int i = 0; i < children.size(); i++)
				newEle.addContent(children.get(i).clone());
			
			doc.getRootElement().addContent(newEle);
			
			XmlLoader.writeXML(doc);
			this.loadXML(true);
		}
		
		else if (newNameID != null)
		{
			JOptionPane.showMessageDialog(this, "That name is already taken.", "Invalid Name", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	//FUNCTION: Open tag editor
	public void openTagEditor()
	{
		TagEditor editor = TagEditor.newTagEditorWindow(getSelectedElement());
	}
	
	//GETTERS
	public Document getDoc()
	{
		return doc;
	}
	
	//Gets the JList so other panels can add listeners to it
	public JList getList()
	{
		return this.list;
	}
	
	//Gets the selected object name (String)
	public String getSelectedName()
	{
		return list.getSelectedValue();
	}
	
	//Gets the selected object name (String)
	public List<String> getSelectedList()
	{
		return list.getSelectedValuesList();
	}
	
	//Gets the selected object (node / element)
	public Element getSelectedElement()
	{
		return doc.getRootElement().getChild(list.getSelectedValue());
	}
	//END GETTERS
	
	public void dispose()
	{
		if (frame != null)
			frame.dispose();
	}
	
	//FUNCTION: Create an XmlExplorer Window!
	public static XMLExplorerPanel newXmlChooser(String xmlPath, String title, Component parent)
	{	
		//Create the sprite chooser window
		Display display = new Display (title, 200, 480);					//The frame
		XMLExplorerPanel chooser = new XMLExplorerPanel(xmlPath, title);	//The xml explorer pane
		
		display.getFrame().getContentPane().add(chooser);
		chooser.frame = display;
		
		display.getFrame().setLocation(parent.getLocationOnScreen());
		
		return chooser;
	}

}
