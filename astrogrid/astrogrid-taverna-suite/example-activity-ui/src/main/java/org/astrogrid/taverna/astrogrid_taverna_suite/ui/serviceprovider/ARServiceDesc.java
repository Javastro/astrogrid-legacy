package org.astrogrid.taverna.astrogrid_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.astrogrid.taverna.astrogrid_taverna_suite.ARActivity;
import org.astrogrid.taverna.astrogrid_taverna_suite.ARActivityConfigurationBean;

import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;


public class ARServiceDesc extends ServiceDescription<ARActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<ARActivityConfigurationBean>> getActivityClass() {
		return ARActivity.class;
	}

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public ARActivityConfigurationBean getActivityConfiguration() {
		ARActivityConfigurationBean bean = new ARActivityConfigurationBean();
		bean.setModuleDescriptor(this.modD);
		bean.setMethodDescriptor(this.md);
		bean.setComponentDescriptor(this.cd);
		return bean;
	}

	/**
	 * An icon to represent this service description in the service palette.
	 */
	@Override
	public Icon getIcon() {
		return ExampleServiceIcon.getIcon();
	}

	/**
	 * The display name that will be shown in service palette and will
	 * be used as a template for processor name when added to workflow.
	 */
	@Override
	public String getName() {
		return this.arName;
	}

	/**
	 * The path to this service description in the service palette. Folders
	 * will be created for each element of the returned path.
	 */
	@Override
	public List<String> getPath() {
		// For deeper paths you may return several strings
		
		return Arrays.asList("Astrogrid", "AR", this.arPath[0], this.arPath[1]);
	}

	/**
	 * Return a list of data values uniquely identifying this service
	 * description (to avoid duplicates). Include only primary key like fields,
	 * ie. ignore descriptions, icons, etc.
	 */
	@Override
	protected List<? extends Object> getIdentifyingData() {
		// FIXME: Use your fields instead of example fields
		//return Arrays.<Object>asList(exampleString, exampleUri);
		return Arrays.<Object>asList("astroruntimeAR",arPath[0] + "." + arPath[1] + "." + arName);
	}

	private String ceaIvorn;
	
	private String[] arPath = new String[2];
	
	public String[] getARPath() {
		return arPath;
	}

	public void setARPath(String[] arPath) {
		this.arPath = arPath;
	}
	
private String arName;
	
	public String getARName() {
		return arName;
	}

	public void setARName(String arName) {
		this.arName = arName;
	}
	
	ModuleDescriptor modD;
	public void setModuleDescriptor(ModuleDescriptor modD) {
		this.modD = modD;
	}
	
	public ModuleDescriptor getModuleDescriptor() {
		return this.modD;
	}
	
	ComponentDescriptor cd;
	public void setComponentDescriptor(ComponentDescriptor cd) {
		this.cd = cd;
	}
	
	public ComponentDescriptor getComponentDescriptor() {
		return this.cd;
	}
	
	
	MethodDescriptor md;
	public void setMethodDescriptor(MethodDescriptor md) {
		this.md = md;
	}
	
	public MethodDescriptor getMethodDescriptor() {
		return this.md;
	}


}
