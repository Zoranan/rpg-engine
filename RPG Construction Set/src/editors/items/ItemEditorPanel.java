package editors.items;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;

import editors.subPanels.XMLExplorerPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ItemEditorPanel extends JPanel {
	private XMLExplorerPanel leftSide;
	/**
	 * Create the panel.
	 */
	public ItemEditorPanel() {
		
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		//Left side of split
		leftSide = new XMLExplorerPanel("items.xml", "Items");
		splitPane.setLeftComponent(leftSide);
		
		//Right side of split
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		scrollPane.setViewportView(new ItemEditorForm());
	}
}
