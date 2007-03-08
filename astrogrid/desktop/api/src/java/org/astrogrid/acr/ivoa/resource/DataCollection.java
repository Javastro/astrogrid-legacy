/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**  A logical grouping of data which, in general, is composed of one 
           or more accessible datasets.  A collection can contain any
           combination of images, spectra, catalogs, or other data.   
 * @author Noel Winstanley
 * @since Aug 5, 20069:41:46 PM
 */
public interface DataCollection extends Resource, HasCoverage {//should this extend organization?
	/** the observatories or facilities used to collect the data contained or managed by this resource */
	ResourceName[] getFacilities();
	
	/** the instruments used to collect the data contained or managed by this resource */
	ResourceName[] getInstruments();
	
	/**  Information about rights held in and over the resource. */
	String[] getRights();	
	
	/** list of the formats this data is available in */
	public Format[] getFormats();
	/** Extent of the content of the resource over space, time, 
                     and frequency. */
	public Coverage getCoverage();
	/** the description of the catalogues in this collection */
	public Catalog[] getCatalogues();
	
	/**  The URL that can be used to download the data contained in 
                     this data collection.*/
	public AccessURL getAccessURL();
}
