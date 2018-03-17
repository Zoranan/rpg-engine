package wizards.sprites;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.text.NumberFormatter;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import gameObjects.SpriteSheet;
import util.Handler;
import util.XmlLoader;
import wizards.sprites.*;

import java.awt.ComponentOrientation;

public class CreateNewSpritePanel extends JPanel {
	
	private final int NUM_PAGES = 3;
	private int currentPage = 0;
	private NewSpritePage[] pages;
	
	private JButton btnCancel;
	private JButton btnNext;
	private JButton btnBack;
	
	private SpriteSheet sprite;
	private boolean finished = false;

	/**
	 * Create the panel.
	 */
	public CreateNewSpritePanel() {
		setLayout(new BorderLayout(0, 0));
		
		//Create pages
		pages = new NewSpritePage[NUM_PAGES];
		
		//Create text field formatter
		
		
		JToolBar toolBar = new JToolBar();
		toolBar.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		toolBar.setFloatable(false);
		add(toolBar, BorderLayout.SOUTH);
		
		//CANCEL
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				cancelWizard();
			}
		});
		toolBar.add(btnCancel);
		
		//NEXT PAGE
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				nextBtnPressed();
			}
		});
		toolBar.add(btnNext);
		
		//PREVIOUS PAGE
		btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				showPage(currentPage - 1);
				btnNext.setEnabled(true);
			}
		});
		toolBar.add(btnBack);

		/////////////
		cancelWizard();
		setNextEnabled(false);
		////////////
	}
	
	public void nextBtnPressed()
	{
		if (currentPage == NUM_PAGES-1)
			finishSprite();
		else
		{
			showPage(currentPage + 1);
			btnNext.setEnabled(pages[currentPage].formComplete());
		}
	}
	
	public void finishSprite()
	{
		if (currentPage == NUM_PAGES-1)
		{
			//Attempt to load the xml
			Document doc = XmlLoader.readXML(Handler.getRootDirectory() + "/sprites.xml");
			
			//Start with page 3 (index 2)
			//This is where we get the root element for our sprite
			Element nameID = pages[2].getSpriteData();
			
			//Check for a name conflict!!!
			//If a node with this name exists in our file, skip to the else
			//Otherwise...
			if (doc.getRootElement().getChild(nameID.getName()) == null)
			{
				
				//To keep things clean, we want the tags at the bottom
				//We're just going to hold on to them here for now
				Element tags = nameID.getChild("tags").detach();
				nameID.removeChild("tags");
				
				//Now we need to add the image path (<src>) tag to our Element
				nameID.addContent(pages[0].getSpriteData());	//Gets the src from page 1
				
				//Now we need the width, height, and fps from page 2.
				//So we detach these children from the element page 2 returns.
				//We will store them in a list so we can iterate through it.
				List<Element> childrenList = pages[1].getSpriteData().getChildren();
				for (Element e : childrenList)
				{
					//We can't directly add the children since they are still tied to a parent element.
					//We will clone them so we don't cause problems with the iterator
					//Cloning detaches as well
					nameID.addContent(e.clone());
				}
				
				//Finally, lets add the tags back in
				nameID.addContent(tags);
				
				//Now we can add the new sprite element we created, 
				//to the root of the sprites xml file
				doc.getRootElement().addContent(nameID);
				
				//Save the xml (overwrite)
				XmlLoader.writeXML(doc);
				
				//Reset the form
				finished = true;
				cancelWizard();
			}
			
			//If we found a sprite with the same nameID as our new one,
			//pop up an error message.
			else
			{
				JOptionPane.showMessageDialog(this, nameID.getName() + " is already in use!\nChoose a unique NameID"
						, "Input Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	//Show a specific page
	public void showPage(int p)
	{
		if (p >= 0 && p < NUM_PAGES)
		{	
			//TRY to remove the current page
			pages[currentPage].setVisible(false);
			this.remove(pages[currentPage]);
			
			//Add the new page
			pages[p].setVisible(true);
			this.add(pages[p], BorderLayout.CENTER);
			
			//Set the current page to the new page
			currentPage = p;
			
			//Enable or disable the next and back buttons
			updateBackBtn();
			btnNext.setEnabled(pages[currentPage].formComplete());
			
			//Change the next button
			if (currentPage == NUM_PAGES-1)
				btnNext.setText("Finish");
			else 
				btnNext.setText("Next");
			
			this.revalidate();
			this.repaint();
		}
	}
	
	//Reset the current page
	public void showPage()
	{
		//TRY to remove the current page
		this.remove(pages[currentPage]);
		//Add the new page
		this.add(pages[currentPage], BorderLayout.CENTER);
		
		updateBackBtn();
		
		this.revalidate();
		this.repaint();
	}
	
	//disable the back button
	public void updateBackBtn()
	{
		//Enable / disable back button
		if (currentPage == 0)
			btnBack.setEnabled(false);
		else
			btnBack.setEnabled(true);
	}
	
	//enable / disable next button
	public void setNextEnabled (boolean b)
	{
		btnNext.setEnabled(b);
	}
	
	//Cancel Button : Reset all data and restart the new sprite wizard
	public void cancelWizard ()
	{	
		
		if (this.getTopLevelAncestor() instanceof JFrame)
			((JFrame) this.getTopLevelAncestor()).dispose();
		
		//This should never trigger
		//Needs to be deleted
		else
		{
			//Delete any old instances
			for (int i = 0; i < NUM_PAGES; i++)
			{
				if (pages[i] != null)
				{
					this.remove(pages[i]);
				}
				pages[i] = null;
			}
			System.gc();
			
			//Create pages
			pages[0] = new NewSpritePage01();
			pages[1] = new NewSpritePage02();
			pages[2] = new NewSpritePage03();
			
			//Set Visibility false
			for (int i = 0; i < NUM_PAGES; i++)
				pages[i].setVisible(false);
			
			showPage(0);
			//Unload all data.
		}
	}
	
	//Handling our sprite sheet data
	public void createSheet(BufferedImage img)
	{
		sprite = new SpriteSheet(img);
	}
	
	public SpriteSheet getSprite ()
	{
		return sprite;
	}
	
	//Get wizard status
	public boolean isFinished()
	{
		return finished;
	}
}
