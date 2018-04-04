package editors;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSplitPane;

import editors.compoundObjects.Action;
import editors.subPanels.XMLExplorerPanel;
import util.ImageLoader;

import javax.swing.JScrollPane;

/*
 * This is a container class that is used to construct all of the Tabs in the main window.
 * The class contains a left and right side, tied together with an interface
 */

public class EditorTab extends JPanel {
	private JSplitPane splitPane;
	private JScrollPane scrollPane;
	protected XMLExplorerPanel explorer;
	protected XmlForm form;
	
	protected Action onSave;
	protected Action onDelete;
	
	//This constructor creates the panel empty
	public EditorTab() {
		setLayout(new BorderLayout(0, 0));
		
		splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
	}
	
	//Creates the panel and adds the XmlExplorer and Form
	public EditorTab(XMLExplorerPanel explorer, XmlForm form)
	{
		this();
		this.explorer = explorer;
		this.form = form;
		setLeft(explorer);
		setRight(form);
		
		
		//Edit Button
		JButton btnEdit = new JButton(ImageLoader.loadResourceIcon("/Icons/edit.png"));
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Loading " + explorer.getSelectedElement());
				form.load(explorer.getSelectedElement());
			}
		});

		//New Button
		JButton btnNew = new JButton(ImageLoader.loadResourceIcon("/Icons/newSingle.png"));
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				form.clearForm();
			}
		});
		
		if (explorer != null)
		{
			explorer.addBtn(btnEdit);
			explorer.addBtn(btnNew);
		}
		
		//Actions (Ties the Explorer and Form together)
		onSave = new Action() {
			@Override
			public void action() {
				explorer.loadXML(true);
			}
		};
		form.setOnSave(onSave);
		
		onDelete = new Action() {
			@Override
			public void action() {
				form.deleteCheck();
			}
		};
		if (explorer != null)
			explorer.setOnDelete(onDelete);
	}
	
	//Creates an XmlExplorer, and then adds it to the panel along with the form
	public EditorTab (String fn, String title, XmlForm form)
	{
		this(new XMLExplorerPanel(fn, title), form);
	}
	
	//Set the left side
	public void setLeft(Component c)
	{
		splitPane.setLeftComponent(c);
	}
	
	//Set the right side (within a scrollpane)
	public void setRight(Component c)
	{
		scrollPane.setViewportView(c);
	}

}
