/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** Record for a data collection.
 * @author Noel Winstanley
 * @since Aug 5, 20069:41:46 PM
 */
public interface DataCollection extends Resource {
	/** list of the formats this data is available in */
	public Format[] getFormats();
	/** description of the coverage of this collection */
	public Coverage getCoverage();
	/** the description of the columns in this collection */
	public Catalog getCatalog(); //@todo might be a multiple here.
	
}
