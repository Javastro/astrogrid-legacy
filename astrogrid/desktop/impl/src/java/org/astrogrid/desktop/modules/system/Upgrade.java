/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.pref.Preference;

/** Run-at-startup class that just performs any necessary upgrade actions between current application version and previous version.
 * 
 * Sadly, upgrading by version number doesn't work - due to varying number schemes used.
 * Instead, will use a new unique key for each upgrade feature - and just do a test as to whether this key exists.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 15, 20074:23:38 PM
 */
public class Upgrade implements Runnable {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog("upgrade");


	private final ConfigurationInternal conf;
	private static final String ALT_REGISTRY_ENDPOINT_1_2_3 = "upgrade.1.2.3.alt-registry-endpoint";
	private static final String RESOURCETREE_EXAMPLES_1_2_3 = "upgrade.1.2.3.resourcetree-examples";
	
	private static final String TURN_OFF_PLASTIC_HUB = "turn-off-plastic-hub";
	
	public void run() {
	    
	    // upgrtade the old default endpoint location.
	    if (conf.getKey(ALT_REGISTRY_ENDPOINT_1_2_3) == null) {
	        // record that we've checked.
	        conf.setKey(ALT_REGISTRY_ENDPOINT_1_2_3,"done");
	        // if registry endpoint is set to previous default, reset to new defaults.
	        final Preference altRegEndpoint = conf.find("org.astrogrid.registry.query.altendpoint");
	        if ("http://alt.registry.astrogrid.org/astrogrid-registry_v1_0/services/RegistryQueryv1_0".equals(altRegEndpoint.getValue())) {
	            altRegEndpoint.setValue(altRegEndpoint.getDefaultValue());
	        }
	    }
	    
		// adjust the resourcetree examples location.
		if (conf.getKey(RESOURCETREE_EXAMPLES_1_2_3) == null) {
		    //record that we've looked at upgrading this.
		    conf.setKey(RESOURCETREE_EXAMPLES_1_2_3,"done");
		    logger.info("Examining resource tree examples location");
		    final Preference resourceTreeLocation = conf.find("resourcetree.examples.location");
		    if ("http://technology.astrogrid.org/raw-attachment/wiki/vodesktopResources/exampleResourceLists.xml".equals(resourceTreeLocation.getValue())) {
		        logger.info("Resetting resource tree examples to new default");
		        // i.e. it's set to the previous default
		        // then set it to the new default
		        resourceTreeLocation.setValue(resourceTreeLocation.getDefaultValue());
		        // this fires propertychanges, which are listend to by the resourceTreeProvider (if it's already existing)
		        // and this takes care of propagating the subscruption change into the tree.
		    }		    		    
		}
		
		if (conf.getKey(TURN_OFF_PLASTIC_HUB) == null) {
		    conf.setKey(TURN_OFF_PLASTIC_HUB,"done");
		    logger.info("Deactivating plastic hub - can be re-enabled if required via 'Preferences..'");
		    final Preference plasHub = conf.find("messaging.start.plastic");
		    if (plasHub.asBoolean()) {
		        plasHub.setValue("false");
		    }
		}
	

	}
	public Upgrade(final String currentVersion, final ConfigurationInternal conf) {
		super();
		this.conf = conf;
		
	}

}
