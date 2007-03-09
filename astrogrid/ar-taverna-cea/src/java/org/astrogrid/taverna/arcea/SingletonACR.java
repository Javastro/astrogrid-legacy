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
	public static synchronized ACR getACR() throws ACRException {
		if (theInstance== null) {
			Finder f = new Finder();
			theInstance = f.find();
		}
		return theInstance;
	}
	
	public static synchronized Hashtable listApps() throws ACRException {
		if (appInfo == null) {
			ACR acr = getACR();
			Applications apps = (Applications)acr.getService(Applications.class);
			URI []appList = apps.list();
			Hashtable appInfo = new Hashtable();
			ArrayList val;
			for(int i = 0;i < appList.length;i++) {
				try {
					Service []providers = apps.listServersProviding(appList[i]);
					if(providers.length > 0) {
						for(int j = 0;j < providers.length;j++) {
							CeaApplication cea = apps.getCeaApplication(appList[i]);
							if(appInfo.containsKey(providers[j].getId())) {
								val = (ArrayList)appInfo.get(providers[j].getId());
								val.add(cea);
								appInfo.put(providers[j].getId(), val);
							}else {
								val = new ArrayList();
								val.add(cea);
								appInfo.put(providers[j].getId(),val);
							}
						}//for
					}
				}catch(NotFoundException nfe) {
					//No service provider for it so just ignore/skip					
				}//catch
			}//for
		}//if
		return appInfo;
	}
	
}
