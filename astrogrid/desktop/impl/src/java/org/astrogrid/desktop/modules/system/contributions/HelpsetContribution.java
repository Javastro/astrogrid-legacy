/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;


/** Contribiution bean for new helpsets.
 * @author Noel Winstanley
 * @since Apr 25, 200612:38:54 PM
 */
public class HelpsetContribution {

	private String path;
	private Class resourceAnchor;
	
	
	
	public void setPath(String path) {
		this.path = path;

	}
	
	public void setResourceAnchor(Class cl) {
		this.resourceAnchor = cl;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * @return the resourceAnchor
	 */
	public Class getResourceAnchor() {
		return this.resourceAnchor;
	}
	
}
