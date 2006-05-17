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
import javax.swing.JRadioButtonMenuItem;

/**
 * Class that has all the components needed for a Decimal Degreee Sexagesimal
 * Toggle.
 * 
 * provides configured ui components - clients of this class need to access them
 * and add them into their own ui.
 * 
 * @author Noel Winstanley
 * @since May 15, 20066:18:48 PM
 */
public class DecSexToggle extends ButtonGroup implements ActionListener {

	public DecSexToggle() {
		sexaRadio = new JRadioButtonMenuItem("Sexagesimal");
		sexaRadio.addActionListener(this);
		degreesRadio = new JRadioButtonMenuItem("Degrees");
		degreesRadio.addActionListener(this);
		add(degreesRadio);
		add(sexaRadio);
		setDegrees(true);
	}

	public boolean isDegrees() {
		return isSelected(getDegreesRadio().getModel());
	}

	public void setDegrees(boolean isDegrees) {
		if (isDegrees) {
			setSelected(degreesRadio.getModel(), true);
		} else {
			setSelected(sexaRadio.getModel(), true);
		}
	}

	private JRadioButtonMenuItem degreesRadio;

	private JRadioButtonMenuItem sexaRadio;

	public JRadioButtonMenuItem getDegreesRadio() {

		return degreesRadio;
	}

	public JRadioButtonMenuItem getSexaRadio() {

		return sexaRadio;
	}

	/** lifts button-clicking events to own listener interface */
	
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
	/** listener interface */
	public static  interface DecSexListener extends EventListener {
		/** called to notify degrees selected */
		public void degreesSelected(EventObject e);

		/** called to notify sexagesimal selected */
		public void sexaSelected(EventObject e);
	}

	private final Set listeners = new HashSet();

	public void addListener(DecSexListener l) {
		listeners.add(l);
	}

	public void removeListener(DecSexListener l) {
		listeners.remove(l);
	}



}
