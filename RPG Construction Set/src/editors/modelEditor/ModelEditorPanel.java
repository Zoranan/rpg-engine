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

import editors.subPanels.XMLExplorerPanel;
import util.Handler;
import util.XmlLoader;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ModelEditorPanel extends JPanel {
	private XMLExplorerPanel leftSide;

	/**
	 * Create the panel.
	 */
	public ModelEditorPanel() {
		setLayout(new BorderLayout(0, 0));
		
		
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		leftSide = new XMLExplorerPanel("models.xml", "Models");
		splitPane.setLeftComponent(leftSide);
		
		//JPanel panel = new JPanel();
		splitPane.setRightComponent(new ModelEditorForm());

	}
}
