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

	public DecSexToggle() {
		sexaRadio = new JRadioButton("Sexagesimal");
		sexaRadio.addActionListener(this);
		degreesRadio = new JRadioButton("Degrees");
		degreesRadio.addActionListener(this);
		add(degreesRadio);
		add(sexaRadio);
		setDegrees(true);
	}

	/** returns true if 'degress' is currently selected */
	public boolean isDegrees() {
		return isSelected(getDegreesRadio().getModel());
	}

	/** set radioGroup value.
	 * Takes care of firing appropriate notifications to update ui.
	 * 
	 * @param isDegrees true for degrees, false for sexagesimal.
	 */
	public void setDegrees(boolean isDegrees) {
		if (isDegrees) {
			setSelected(degreesRadio.getModel(), true);
		} else {
			setSelected(sexaRadio.getModel(), true);
		}
	}

	private JRadioButton degreesRadio;

	private JRadioButton sexaRadio;

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
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == degreesRadio) {
			fireDegreesSelected();
		} else {
			fireSexaSelected();
		}
	}
	/**
	 * 
	 */
	private void fireDegreesSelected() {
		EventObject e = new EventObject(this);
		for (Iterator i = listeners.iterator(); i.hasNext(); ){
			((DecSexListener)i.next()).degreesSelected(e);
		}
	}

	/**
	 * 
	 */
	private void fireSexaSelected() {
		EventObject e = new EventObject(this);
		for (Iterator i = listeners.iterator(); i.hasNext(); ){
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

	/** add a listener to the toggle */
	public void addListener(DecSexListener l) {
		listeners.add(l);
	}

	/** remove a listener */
	public void removeListener(DecSexListener l) {
		listeners.remove(l);
	}



}
