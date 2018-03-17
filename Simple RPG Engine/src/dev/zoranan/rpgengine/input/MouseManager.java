package dev.zoranan.rpgengine.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import dev.zoranan.rpgengine.gui.GUIManager;

public class MouseManager implements MouseListener, MouseMotionListener{

	private boolean leftPressed, rightPressed, leftToggled, rightToggled;
	private int mouseX, mouseY;
	private GUIManager guiManager;
	
	public MouseManager()
	{
		
	}
	
	public void setGuiManager (GUIManager guiManager)
	{
		this.guiManager = guiManager;
	}
	
	//Getters

	public boolean isLeftPressed() {
		return leftPressed;
	}
	
	public boolean isRightPressed() {
		return rightPressed;
	}
	
	public boolean isLeftToggled() {
		boolean b = leftToggled;
		leftToggled = false;
		return b;
	}

	public boolean isRightToggled() {
		boolean b = rightToggled;
		rightToggled = false;
		return b;
	}

	public int getMouseX() {
		return mouseX;
	}
	
	public int getMouseY() {
		return mouseY;
	}
	
	//Implemented Methods
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		
		if (guiManager != null)
			guiManager.onMouseMove(e);
		
		//Reset the toggles
		leftToggled = false;
		rightToggled = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			leftPressed = true;
			leftToggled = true;
		}
		
		else if (e.getButton() == MouseEvent.BUTTON3)
		{
			rightPressed = true;
			rightToggled = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			leftPressed = false;
		
		else if (e.getButton() == MouseEvent.BUTTON3)
			rightPressed = false;
		
		if (guiManager != null)
			guiManager.onMouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
