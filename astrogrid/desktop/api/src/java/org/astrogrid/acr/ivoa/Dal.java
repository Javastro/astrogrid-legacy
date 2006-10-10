/**
 * 
 */
package org.astrogrid.acr.ivoa;

import java.net.URI;
import java.net.URL;
import java.util.Map;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.util.Tables;
import org.w3c.dom.Document;

/** Functionality common to all DAL services  - Cone, SIAP, SSAP, etc.
 * 
 * Addresses how to locate DAL services, perform the DAL query, inspect the response, and save and datasets
 * it points to. See subclasses for specifics of how to construct a query suitable for that 
 * protocol.
 * 
 * @author Noel Winstanley
 * @since 2006.03.rc4
 * @see Cone
 * @see Siap
 * @see Ssap
 * @see Tables (util.tables) for functions to convert a votable to other formats (e.g. csv, ascii, etc)
 */
public interface Dal {

	
	   /** add an option to a previously constructed query
	    * 
	    * @param query the query url
	    * @param optionName name of the option to add
	    * @param optionValue value for the new option
	    * @return <tt>query</tt> with the option appended.
	 * @throws InvalidArgumentException if the parameter cannot be added.
	    */
	   URL addOption(URL query, String optionName, String optionValue) throws InvalidArgumentException;

	   /** execute a DAL query, returning a datastructure
	    * @param query query url to execute
	    * @return  A model the DAL query response as a list of
	    * of rows. Each row is represented is a map between UCD keys or datamodel names
	    *  and values from the response
	    * @throws ServiceException if an error occurs while communicating with the  service
	    * @since 2006.3.rc4
	    */
	   Map[] execute(URL query) throws ServiceException;
	    
	   /** execute a DAL query, returning a votable document.
	    * 
	    * This is a convenience method  - just performs a 'GET' on the query url- many programming languages support this functionality themselves
	 * @param query query url to execute
	 * @return a votable document of results
	 * @throws ServiceException if an error occurs while communicating with the  service
	    * @since 2006.3.rc4
	    */
	   Document executeVotable(URL query) throws ServiceException;	 
	   
	   /** @deprecated - use executeVotable() */
	   Document getResults(URL url) throws ServiceException;
	   

	   /**execute a DAL query and save the resulting document.
	 * @param query query url to execute
	 * @param saveLocation location to save result document - may be file:/, ivo:// (myspace), ftp://
	 * @throws SecurityException if the user is not permitted to write to the save location
	 * @throws ServiceException if an error occurs while communicating with the query service
	 * @throws InvalidArgumentException if the save location cannot be written to
	 * @since 2006.3.rc4
	 */
	void executeAndSave(URL query, URI saveLocation) throws SecurityException, ServiceException, InvalidArgumentException;
	/** @deprecated - use executeAndSave() */
	void saveResults(URL url, URI saveLocation) throws SecurityException, ServiceException, InvalidArgumentException;
	
	/** save the datasets pointed to by this DAL query response 
	 * 
	 * @param query the DAL query
	 * @param saveLocation location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference
	 * @throws SecurityException if the user is not permitted to write to the save location
	 * @throws ServiceException if either the query service or data service are unavailable
	 * @throws InvalidArgumentException if the save location cannot be written to.
	 */
	void saveDatasets(URL query, URI saveLocation) throws SecurityException, ServiceException, InvalidArgumentException;
	
	/** save a subset of the datasets point to by this DAL query response
	 * 
	 * @param query the DAL query
	 * @param saveLocation location of a directory in which to save the datasets. may be a file:/, ivo:// or ftp:// reference
	 * @param rows indexes of the rows in the query response for which to save the dataset. 
	 * @throws SecurityException if the user is not permitted to write to the save location
	 * @throws ServiceException if either the query service or data service are unavailable
	 * @throws InvalidArgumentException if the save location cannot be written to, or the <tt>rows</tt>
	 * refers to invalid row indexes.
	 */
	void saveDatasetsSubset(URL query, URI saveLocation, int[] rows) throws SecurityException, ServiceException, InvalidArgumentException;
	
	   /** helper method - returns an ADQL/s query that should be passed to a registry to list all 
	    * available DAL services of this type. 
	    * <br/>
	    * can be used as a starting point for filters, etc.
	    * @return an adql query string
	    * @deprecated use getRegistryAdqlQuery
	    */
	   String getRegistryQuery();
	   
	   /** helper method - returns an ADQL/s query that should be passed to a registry to list all 
	    * available DAL services of this type. 
	    * <br/>
	    * can be used as a starting point for filters, etc.
	    * @return an adql query string
	    * @since 2006.03
	    */
	   String getRegistryAdqlQuery();
	   
	   /** helper method - returns an Xquery that should be passed to a registry to list all 
	    * available DAL services of this type. 
	    * <br/>
	    * can be used as a starting point for filters, etc.
	    * @return an xquery string
	    * @since 2006.03
	    */
	   String getRegistryXQuery();
	
}
