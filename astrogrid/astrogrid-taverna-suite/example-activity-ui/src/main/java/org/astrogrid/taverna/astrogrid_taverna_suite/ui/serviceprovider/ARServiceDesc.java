package org.astrogrid.taverna.astrogrid_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.swing.Icon;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;

import org.astrogrid.taverna.astrogrid_taverna_suite.CEAActivity;
import org.astrogrid.taverna.astrogrid_taverna_suite.CEAActivityConfigurationBean;

public class ARServiceDesc extends ServiceDescription<CEAActivityConfigurationBean> {

	/**
	 * The subclass of Activity which should be instantiated when adding a service
	 * for this description 
	 */
	@Override
	public Class<? extends Activity<CEAActivityConfigurationBean>> getActivityClass() {
		return CEAActivity.class;
	}

	/**
	 * The configuration bean which is to be used for configuring the instantiated activity.
	 * Making this bean will typically require some of the fields set on this service
	 * description, like an endpoint URL or method name. 
	 * 
	 */
	@Override
	public CEAActivityConfigurationBean getActivityConfiguration() {
		CEAActivityConfigurationBean bean = new CEAActivityConfigurationBean();
		//bean.setCeaInterfaceName(this.ceaInterfaceName);
		//bean.setCeaIvorn(this.ceaIvorn);
		//bean.setExampleString(exampleString);
		//bean.setExampleUri(exampleUri);
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
	
	
	// FIXME: Replace example fields and getters/setters with any required
	// and optional fields. (All fields are searchable in the Service palette,
	// for instance try a search for exampleString:3)
	/*
	private String exampleString;
	private URI exampleUri;
	public String getExampleString() {
		return exampleString;
	}
	public URI getExampleUri() {
		return exampleUri;
	}
	public void setExampleString(String exampleString) {
		this.exampleString = exampleString;
	}
	public void setExampleUri(URI exampleUri) {
		this.exampleUri = exampleUri;
	}
	*/

}
