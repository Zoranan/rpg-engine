package editors.compoundObjects;

import javax.swing.JTextField;
import javax.swing.SpringLayout;

import dev.zoranan.utils.TextValidator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**A simple labeled text field. Child of {@link CompoundComponent}.
 * @author Will
 *
 */
public class LabeledTextBox extends CompoundComponent {
	public static enum CharacterMode {ALL, ALPHA, NUMERIC, ALPHA_NUMERIC};
	protected JTextField txtSelectionField;
	protected KeyAdapter inputMask;
	protected char[] validChars;
	protected CharacterMode mode;
	protected Action keyTypeAction;
	
	/**
	 * Creates a new {@link LabeledTextBox}.
	 * @param labelTxt The label lext that appears to the left of the text box on the form.
	 * @param nodeName The name of the xml node that this CompoundComponent is associated with. 
	 */
	public LabeledTextBox(String labelTxt, String nodeName) 
	{
		super (labelTxt, nodeName);
		this.mode = CharacterMode.ALL;	//Default character mode is ALL
		SpringLayout springLayout = (SpringLayout) getLayout();
		
		//Create the key mask
		inputMask = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
				boolean valid = false;
				
				//Check through our valid characters first
				if (validChars != null)
					for (int i = 0; i < validChars.length && !valid; i++)
					{
						valid |= (e.getKeyChar() == validChars[i]);
					}
				
				//If the character entered doesn't match any specific valid characters, validate based on Mode
				if (valid == false)
				{
					if (mode == CharacterMode.ALPHA && TextValidator.isNotAlpha(e.getKeyChar()))
						e.consume();
					else if (mode == CharacterMode.NUMERIC && TextValidator.isNotNumeric(e.getKeyChar()))
						e.consume();
					else if (mode == CharacterMode.ALPHA_NUMERIC && TextValidator.isNotAlphaNumeric(e.getKeyChar()))
						e.consume();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e)
			{
				if (keyTypeAction != null)
					keyTypeAction.action();
			}
		};
		
		txtSelectionField = new JTextField();
		txtSelectionField.addKeyListener(inputMask);
		springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, txtSelectionField, 0, SpringLayout.VERTICAL_CENTER, lblLabel);
		springLayout.putConstraint(SpringLayout.WEST, txtSelectionField, 6, SpringLayout.EAST, lblLabel);
		
		txtSelectionField.setText("");
		txtSelectionField.setColumns(10);
		
		add(txtSelectionField);
	}
	
	//FUNCTIONS
	
	/**Sets if the text field is editable or not
	 * @param isEditable If true, the text field is editable. Otherwise, it is not editable.
	 */
	public void setEditable (boolean isEditable)
	{
		this.txtSelectionField.setEditable(isEditable);
	}
	
	/**Gets the text field component within this {@link LabeledTextBox}
	 * @return The JTextField Component of this form element
	 */
	public JTextField getTextField()
	{
		return this.txtSelectionField;
	}
	
	/* (non-Javadoc)
	 * @see editors.compoundObjects.CompoundComponent#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String value)
	{
		this.txtSelectionField.setText(value);
	}
	
	/**Sets which characters are acceptable in this text field. 
	 * @param cm The character mode that determines which characters are acceptable
	 */
	public void setCharacterMode(CharacterMode cm)
	{
		this.mode = cm;
	}
	
	/**Sets a list of additional valid characters for the text field.<br/> 
	 * These characters will pass by the CharacterMode filter.
	 * @param chars A string containing all characters that are allowed to pass through the the CharacterMode filter. 
	 */
	public void setValidChars(String chars)
	{
		this.validChars = chars.toCharArray();
	}
	
	
	/**Sets an action to be performed when the user types anything into the text field.
	 * @param action The {@link Action} interface to be triggered when text is typed
	 */
	public void setKeyTypedAction(Action action)
	{
		this.keyTypeAction = action;
	}
	
	/**Gets the text entered into this CompoundComponent's JTextField.
	 * @return A string of text from the JTextField.
	 * @see editors.compoundObjects.CompoundComponent#getValue()
	 */
	@Override
	public String getValue()
	{
		return this.txtSelectionField.getText();
	}
	
	@Override
	public void setEnabled(boolean b)
	{
		this.txtSelectionField.setEnabled(b);
	}

}
