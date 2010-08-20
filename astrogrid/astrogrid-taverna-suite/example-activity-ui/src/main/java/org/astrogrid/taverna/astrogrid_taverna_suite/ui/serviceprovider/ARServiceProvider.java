package org.astrogrid.taverna.astrogrid_taverna_suite.ui.serviceprovider;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;


import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ExecutionInformation;

import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.taverna.astrogrid_taverna_suite.SingletonACR;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

public class ARServiceProvider implements ServiceDescriptionProvider {
	
	private static final URI providerId = URI
		.create("http://www.astrogrid.org/taverna/plugin/suite");
	
	/**
	 * Do the actual search for services. Return using the callBack parameter.
	 */
	@SuppressWarnings("unchecked")
	public void findServiceDescriptionsAsync(
			FindServiceDescriptionsCallBack callBack) {
		// Use callback.status() for long-running searches
		// callBack.status("Resolving example services");

		List<ServiceDescription> results = new ArrayList<ServiceDescription>();

		try {
			ACR acr = SingletonACR.getACR();
			ModuleDescriptor[] modules = SingletonACR.listModules();
			for (int i =0 ; i < modules.length; i++) {
				ComponentDescriptor[] components = modules[i].getComponents();
				for (int j = 0; j < components.length; j++) {
					MethodDescriptor[] methods = components[j].getMethods();
					for (int k = 0; k < methods.length; k++) {
						ARServiceDesc arService = new ARServiceDesc();
						arService.setARPath(new String[]{modules[i].getName(),components[j].getName()});
						arService.setARName(methods[k].getName());
						
					}
					
				}
			}
		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		// partialResults() can also be called several times from inside
		// for-loop if the full search takes a long time
		callBack.partialResults(results);
		// No more results will be coming
		callBack.finished();
	}

	/**
	 * Icon for service provider
	 */
	public Icon getIcon() {
		return ExampleServiceIcon.getIcon();
	}

	/**
	 * Name of service provider, appears in right click for 'Remove service
	 * provider'
	 */
	public String getName() {
		return "Astrogrtid AR";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getId() {
		return providerId.toASCIIString();
	}

}
