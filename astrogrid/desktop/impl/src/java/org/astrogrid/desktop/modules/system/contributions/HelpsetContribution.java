/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.net.URL;

import javax.help.HelpSet;
import javax.help.HelpSetException;

/**
 * @author Noel Winstanley
 * @since Apr 25, 200612:38:54 PM
 */
public class HelpsetContribution {

	private String path;
	private Class resourceAnchor;
	
	public HelpSet getHelpset() throws HelpSetException {
		return  new HelpSet(
				resourceAnchor.getClassLoader()
				,HelpSet.findHelpSet(resourceAnchor.getClassLoader(),path)
				);
	}
	
	public void setPath(String path) {
		this.path = path;

	}
	
	public void setResourceAnchor(Class cl) {
		this.resourceAnchor = cl;
	}
	
}
