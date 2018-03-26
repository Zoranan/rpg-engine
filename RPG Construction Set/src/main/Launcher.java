package main;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import util.Handler;
import window.Display;

public class Launcher{
	private static Display MainEditor;
	private static MainPanel mainPanel;
	private static String[] fNames;
	private static String[] fRoots;
	
	public synchronized static void main(String[] args){
		//Initialize file names for checking the root folder
		fNames = new String[] {"sprites.xml",
				"models.xml",
				"environmentalObjects.xml",
				"items.xml",
				"races.xml"
				};
		
		fRoots = new String[] {"sprites", 
								"models",
								"objects",
								"items",
								"races"};
		
		//Attempt to load preferences
		Handler.loadPrefs();
		
		//Make sure a valid root folder is selected
		while (isRootDirValid() == false)
		{
			//First attempt to load our saved directory from last time?
			int i = selRootOptionPane();
			
			//Select existing root
			if (i == 0)
			{
				Handler.setRootDirectory(".");
				Handler.selectRootFolder();
				
				//Check if the choice was valid
				if (isRootDirValid() == false)
					JOptionPane.showMessageDialog(null, Handler.getRootDirectory() + "\nThat folder is not a valid root", "Invalid Root", JOptionPane.ERROR_MESSAGE);
			}
			//Create a new root
			else if (i == 1)
			{
				createRootFolder();
			}
			//Only one other option: Exit
			else
			{
				System.exit(0);
			}
		}
		
		//Create the window
		MainEditor = new Display("Main Editor", 1000, 600);
		MainEditor.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Add the main panel to the window.
		for (int j = 0; j < 2; j++)	// At times, the panel would fail to initialize. This seems to have fixed it
			mainPanel = new MainPanel();
		
		MainEditor.setPanel(mainPanel);
	}
	
	
	//Checks is the root folder is valid
	public static boolean isRootDirValid()
	{		
		boolean b = true;
		
		for (int i=0; i<fNames.length; i++)
		{
			File file = new File(Handler.getRootDirectory() + "/" + fNames[i]);
			
			if (file.exists() == false || file.isDirectory())
				b = false;
		}
		
		
		return b;
	}
	
	//Shows an option pane prompting the user to select a root folder
	private static int selRootOptionPane()
	{
		String message = "You must select a root folder, or create a new one.";
		String[] options = {"Select Root Folder", "Create New Root", "Exit"}; 
		int i = JOptionPane.showOptionDialog(null, message, "Root folder needed", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		
		return i;
	}
	
	//Make a new root directory
	private static void createRootFolder()
	{
		
	}
}
