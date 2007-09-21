/**
 * 
 */
package org.astrogrid.desktop.modules.ui.comp;

import java.awt.BorderLayout;
import java.awt.geom.AffineTransform;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.prefs.Preferences;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/** A popup informaiton / warning dialogue,
 * that has a 'don't show this again' option.
 * 
 * the user's input for 'don't show this again' are stored in a java.util.Preference.
 * There's no integration with the rest of the workbench preferences system - as these
 * are lightweight, non-user-configurable options.
 * 
 * each subclass will have it's own 'don't show this again' preference key.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since May 16, 200710:42:02 AM
 */
public abstract class ShowOnceDialogue extends JDialog implements PropertyChangeListener, ChangeListener{
	/**
	 * 
	 */
	public ShowOnceDialogue(JFrame parent,String message) {
		super(parent,true);
		prefs = Preferences.userNodeForPackage(this.getClass());
		show = prefs.getBoolean(PREF_KEY,true);
		if (show) { // otherwise we're not going to show it, so no point..
			JPanel panel = new JPanel(new BorderLayout());

			JOptionPane p = new JOptionPane(message,JOptionPane.INFORMATION_MESSAGE);
			p.addPropertyChangeListener(JOptionPane.VALUE_PROPERTY,this);
			panel.add(p,BorderLayout.CENTER);
			
			opt = new JCheckBox("Don't show this message again");
			opt.setSelected(false);
			opt.addChangeListener(this);
			AffineTransform trans = AffineTransform.getScaleInstance(0.75,0.75);
			opt.setFont(opt.getFont().deriveFont(trans));
			panel.add(opt,BorderLayout.SOUTH);

			setContentPane(panel);
			setTitle("Information");
			pack();
		}		
	}
	private final Preferences prefs;
	private JCheckBox opt;
	private boolean show;
	private final static String PREF_KEY = "show";

	public void setVisible(boolean b) {
		if (show) {
			super.setVisible(b);
		}
	}
	public void show() {
		if (show) {
			super.show();
		}
	}
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.show();
		(new ShowOnceDialogue(f,"Hi"){}).show();
		System.exit(0);

	}

	public void propertyChange(PropertyChangeEvent evt) {
		setVisible(false);
		dispose();
	}
	public void stateChanged(ChangeEvent e) {
		show = ! opt.isSelected();
		prefs.putBoolean(PREF_KEY, show);
	}
}
