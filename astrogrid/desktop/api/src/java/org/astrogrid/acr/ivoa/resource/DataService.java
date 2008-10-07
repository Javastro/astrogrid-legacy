/**
 * 
 */
package org.astrogrid.acr.ivoa.resource;

/**
 *  A service for accessing astronomical data.
 *  
 *  @note Previously was called SkyService.
 * @author Noel Winstanley
 */
public interface DataService extends Service, HasCoverage {
	/** the observatories or facilities used to collect the data contained or managed by this resource */
	ResourceName[] getFacilities();
	
	/** the instruments used to collect the data contained or managed by this resource */
	ResourceName[] getInstruments();
	
//	/** 
//                     Extent of the content of the resource over space, time, 
//                     and frequency. */
//	public Coverage getCoverage();
}
