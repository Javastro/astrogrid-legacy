package org.astrogrid.activities.ar.query;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sf.taverna.t2.partition.AbstractActivityItem;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.astrogrid.activities.ar.AstrogridRuntimeActivityConfigurationBean;
import org.astrogrid.activities.ar.AstrogridRuntimeActivity;
import org.astrogrid.acr.builtin.ModuleDescriptor;


public class AstrogridRuntimeActivityItem extends AbstractActivityItem {
	
	private ModuleDescriptor md = null;
	
	private String operation;
	private String category;
	
	
	
	public AstrogridRuntimeActivityItem(ModuleDescriptor md) {
		this.md = md;
	}

	@Override
	public Icon getIcon() {
		return new ImageIcon(AstrogridRuntimeActivityItem.class.getResource("/astrogrid.png"));
	}
	
	@Override
	public Object getConfigBean() {
		AstrogridRuntimeActivityConfigurationBean bean = new AstrogridRuntimeActivityConfigurationBean();
		bean.setModuleDescriptor(md);
		//do a set of the method here
		return bean;
	}
	
	/**
	 * Returns a {@link BeanshellActivity} which represents this local worker
	 */
	@Override
	public Activity<?> getUnconfiguredActivity() {
		Activity<?> activity = new AstrogridRuntimeActivity();
		return activity;
	}
	
	public String getType() {
		return "Astrogrid Runtime";
	}
	
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	

	@Override
	public String toString() {
		return getType();
	}
	
	
	
}
