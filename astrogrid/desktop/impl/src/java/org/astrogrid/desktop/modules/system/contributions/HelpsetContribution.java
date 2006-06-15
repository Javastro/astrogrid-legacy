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
	
	
	/**( set the path to the helpset index file */
	public void setPath(String path) {
		this.path = path;

	}
	
	/** sets a class that the {@link #path} should be resolved relative to 
	 * - this class should be in the <i>same</i> jar file as the helpset - to 
	 * fix class resolving problems when running under webstart.
	 * @param cl
	 */
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
