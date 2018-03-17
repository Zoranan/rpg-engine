package util;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;

import org.jdom2.Document;

import main.Launcher;

public class Handler {
	private static String rootDirectory = ".";
	
	//XML files!!!
	
	//Choosing a root
	public static void selectRootFolder()
	{
		//Open the dialog
		JFileChooser fc = new JFileChooser();
		if (Handler.getRootDirectory() != null)
			fc.setCurrentDirectory(new File (Handler.getRootDirectory()));
		else
			fc.setCurrentDirectory(new File ("."));
		
		fc.setDialogTitle("Choose Directory");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			//get the information
			Handler.setRootDirectory(fc.getSelectedFile().getAbsolutePath());
			//Save the information
			savePrefs();
		}
	}
	
	//Save preferences
	public static void savePrefs()
	{
		try 
		{
			FileOutputStream out = new FileOutputStream(new File ("config.cfg"));
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			
			objOut.writeByte(rootDirectory.length());
			objOut.writeChars(rootDirectory);
			
			//Close streams
			objOut.flush();
			objOut.close();
			out.flush();
			out.close();
			
			System.out.println("Saved");
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}
	
	//Load preferences
	public static void loadPrefs()
	{
		try 
		{
			//
			FileInputStream in = new FileInputStream(new File ("config.cfg"));
			ObjectInputStream objIn = new ObjectInputStream(in);
			
			//Read number of characters and prepare empty string for storage.
			String path = "";
			int size = objIn.readByte();
			
			//Read each character
			for (int i=0; i<size; i++)
				path += objIn.readChar();
			
			rootDirectory = path;
			System.out.println("Loaded ::: " + path);
			
			//Close streams
			objIn.close();
			in.close();
		} 
		catch (FileNotFoundException e) 
		{
			
			e.printStackTrace();
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}
	
	//Converts the file path passed in to a relative file path, with forward slashes
	public static String ConvertImgPath(String path)
	{
		int i = 0;
		String newPath = "";
		
		//Make relative
		while (i < rootDirectory.length() && path.charAt(i) == rootDirectory.charAt(i))
		{
			i++;
		}
		
		//Change slashes
		path = path.substring(i);
		for (int j=0; j < path.length(); j++)
		{
			if (path.charAt(j) == '\\')
			{
				newPath += "/";
			}
			else
			{
				newPath += path.charAt(j);
			}
		}
		
		return newPath;
	}
	
	//Getters and setters
	public static String getRootDirectory() 
	{
		return rootDirectory;
	}

	public static void setRootDirectory(String rootDirectory) 
	{
		Handler.rootDirectory = rootDirectory;
	}
}
