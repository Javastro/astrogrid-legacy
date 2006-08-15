/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**
 * @author Noel Winstanley
 * @since Aug 5, 20069:41:46 PM
 */
public interface DataCollection extends Resource {
	public Format[] getFormats();
	public Coverage getCoverage();
	public Catalog getCatalog(); //@todo might be a multiple here.
	
}
