package editors.compoundObjects;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;

/**A {@link CompoundComponent} Consisting of a JLabel to the left, and a JScrollPane to the right.<br/>
 * This Component is a container for other CompoundComponents. It has its own xml node associated with it, 
 * and all Components within this Component have their own xml node associated with them. This allows for 
 * a nested "group" xml node to be created.
 * @author Will
 *
 */
public class XmlSubform extends CompoundComponent {
	public final int VERT_PADDING = 0;
	private SpringLayout innerLayout;
	private JPanel formPanel;
	private JScrollPane scrollPane;
	private ArrayList<CompoundComponent> fields;
	private ArrayList<String> values;
	
	/**Creates an XmlSubform with 3 parameters.
	 * @param label The text to be displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 * @param fields A list of components to be displayed in the subform
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
	
	/**Creates an XmlSubform with 4 parameters.
	 * @param label The text to be displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 * @param fields A list of components to be displayed in the subform
	 * @param values A list of default values for each field in the subform (parallel with the "fields" list).
	 */
	public XmlSubform(String label, String nodeName, ArrayList<CompoundComponent> fields, ArrayList<String> values) 
	{
		this(label, nodeName, fields);
		this.setValues(values);
	}
	
	/**Creates an empty XmlSubform with 2 parameters.
	 * @param label The text to be displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 */
	public XmlSubform(String label, String nodeName) 
	{
		this(label, nodeName, new ArrayList<CompoundComponent>());
	}
	
	/**
	 * Builds the subform from the components currently in {@link #fields}.
	 */
	public void buildForm()
	{
		
		formPanel = new JPanel();
		scrollPane.setViewportView(formPanel);
		innerLayout = new SpringLayout();
		formPanel.setLayout(innerLayout);
		
		if (!fields.isEmpty())
			{
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
	}
	
	/**Adds a CompoundComponent to the {@link #fields} list, and rebuilds the form. All fields are added from top to bottom.
	 * @param component The CompoundComponent to be added.
	 */
	public void addComponent(CompoundComponent component)
	{
		fields.add(component);
		buildForm();
	}
	
	/**
	 * Removes all  CompoundComponents from {@link #fields} and rebuilds the form (now empty).
	 */
	@Override
	public void removeAll()
	{
		fields.clear();
		buildForm();
	}
	
	//Get a list of all the current values
	/**Creates an ArrayList of values from the form Components in {@link #fields}.
	 * @return A list of String values from each form Component.
	 */
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
	/**Creates an ArrayList of values from the form Component labels in {@link #fields}.
	 * @return A list of String values from each form Component's label.
	 */
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
	/**Creates an ArrayList of values from the form Component xml nodes in {@link #fields}.
	 * @return A list of String values from each form Component's node name.
	 */
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
	/**Sets each form component's value to its corresponding value in the passed in ArrayList. 
	 * @param values The list of values to set the form components to.
	 */
	public void setValues(ArrayList<String> values)
	{
		this.values = values;
		
		for (int i = 0; i < values.size(); i++)
		{
			if (!values.get(i).isEmpty())
				fields.get(i).setValue(values.get(i));
		}
	}
	
	//Set value by nodeName
	/**Sets the value of the first inner component found with the passed in nodeName, to the passed in value.
	 * @param nodeName The nodeName of the component.
	 * @param value The value to set that component to.
	 */
	public void setValueAtNode (String nodeName, String value)
	{
		for (int i = 0; i < fields.size(); i++)
		{
			if (fields.get(i).getNodeName().equals(nodeName))
			{
				fields.get(i).setValue(value);
				break;
			}
		}
	}
	
	//Set value by Label text
	/**Sets the value of the first inner component found with the passed in label text, to the passed in value.
	 * @param labelTxt The label text of the component.
	 * @param value The value to set that component to.
	 */
	public void setValueAtLabel (String labelTxt, String value)
	{
		for (int i = 0; i < fields.size(); i++)
		{
			if (fields.get(i).getLabelText().equals(labelTxt))
			{
				fields.get(i).setValue(value);
				break;
			}
		}
	}
	
	/**Get the number of elements added to the form.
	 * @return the length of the {@link #fields} array list.
	 */
	public int length()
	{
		return this.fields.size();
	}
	
	@Override
	public void clear()
	{
		if (values == null)
			for (int i = 0; i < fields.size(); i++)
			{
				fields.get(i).clear();
			}
		else
		{
			setValues(values);
		}
			
	}
}
