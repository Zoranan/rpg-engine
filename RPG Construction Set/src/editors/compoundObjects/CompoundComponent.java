package editors.compoundObjects;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

/**A commonly used, pre-defined form element consisting of multiple swing components
 * @author Will
 *
 */
public abstract class CompoundComponent  extends JPanel{
	protected String nodeName;
	protected String labelText;
	protected JLabel lblLabel;
	protected SpringLayout springLayout;
	protected int height = 22;

	/**Creates a new {@link CompoundComponent}.
	 * @param labelTxt The text to be displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 */
	public CompoundComponent(String labelTxt, String nodeName)
	{
		this.nodeName = nodeName; 
		this.labelText = labelTxt;
		
		springLayout = new SpringLayout();
		setLayout(springLayout);
		
		lblLabel = new JLabel(labelTxt + ":");
		springLayout.putConstraint(SpringLayout.NORTH, lblLabel, 5, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblLabel, 5, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblLabel, 90, SpringLayout.WEST, this);
		lblLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblLabel);
		
	}
	
	/**Enables, or disables the editable components of this CompoundComponent.
	 * @param b If true, the editable components are enabled. If false, they are disabled.
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean b)
	{
		
	}
	
	/**Sets the height of this field
	 * @param height
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	/**Sets the value entered into this field
	 * @param value The desired value to be entered into the field
	 */
	public void setValue(String value)
	{
		
	}
	
	/**Reset this Component to its default value
	 * 
	 */
	public void clear()
	{
		setValue("");
	}
	
	public String getNodeName()
	{
		return this.nodeName;
	}
	
	public String getLabelText()
	{
		return this.labelText;
	}
	
	/**Gets the value entered into this Component's field as a String
	 * @return The value entered into this Component
	 */
	public String getValue()
	{
		return "";
	}
	
	public int getComponentWidth()
	{
		return 450;
	}
	
	public int getComponentHeight()
	{
		return height;
	}

}
