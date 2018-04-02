package editors.compoundObjects;

import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdom2.Element;

import java.util.ArrayList;

import javax.swing.JScrollPane;

public class PropertiesList extends CompoundComponent{
	private JTable table;
	private JTable table_1;
	
	public PropertiesList(String labelTxt, String nodeName, String[] heading, String[][] data) 
	{
		super(labelTxt, nodeName);
		SpringLayout springLayout = (SpringLayout) getLayout();
		
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.NORTH, lblLabel);
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 6, SpringLayout.EAST, lblLabel);
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, 100, SpringLayout.SOUTH, lblLabel);
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, 200, SpringLayout.EAST, lblLabel);
		add(scrollPane);
		
		table_1 = new JTable(data, heading);
		table_1.setFillsViewportHeight(true);
		scrollPane.setViewportView(table_1);
		height = 120;
	}
	
	public ArrayList<String> getList()
	{
		TableModel model = table_1.getModel();
		ArrayList<String> values = new ArrayList<String>();
		
		for (int i = 0; i < model.getRowCount(); i++)
			values.add(model.getValueAt(i, 1).toString());
		
		return values;
	}
}
