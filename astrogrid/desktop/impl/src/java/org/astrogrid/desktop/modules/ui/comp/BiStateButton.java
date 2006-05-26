package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.Action;
import javax.swing.JButton;

/** 2-state button - when button is pressed, flip states - displaying different
 * text, icon, etc. associated action, etc.
 * @author Noel Winstanley
 * @since Apr 26, 20063:51:19 PM
 */
public class BiStateButton extends JButton {
	
	public BiStateButton(Action a, Action b){
		this.a = a;
		this.b = b;
		enableA();
	}
	
	protected final Action a;
	protected final Action b;

	private boolean aLastPressed;

	public boolean wasALastPressed() {
		return aLastPressed;
	}
		
	
	public void enableA() {
		b.setEnabled(false);
		a.setEnabled(true);
		setAction(a);
		aLastPressed = false;
		
	}

	public void enableB() {
		a.setEnabled(false);
		b.setEnabled(true);
		setAction(b);
		aLastPressed=true;
	}

	public void flip() {
		if (aLastPressed) {
			enableA();
		} else {
			enableB();
		}
	}


}