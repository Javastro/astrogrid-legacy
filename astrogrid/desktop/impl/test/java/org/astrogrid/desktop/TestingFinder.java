/**
 * 
 */
package org.astrogrid.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;

/** Extensioin of finder for testing.
 * makes it fail-fast, rather than prompting user.
 * and will connect to a hub, if one is running, so that 
 * the basic portions of AR within it can be tested.
 * @author Noel Winstanley
 * @since Jan 23, 20076:49:55 PM
 */
public class TestingFinder extends Finder {
/**
 * 
 */
public TestingFinder() {
}
	// default find action is to fail fast, without prompting.
	@Override
    public synchronized ACR find() throws ACRException {
		return super.find(false,false); //
	}

	// try the conventional connection method first, then try to connect using
	// the plastic properties file instead.
	@Override
    protected int parseConfigFile() throws IOException {
		try {
		return super.parseConfigFile();
		} catch (IOException e) {
		       File homeDir = new File(System.getProperty("user.home"));
		        File plasticFile =  new File(homeDir,".plastic");		
		        if (! plasticFile.exists()) {
		        	throw new FileNotFoundException(plasticFile.getAbsolutePath());
		        }
		        logger.info("Will connect via .plastic file");
		    	InputStream br = null;
				try {
					br = new FileInputStream(plasticFile);
					Properties props = new Properties();
					props.load(br);
					int port = Integer.parseInt(props.getProperty("plastic.rmi.port"));
					logger.info("Port determined to be " + port);
					return port;
				} finally {
					if (br != null) {
						try {
							br.close(); //Otherwise the file can be locked and left undeleted when the ACR shuts down.
						} catch (IOException x) {
						}
					}
				}		        
		}
	}

}
