package editors.compoundObjects;

import javax.swing.JTextField;
import javax.swing.SpringLayout;

import util.TextValidator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LabeledTextBox extends CompoundComponent {
	public static enum CharacterMode {ALL, ALPHA, NUMERIC, ALPHA_NUMERIC};
	protected JTextField txtSelectionField;
	protected KeyAdapter inputMask;
	protected char[] validChars;
	protected CharacterMode mode;
	protected Action keyTypeAction;
	
	/**
	 * Create the panel.
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
	
	public void setEditable (boolean b)
	{
		this.txtSelectionField.setEditable(b);
	}
	
	public JTextField getTextField()
	{
		return this.txtSelectionField;
	}
	
	@Override
	public void setValue(String s)
	{
		this.txtSelectionField.setText(s);
	}
	
	public void setMode(CharacterMode cm)
	{
		this.mode = cm;
	}
	
	public void setValidChars(String chars)
	{
		this.validChars = chars.toCharArray();
	}
	
	//Custom actions for our key typed action
	public void setKeyTypedAction(Action a)
	{
		this.keyTypeAction = a;
	}
	
	@Override
	public String getValue()
	{
		return this.txtSelectionField.getText();
	}
	
	@Override
	public int getComponentWidth()
	{
		return 300;
	}
	
	@Override
	public void setEnabled(boolean b)
	{
		this.txtSelectionField.setEnabled(b);
	}

}
