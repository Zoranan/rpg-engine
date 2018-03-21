package main;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import editors.EditorTab;
import editors.environmentalEditor.EnvironmentalEntityForm;
import editors.items.ItemEditorForm;
import editors.modelEditor.ModelEditorForm;
import editors.npcEditor.NpcEditorForm;
import editors.races.RaceEditorForm;
import util.Handler;
import wizards.sprites.ExistingSpritePanel;

import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//This is the main panel for the Construction set

public class MainPanel extends JPanel{
	private JTabbedPane tabbedPane;
	private String names[] = {"Sprites", "Models", "Environmental", "Items", "Races", "NPC's"};
	private boolean tabsLoaded[] = {true, false, false, false, false, false};
	private int lastTabEvent = 0;
	private boolean buildingTab = false;
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
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (e.getSource() instanceof JTabbedPane) {
					if (!buildingTab)
					{
                    	initTab(tabbedPane.getSelectedIndex());
					}
				}
			}
		});
		add(tabbedPane, BorderLayout.CENTER);
		
		//ADD TABS
		tabbedPane.addTab("Sprites", new ExistingSpritePanel());
		//Placeholder tabs
		for (int i = 1; i < names.length; i++)
		{
			tabbedPane.addTab(names[i], new JPanel());
		}
	}
	
	//Initialize a tab
	//If a tab is clicked for the first time, we need to create it.
	public void initTab(int i)
	{
		if (!buildingTab && !tabsLoaded[i] && i > 0)
		{
			buildingTab = true;
			tabbedPane.removeTabAt(i);
			if (i == 1)
				tabbedPane.insertTab("Models", null, new EditorTab("models.xml", "Models", new ModelEditorForm()), "", i);
			else if (i == 2)
				tabbedPane.insertTab("Environmental", null, new EditorTab("environmentalObjects.xml", "Environmental Objects", new EnvironmentalEntityForm()), "", i);
			else if (i == 3)
				tabbedPane.insertTab("Items", null, new EditorTab("items.xml", "Items", new ItemEditorForm()), "", i);
			else if (i == 4)
				tabbedPane.insertTab("Races", null, new EditorTab("/races.xml", "Races", new RaceEditorForm()), "", i);
			else if (i == 5)
				tabbedPane.insertTab("NPC's", null, new EditorTab(null, new NpcEditorForm()), "", i);
			
			tabsLoaded[i] = true;
			tabbedPane.setSelectedIndex(i);
			System.out.println("Created tab " + names[i]);
			buildingTab = false;
		}
	}
}
