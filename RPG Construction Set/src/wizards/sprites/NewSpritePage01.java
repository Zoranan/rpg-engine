package wizards.sprites;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.jdom2.Element;
import org.jdom2.Text;

import util.Handler;
import util.ImageLoader;
import util.PersistentFileChooser;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

public class NewSpritePage01 extends NewSpritePage{
	
	private BufferedImage img;
	private ImageIcon imgIcon;
	private String imgPath = "";
	
	private JLabel lblImageFrame;

	/**
	 * Create the panel.
	 */
	public NewSpritePage01() {
		
		setLayout(new BorderLayout(0, 0));
		this.setMinimumSize(new Dimension (100, 100));
		
		JButton btnSelectImage = new JButton("Select Image");
		btnSelectImage.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				selectImageFile();
			}
		});
		add(btnSelectImage, BorderLayout.SOUTH);
		
		//Image Frame
		lblImageFrame = new JLabel("Your Sprite Sheet");
		lblImageFrame.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblImageFrame, BorderLayout.CENTER);

	}
	
	//FUNCTIONS
	public void allowNext()
	{
		if (this.getParent() instanceof CreateNewSpritePanel)
		{
			//Enable the next button in our parent component
			((CreateNewSpritePanel) this.getParent()).setNextEnabled(true);
		}
		else
			System.err.println("could not find a valid parent frame");
	}
	
	//Select the image to load
	//Should check for the proper folder to exist (doesn't yet)
	public void selectImageFile()
	{
		//Open the dialog
		PersistentFileChooser fc = new PersistentFileChooser("Choose Sprite Sheet...", false);
		
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			//get the information
			imgPath = fc.getSelectedFile().getAbsolutePath();
			
			//CHECK FILE HERE
			
			
			//Load the file we selected
			img = ImageLoader.loadImageFullPath(imgPath);
			imgIcon = new ImageIcon(img);
			lblImageFrame.setIcon(imgIcon);
			lblImageFrame.setText("");
			
			//Send the image to our preview page
			if (this.getParent() instanceof CreateNewSpritePanel)
				((CreateNewSpritePanel) this.getParent()).createSheet(img);
			
			allowNext();
		}
		
	}
	
	@Override
	public boolean formComplete()
	{
		if (imgPath.length() > 0)
			this.formCompleted = true;
		else this.formCompleted = false;
		
		return formCompleted;
	}
	
	//Getting the element data
	@Override
	public Element getSpriteData()
	{
		Element src = new Element("src");
		src.addContent(new Text(Handler.ConvertImgPath(imgPath)));
		return src;
	}

}
