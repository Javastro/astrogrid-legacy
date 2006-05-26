/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.SymbolSource;

/**
 *a hivemind symbol source that retrieves values from a file called 'version.properties'
 * 
 * @author Noel Winstanley
 * @since May 18, 20061:56:57 PM
 */
public class VersionSymbolSource implements SymbolSource {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(VersionSymbolSource.class);

	public VersionSymbolSource() {
		v = new Properties();
		try {
		InputStream is = Launcher.class.getResourceAsStream("astrogrid.version");
		if (is != null) {
			v.load(is);
		}
		} catch (Exception e) {
			// oh well.
		}
	}
	
	private final Properties v;
	public String valueForSymbol(String arg0) {
		return v.getProperty(arg0);
	}

}
