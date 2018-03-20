package editors.modelEditor;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

import org.jdom2.Document;
import org.jdom2.Element;

import editors.compoundObjects.XmlMultiSelection;
import editors.subPanels.XMLExplorerPanel;
import util.Handler;
import util.ImageLoader;
import util.XmlLoader;

import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ModelEditorPanel extends JPanel {
	private XMLExplorerPanel leftSide;
	private ModelEditorForm form;
	/**
	 * Create the panel.
	 */
	public ModelEditorPanel() {
		setLayout(new BorderLayout(0, 0));
		
		
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		//Set up the XML Explorer (left side)
		leftSide = new XMLExplorerPanel("models.xml", "Models");
		
		//Edit Button
		JButton btnEdit = new JButton(ImageLoader.loadResourceIcon("/Icons/edit.png"));
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				form.load(leftSide.getSelectedElement());
			}
		});
		leftSide.addBtn(btnEdit);
		
		//New Button
		JButton btnNew = new JButton(ImageLoader.loadResourceIcon("/Icons/newSingle.png"));
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				form.clearForm();
			}
		});
		leftSide.addBtn(btnNew);
		
		splitPane.setLeftComponent(leftSide);
		
		form = new ModelEditorForm();
		splitPane.setRightComponent(form);

	}
}
