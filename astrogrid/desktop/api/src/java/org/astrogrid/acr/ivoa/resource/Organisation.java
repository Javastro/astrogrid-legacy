/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** A resource that brinds people togehter to persue participatio in VO applications.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 20, 20072:35:17 PM
 */
public interface Organisation extends Resource {
	/** the observatories or facilities used to collect the data contained or managed by this resource */
	ResourceName[] getFacilities();
	
	/** the instruments used to collect the data contained or managed by this resource */
	ResourceName[] getInstruments();
	
}
