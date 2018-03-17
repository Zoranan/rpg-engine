package editors.races;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;

public class RaceViewerPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public RaceViewerPanel() {
		setLayout(null);
		
		JLabel lblRaces = new JLabel("Races");
		lblRaces.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblRaces.setBounds(10, 11, 56, 20);
		add(lblRaces);

	}
}
