/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.desktop.modules.system.pref.Preference;

/** Run-at-startup class that just performs any necessary upgrade actions between current application version and previous version.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jun 15, 20074:23:38 PM
 */
public class Upgrade implements Runnable {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog("upgrade");

	private static final String UPGRADED_VERSION = "last.upgraded.to";

	/** version key stored in preferences - it's the version we last upgraded to */
	private final String upgradedVersion;
	/** the current version of the application.*/
	private final String currentVersion;
	private final ConfigurationInternal conf;
	
	public void run() {
		if (upgradedVersion != null && upgradedVersion.compareTo(currentVersion) >= 0) {
			return;
		}
		// current version is more recent than last upgrade.
		logger.info("Upgrading from " + (upgradedVersion == null ? "unknown" : upgradedVersion) + " to " + currentVersion);
		// now test for individual versions.
		if (upgradedVersion == null || upgradedVersion.compareTo("2007.2") < 0) { // corrects bugs in 2007.1
			Preference vomon = conf.find("votech.vomon.endpoint");
			// reset this - as was previously wrong.
			logger.info("Resetting VOMon endpoint");
			vomon.setValue(vomon.getDefaultValue());
		}
		if (upgradedVersion == null || upgradedVersion.compareTo("2007.3.0") < 0) { // corrects bugs in 2007.3. alphas
				//was going to add in a resourceList upgrade here, but too far in the past - shan't bother now.
		}
		if (upgradedVersion == null || upgradedVersion.compareTo("2008.1") <= 0 || upgradedVersion.startsWith("2008.1.beta")) { //sets the registry endpoints to the correct settings.
		    // do this for every interim release upto and including the big 2008.2.		    
		    Preference reg = conf.find("org.astrogrid.registry.query.endpoint");
		    reg.setValue(reg.getDefaultValue());
            reg = conf.find("org.astrogrid.registry.query.altendpoint");
            reg.setValue(reg.getDefaultValue());		    
		}
		if (upgradedVersion == null || upgradedVersion.compareTo("2008.1.1") <=0) { //@fixme - alter to check against the correct version number.
		    // I've changed the default for the  registry cache preference - so reset it back to default - previous default is much too long for the new scheme
		    Preference cacheLife = conf.find("ivo.registry.cacheLife");
		    cacheLife.setValue(cacheLife.getDefaultValue());
		}
		// finaly, record that we've upgraded.
		conf.setKey(UPGRADED_VERSION,currentVersion);
		logger.info("Upgraded");
	}
	public Upgrade(final String currentVersion, ConfigurationInternal conf) {
		super();
		this.conf = conf;
		this.upgradedVersion = conf.getKey(UPGRADED_VERSION); 
		this.currentVersion = currentVersion;
		
	}

}
