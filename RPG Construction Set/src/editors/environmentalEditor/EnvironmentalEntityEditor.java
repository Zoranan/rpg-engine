package editors.environmentalEditor;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import editors.subPanels.XMLExplorerPanel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class EnvironmentalEntityEditor extends JPanel {
	private JButton btnEdit;
	private JButton btnNew;
	private XMLExplorerPanel left;
	
	/**
	 * Create the panel.
	 */
	public EnvironmentalEntityEditor () {
		setLayout(new BorderLayout(0, 0));
		
		new ArrayList<JTextField>(); 
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		//set the right side
		JPanel right = new JPanel();
		splitPane.setRightComponent(right);
		right.setLayout(new BorderLayout(0, 0));
		
		//Set the left side
		btnEdit = new JButton("L");						//Create a load button for the eeep panel
		left = new XMLExplorerPanel("environmentalObjects.xml", "Environmental Objects");
		btnEdit.addActionListener(new ActionListener() 		//Add the actions for a button to be added to the eeep panel
		{
			public void actionPerformed(ActionEvent e) 
			{
				//Get our Entity from the list
			}
		});
		btnEdit.setBounds(107, 36, 89, 23);
		left.addBtn(btnEdit);				//Add the load putton to the eeep panel
		
		btnNew = new JButton("");
		btnNew.setIcon(new ImageIcon(EnvironmentalEntityEditor.class.getResource("/Icons/newSingle.png")));
		btnNew.addActionListener(new ActionListener() 		//Add the actions for a button to be added to the eeep panel
		{
			public void actionPerformed(ActionEvent e) 
			{
				right.add(new EnvironmentalEntityForm(), BorderLayout.CENTER);
				right.validate();
				right.repaint();
			}
		});
		left.addBtn(btnNew);
		
		splitPane.setLeftComponent(left);	//Add the eeep panel to the left side
	}
}
