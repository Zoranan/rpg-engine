package dev.zoranan.rpgengine.gfx;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Text {
	public static enum H_align {LEFT, CENTER, RIGHT};
	public static enum V_align {ABOVE, CENTER, BELOW};
	
	private static H_align h_align = H_align.CENTER;
	private static V_align v_align = V_align.CENTER;
	
	public static void drawText(Graphics g, String text, int x, int y, boolean center, Color c, Font font)
	{
		g.setColor(c);
		g.setFont(font);
		
		FontMetrics fm = g.getFontMetrics(font);
		if (center || h_align == H_align.CENTER && v_align == V_align.CENTER)
		{
			x = (x - fm.stringWidth(text) / 2);
			y = (y - fm.getHeight() / 2) + fm.getAscent();
		}
		else
		{
			//Find horizontal alignment
			if (h_align == H_align.CENTER)
				x = (x - fm.stringWidth(text) / 2);
			else if (h_align == H_align.RIGHT)
				x = (x - fm.stringWidth(text));
			
			//Vertical alignment
			if (v_align == V_align.CENTER)
				y = (y - fm.getHeight() / 2) + fm.getAscent();
			else if (v_align == V_align.BELOW)
				y = (y - fm.getHeight()) + fm.getAscent();
		}
		g.drawString(text, x, y);
		
	}
	
	//Set alignments
	public static void setAlign(H_align h)
	{
		h_align = h;
	}
	public static void setAlign(V_align v)
	{
		v_align = v;
	}
	public static void setAlign(H_align h, V_align v)
	{
		h_align = h;
		v_align = v;
	}
}
