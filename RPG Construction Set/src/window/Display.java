package window;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display {
	private JFrame frame;
	
	private String title;
	private int width, height;
	
	public Display(String title, int width, int height)
	{
		this.title = title;
		this.width = width;
		this.height = height;
		
		createDisplay();
	}
	
	private synchronized void createDisplay()
	{
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		/*
		canvas = new Canvas();
		canvas.setBackground(Color.DARK_GRAY);
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);
		
		frame.add(canvas);
		frame.pack();
		*/
	}
	
	public JFrame getFrame()
	{
		return frame;
	}
	
	public synchronized void setPanel (JPanel jpanel)
	{
		frame.add(jpanel);
	}
	
	public void dispose()
	{
		frame.dispose();
	}

}
