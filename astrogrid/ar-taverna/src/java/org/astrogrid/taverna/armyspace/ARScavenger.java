/**
 * 
 */
package org.astrogrid.taverna.armyspace;

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
		super("Astro Runtime VOSpace");
		logger.warn("start constructor in ARScavenger");
		listMyspaceProcessors();
	}
	
	
	/** alternate implementation of listApi */
	private void listMyspaceProcessors() throws ScavengerCreationException {
		//try {
			logger.warn("start listMyspaceProcessors in ARScavenger");
			//DefaultMutableTreeNode root = new DefaultMutableTreeNode("Myspace");
			DefaultMutableTreeNode vospaceUserInfo = 
				new DefaultMutableTreeNode(new ARProcessorFactory("VOSpace-UserInfo","VOSpace-UserInfo"));
			DefaultMutableTreeNode myspaceSave = 
				new DefaultMutableTreeNode(new ARProcessorFactory("Save","Save"));
			DefaultMutableTreeNode myspaceVotableSave = 
				new DefaultMutableTreeNode(new ARProcessorFactory("Save_For_VOTables","Save_For_VOTables"));
			DefaultMutableTreeNode myspaceFetch = 
				new DefaultMutableTreeNode(new ARProcessorFactory("Fetch","Fetch"));
			//DefaultMutableTreeNode myspaceFetchString = 
			//	new DefaultMutableTreeNode(new ARProcessorFactory("Fetch_String_Content"));			
			add(vospaceUserInfo);
			add(myspaceSave);
			add(myspaceVotableSave);
			add(myspaceFetch);
			//root.add(myspaceFetchString);			
			//add(root);
			logger.warn("end listMyspaceProcessors successful ARScavenger");
		/*} catch (ACRException x) {
			throw new ScavengerCreationException("Failed to list components of AR" + x.getMessage());
		}	*/
	}

}