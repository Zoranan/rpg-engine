package util;

import java.io.File;

import javax.swing.JFileChooser;

public class PersistentFileChooser extends JFileChooser{
	private static String lastDirectory = ".";

	public PersistentFileChooser(String title, boolean multiSelect) 
	{
		super();
		
		if (lastDirectory != ".")
			this.setCurrentDirectory(new File (lastDirectory));
		else if (lastDirectory != null)
			this.setCurrentDirectory(new File (Handler.getRootDirectory()));
		else
			this.setCurrentDirectory(new File ("."));
		
		this.setDialogTitle(title);
		this.setMultiSelectionEnabled(multiSelect);
		this.setFileSelectionMode(JFileChooser.OPEN_DIALOG);		
	}
	
	@Override
	public void approveSelection()
	{
		super.approveSelection();
		System.out.println("Approved");
		lastDirectory = this.getSelectedFile().getParentFile().getAbsolutePath();
	}
}
