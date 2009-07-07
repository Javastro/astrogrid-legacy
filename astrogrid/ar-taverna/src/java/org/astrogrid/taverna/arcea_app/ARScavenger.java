/**
 * 
 */
package org.astrogrid.taverna.arcea_app;

import java.util.Iterator;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Properties;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;

import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.embl.ebi.escience.scufl.DuplicatePortNameException;
import org.embl.ebi.escience.scufl.PortCreationException;
import org.embl.ebi.escience.scufl.ProcessorCreationException;
import org.embl.ebi.escience.scuflui.workbench.Scavenger;
import org.embl.ebi.escience.scuflui.workbench.ScavengerCreationException;

import org.astrogrid.acr.astrogrid.Applications;
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
		super("Astro Runtime CEA");
		logger.info("start constructor in ARScavenger");
		makeCEAAppTree();
		//ceaProp = new Properties();
	}
	
	private static Properties ceaProp = new Properties();
	public  static final String PROPERTY_FILE = "my_cealist.properties";
	
	public static void saveProperties(String []ivorns) throws java.io.IOException {
		//File confDir = MyGridConfiguration.getUserDir(MyGridConfiguration.CONFIGURATION_DIRECTORY);
		File confDir = MyGridConfiguration.getUserDir("conf");
		File propertyFile = new File(confDir, PROPERTY_FILE);
		int size = ceaProp.size();
		size++;
		for(int i = 0;i < ivorns.length;i++) {
			ceaProp.setProperty("my.cea_app." + (size + i), ivorns[i]);
		}
		FileOutputStream fs = new FileOutputStream(propertyFile,false);
		ceaProp.store(fs, null);
	}
	
	/** alternate implementation of listApi */
	private void makeCEAAppTree() throws ScavengerCreationException {
		//try {
			logger.warn("start makeCEAAppTree in ARScavenger");
			//DefaultMutableTreeNode moduleNode = new DefaultMutableTreeNode("CEA Tree");
			//DefaultMutableTreeNode serviceNode = new DefaultMutableTreeNode("CEA");
			
			
			//Read the internal properties file first looking at conf then in the jar itself.
			//Get each line of the file it should be a ivorn.
			//Process the ivorn for all the inputs and outputs forget ceaservice.
			//possibly think about it more as authid/reskey=title
			//http://www.astrogrid.org/maven/taverna_plugins/plugins/my_cealist.properties
			//MyGridConfigurationgetUserDir("conf"); //could be handy to add&remove apps.
			//Properties prop = new Properties();//MyGridConfiguration.loadUserProperties("my_cealist.properties");
			try {
				//File confDir = MyGridConfiguration.getUserDir(MyGridConfiguration.CONFIGURATION_DIRECTORY);
				File confDir = MyGridConfiguration.getUserDir("conf");
				logger.warn("here is the confdir = " + confDir.toString());
				File propertyFile = new File(confDir, PROPERTY_FILE);
				logger.warn("try to load ceaProp");
				if(propertyFile.exists() && propertyFile.isFile()) {
					ceaProp.load(propertyFile.toURL().openStream());
				}else {
					logger.warn("setting base properties");
					ceaProp.setProperty("my.cea_app.1", "ivo://org.astrogrid/SExtractor");
					ceaProp.setProperty("my.cea_app.2", "ivo://mssl.ucl.ac.uk/SolarMovieMaker");
					ceaProp.setProperty("my.cea_app.3", "ivo://org.astrogrid/HyperZ");
					ceaProp.setProperty("my.cea_app.4", "ivo://org.astrogrid/CrossMatcher");
					//ceaProp.setProperty("my.cea_app.5", "ivo://org.astrogrid/MERLINImager");
					ceaProp.setProperty("my.cea_app.5", "ivo://uk.ac.starlink/stilts");					
				}
				logger.warn("ceaProp size = " + ceaProp.size());
				ACR acr = SingletonACR.getACR();
				//do some looping through all the apps and interface beans.
				Applications apps = (Applications)acr.getService(Applications.class);
				Object []ivornArr = ceaProp.values().toArray();
				logger.warn("property array size = " + ivornArr.length);
				for(int k = 0;k < ivornArr.length;k++) {
					logger.warn("looking up CeaApplication");
					CeaApplication ai = apps.getCeaApplication(new java.net.URI((String)ivornArr[k]));
					logger.warn("found ceaapplicaiton get the interfaces");
					InterfaceBean []ib = ai.getInterfaces();
					logger.warn("set the appNode to title = " + ai.getTitle());
					DefaultMutableTreeNode appNode = new DefaultMutableTreeNode(ai.getTitle());
					for(int m = 0;m < ib.length;m++) {
						logger.warn("calling ARProcessorFactory from Arscavenger with ivorn = " + (String)ivornArr[k] + " and interface name = " + ib[m].getName());
						DefaultMutableTreeNode ceaNodeForDSA = 
							new DefaultMutableTreeNode(new ARProcessorFactory(ib[m].getName(), (String)ivornArr[k],ib[m].getName()));
						appNode.add(ceaNodeForDSA);
					}
					//serviceNode.add(appNode);
					add(appNode);
				}
				//add(serviceNode);			
				logger.warn("end makeCEAAppTree successful ARScavenger");
		}catch (java.net.URISyntaxException e) {
			e.printStackTrace();
			throw new ScavengerCreationException(e.getMessage());
		}catch(ACRException e) {
			e.printStackTrace();
			throw new ScavengerCreationException(e.getMessage());
		}catch(java.io.IOException e) {
			e.printStackTrace();
			throw new ScavengerCreationException(e.getMessage());			
		}
		/*} catch (ACRException x) {
			throw new ScavengerCreationException("Failed to list components of AR" + x.getMessage());
		}*/	
	}

}
