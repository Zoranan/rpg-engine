package editors.compoundObjects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.SpringLayout;

public class CheckBoxSelection extends CompoundComponent {
	private JCheckBox chkBox;
	private String checkedTxt = "", uncheckedTxt = "";
	private Action clickAction;
	/**
	 * Create the panel.
	 * @wbp.parser.constructor
	 */
	//CONSTRUCTOR: 5 args
	public CheckBoxSelection(String labelTxt, String nodeName, String checkedTxt, String uncheckedTxt, boolean initialState) {
		super (labelTxt, nodeName);
		SpringLayout springLayout = (SpringLayout) getLayout();
		this.checkedTxt = checkedTxt;
		this.uncheckedTxt = uncheckedTxt;
		
		chkBox = new JCheckBox();
		springLayout.putConstraint(SpringLayout.NORTH, chkBox, -3, SpringLayout.NORTH, lblLabel);
		springLayout.putConstraint(SpringLayout.WEST, chkBox, 6, SpringLayout.EAST, lblLabel);
		springLayout.putConstraint(SpringLayout.EAST, chkBox, 120, SpringLayout.EAST, lblLabel);
		
		chkBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				updateText();
				
				if (clickAction != null)
					clickAction.action();
			}
		});
		
		setChecked(initialState);
		add(chkBox);
	}
	//CONSTRUCTOR: 3 args
	public CheckBoxSelection(String labelTxt, String nodeName, boolean initialState) {
		this (labelTxt, nodeName, "", "", initialState);
	}
	
	//FUNCTION: Update Check Box Text
	private void updateText()
	{
		if (chkBox.isSelected())
			chkBox.setText(checkedTxt);
		else
			chkBox.setText(uncheckedTxt);
	}
	
	//FUNCTION: Set actions to complete when checkbox is clicked
	public void setClickAction(Action a)
	{
		this.clickAction = a;
	}
	
	//Disable / Enable
	@Override
	public void setEnabled(boolean b)
	{
		chkBox.setEnabled(b);
	}
	
	
	//Getting and Setting the chkBox State
	public void setChecked(boolean b)
	{
		chkBox.setSelected(b);
		updateText();
	}
	
	@Override
	public String getValue()
	{
		return Boolean.toString(chkBox.isSelected());
	}
	
	public boolean getBoolValue()
	{
		return chkBox.isSelected();
	}

}
