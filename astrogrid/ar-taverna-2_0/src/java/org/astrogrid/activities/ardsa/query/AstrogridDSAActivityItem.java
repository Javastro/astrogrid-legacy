package org.astrogrid.activities.ardsa.query;

import net.sf.taverna.t2.partition.AbstractActivityItem;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.astrogrid.activities.ardsa.AstrogridDSAActivityConfigurationBean;
import org.astrogrid.activities.ardsa.AstrogridDSAActivity;

import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;



public class AstrogridDSAActivityItem extends AbstractActivityItem  {
		
	@Override
	public Icon getIcon() {
		return new ImageIcon(AstrogridDSAActivityItem.class.getResource("/astrogrid.png"));
	}
	
	@Override
	public Object getConfigBean() {
		AstrogridDSAActivityConfigurationBean bean = new AstrogridDSAActivityConfigurationBean();
		
		return bean;
	}
	
	/**
	 * Returns a {@link BeanshellActivity} which represents this local worker
	 */
	@Override
	public Activity<?> getUnconfiguredActivity() {
		Activity<?> activity = new AstrogridDSAActivity();
		return activity;
	}
	
	public String getType() {
		return "Astrogrid DSA";
	}

	@Override
	public String toString() {
		return getType();
	}
		


}
