package org.astrogrid.taverna.astrogrid_taverna_suite.ui.serviceprovider;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.workbench.activityicons.ActivityIconSPI;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import org.astrogrid.taverna.astrogrid_taverna_suite.DSAActivity;

public class DSAServiceIcon implements ActivityIconSPI {

	private static Icon icon;

	public int canProvideIconScore(Activity<?> activity) {
		if (activity instanceof DSAActivity) {
			return DEFAULT_ICON;
		}
		return NO_ICON;
	}

	public Icon getIcon(Activity<?> activity) {
		return getIcon();
	}
	
	public static Icon getIcon() {
		if (icon == null) {
			icon = new ImageIcon(DSAServiceIcon.class.getResource("/exampleIcon.png"));
		}
		return icon;
	}

}