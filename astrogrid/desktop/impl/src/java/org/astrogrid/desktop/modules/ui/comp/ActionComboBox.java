package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventComboBoxModel;

/** A combo box that displays a list of Actions, and delegates selection
 * of an item to the 'actionPerformed' method of that action
 * 
 * also, list of actions is an <i>EventList</i>, which means that actions
 * can be added / removed / reordered, and the JComboBox will update automatically.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 29, 200711:42:17 PM
 */
public final class ActionComboBox extends JComboBox {
	/**
	 * @param actions new list of actions.
	 */
	public ActionComboBox(EventList actions) {
		super(new EventComboBoxModel(actions));
		setEditable(false);
		setSelectedIndex(0);

		// renderer that displays Actions.
		setRenderer(new BasicComboBoxRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(list.getSelectionForeground());
				} else {
					setBackground(list.getBackground());
					setForeground(list.getForeground());
				}
				Action a = (Action)value;
				setIcon((Icon)a.getValue(Action.SMALL_ICON));
				setText((String)a.getValue(Action.NAME));
				return this;

			}
		});
		// delegate to the action implementation itself
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((Action)getSelectedItem()).actionPerformed(e);
			}
		});
	}
}