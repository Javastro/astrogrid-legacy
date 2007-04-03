/**
 * 
 */
package org.astrogrid.taverna.arcea;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.NotFoundException;
import java.util.Hashtable;
import java.util.ArrayList;
import java.net.URI;

import org.apache.log4j.Logger;


/** Singleton static that contains a reference to ACR.
 * seems only way to share context object between processor / factory / etc.
 * @todo replace with a better-architected solution - in particular, something
 * that will reconnect to ACR if connection was lost.
 * @author Noel Winstanley
 * @since May 30, 20064:36:02 PM
 */
public class SingletonACR {

	private static Logger logger = Logger.getLogger(SingletonACR.class);
	
	
	private SingletonACR() {
		logger.warn("SingletonACR constructor");
	}
	
	private static ACR theInstance;
	private static Hashtable appInfo;
	public static synchronized ACR getACR() throws ACRException {
		if (theInstance== null) {
			Finder f = new Finder();
			theInstance = f.find();
		}
		return theInstance;
	}	
}
