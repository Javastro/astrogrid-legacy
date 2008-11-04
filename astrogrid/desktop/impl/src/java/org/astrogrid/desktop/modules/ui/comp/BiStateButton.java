package org.astrogrid.desktop.modules.ui.comp;

import javax.swing.Action;
import javax.swing.JButton;

/** 2-state button - when button is pressed, flip states. -
 * Each state displays different
 * text, icon, etc. associated action, etc.
 * @author Noel Winstanley
 * @since Apr 26, 20063:51:19 PM
 */
public class BiStateButton extends JButton {
	
	public BiStateButton(final Action a, final Action b){
		this(a,b,false);
	}
	
	public BiStateButton(final Action a, final Action b, final boolean iconOnly){
		this.a = a;
		this.b = b;
		enableA();
		this.iconOnly = iconOnly;
	}
	
	protected final Action a;
	protected final Action b;
	protected final boolean iconOnly;
	private boolean aLastPressed;

	public boolean wasALastPressed() {
		return aLastPressed;
	}
		
	
	public void enableA() {
		b.setEnabled(false);
		a.setEnabled(true);
		setAction(a);
		if (iconOnly) {
			setText(null);
		}
		aLastPressed = false;
		
	}

	public void enableB() {
		a.setEnabled(false);
		b.setEnabled(true);
		setAction(b);
		if (iconOnly) {
			setText(null);
		}
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