package editors.subPanels;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import gameObjects.SpriteSheet;
import util.FpsTimer;

public class SpritePreviewLabel extends JLabel implements Runnable
{
	public final int PADDING = 30;
	private Thread thread;
	private ImageIcon preview;
	private SpriteSheet sprite;
	private boolean playing = false;
	private String placeholderText = "preview";
	
	//Constructor
	public SpritePreviewLabel ()
	{
		this.setText(placeholderText);
		this.setHorizontalAlignment(JLabel.CENTER);
		this.preview = new ImageIcon();
		this.setIcon(preview);
		thread = new Thread(this);
		
	}
	
	//Animating the sprite
	@Override
	public void run() 
	{
		//Only animate if we have a sprite and its animated
		if (sprite != null && sprite.getFps() > 0)
		{
			FpsTimer timer = new FpsTimer(sprite.getFps());
			int i = 0;
			
			//The loop
			while (playing)
			{
				if (timer.check())
				{
					
					preview.setImage(sprite.getFrame(i));
					
					this.validate();
					this.repaint();
					
					i++;
					i %= sprite.size();
				}
			}
			
		}
	}
	
	//Stop
	public void stop()
	{
		playing = false;
		
		if (thread != null)
			thread.interrupt();
		
		if (sprite != null)
			this.preview.setImage(sprite.getFrame(0));
		
		while (thread.isAlive()); //Wait for the thread to die
	}
	
	//play
	public void play()
	{
		if (playing == false)
		{
			if (!thread.isAlive())
				thread = new Thread(this);
			
			playing = true;
			thread.start();
		}
	}
	
	//Setting the preview
	public void setSprite(SpriteSheet ss)
	{
		this.stop();
		
		if (ss == null)
		{
			this.setIcon(null);
			this.setText(placeholderText);
		}
		
		else
		{
			this.preview.setImage(ss.getFrame(0));
			this.setText("");
			this.setIcon(preview);
		}
		
		this.sprite = ss;
		
		int w = (int) this.getPreferredSize().getWidth() + PADDING;
		int h =  (int) this.getPreferredSize().getHeight() + PADDING;
		
		//this.setMinimumSize(this.getPreferredSize());
		this.setSize(w, h);
		//Validate and repaint
		this.validate();
		this.repaint();
	}
	
	//get playing status
	public boolean isPlaying()
	{
		return playing;
	}
}

