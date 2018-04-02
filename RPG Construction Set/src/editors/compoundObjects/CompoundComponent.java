package editors.compoundObjects;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class CompoundComponent  extends JPanel{
	protected String nodeName;
	protected JLabel lblLabel;
	protected SpringLayout springLayout;
	protected int height = 22;

	public CompoundComponent(String labelTxt, String nodeName)
	{
		this.nodeName = nodeName; 
		
		springLayout = new SpringLayout();
		setLayout(springLayout);
		
		lblLabel = new JLabel(labelTxt + ":");
		springLayout.putConstraint(SpringLayout.NORTH, lblLabel, 5, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, lblLabel, 5, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, lblLabel, 90, SpringLayout.WEST, this);
		lblLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(lblLabel);
		
	}
	
	@Override
	public void setEnabled(boolean b)
	{
		
	}
	
	public void setHeight(int h)
	{
		this.height = h;
	}
	
	public void setValue(String s)
	{
		
	}
	
	public void clear()
	{
		setValue("");
	}
	
	public String getNodeName()
	{
		return this.nodeName;
	}
	
	public String getValue()
	{
		return "";
	}
	
	public int getComponentWidth()
	{
		return 300;
	}
	
	public int getComponentHeight()
	{
		return height;
	}

}
