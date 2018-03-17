package wizards.sprites;

import javax.swing.JPanel;

import org.jdom2.Element;

public class NewSpritePage extends JPanel{
	protected boolean formCompleted = false;
	protected Element spriteElement;
	
	public boolean formComplete() {
		return true;
	}
	
	public Element getSpriteData()
	{
		return null;
	}
}
