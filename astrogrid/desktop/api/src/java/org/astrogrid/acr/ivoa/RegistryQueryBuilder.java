/**
 * 
 */
package org.astrogrid.acr.ivoa;

/** Abstract interface for a service that builds registry queries.
 * @see RegistryXQueryBuilder Builder for XQuery
 * @see RegistryAdqlBuilder Builder for ADQL
 * @author Noel Winstanley
 * @since Oct 19, 20067:33:47 PM
 */
public interface RegistryQueryBuilder {

	/** query that returns all active records in the registry */
	String allRecords();
	
	/** build a full text search 
	 * */
	String fullTextSearch(String recordSet,String searchTerm);
	
	/** build a summary text search 
	 * 
	 * searches on identifier, shortName, title and content/description
	 * */
	String summaryTextSearch(String recordSet, String searchTerm);
	
	
	/** build an identifier search */
	String identifierSearch(String recordSet, String searchTerm);
	
	
	/** build a short-name search */
	String shortNameSearch(String recordSet, String searchTerm);
	
	/** build a search on title */
	String titleSearch(String recordSet, String searchTerm);
	
	/** build a search on description */
	String descriptionSearch(String recordSet, String searchTerm);
}
