package org.astrogrid.taverna.astrogrid_taverna_suite.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.astrogrid.taverna.astrogrid_taverna_suite.DSAActivity;
import org.astrogrid.taverna.astrogrid_taverna_suite.DSAActivityConfigurationBean;

@SuppressWarnings("serial")
public class DSAConfigureAction
		extends
		ActivityConfigurationAction<DSAActivity, DSAActivityConfigurationBean> {

	public DSAConfigureAction(DSAActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<DSAActivity, DSAActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		DSAConfigurationPanel panel = new DSAConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<DSAActivity, DSAActivityConfigurationBean> dialog = new ActivityConfigurationDialog<DSAActivity, DSAActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
