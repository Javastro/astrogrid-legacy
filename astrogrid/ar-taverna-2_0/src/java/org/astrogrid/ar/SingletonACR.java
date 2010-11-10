package org.astrogrid.ar;

import java.net.URI;
import java.util.Hashtable;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.system.ApiHelp;


public class SingletonACR {
	
//private static Logger logger = Logger.getLogger(SingletonACR.class);
	
	private static ModuleDescriptor[] modules;
	
	
	private SingletonACR() {
		//logger.warn("SingletonACR constructor");
	}
	
	private static ACR theInstance;
	//private static Hashtable appInfo;
	public static synchronized ACR getACR() throws ACRException {
		if (theInstance== null) {
			Finder f = new Finder();
			theInstance = f.find();
		}
		return theInstance;
	}
	
	public static synchronized ModuleDescriptor[] listModules() throws ACRException {
		//logger.warn("2222listmodules called");
		if (modules == null) {
			//logger.warn("2222module null");
			ACR acr = getACR();
			//logger.warn("2222got an acr now get apihelp servvice");
			ApiHelp api = (ApiHelp)acr.getService(ApiHelp.class);
			//logger.warn("2222got the help so call listmodule descriptors");
			modules = api.listModuleDescriptors();
		}
		//logger.warn("2222done with list modles");
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
