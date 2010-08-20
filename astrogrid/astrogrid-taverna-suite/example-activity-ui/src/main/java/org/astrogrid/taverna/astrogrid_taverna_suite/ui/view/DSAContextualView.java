package org.astrogrid.taverna.astrogrid_taverna_suite.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.astrogrid.taverna.astrogrid_taverna_suite.DSAActivity;
import org.astrogrid.taverna.astrogrid_taverna_suite.DSAActivityConfigurationBean;
import org.astrogrid.taverna.astrogrid_taverna_suite.ui.config.DSAConfigureAction;

@SuppressWarnings("serial")
public class DSAContextualView extends ContextualView {
	private final DSAActivity activity;
	private JLabel description = new JLabel("ads");

	public DSAContextualView(DSAActivity activity) {
		this.activity = activity;
		initView();
	}

	@Override
	public JComponent getMainFrame() {
		JPanel jPanel = new JPanel();
		jPanel.add(description);
		refreshView();
		return jPanel;
	}

	@Override
	public String getViewTitle() {
		DSAActivityConfigurationBean configuration = activity.getConfiguration();
		//return "Example service " + configuration.getExampleString();
		return "Astrogrid DSA";
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		DSAActivityConfigurationBean configuration = activity
				.getConfiguration();
		description.setText("DSA2 ");
		// TODO: Might also show extra service information looked
		// up dynamically from endpoint/registry
	}

	/**
	 * View position hint
	 */
	@Override
	public int getPreferredPosition() {
		// We want to be on top
		return 100;
	}
	
	@Override
	public Action getConfigureAction(final Frame owner) {
		return new DSAConfigureAction(activity, owner);
	}

}
