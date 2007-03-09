/**
 * 
 */
package org.astrogrid.taverna.arcea;

import java.util.Iterator;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.embl.ebi.escience.scuflui.workbench.Scavenger;
import org.embl.ebi.escience.scuflui.workbench.ScavengerCreationException;

/** @todo work out how to remove remaining life science scavengers.
 * @todo work out how to get this scavenger listed at the top level. - as it's not really a local service - only a minor poblem for now.
 * think this is usually indicated by implementing ScavengerHelper (which allows for discovery of new services). But this isn't really appropriate for acr either.
 * @author Noel Winstanley
 * @since May 24, 20065:32:30 PM
 */
public class ARScavenger extends Scavenger {

	private static final Log logger = LogFactory.getLog(ARScavenger.class);
	/**
	 * @param arg0
	 * @throws ScavengerCreationException 
	 */
	public ARScavenger() throws ScavengerCreationException {
		super("Astro Runtime");
		logger.info("start constructor in ARScavenger");
		listApps();
	}
	
	
	/** alternate implementation of listApi */
	private void listApps() throws ScavengerCreationException {
		try {
			logger.info("start listApi in ARScavenger");
			Hashtable apps= SingletonACR.listApps();
			DefaultMutableTreeNode moduleNode = new DefaultMutableTreeNode("CEA Tree");
			DefaultMutableTreeNode serviceNode = new DefaultMutableTreeNode("Specific Service");
			DefaultMutableTreeNode allNode = new DefaultMutableTreeNode("All Apps");
			moduleNode.add(serviceNode);			
			moduleNode.add(allNode);
			add(moduleNode);
			String serviceId;
			ArrayList ceaList;
		    for (Enumeration e = apps.keys() ; e.hasMoreElements() ;) {
		    	serviceId = (String)e.nextElement();
		    	DefaultMutableTreeNode ceaServiceNode = new DefaultMutableTreeNode(serviceId.substring(6));
		    	ceaList = (ArrayList)apps.get(serviceId);
		    	Iterator appIterator = ceaList.iterator();
		    	while(appIterator.hasNext()) {
		    		CeaApplication cea = (CeaApplication)appIterator.next();
		    		InterfaceBean []ib = cea.getInterfaces();
		    		for(int j = 0;j < ib.length;j++) {
		    			//DefaultMutableTreeNode ceaNode = new DefaultMutableTreeNode();
						DefaultMutableTreeNode ceaNodeForService = 
							new DefaultMutableTreeNode(
								new ARProcessorFactory(cea.getId().toString(),serviceId,ib[j].getName())
									);			    			
		    			
						DefaultMutableTreeNode ceaNode = 
							new DefaultMutableTreeNode(
								new ARProcessorFactory(cea.getId().toString(),null, ib[j].getName())
									);
						serviceNode.add(ceaNodeForService);						
		    			allNode.add(ceaNode);
		    		}//for
		    	}//while
		    }//for
			logger.info("end listApi successful ARScavenger");
		} catch (ACRException x) {
			throw new ScavengerCreationException("Failed to list components of AR" + x.getMessage());
		}	
	}

}
