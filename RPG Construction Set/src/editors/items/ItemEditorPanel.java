package editors.items;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSplitPane;

import editors.subPanels.XMLExplorerPanel;
import util.ImageLoader;

import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ItemEditorPanel extends JPanel {
	private XMLExplorerPanel leftSide;
	private ItemEditorForm editor;
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
		
		JButton btnEdit = new JButton(ImageLoader.loadResourceIcon("/Icons/edit.png"));
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (leftSide.getSelectedName() != null)
					editor.load(leftSide.getSelectedElement());
			}
		});
		leftSide.addBtn(btnEdit);
		
		JButton btnNew = new JButton(ImageLoader.loadResourceIcon("/Icons/newSingle.png"));
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editor.clearForm();
			}
		});
		leftSide.addBtn(btnNew);
		
		//Right side of split
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		editor = new ItemEditorForm();
		scrollPane.setViewportView(editor);
	}
}
