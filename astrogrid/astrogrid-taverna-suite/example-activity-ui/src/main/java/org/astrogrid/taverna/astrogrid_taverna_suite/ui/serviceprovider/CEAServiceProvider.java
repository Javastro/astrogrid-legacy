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

import org.astrogrid.taverna.astrogrid_taverna_suite.SingletonACR;

import net.sf.taverna.t2.servicedescriptions.ServiceDescription;
import net.sf.taverna.t2.servicedescriptions.ServiceDescriptionProvider;

public class CEAServiceProvider implements ServiceDescriptionProvider {
	
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

		String []ivornArr = new String[1];
		ivornArr[0] = "ivo://mssl.ucl.ac.uk/SolarMovieMaker";
		
		try {
			ACR acr = SingletonACR.getACR();
			//do some looping through all the apps and interface beans.
			Applications apps = (Applications)acr.getService(Applications.class);
			CeaApplication ai = apps.getCeaApplication(new java.net.URI((String)ivornArr[0]));
			// FIXME: Implement the actual service search/lookup instead
			// of dummy for-loop
			
			InterfaceBean []ib = ai.getInterfaces();
			for(int m = 0;m < ib.length;m++) {
				CEAServiceDesc ceaService = new CEAServiceDesc();
				ceaService.setCeaInterfaceName(ib[m].getName());
				ceaService.setCeaIvorn(ivornArr[0]);
				ceaService.setDescription("Astrogrid CEA");
				String tmpTitle = ai.getTitle();
				ceaService.setCeaTitle(ai.getTitle());
				results.add(ceaService);
			}						
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		/*
		for (int i = 1; i <= 5; i++) {
			ExampleServiceDesc service = new ExampleServiceDesc();
			// Populate the service description bean
			service.setExampleString("Example " + i);
			service.setExampleUri(URI.create("http://localhost:8192/service"));

			// Optional: set description
			service.setDescription("Service example number " + i);
			results.add(service);
		}
		*/

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
		return "Astrogrtid CEA";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getId() {
		return providerId.toASCIIString();
	}

}
