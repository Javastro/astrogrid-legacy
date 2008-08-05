/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 * a Decimal Degreee / Sexagesimal
 * Toggle.
 * 
 * provides configured ui components (adio buttons) - clients of this class need to access them
 * and add them into their own ui.
 * 
 * 
 * @author Noel Winstanley
 * @since May 15, 20066:18:48 PM
 */
public class DecSexToggle extends ButtonGroup implements ActionListener {

    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(DecSexToggle.class);
    private static final String PREFER_DECIMAL_KEY = "prefer.decimal";
   
	public DecSexToggle() {
		sexaRadio = new JRadioButton("Sexagesimal");
		sexaRadio.addActionListener(this);
		degreesRadio = new JRadioButton("Degrees");
		degreesRadio.addActionListener(this);
		add(degreesRadio);
		add(sexaRadio);
		if (PREFERENCES.getBoolean(PREFER_DECIMAL_KEY,true)) {
        	setSelected(degreesRadio.getModel(), true);
        } else {
        	setSelected(sexaRadio.getModel(), true);
        }

	}

	/** returns true if 'degress' is currently selected */
	public boolean isDegrees() {
		return isSelected(getDegreesRadio().getModel());
	}

	private final JRadioButton degreesRadio;

	private final JRadioButton sexaRadio;

	/** access the radio button UI component for degrees */
	public JRadioButton getDegreesRadio() {

		return degreesRadio;
	}

	/** access the radio bytton UI component for sexagesimal */
	public JRadioButton getSexaRadio() {

		return sexaRadio;
	}

	/** used internall. 
	 * lifts button-clicking events to own listener interface */
	
	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == degreesRadio) {
			fireDegreesSelected();
		} else {
			fireSexaSelected();
		}
        PREFERENCES.putBoolean(PREFER_DECIMAL_KEY,e.getSource() == degreesRadio);		
	}
	/**
	 * 
	 */
	private void fireDegreesSelected() {
		final EventObject e = new EventObject(this);
		for (final Iterator i = listeners.iterator(); i.hasNext(); ){
			((DecSexListener)i.next()).degreesSelected(e);
		}
	}

	/**
	 * 
	 */
	private void fireSexaSelected() {
		final EventObject e = new EventObject(this);
		for (final Iterator i = listeners.iterator(); i.hasNext(); ){
			((DecSexListener)i.next()).sexaSelected(e);
		}	
	}
	/** listener interface. Implement and register to be notified when user sleects 'degrees' or 'sexagesimal' */
	public static  interface DecSexListener extends EventListener {
		/** called to notify degrees selected
		 * @param e event object whose source will be this DecSexToggle */
		public void degreesSelected(EventObject e);

		/** called to notify sexagesimal selected 
		 * @param e event object whos source will be this dec sex toggle
		 * */
		public void sexaSelected(EventObject e);
	}

	private final Set listeners = new HashSet();

	/** add a listener to the toggle - and fire a selection message to it straight away,
	 * so it knows what the current preference is.*/
	public void addListener(final DecSexListener l) {
		listeners.add(l);
		if (isDegrees()) {
		    l.degreesSelected(new EventObject(this));
		} else {
		    l.sexaSelected(new EventObject(this));
		}
	}

	/** remove a listener */
	public void removeListener(final DecSexListener l) {
		listeners.remove(l);
	}



}
