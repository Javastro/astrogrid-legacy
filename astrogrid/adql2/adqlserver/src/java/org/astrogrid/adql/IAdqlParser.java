package org.astrogrid.adql;

import javax.xml.transform.TransformerException;
import org.astrogrid.adql.beans.SelectDocument ;
import org.astrogrid.adql.InvalidStateException ;

/**
 * An interface presenting common methods for parsing, transforming and converting
 * ADQL queries.
 * 
 * @author Jeff Lusted jl99@star.le.ac.uk
 *
 */
public interface IAdqlParser {

	 /**
     * Accepts an ADQL query in string format and parsers it into 
     * an XmlBean object. The input is a full query, starting
     * with a SELECT statement, and not simply a fragment of a query. 
     * For example:
     * <p>
     * Select * from first as f ;
     * <p><p>
     * 
     * @param adqls - An ADQL query in string format
     * @return A specific SQL version of the query
     * @throws AdqlException 
     *         if ADQL input fails parsing.
     * @throws InvalidStateException 
     * @see org.astrogrid.adql.AdqlParserSV
     * @see org.astrogrid.adql.AdqlParserSVNC       
     * 
     */
	public abstract SelectDocument parseToXML( String adqls )
			throws AdqlException, InvalidStateException;

	/**
     * Transforms an ADQL query presented as an XmlBean object to a specific
     * variant of SQL.
     * 
     * @param selectDoc - An XmlBean object.
     * @return A specific SQL version of the query
     * @throws InvalidStateException 
     *         if there is an invalid state, for example: an instance of
     *         the transformer corresponding to the XSLT style sheet cannot be formed.
     * @throws TransformerException
     *         if there is a problem with the XSLT style sheet.
     * @see org.astrogrid.adql.AdqlParserSV
     * @see org.astrogrid.adql.AdqlParserSVNC       
     * 
     */
	public abstract String transformToSQL(SelectDocument selectDoc)
			throws InvalidStateException, TransformerException;
	
	 /**
     * Accepts an ADQL query in string format and turns it into a specific
     * variant of SQL. The input is a full query, starting
     * with a SELECT statement, and not simply a fragment of a query. 
     * For example:
     * <p>
     * Select * from first as f ;
     * <p><p>
     * 
     * @param adqls - An ADQL query in string format
     * @return A specific SQL version of the query
     * @throws AdqlException 
     *         if ADQL input fails parsing.
     * @throws InvalidStateException 
     *         if there is an invalid state, for example: an instance of
     *         the transformer corresponding to the XSLT style sheet cannot be formed.
     * @throws TransformerException
     *         if there is a problem with the XSLT style sheet.
     * @see org.astrogrid.adql.AdqlParserSV
     * @see org.astrogrid.adql.AdqlParserSVNC       
     * 
     */
	public abstract String parseToSQL( String adqls ) 
	        throws AdqlException, InvalidStateException, TransformerException ;

	/**
     * Converts an ADQL query prior to version 2.0 presented in XML string format
     * 
     * @param XML version 1.0 or prior ADQL query
     * @return XML version 2.0 ADQL query
     * @throws InvalidStateException 
     * @throws TransformerException
     *         if there is a problem with the XSLT style sheet.
     * @see org.astrogrid.adql.AdqlParserSV
     * @see org.astrogrid.adql.AdqlParserSVNC       
     * 
     */
	public abstract String convert(String adqlString)
			throws InvalidStateException, TransformerException;

}