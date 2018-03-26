package wizards.sprites;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.jdom2.Element;
import org.jdom2.Text;

import util.TextValidator;

import javax.swing.JTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class NewSpritePage03 extends NewSpritePage {
	private JTextField txtName;
	private JTextField txtNameid;
	private JTextField txtNewTagBox;
	private JList<String> tagList;
	private DefaultListModel<String> tagListModel;

	/**
	 * Create the panel.
	 *  
	 */
	public NewSpritePage03() {
		setLayout(null);
		
		JLabel lblName = new JLabel("NameID:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(10, 11, 80, 14);
		add(lblName);
		
		JLabel lblName_1 = new JLabel("Name:");
		lblName_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName_1.setBounds(10, 36, 80, 14);
		add(lblName_1);
		
		txtName = new JTextField();
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				if (TextValidator.isNotAlphaNumeric(e.getKeyChar()) && e.getKeyChar() != ' ')
					e.consume();
			}
		});
		txtName.addFocusListener(new FocusAdapter() 
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				txtBoxUpdate();
			}
		});
		txtName.setText("");
		txtName.setBounds(100, 33, 86, 20);
		add(txtName);
		txtName.setColumns(10);
		
		txtNameid = new JTextField();
		txtNameid.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				if (TextValidator.isNotAlphaNumeric(e.getKeyChar()) && e.getKeyChar() != '_')
					e.consume();
			}
		});
		txtNameid.addFocusListener(new FocusAdapter() 
		{
			@Override
			public void focusLost(FocusEvent e) 
			{
				txtBoxUpdate();
			}
		});
		txtNameid.setBounds(100, 8, 86, 20);
		add(txtNameid);
		txtNameid.setColumns(10);
		
		JLabel lblTags = new JLabel("New Tag:");
		lblTags.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTags.setBounds(10, 61, 80, 14);
		add(lblTags);
		
		txtNewTagBox = new JTextField();
		txtNewTagBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				if (TextValidator.isNotAlpha(e.getKeyChar()))
					e.consume();
			}
		});
		txtNewTagBox.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				addTagToList();
			}
		});
		txtNewTagBox.setBounds(100, 58, 86, 20);
		add(txtNewTagBox);
		txtNewTagBox.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				addTagToList();
			}
		});
		btnAdd.setBounds(196, 57, 60, 23);
		add(btnAdd);
		
		tagListModel = new DefaultListModel<String>();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(100, 89, 156, 91);
		add(scrollPane);
		tagList = new JList<String>(tagListModel);
		scrollPane.setViewportView(tagList);
		
		JButton btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				deleteTag();
			}
		});
		btnDeleteSelected.setBounds(100, 191, 156, 23);
		add(btnDeleteSelected);
		
		JLabel lblAllTags = new JLabel("All Tags:");
		lblAllTags.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAllTags.setBounds(10, 86, 80, 14);
		add(lblAllTags);
	}
	
	//Add a tag to the list
	public void addTagToList()
	{
		if (txtNewTagBox.getText().length() != 0)
		{
			tagListModel.addElement(txtNewTagBox.getText());
			clearNewTag();
		}
	}
	
	//Delete a tag from the list
	public void deleteTag()
	{
		int i = tagList.getSelectedIndex();
		
		if (i >= 0)
		{
			tagListModel.remove(i);
		}
	}
	
	//Runs when our text boxes are updated
	public void txtBoxUpdate()
	{
		if (this.getParent() instanceof CreateNewSpritePanel)
			if (txtNameid.getText().length() > 0 && txtName.getText().length() > 0)
			{
				((CreateNewSpritePanel) this.getParent()).setNextEnabled(true);
				this.formCompleted = true;
			}
			else
			{
				((CreateNewSpritePanel) this.getParent()).setNextEnabled(false);
				this.formCompleted = false;
			}
	}
	
	public void clearId()
	{
		txtNameid.setText("");
	}
	
	public void clearNewTag()
	{
		txtNewTagBox.setText("");
	}
	
	public void showError(String err)
	{
		JOptionPane.showMessageDialog(this, err, "Input Error", JOptionPane.ERROR_MESSAGE);
	}
	
	//Getting the element data
	@Override
	public Element getSpriteData()
	{
		Element nameID = new Element(txtNameid.getText());	//Root
		Element name = new Element("name");					//Holds our name
		Element tags = new Element("tags");					//Holds our tags
		
		name.addContent(new Text(txtName.getText()));
		
		//find and make our tags
		for (int i = 0; i < tagListModel.size(); i++)
		{
			Element tag = new Element("tag");
			tag.addContent(new Text(tagListModel.get(i)));
			tags.addContent(tag);
		}
		
		nameID.addContent(name);
		nameID.addContent(tags);
		
		return nameID;
	}
}
