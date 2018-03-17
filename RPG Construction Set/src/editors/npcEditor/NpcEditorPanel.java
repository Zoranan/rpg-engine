package editors.npcEditor;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;

import editors.subPanels.XMLExplorerPanel;
import util.Handler;

import javax.swing.JScrollPane;

public class NpcEditorPanel extends JPanel {
	private NpcEditorForm right;
	private XMLExplorerPanel left;
	/**
	 * Create the panel.
	 */
	public NpcEditorPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
		//left = new XMLExplorerPanel(Handler.getRootDirectory() + "/npcs.xml", "Npc's");
		//splitPane.setLeftComponent(left);
		right = new NpcEditorForm();
		scrollPane.setViewportView(right);

	}

}
