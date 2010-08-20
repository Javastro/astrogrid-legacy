package org.astrogrid.taverna.astrogrid_taverna_suite.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;

import org.astrogrid.taverna.astrogrid_taverna_suite.CEAActivity;
import org.astrogrid.taverna.astrogrid_taverna_suite.CEAActivityConfigurationBean;

@SuppressWarnings("serial")
public class CEAConfigureAction
		extends
		ActivityConfigurationAction<CEAActivity, CEAActivityConfigurationBean> {

	public CEAConfigureAction(CEAActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<CEAActivity, CEAActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		CEAConfigurationPanel panel = new CEAConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<CEAActivity, CEAActivityConfigurationBean> dialog = new ActivityConfigurationDialog<CEAActivity, CEAActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
