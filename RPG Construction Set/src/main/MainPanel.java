package main;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import editors.ExistingSpritePanel;
import editors.environmentalEditor.EnvironmentalEntityEditor;
import editors.environmentalEditor.EnvironmentalEntityForm;
import editors.items.ItemEditorForm;
import editors.items.ItemEditorPanel;
import editors.modelEditor.ModelEditorPanel;
import editors.npcEditor.NpcEditorForm;
import editors.npcEditor.NpcEditorPanel;
import editors.races.RacesEditorPanel;
import util.Handler;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//This is going to be the main panel for the Construction set

public class MainPanel extends JPanel{

	private JTabbedPane tabbedPane;
	private ModelEditorPanel modelEditorPanel;
	private EnvironmentalEntityEditor eeep;
	private ItemEditorPanel itemEditPanel;
	private RacesEditorPanel racesEditorPanel;
	
	
	//private ExistingSpritePanelXXX espx;
	/**
	 * Create the panel.
	 */
	public MainPanel() {
		
		setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.NORTH);
		
		//Select Root button
		JButton btnSelectRoot = new JButton("Select Root");
		btnSelectRoot.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Handler.selectRootFolder();
			}
		});
		toolBar.add(btnSelectRoot);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		//ADD TABS
		tabbedPane.addTab("Sprites", new ExistingSpritePanel());
		eeep = new EnvironmentalEntityEditor();
		modelEditorPanel = new ModelEditorPanel();
		itemEditPanel = new ItemEditorPanel();
		racesEditorPanel = new RacesEditorPanel();
		
		tabbedPane.addTab("Models", modelEditorPanel);
		tabbedPane.addTab("Environmental", eeep);
		tabbedPane.addTab("Items", itemEditPanel);
		//tabbedPane.addTab("Items", new ItemEditorForm());
		tabbedPane.addTab("Races", racesEditorPanel);
		
		tabbedPane.addTab("NPC", new NpcEditorPanel());
		
		//Start the Main Thread
		//start();

	}
}
