/**
 * 
 */
package org.astrogrid.taverna.arvohttp;

import java.util.Iterator;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;

import org.astrogrid.acr.ACRException;
import org.embl.ebi.escience.scuflui.workbench.Scavenger;
import org.embl.ebi.escience.scuflui.workbench.ScavengerCreationException;

/** @todo work out how to remove remaining life science scavengers.
 * @todo work out how to get this scavenger listed at the top level. - as it's not really a local service - only a minor poblem for now.
 * think this is usually indicated by implementing ScavengerHelper (which allows for discovery of new services). But this isn't really appropriate for acr either.
 * @author Noel Winstanley
 * @since May 24, 20065:32:30 PM
 */
public class ARScavenger extends Scavenger {

	private static Logger logger = Logger.getLogger(ARScavenger.class);
	/**
	 * @param arg0
	 * @throws ScavengerCreationException 
	 */
	public ARScavenger() throws ScavengerCreationException {
		super("Astro Runtime VOHTTP");
		logger.debug("start constructor in ARScavenger");
		listVOHTTPS();
	}
	
	
	/** alternate implementation of listApi */
	private void listVOHTTPS() throws ScavengerCreationException {
		//try {
			logger.debug("start listApi in ARScavenger");
			//DefaultMutableTreeNode root = new DefaultMutableTreeNode("VO HTTP Services");
			DefaultMutableTreeNode siapNode = 
				new DefaultMutableTreeNode(new ARProcessorFactory("SIAP","SIAP"));
			DefaultMutableTreeNode stapNode = 
				new DefaultMutableTreeNode(new ARProcessorFactory("STAP","STAP"));
			DefaultMutableTreeNode coneNode = 
				new DefaultMutableTreeNode(new ARProcessorFactory("CONE","CONE"));
			DefaultMutableTreeNode ssapNode = 
				new DefaultMutableTreeNode(new ARProcessorFactory("SSAP","SSAP"));

			DefaultMutableTreeNode siapRegNode = 
				new DefaultMutableTreeNode(new ARProcessorFactory("SIAP_RegQuery","SIAP_RegQuery"));
			DefaultMutableTreeNode coneRegNode = 
				new DefaultMutableTreeNode(new ARProcessorFactory("Cone_RegQuery","Cone_RegQuery"));
			DefaultMutableTreeNode ssapRegNode = 
				new DefaultMutableTreeNode(new ARProcessorFactory("SSAP_RegQuery","SSAP_RegQuery"));
			DefaultMutableTreeNode stapRegNode = 
				new DefaultMutableTreeNode(new ARProcessorFactory("STAP_RegQuery","STAP_RegQuery"));
			
			add(siapNode);
			add(coneNode);
			add(stapNode);	
			add(ssapNode);	
			add(siapRegNode);
			add(coneRegNode);	
			add(ssapRegNode);	
			add(stapRegNode);	
			//add(root);
			logger.debug("end listVOHTTPS successful ARScavenger");
		/*} catch (ACRException x) {
			throw new ScavengerCreationException("Failed to list components of AR" + x.getMessage());
		}	*/
	}

}