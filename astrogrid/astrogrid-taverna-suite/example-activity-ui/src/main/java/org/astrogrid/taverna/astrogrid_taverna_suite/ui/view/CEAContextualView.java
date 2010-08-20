package org.astrogrid.taverna.astrogrid_taverna_suite.ui.view;

import java.awt.Frame;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;

import org.astrogrid.taverna.astrogrid_taverna_suite.CEAActivity;
import org.astrogrid.taverna.astrogrid_taverna_suite.CEAActivityConfigurationBean;
import org.astrogrid.taverna.astrogrid_taverna_suite.ui.config.CEAConfigureAction;

@SuppressWarnings("serial")
public class CEAContextualView extends ContextualView {
	private final CEAActivity activity;
	private JLabel description = new JLabel("ads");

	public CEAContextualView(CEAActivity activity) {
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
		CEAActivityConfigurationBean configuration = activity.getConfiguration();
		//return "Example service " + configuration.getExampleString();
		return "Astrogrid CEA";
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		CEAActivityConfigurationBean configuration = activity
				.getConfiguration();
		description.setText("CEA ");
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
		return new CEAConfigureAction(activity, owner);
	}

}
