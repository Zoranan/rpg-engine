package dev.zoranan.rpgengine.util;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

/*
 * This class loads XML files, returning the document object for them
 */

public class XmlLoader {
	
	//Reference documents to keep loaded
	public static Document equipment;
	//
	
	public static void writeXML()
	{
		Document doc = new Document();
	}
	
	public static Document readXML(String path)
	{
		SAXBuilder builder = new SAXBuilder();
		Document doc;
		
		try
		{
			//doc = builder.build(new File(path));
			doc = builder.build(XmlLoader.class.getResource(path));
			
		}
		catch (Exception e)
		{
			doc = null;
			e.printStackTrace();
		}
		
		return doc;
	}
}
