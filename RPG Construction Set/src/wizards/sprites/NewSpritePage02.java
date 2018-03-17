package wizards.sprites;

import javax.swing.JLabel;

import java.text.NumberFormat;

import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import org.jdom2.Element;
import org.jdom2.Text;

import util.FpsTimer;
import util.TextValidator;

import javax.swing.ImageIcon;
import javax.swing.JFormattedTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NewSpritePage02 extends NewSpritePage implements Runnable{
	private JFormattedTextField frmtdtxtfldWidth;
	private JFormattedTextField frmtdtxtfldHeight;
	private JFormattedTextField frmtdtxtfldFps;
	
	private NumberFormatter numsOnly;
	private JLabel lblSpritePreview;
	private ImageIcon spritePreview;
	
	private JButton btnPlay;
	private boolean playing = false;
	private Thread playThread;
	int fps = 0;
	private JButton btnMax;

	/**
	 * Create the panel.
	 */
	public NewSpritePage02() {
		setLayout(null);
		spritePreview = new ImageIcon();
		
		//Set formatter
		NumberFormat format = NumberFormat.getInstance();
	    numsOnly = new NumberFormatter(format);
	    numsOnly.setValueClass(Integer.class);
	    numsOnly.setMinimum(0);
	    numsOnly.setMaximum(2000);
	    numsOnly.setAllowsInvalid(true);
	    
		
		JLabel lblFrameWidth = new JLabel("Frame Width:");
		lblFrameWidth.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFrameWidth.setBounds(10, 11, 80, 14);
		add(lblFrameWidth);
		
		JLabel lblFrameHeight = new JLabel("Frame Height:");
		lblFrameHeight.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFrameHeight.setBounds(0, 36, 90, 14);
		add(lblFrameHeight);
		
		JLabel lblFps = new JLabel("FPS:");
		lblFps.setHorizontalAlignment(SwingConstants.RIGHT);
		lblFps.setBounds(44, 61, 46, 14);
		add(lblFps);
		
		frmtdtxtfldWidth = new JFormattedTextField(numsOnly);
		frmtdtxtfldWidth.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				if (TextValidator.isNotNumeric(e.getKeyChar()) || (frmtdtxtfldWidth.getCaretPosition() == 0 && e.getKeyChar() == '0'))
					e.consume();
			}
		});
		frmtdtxtfldWidth.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) 
			{
				updateTextBox();
			}
		});
		frmtdtxtfldWidth.setColumns(4);
		frmtdtxtfldWidth.setText("0");
		frmtdtxtfldWidth.setBounds(100, 8, 50, 20);
		add(frmtdtxtfldWidth);
		
		frmtdtxtfldHeight = new JFormattedTextField(numsOnly);
		frmtdtxtfldHeight.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{
				if (TextValidator.isNotNumeric(e.getKeyChar()) || (frmtdtxtfldHeight.getCaretPosition() == 0 && e.getKeyChar() == '0'))
					e.consume();
			}
		});
		frmtdtxtfldHeight.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) 
			{
				updateTextBox();
			}
		});
		frmtdtxtfldHeight.setColumns(4);
		frmtdtxtfldHeight.setText("0");
		frmtdtxtfldHeight.setBounds(100, 33, 50, 20);
		add(frmtdtxtfldHeight);
		
		frmtdtxtfldFps = new JFormattedTextField(numsOnly);
		frmtdtxtfldFps.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) 
			{				
				if (TextValidator.isNotNumeric(e.getKeyChar()))
					e.consume();
			}
		});
		frmtdtxtfldFps.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) 
			{
				updateTextBox();
			}
		});
		frmtdtxtfldFps.setColumns(2);
		frmtdtxtfldFps.setText("0");
		frmtdtxtfldFps.setBounds(100, 58, 50, 20);
		add(frmtdtxtfldFps);
		
		//Sprite preview
		lblSpritePreview = new JLabel("");
		lblSpritePreview.setBounds(160, 8, 46, 14);
		lblSpritePreview.setIcon(spritePreview);
		add(lblSpritePreview);
		
		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				//
				playing = !playing;
				playBtnUpdate();
			}
		});
		btnPlay.setBounds(61, 86, 89, 23);
		btnPlay.setEnabled(false);
		add(btnPlay);
		
		btnMax = new JButton("Max");
		btnMax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				maxBtnClicked();
			}
		});
		btnMax.setBounds(61, 120, 89, 23);
		add(btnMax);
		
		updateTextBox();
		updateSpritePreview(0);
		
	}
	
	public void maxBtnClicked()
	{
		if (this.getParent() instanceof CreateNewSpritePanel)
		{
			int maxW = ((CreateNewSpritePanel) this.getParent()).getSprite().getImage().getWidth();
			int maxH = ((CreateNewSpritePanel) this.getParent()).getSprite().getImage().getHeight();
			this.frmtdtxtfldHeight.setText(Integer.toString(maxH));
			this.frmtdtxtfldWidth.setText(Integer.toString(maxW));
			updateTextBox();
		}
	}
	
	public void updateTextBox()
	{
		//Get our variables from the text boxes
		int w, h;
		try
		{
			w = Integer.parseInt(frmtdtxtfldWidth.getText());
		}
		catch (Exception e)
		{
			w = 0;
		}
		try
		{
			h = Integer.parseInt(frmtdtxtfldHeight.getText());
		}
		catch (Exception e)
		{
			h = 0;
		}
		
		try
		{
			fps = Integer.parseInt(frmtdtxtfldFps.getText());
		}
		catch (Exception e)
		{
			fps = 0;
		}
		
		if (this.getParent() instanceof CreateNewSpritePanel)
		{
			int maxW = ((CreateNewSpritePanel) this.getParent()).getSprite().getImage().getWidth();
			int maxH = ((CreateNewSpritePanel) this.getParent()).getSprite().getImage().getHeight();
			
			//Validate width
			if (w > maxW)
			{
				w = maxW;
				frmtdtxtfldWidth.setText(Integer.toString(w));
			}
			
			//Validate height
			if (h > maxH)
			{
				h = maxH;
				frmtdtxtfldHeight.setText(Integer.toString(h));
			}
		}
		
		//Crop frames if a valid width and height are set
		if (w > 0 && h > 0)
		{
			
			if (this.getParent() instanceof CreateNewSpritePanel)
			{
				((CreateNewSpritePanel) this.getParent()).getSprite().cropFrames(w, h);
				((CreateNewSpritePanel) this.getParent()).setNextEnabled(true);
			}
			formCompleted = true;
		}
		//If width and height are invalid, do not allow the next button
		else if (this.getParent() instanceof CreateNewSpritePanel)
		{
			((CreateNewSpritePanel) this.getParent()).setNextEnabled(false);
			formCompleted = false;
		}
		
		//Enable / disable the play button
		if (fps > 0 && this.getParent() instanceof CreateNewSpritePanel)
		{
			if (((CreateNewSpritePanel) this.getParent()).getSprite().size() == 1)
				btnPlay.setEnabled(false);
			else
				btnPlay.setEnabled(true);
				
		}
		else
			btnPlay.setEnabled(false);
		
		playing = false;
		playBtnUpdate();
		updateSpritePreview(0);
	}
	
	public void updateSpritePreview(int i)
	{
		if (this.getParent() instanceof CreateNewSpritePanel)
			spritePreview.setImage(((CreateNewSpritePanel) this.getParent()).getSprite().getFrame(i));
		
		//Resize / repaint
		lblSpritePreview.setSize(lblSpritePreview.getPreferredSize());
		lblSpritePreview.validate();
		lblSpritePreview.repaint();
	}
	
	//What happens when the play button is clicked
	//Changes button text and starts / stops the thread
	public void playBtnUpdate()
	{		
		//If we press play
		if (playing)
		{	
			btnPlay.setText("Stop");
			playThread = new Thread(this);
			playThread.start();
		}
		//If we press stop
		else
		{
			btnPlay.setText("Play");
			
			if (playThread != null)
			{
				playThread.interrupt();
			}
		}
	}

	//Our frame by frame update thread
	@Override
	public void run() 
	{
		int frameTotal = 0;
		if (this.getParent() instanceof CreateNewSpritePanel)
			frameTotal = ((CreateNewSpritePanel) this.getParent()).getSprite().size();
		
		FpsTimer timer = new FpsTimer(fps);			//Set up the proper fps
		int frameNum = 0;
		
		//The play loop
		while (playing)
		{
			if (timer.check())						//Advance to the next frame
			{
				frameNum++;
				frameNum %= frameTotal;
				
				this.updateSpritePreview(frameNum);
			}
			
			//If we change pages
			if (!this.isVisible())
			{
				playing = false;
				playBtnUpdate();
			}
		}
	}
	
	@Override
	public boolean formComplete()
	{
		updateTextBox();
		return this.formCompleted;
	}
	
	//Getting the element data
	@Override
	public Element getSpriteData()
	{
		Element nameID = new Element("NameID");
		Element w = new Element("width");
		Element h = new Element("height");
		Element fps = new Element("fps");
		
		w.addContent(new Text(frmtdtxtfldWidth.getText()));
		h.addContent(new Text(frmtdtxtfldHeight.getText()));
		fps.addContent(new Text(frmtdtxtfldFps.getText()));
		
		nameID.addContent(w);
		nameID.addContent(h);
		nameID.addContent(fps);
		
		return nameID;
	}
}
