package editors.races;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;

import editors.subPanels.XMLExplorerPanel;
import util.ImageLoader;

import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
		
		left = new XMLExplorerPanel("/races.xml", "Races");
		splitPane.setLeftComponent(left);
		
		JScrollPane scrollPane = new JScrollPane();
		JPanel right = new JPanel();
		right.setLayout(new BorderLayout());
		right.add(scrollPane, BorderLayout.CENTER);
		splitPane.setRightComponent(right);
		
		//Edit Button
		JButton btnEdit = new JButton(ImageLoader.loadResourceIcon("/Icons/edit.png"));
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newRacePanel.load(left.getSelectedElement());
			}
		});
		left.addBtn(btnEdit);
		
		//New Button
		JButton btnNew = new JButton(ImageLoader.loadResourceIcon("/Icons/newSingle.png"));
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newRacePanel.clearForm();
			}
		});
		left.addBtn(btnNew);
		
		newRacePanel = new RaceEditorForm();
		scrollPane.setViewportView(newRacePanel);
	}

}
