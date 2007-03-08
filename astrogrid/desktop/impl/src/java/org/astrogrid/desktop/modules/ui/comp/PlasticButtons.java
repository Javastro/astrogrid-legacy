package org.astrogrid.desktop.modules.ui.comp;

import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.apache.commons.collections.ListUtils;
import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;

import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.CompositeList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.JEventListPanel;

/** consumes a list of plastic application descriptions and 
 * produces a panel with buttons for functionality for each app.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 7, 200710:25:02 AM
 */
public class PlasticButtons  implements CollectionList.Model{
	/** defines what buttons are built from each application */
	public interface ButtonBuilder {
		/** construct the buttons for this application */
		JButton[] buildPlasticButtons(PlasticApplicationDescription plas);
	}
	/** part of the internal implementation */
	public List getChildren(Object sourceValue) {
		PlasticApplicationDescription plas= (PlasticApplicationDescription)sourceValue;
		Component[] butts = builder.buildPlasticButtons(plas);
		if (butts == null) {
			return ListUtils.EMPTY_LIST;
		}
		switch(butts.length) {
		case 0:
			return Collections.EMPTY_LIST;
		case 1:
			return Collections.singletonList(butts[0]);
		default:
			return Arrays.asList(butts);
		}
	}
	/**
	 * Create a default plastic buttons
	 * @param plasticApps the event list of plastic application descirptions
	 */
	public PlasticButtons(EventList plasticApps, ButtonBuilder builder) {
		this(plasticApps,builder,new ButtonPanelFormat());
	}
	/**
	 * 
	 * @param plasticApps list of application descriptions.
	 * @param format defines the format of this panel.
	 */
	public PlasticButtons(EventList plasticApps,ButtonBuilder builder, JEventListPanel.Format format) {
		this.builder = builder;
		EventList dynamic = new CollectionList(plasticApps,this);

		//create a list of buttons that are always there. 
		CompositeList buttonList = new CompositeList(dynamic.getPublisher(),dynamic.getReadWriteLock());		// then display this list in a panel.
		stat = buttonList.createMemberList();

		// compose both lists together.
		buttonList.addMemberList(stat);
		buttonList.addMemberList(dynamic);
		panel = new JEventListPanel(buttonList,format);
	}
	private final JEventListPanel panel;
	private final EventList stat;
	private final ButtonBuilder builder;
	/** expose the static component of this list- allows you to add additional buttons which will always appear */
	public EventList getStaticList() {
		return stat;
	}
	/** access the constructed panel */
	public JEventListPanel getPanel() {
		return panel;
	}
/** predefined format for the panel */
public static class ButtonPanelFormat extends JEventListPanel.AbstractFormat {
	public ButtonPanelFormat() {
		super("0dlu,pref,0dlu","0dlu,fill:pref:grow,0dlu"
				,"4dlu","4dlu"
				, new String[] {"2,2"}
		);
	}
	public JComponent getComponent(Object arg0, int arg1) {
		return (JComponent)arg0;
	}
	public int getComponentsPerElement() {
		return 1;
	}
}
}