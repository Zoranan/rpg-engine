package editors.compoundObjects;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;

public class XmlSubform extends CompoundComponent {
	public final int VERT_PADDING = 0;
	private SpringLayout innerLayout;
	private JPanel formPanel;
	private JScrollPane scrollPane;
	private ArrayList<CompoundComponent> fields;
	/**
	 * Create the panel.
	 */
	public XmlSubform(String label, String nodeName, ArrayList<CompoundComponent> fields) 
	{
		super (label, nodeName);
		SpringLayout springLayout = (SpringLayout) getLayout();
		this.fields = fields;
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, lblLabel);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 6, SpringLayout.EAST, lblLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 120, SpringLayout.NORTH, lblLabel);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 260, SpringLayout.EAST, lblLabel);
		add(scrollPane);
		
		this.setHeight(130);
		buildForm();
	}
	
	public XmlSubform(String label, String nodeName, ArrayList<CompoundComponent> fields, ArrayList<String> values) 
	{
		this(label, nodeName, fields);
		this.setValues(values);
	}
	
	public void buildForm()
	{
		formPanel = new JPanel();
		scrollPane.setViewportView(formPanel);
		innerLayout = new SpringLayout();
		formPanel.setLayout(innerLayout);
		
		CompoundComponent field = fields.get(0);
		innerLayout.putConstraint(SpringLayout.NORTH, field, 0, SpringLayout.NORTH, formPanel);
		innerLayout.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.WEST, formPanel);
		innerLayout.putConstraint(SpringLayout.SOUTH, field, field.getComponentHeight(), SpringLayout.NORTH, field);
		innerLayout.putConstraint(SpringLayout.EAST, field, field.getComponentWidth(), SpringLayout.WEST, field);
		formPanel.add(field);
		
		int h = (field.getComponentHeight() + 10);
		for (int i = 1; i < fields.size(); i++)
		{
			field = fields.get(i);
			innerLayout.putConstraint(SpringLayout.NORTH, field, VERT_PADDING, SpringLayout.SOUTH, fields.get(i-1));
			innerLayout.putConstraint(SpringLayout.WEST, field, 0, SpringLayout.WEST, formPanel);
			innerLayout.putConstraint(SpringLayout.SOUTH, field, field.getComponentHeight() + VERT_PADDING, SpringLayout.SOUTH, fields.get(i-1));
			innerLayout.putConstraint(SpringLayout.EAST, field, field.getComponentWidth(), SpringLayout.WEST, formPanel);
			formPanel.add(field);
			
			h += (field.getComponentHeight() + VERT_PADDING);
		}
		
		formPanel.setPreferredSize(new Dimension(240, h));
	}
	
	//Get a list of all the current values
	public ArrayList<String> getValues()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for (int i = 0; i < fields.size(); i ++)
		{
			list.add(fields.get(i).getValue());
		}
		
		return list;
	}
	
	//Get a list of all label texts
	public ArrayList<String> getLabelTexts()
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for (int i = 0; i < fields.size(); i ++)
		{
			list.add(fields.get(i).getLabelText());
		}
		
		return list;
	}
	
	//Get a list of all node names
	public ArrayList<String> getNodeNames()
	{
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 0; i < fields.size(); i ++)
		{
			list.add(fields.get(i).getNodeName());
		}

		return list;
	}
	
	//Set the values of each element in the form
	public void setValues(ArrayList<String> values)
	{
		for (int i = 0; i < values.size(); i++)
		{
			if (!values.get(i).isEmpty())
				fields.get(i).setValue(values.get(i));
		}
	}
}
