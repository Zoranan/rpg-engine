package wizards.sprites;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JSplitPane;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;

import dev.zoranan.utils.XmlLoader;
import editors.subPanels.SpriteViewerPanel;
import editors.subPanels.XMLExplorerPanel;
import gameObjects.SpriteSheet;
import util.Handler;
import util.ImageLoader;
import util.PersistentFileChooser;
import window.Display;

public class ExistingSpritePanel extends JPanel {
		//private JLabel lblPreviewFrame;
	
	private XMLExplorerPanel leftSide;
	private SpriteViewerPanel rightSide;
	private JButton btnNewSprite;
	private JButton btnNewSpriteBatch;
	
	/**
	 * Create the panel.
	 */
	public ExistingSpritePanel() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		leftSide = new XMLExplorerPanel("sprites.xml", "Sprites");
		splitPane.setLeftComponent(leftSide);
		
		rightSide = new SpriteViewerPanel();
		splitPane.setRightComponent(rightSide);
		
		//Add a NEW button to the loader
		btnNewSprite = new JButton("");
		btnNewSprite.setIcon(ImageLoader.loadResourceIcon("/Icons/newSingle.png"));
		btnNewSprite.setToolTipText("New Sprite: Wizard");
		btnNewSprite.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				newSpriteWizard();
			}
		});
		leftSide.addBtn(btnNewSprite);
		
		//Add a new batch button to the loader
		btnNewSpriteBatch = new JButton("");
		btnNewSpriteBatch.setIcon(ImageLoader.loadResourceIcon("/Icons/newMulti.png"));
		btnNewSpriteBatch.setToolTipText("New Sprite(s): Quick");
		btnNewSpriteBatch.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				newSpriteBatch();
			}
		});
		leftSide.addBtn(btnNewSpriteBatch);
		
		//Add a listener to the loader (left side)
		leftSide.getList().addListSelectionListener(new ListSelectionListener() 
		{
			@Override
			public void valueChanged(ListSelectionEvent e) 
			{
				loadSprite();
			}
		});
	}
	
	//Load the sprite selected in the list in the left pane
	public void loadSprite()
	{
		String path = Handler.getRootDirectory() + leftSide.getSelectedElement().getChild("src").getValue();
		int w = Integer.parseInt(leftSide.getSelectedElement().getChild("width").getValue());
		int h = Integer.parseInt(leftSide.getSelectedElement().getChild("height").getValue());
		int fps = Integer.parseInt(leftSide.getSelectedElement().getChild("fps").getValue());
		
		
		SpriteSheet ss = new SpriteSheet(ImageLoader.loadImage(path));
		ss.cropFrames(w, h);
		ss.setFps(fps);
		rightSide.setSprite(ss);
	}
	
	//New sprite batch
	public void newSpriteBatch()
	{
		//Open the dialog
		PersistentFileChooser fc = new PersistentFileChooser("Choose Sprite Sheets", true);
				
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			Document doc = leftSide.getDoc();
			int failed = 0;
			int succeded = 0;
			//get the information. Iterate through files
			File[] sprites = fc.getSelectedFiles();
			for (int i = 0; i<sprites.length; i++)
			{
				String name = sprites[i].getName();
				
				//Strip the file extension
				int end = name.length()-1;
				for (int j = name.length()-1; j > 0; j--)
				{
					if (name.charAt(j) == '.')
					{
						end = j;
						break;
					}
				}
				name = name.substring(0, end);
				
				//If file name is unique, save
				if (doc.getRootElement().getChild(name) == null)
				{
					Element nameID = new Element(name);
					
					Element src = new Element("src");
					src.addContent(new Text(Handler.ConvertImgPath(sprites[i].getAbsolutePath())));
					
					BufferedImage image = ImageLoader.loadImageFullPath(sprites[i].getAbsolutePath());
					Element width = new Element ("width");
					width.addContent(Integer.toString(image.getWidth()));
					Element height = new Element ("height");
					height.addContent(Integer.toString(image.getHeight()));
					
					Element fps = new Element("fps").addContent("0");
					Element tags = new Element("tags");
					
					nameID.addContent(new Element("name").addContent(name));
					nameID.addContent(src);
					nameID.addContent(width);
					nameID.addContent(height);
					nameID.addContent(fps);
					nameID.addContent(tags);
					
					doc.getRootElement().addContent(nameID);
					
					succeded++;
				}
				else
				{
				//choose a different name
					failed++;
				}
			}//Done with all files
			XmlLoader.writeXML(doc); //Save the new sprites
			JOptionPane.showMessageDialog(this, "Operation Completed!\n" + succeded + " succeded\n" + failed + " failed");
			leftSide.loadXML(true);
		}
	}
	
	//Open the new sprite wizard
	public void newSpriteWizard()
	{
		Display spriteWindow = new Display("Sprites", 640, 480);
		spriteWindow.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//Dock the sprite select / create pane in our new window
		CreateNewSpritePanel wizard = new CreateNewSpritePanel();
		spriteWindow.setPanel(wizard);
		spriteWindow.getFrame().addWindowListener(
				new WindowListener()
				{

					@Override
					public void windowOpened(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowClosing(WindowEvent e) {
						
						
					}

					@Override
					public void windowClosed(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowIconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowDeiconified(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowActivated(WindowEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void windowDeactivated(WindowEvent e) {
						if (wizard.isFinished())
							leftSide.loadXML();
					}
				});
	}
}