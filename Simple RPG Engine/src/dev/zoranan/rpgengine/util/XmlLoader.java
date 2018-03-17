package dev.zoranan.rpgengine.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

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
	
	public static void init()
	{
		//equipment = readXML("res/items/equipment/equipment.xml");
	}

}
