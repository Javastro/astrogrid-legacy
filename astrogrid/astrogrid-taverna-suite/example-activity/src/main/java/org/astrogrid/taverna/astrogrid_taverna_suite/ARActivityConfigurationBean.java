package org.astrogrid.taverna.astrogrid_taverna_suite;

import java.io.Serializable;
import java.net.URI;

import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;

/**
 * Example activity configuration bean.
 * 
 */
public class ARActivityConfigurationBean implements Serializable {

	/*
	 * TODO: Remove this comment.
	 * 
	 * The configuration specifies the variable options and configurations for
	 * an activity that has been added to a workflow. For instance for a WSDL
	 * activity, the configuration contains the URL for the WSDL together with
	 * the method name. String constant configurations contain the string that
	 * is to be returned, while Beanshell script configurations contain both the
	 * scripts and the input/output ports (by subclassing
	 * ActivityPortsDefinitionBean).
	 * 
	 * Configuration beans are serialised as XML (currently by using XMLBeans)
	 * when Taverna is saving the workflow definitions. Therefore the
	 * configuration beans need to follow the JavaBeans style and only have
	 * fields of 'simple' types such as Strings, integers, etc. Other beans can
	 * be referenced as well, as long as they are part of the same plugin.
	 */
	
	// TODO: Remove the example fields and getters/setters and add your own	
	private String exampleString;
	
	private ModuleDescriptor md;
	
	private  ComponentDescriptor cd;
	
	private MethodDescriptor methD;

	private URI exampleUri;

	public String getExampleString() {
		return exampleString;
	}
	//ModuleDescriptor md, ComponentDescriptor cd, MethodDescriptor methD
	
	public ModuleDescriptor getModuleDescriptor() {
		return md;
	}
	
	public void setModuleDescriptor(ModuleDescriptor md) {
		this.md = md;
	}

	
	public ComponentDescriptor getComponentDescriptor() {
		return cd;
	}
	
	public void setModuleDescriptor(ComponentDescriptor cd) {
		this.cd = cd;
	}
	
	public MethodDescriptor getMethodDescriptor() {
		return methD;
	}
	
	public void setMethodDescriptor(MethodDescriptor methD) {
		this.methD = methD;
	}


	public void setExampleString(String exampleString) {
		this.exampleString = exampleString;
	}

	public URI getExampleUri() {
		return exampleUri;
	}

	public void setExampleUri(URI exampleUri) {
		this.exampleUri = exampleUri;
	}

}
