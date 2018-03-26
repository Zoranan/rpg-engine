package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TxtLoader {
	//private static HashMap<String>
	public TxtLoader() {
		
	}
	
	public static ArrayList<String> getTxtAsList(String path)
	{
		ArrayList<String> lines = new ArrayList<String>();
		//File inFile = null;
		InputStream is = null;
		try
		{
			is = new FileInputStream(new File(path));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		//read the lines
		try
		{
			InputStreamReader isReader = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isReader);
			String next = null;
			while ((next = reader.readLine()) != null)
				lines.add(next);
			
			reader.close();
			isReader.close();
			is.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return lines;
	}
	
	public static String[] getTxtAsArray (String path)
	{
		ArrayList<String> list = getTxtAsList(path);
		String[] array = new String[list.size()];
		
		for (int i=0; i < array.length; i++)
			array[i] = list.get(i);
		
		return array;
		
	}

}
