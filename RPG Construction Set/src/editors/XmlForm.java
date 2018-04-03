package editors;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.jdom2.Element;

import editors.compoundObjects.Action;
import editors.compoundObjects.CompoundComponent;

import javax.swing.JLabel;

import java.awt.Dimension;
import java.awt.Font;

public class XmlForm extends JPanel {
	protected final int VERT_PADDING = 5;
	protected SpringLayout springLayout;
	protected JLabel lblTitle;
	protected JLabel lblSubTitle;
	protected CompoundComponent fields[];
	protected int h = 100;
	
	protected Element editElement = null;
	
	protected Action onSave;
	/**
	 * Create the panel.
	 */
	public XmlForm(String title) {
		springLayout = new SpringLayout();
		setLayout(springLayout);
		
		lblTitle = new JLabel(title);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 16));
		springLayout.putConstraint(SpringLayout.NORTH, lblTitle, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblTitle, 10, SpringLayout.WEST, this);
		add(lblTitle);
		
		lblSubTitle = new JLabel("New");
		lblSubTitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		springLayout.putConstraint(SpringLayout.NORTH, lblSubTitle, 10, SpringLayout.SOUTH, lblTitle);
		springLayout.putConstraint(SpringLayout.WEST, lblSubTitle, 10, SpringLayout.WEST, lblTitle);
		add(lblSubTitle);

	}
	
	public void buildForm()
	{
		//Place first component
		springLayout.putConstraint(SpringLayout.NORTH, fields[0], VERT_PADDING, SpringLayout.SOUTH, lblSubTitle);
		springLayout.putConstraint(SpringLayout.WEST, fields[0], 0, SpringLayout.WEST, lblSubTitle);
		springLayout.putConstraint(SpringLayout.EAST, fields[0], 300, SpringLayout.WEST, lblSubTitle);
		springLayout.putConstraint(SpringLayout.SOUTH, fields[0], VERT_PADDING + fields[0].getComponentHeight(), SpringLayout.SOUTH, lblSubTitle);
		add(fields[0]);
		
		for (int i = 1; i < fields.length; i++) //Start at our second element. The first one is placed manually
		{
			springLayout.putConstraint(SpringLayout.NORTH, fields[i], VERT_PADDING, SpringLayout.SOUTH, fields[i-1]);
			springLayout.putConstraint(SpringLayout.WEST, fields[i], 0, SpringLayout.WEST, fields[i-1]);
			springLayout.putConstraint(SpringLayout.EAST, fields[i], fields[i].getComponentWidth(), SpringLayout.WEST, fields[i-1]);
			springLayout.putConstraint(SpringLayout.SOUTH, fields[i], VERT_PADDING + fields[i].getComponentHeight(), SpringLayout.SOUTH, fields[i-1]);
			add(fields[i]);
			h += VERT_PADDING + fields[i].getComponentHeight();
		}
		
		h += 25; //Standard buffer for added buttons
		this.setPreferredSize(new Dimension(450, h));
	}
	
	//Clears all elements in the form, sets the loaded element to null, and changes the subTitle text
	public void clearForm()
	{
		editElement = null;
		for (int i=0; i<fields.length; i++)
		{
			fields[i].clear();
		}
		this.lblSubTitle.setText("New");
	}
	
	//Loads an element to be edited. Further processing needs to be handled by each form using the "postLoad" function
	public final void load(Element e)
	{
		if (e != null)
		{
			this.editElement = e;
			this.lblSubTitle.setText("Editing \"" + e.getName() + "\"");

			postLoad();
		}
	}
	
	//Actions to take after an element is loaded
	protected void postLoad()
	{
		
	}
	
	public final void setOnSave(Action a)
	{
		onSave = a;
	}
	
	public final void deleteCheck()
	{
		this.clearForm();
	}
}
