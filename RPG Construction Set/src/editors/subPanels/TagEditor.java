package editors.subPanels;

import javax.swing.JPanel;

import org.jdom2.Element;

import util.TextValidator;
import util.XmlLoader;
import window.Display;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class TagEditor extends JPanel {
	private Display frame;
	private Element edit = null;
	private JLabel lblElementName;
	private JTextField txtTagField;
	private JList<String> list;
	private DefaultListModel<String> listModel;
	/**
	 * Create the panel.
	 */
	//Create a blank tag editor
	public TagEditor() 
	{
		edit = new Element("tags");
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		lblElementName = new JLabel("Tags");
		lblElementName.setHorizontalAlignment(SwingConstants.RIGHT);
		springLayout.putConstraint(SpringLayout.NORTH, lblElementName, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblElementName, 10, SpringLayout.WEST, this);
		add(lblElementName);
		
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, lblElementName);
		add(scrollPane);
		
		list = new JList<String>();
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) 
			{
				keyboardShortcut(e);
			}
		});
		scrollPane.setViewportView(list);
		
		listModel = new DefaultListModel<String>();
		list.setModel(listModel);
		
		txtTagField = new JTextField();
		springLayout.putConstraint(SpringLayout.EAST, txtTagField, 130, SpringLayout.WEST, lblElementName);
		txtTagField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				if (TextValidator.isNotAlphaNumeric(e.getKeyChar()))
					e.consume();
			}
		});
		txtTagField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				addTag();
			}
		});
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, txtTagField);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 120, SpringLayout.NORTH, txtTagField);
		springLayout.putConstraint(SpringLayout.NORTH, txtTagField, 6, SpringLayout.SOUTH, lblElementName);
		springLayout.putConstraint(SpringLayout.WEST, txtTagField, 0, SpringLayout.WEST, lblElementName);
		add(txtTagField);
		txtTagField.setColumns(10);
		
		JButton btnAdd = new JButton("");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				addTag();
			}
		});
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, btnAdd);
		springLayout.putConstraint(SpringLayout.NORTH, btnAdd, -1, SpringLayout.NORTH, txtTagField);
		springLayout.putConstraint(SpringLayout.SOUTH, btnAdd, 0, SpringLayout.SOUTH, txtTagField);
		springLayout.putConstraint(SpringLayout.EAST, btnAdd, 34, SpringLayout.EAST, txtTagField);
		btnAdd.setIcon(new ImageIcon(TagEditor.class.getResource("/Icons/enter.png")));
		springLayout.putConstraint(SpringLayout.WEST, btnAdd, 6, SpringLayout.EAST, txtTagField);
		add(btnAdd);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				deleteTag();
			}
		});
		btnDelete.setToolTipText("Delete the selected tag (del)");
		btnDelete.setIcon(new ImageIcon(TagEditor.class.getResource("/Icons/delete.png")));
		springLayout.putConstraint(SpringLayout.NORTH, btnDelete, 6, SpringLayout.SOUTH, scrollPane);
		springLayout.putConstraint(SpringLayout.WEST, btnDelete, 0, SpringLayout.WEST, scrollPane);
		springLayout.putConstraint(SpringLayout.EAST, btnDelete, 0, SpringLayout.EAST, scrollPane);
		add(btnDelete);
		
	}
	
	//Create a Tag editor for a specific xml element
	public TagEditor(Element e)
	{
		this();	// Call our main constructor
		
		if (e.getChild("tags") == null)
			e.addContent(edit);
		else
			edit = e.getChild("tags");
		
		updateListModel();
		lblElementName.setText(e.getName());
	}
	
	//FUNCTION: Add tag
	public void addTag()
	{
		String newTag = this.txtTagField.getText();
		this.txtTagField.setText("");
		
		boolean exists = false;
		for (int i=0; i < listModel.size(); i++)
		{
			if (!exists)
			{
				exists = (listModel.get(i).toLowerCase().equals(newTag.toLowerCase()));
			}
		}
		
		if (!exists)
		{
			listModel.addElement(newTag);
			edit.addContent(new Element("tag").addContent(newTag));
		}
	}
	
	//FUNCTION: Delete tag
	public void deleteTag()
	{
		if (list.isSelectionEmpty() == false)
		{	
			if (edit.getContentSize() > 0)
			{
				List<Element> tags = edit.getChildren();
				Element remove = null;
				for (Element e : tags)
				{
					if (e.getValue().equals(list.getSelectedValue()))
					{
						remove = e;
					}
				}
				if (remove != null)
					edit.removeContent(remove);
			}
			
			listModel.remove(list.getSelectedIndex());
		}
		
		updateListModel();
	}
	
	//FUNCTION: Process keyboard shortcuts on the list
	public void keyboardShortcut(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_DELETE)
			deleteTag();
	}
	
	//FUNCTION: update listModel
	public void updateListModel()
	{
		if (edit.getContentSize() > 0)
		{
			listModel.clear();
			
			for (Element e : edit.getChildren())
			{
				listModel.addElement(e.getValue());
			}
		}
	}
	
	public static TagEditor newTagEditorWindow (Element e)
	{
		//Create the sprite chooser window
		Display display = new Display ("Tag Editor", 200, 245);
		TagEditor editor = new TagEditor(e);
				
		display.getFrame().getContentPane().add(editor);
		editor.frame = display;
		
		
		display.getFrame().addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e) 
			{
				XmlLoader.writeXML(editor.edit.getDocument());
			}	
		});
		return editor;
	}
	
	public Display getDisplay()
	{
		return frame;
	}
}
