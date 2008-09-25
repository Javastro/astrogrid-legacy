/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/** An organization that is involved in the virtual observatory.
 * @author Noel.Winstanley@manchester.ac.uk
 */
public interface Organisation extends Resource {
	/** the observatories or facilities used to collect the data contained or managed by this resource */
	ResourceName[] getFacilities();
	
	/** the instruments used to collect the data contained or managed by this resource */
	ResourceName[] getInstruments();
	
}
