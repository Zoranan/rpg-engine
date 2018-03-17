package editors.races;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;

import editors.subPanels.XMLExplorerPanel;

import javax.swing.JScrollPane;
import java.awt.FlowLayout;

public class RacesEditorPanel extends JPanel {
	private RaceEditorForm newRacePanel;
	private XMLExplorerPanel left;
	/**
	 * Create the panel.
	 */
	public RacesEditorPanel() 
	{
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		left = new XMLExplorerPanel("races.xml", "Races");
		splitPane.setLeftComponent(left);
		
		JScrollPane scrollPane = new JScrollPane();
		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.add(scrollPane, BorderLayout.CENTER);
		splitPane.setRightComponent(right);
		
		
		newRacePanel = new RaceEditorForm();
		scrollPane.setViewportView(newRacePanel);
	}

}
