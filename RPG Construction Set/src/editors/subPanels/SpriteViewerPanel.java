package editors.subPanels;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import gameObjects.SpriteSheet;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class SpriteViewerPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	private JButton btnPlayToggle;
	private SpritePreviewLabel preview;
	
	public SpriteViewerPanel() 
	{
		setLayout(new BorderLayout(0, 0));		//Set THIS panels layout
		JPanel center = new JPanel();			//Create a panel to fill the center
		center.setLayout(new BorderLayout(0, 0));	//Set the layout of the center panel
		this.add(center, BorderLayout.CENTER);	//Add the center panel to the center of THIS panel
		
		preview = new SpritePreviewLabel();
		preview.setHorizontalAlignment(SwingConstants.CENTER);
		center.add(preview, BorderLayout.CENTER);
		
		//The play button
		btnPlayToggle = new JButton("Play");
		btnPlayToggle.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (preview.isPlaying())
					stop();
				else
					play();
			}
		});
		center.add(btnPlayToggle, BorderLayout.SOUTH);
		btnPlayToggle.setEnabled(false);
		
	}
	
	//Sets the sprite within the preview label
	public void setSprite(SpriteSheet ss)
	{
		this.stop();
		preview.setSprite(ss);
		
		if (ss == null || ss.getFps() <= 1)
			btnPlayToggle.setEnabled(false);
		else
			btnPlayToggle.setEnabled(true);
		
		this.validate();
		this.repaint();
	}
	
	public void resize()
	{
		this.setSize(this.getPreferredSize());
		this.validate();
		this.repaint();
	}
	
	//Stop animation of the sprite
	public void stop()
	{
		preview.stop();
		btnPlayToggle.setText("Play");
	}
	
	//Start animation of the sprite
	public void play()
	{
		preview.play();
		btnPlayToggle.setText("Stop");
	}

}
