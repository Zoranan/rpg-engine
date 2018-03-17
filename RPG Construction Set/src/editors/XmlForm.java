package editors;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import editors.compoundObjects.CompoundComponent;

import javax.swing.JLabel;
import java.awt.Font;

public class XmlForm extends JPanel {
	protected final int VERT_PADDING = 5;
	protected SpringLayout springLayout;
	protected JLabel lblLabel;
	protected CompoundComponent fields[];
	protected int h = 80;
	/**
	 * Create the panel.
	 */
	public XmlForm(String title) {
		springLayout = new SpringLayout();
		setLayout(springLayout);
		
		lblLabel = new JLabel(title);
		lblLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		springLayout.putConstraint(SpringLayout.NORTH, lblLabel, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblLabel, 10, SpringLayout.WEST, this);
		add(lblLabel);

	}
	
	public void buildForm()
	{
		//Place first component
		springLayout.putConstraint(SpringLayout.NORTH, fields[0], VERT_PADDING, SpringLayout.SOUTH, lblLabel);
		springLayout.putConstraint(SpringLayout.WEST, fields[0], 0, SpringLayout.WEST, lblLabel);
		springLayout.putConstraint(SpringLayout.EAST, fields[0], 300, SpringLayout.WEST, lblLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, fields[0], VERT_PADDING + fields[0].getComponentHeight(), SpringLayout.SOUTH, lblLabel);
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
	}
}
