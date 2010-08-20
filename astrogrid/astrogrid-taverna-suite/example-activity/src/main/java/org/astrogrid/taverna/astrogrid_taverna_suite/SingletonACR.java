/**
 * 
 */
package org.astrogrid.taverna.astrogrid_taverna_suite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.system.ApiHelp;
import org.astrogrid.acr.builtin.ModuleDescriptor;
//import org.astrogrid.taverna.arcea.Myspace;
//import org.astrogrid.taverna.arcea.ResourceChooser;

import java.util.Hashtable;
import java.util.ArrayList;
import java.net.URI;

/** Singleton static that contains a reference to ACR.
 * seems only way to share context object between processor / factory / etc.
 * @todo replace with a better-architected solution - in particular, something
 * that will reconnect to ACR if connection was lost.
 * @author Noel Winstanley
 * @since May 30, 20064:36:02 PM
 */
public class SingletonACR {

	private SingletonACR() {
	}
	
	private static ACR theInstance;
	private static Hashtable appInfo;
	private static ModuleDescriptor[] modules;
	
	public static synchronized ACR getACR() throws ACRException {
		if (theInstance== null) {
			Finder f = new Finder();
			theInstance = f.find();
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
	
	/*
	public static synchronized URI getURI(String title) throws ACRException {
		ACR acr = getACR();
		ResourceChooser rc = (ResourceChooser)acr.getService(ResourceChooser.class);
		URI chosenRes = rc.chooseResource(title,true);
		return chosenRes;
	}
	
	public static synchronized Myspace getMyspace() throws ACRException {
		ACR acr = getACR();
		Myspace myspace = (Myspace)acr.getService(Myspace.class);
		return myspace;
	}
	*/
		
}
