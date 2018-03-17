package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlLoader {
	
	//Reference documents to keep loaded
	public static Document equipment;
	private static HashMap<String, Document> xmlDocs;
	//
	
	//Write a new XML file
	public static void writeXML(Document doc, String path)
	{	
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try 
		{
			out.output(doc, new FileOutputStream(new File(path)));
			System.out.println("Wrote to : " + path);
			
			//we need to reload the file after we write to it
			doc = readXML(path, true);
			xmlDocs.put(doc.getBaseURI(), doc);
		} 
		
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Overwrite an xml document
	public static void writeXML(Document doc)
	{
		String s = doc.getBaseURI().replaceAll("%20", " ").substring(6);
		System.out.println(s);
		writeXML(doc, s);
	}
	
	//Read from a file
	public static Document readXML(File f)
	{
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		
		try
		{
			doc = builder.build(f);
			
		}
		catch (Exception e)
		{
			doc = null;
			e.printStackTrace();
		}
		
		return doc;
	}
	
	//Get / reload a stored document
	public static Document readXML(String path, boolean reload)
	{
		if (xmlDocs == null)
			xmlDocs = new HashMap<String, Document>();
		
		//Try to get the stored document
		Document doc = xmlDocs.get(path);
		
		//Check if we need to load, or reload the file
		if (reload || doc == null)
		{
			//Try to read from the file
			doc = readXML(new File(path));
			System.out.println("File reloaded");
		}
		else
			System.out.println("File NOT reloaded");
		
		xmlDocs.put(path, doc);
		
		return doc;
	}
	
	//Get a stored document (no reload)
	public static Document readXML(String path)
	{
		return readXML(path, false);
	}
	
	public static void init()
	{
		
	}

}
