/**
 * 
 */
package org.astrogrid.taverna.arcea_app;

import java.util.Iterator;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.embl.ebi.escience.scuflui.workbench.Scavenger;
import org.embl.ebi.escience.scuflui.workbench.ScavengerCreationException;

import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.ExecutionInformation;

import net.sf.taverna.utils.MyGridConfiguration;

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
		super("Astro Runtime DSA");
		logger.info("start constructor in ARScavenger");
		makeCEAAppTree();
	}
	
	
	/** alternate implementation of listApi */
	private void makeCEAAppTree() throws ScavengerCreationException {
		//try {
			logger.warn("start makeDSAAppTree in ARScavenger");
			//DefaultMutableTreeNode moduleNode = new DefaultMutableTreeNode("CEA Tree");
			DefaultMutableTreeNode serviceNode = new DefaultMutableTreeNode("CEA");
			
			
			//Read the internal properties file first looking at conf then in the jar itself.
			//Get each line of the file it should be a ivorn.
			//Process the ivorn for all the inputs and outputs forget ceaservice.
			//possibly think about it more as authid/reskey=title
			//http://www.astrogrid.org/maven/taverna_plugins/plugins/my_cealist.properties
			//MyGridConfigurationgetUserDir("conf"); //could be handy to add&remove apps.
			Properties prop = MyGridConfiguration.loadUserProperties("my_cealist.properties");
			if(prop.isEmpty()) {
				prop.setProperty("my.ceaapp.1", "ivo://org.astrogrid/SExtractor");
				prop.setProperty("my.ceaapp.2", "ivo://mssl.ucl.ac.uk/SolarMovieMaker");
				prop.setProperty("my.ceaapp.3", "ivo://org.astrogrid/HyperZ");
				prop.setProperty("my.ceaapp.4", "ivo://org.astrogrid/CrossMatcher");
				prop.setProperty("my.ceaapp.5", "ivo://org.astrogrid/MERLINImager");
			}
			ACR acr = SingletonACR.getACR();
			//do some looping through all the apps and interface beans.
			Applications apps = (Applications)acr.getService(Applications.class);
			ApplicationInformation ai = apps.getCeaApplication("ivorn");
			InterfaceBean ib = apps.getInterfaces();
			
			DefaultMutableTreeNode ceaNodeForDSA = 
				new DefaultMutableTreeNode(new ARProcessorFactory("ivorn","interface[i]"));
			serviceNode.add(ceaNodeForDSA);
			add(serviceNode);			
			logger.warn("end makeDSAAppTree successful ARScavenger");
		/*} catch (ACRException x) {
			throw new ScavengerCreationException("Failed to list components of AR" + x.getMessage());
		}*/	
	}

}
