/**
 * 
 */
package org.astrogrid.taverna.ar;

import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.builtin.ComponentDescriptor;
import org.astrogrid.acr.builtin.MethodDescriptor;
import org.astrogrid.acr.builtin.ModuleDescriptor;
import org.astrogrid.acr.system.ApiHelp;
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
		logger.error("22start constructor in ARScavenger for Astro Runtime");
		listApi();
	}
	
	
	/** alternate implementation of listApi */
	private void listApi() throws ScavengerCreationException {
		try {
			ModuleDescriptor[] modules = SingletonACR.listModules();
			for (int i =0 ; i < modules.length; i++) {
				DefaultMutableTreeNode moduleNode = new DefaultMutableTreeNode(modules[i].getName());
				add(moduleNode);
				ComponentDescriptor[] components = modules[i].getComponents();
				for (int j = 0; j < components.length; j++) {
					DefaultMutableTreeNode componentNode = new DefaultMutableTreeNode(components[j].getName());
					moduleNode.add(componentNode);
					MethodDescriptor[] methods = components[j].getMethods();
					for (int k = 0; k < methods.length; k++) {
						DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(
								new ARProcessorFactory(modules[i], components[j],methods[k])
								);
						componentNode.add(methodNode);
					}								
					}					
			}
		} catch (ACRException x) {
			throw new ScavengerCreationException("Failed to list components of AR" + x.getMessage());
		}	
	}

}
