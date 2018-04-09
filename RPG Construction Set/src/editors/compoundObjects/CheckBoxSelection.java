package editors.compoundObjects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.SpringLayout;

/**A {@link CompoundComponent} consisting of a Jlabel to the left, and a JCheckBox to the right.
 * @author Will
 *
 */
public class CheckBoxSelection extends CompoundComponent {
	private JCheckBox chkBox;
	private String checkedTxt = "", uncheckedTxt = "";
	private Action clickAction;
	private boolean initialState = false;
	/**Creates a new CheckBoxSelection from 5 arguments.
	 * @param labelTxt The label displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 * @param checkedTxt The text to be displayed to the left of the check box when it is checked (true state)
	 * @param uncheckedTxt The text to be displayed to the left of the check box when it is unchecked (false state)
	 * @param initialState The starting state for the check box. If true, check box is selected. If false, check box is unselected.
	 * @wbp.parser.constructor
	 */
	//CONSTRUCTOR: 5 args
	public CheckBoxSelection(String labelTxt, String nodeName, String checkedTxt, String uncheckedTxt, boolean initialState) {
		super (labelTxt, nodeName);
		SpringLayout springLayout = (SpringLayout) getLayout();
		this.checkedTxt = checkedTxt;
		this.uncheckedTxt = uncheckedTxt;
		this.initialState = initialState;
		
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
	/**Creates a new CheckBoxSelection from 3 arguments.
	 * @param labelTxt The label displayed to the left of this component on the form.
	 * @param nodeName The name of the xml node that this component is associated with.
	 * @param initialState The starting state for the check box. If true, check box is selected. If false, check box is unselected.
	 */
	//CONSTRUCTOR: 3 args
	public CheckBoxSelection(String labelTxt, String nodeName, boolean initialState) {
		this (labelTxt, nodeName, "", "", initialState);
	}
	
	/**
	 * Updates the text displayed to the right of the check box, based on its current state.
	 */
	//FUNCTION: Update Check Box Text
	private void updateText()
	{
		if (chkBox.isSelected())
			chkBox.setText(checkedTxt);
		else
			chkBox.setText(uncheckedTxt);
	}
	
	//FUNCTION: Set actions to complete when checkbox is clicked
	/**Set an Action Interface to be triggered when the checkbox is clicked.
	 * @param action The action interface to be trigger.
	 */
	public void setClickAction(Action action)
	{
		this.clickAction = action;
	}
	
	//Disable / Enable
	@Override
	public void setEnabled(boolean b)
	{
		chkBox.setEnabled(b);
	}
	
	
	//Setting the chkBox State
	/**Sets the check box state, to the value of the passed in boolean.
	 * @param b If true, check box is clicked. Otherwise, checkbox is unchecked.
	 */
	public void setChecked(boolean b)
	{
		chkBox.setSelected(b);
		updateText();
	}
	
	@Override
	public void clear()
	{
		setChecked(initialState);
	}
	
	@Override
	public String getValue()
	{
		return Boolean.toString(chkBox.isSelected());
	}
	
	@Override
	public void setValue(String s)
	{
		try {
			setChecked(Boolean.parseBoolean(s));
		}
		catch (Exception e)
		{
			clear();
		}
	}
	
	/**Gets the current check box state as a boolean
	 * @return True if the check box is checked, False if it's unchecked.
	 */
	public boolean getBoolValue()
	{
		return chkBox.isSelected();
	}

}
