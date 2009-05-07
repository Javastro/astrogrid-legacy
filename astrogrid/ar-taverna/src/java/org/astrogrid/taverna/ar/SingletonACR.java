/**
 * 
 */
package org.astrogrid.taverna.ar;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.system.ApiHelp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.VODesktop;

/** Singleton static that contains a reference to ACR.
 * seems only way to share context object between processor / factory / etc.
 * @todo replace with a better-architected solution - in particular, something
 * that will reconnect to ACR if connection was lost.
 * @author Noel Winstanley
 * @since May 30, 20064:36:02 PM
 */
public class SingletonACR {
	
	private static final Log logger = LogFactory.getLog(SingletonACR.class);

	private SingletonACR() {
	}
	
	private static ACR theInstance;
	private static ModuleDescriptor[] modules;
	public static synchronized ACR getACR() throws ACRException {
		logger.warn("2222in getACR()");
		if (theInstance== null) {
			Finder f = new Finder();
			try {
				theInstance = f.find();
			}catch(ACRException ae) {
				logger.warn("Cannot find a connection to Astro Runtime Library");
				/*
				VODesktop.main(null);
				logger.warn("now try the find.");
				f = new Finder();
				theInstance = f.find();
				*/
			}
		}
		return theInstance;
	}
	
	public static synchronized ModuleDescriptor[] listModules() throws ACRException {
		if (modules == null) {
			ACR acr = getACR();
			ApiHelp api = (ApiHelp)acr.getService(ApiHelp.class);
			modules = api.listModuleDescriptors();
		}
		return modules;
	}
	
}
