/**
 * UCD.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.acr.cds;

import org.astrogrid.acr.ServiceException;
/**
 *  AR Service: Web Service for manipulating 
Unified Content Descriptors, from CDS.

 * @author CDS
 *@see <a href="http://cdsweb.u-strasbg.fr/cdsws/ucdClient.gml">Webservice Description</a>
 *@service cds.ucd
 */
public interface UCD  {
    /** Return a list of UCD1.
     * @return html document containing all ucd1
     * @throws ServiceException
     */
    public java.lang.String UCDList()throws ServiceException;
    /** Resolve a UCD1 (won't work with UCD1+)
     * @param ucd               the UCD1 to resolve (example : {@code PHOT_JHN_V})

     * @return  sentence corresponding to the UCD1 (example : {@code Johnson magnitude V (JHN)})

     * @throws ServiceException
     */
    public java.lang.String resolveUCD(java.lang.String ucd) throws ServiceException;
    
    /** Access UCD1 for a catalogue
     * @param catalog_designation designes the catalog (example : {@code I/239})

     * @return list of UCD1 (in raw text) contained in a given catalog

     * @throws ServiceException
     */
    public java.lang.String UCDofCatalog(java.lang.String catalog_designation) throws ServiceException;
    
    /** Convert a legacy UCD1 to the equivalent UCD1+
     * @param ucd The argument is a UCD1 (not UCD1+ !).

     * @return ucd. This function returns the default UCD1+ corresponding to an old-style UCD1.

     * @throws ServiceException
     */
    public java.lang.String translate(java.lang.String ucd) throws ServiceException;
    /** Upgrade a deprecated UCD1+.
     * @param ucd a deprecated UCD1+ (word or combination).
                    Useful when the 'validate' method returns with code 2.

     * @return  ucd. This function returns a valid UCD1+ corresponding to a deprecated word.
                      It is useful when some reference words of the UCD1+ vocabulary are changed,
                      and ensures backward compatibility.

     * @throws ServiceException
     */
    public java.lang.String upgrade(java.lang.String ucd) throws ServiceException;
    /** validate a UCD1+.
     * This function checks that a UCD is well-formed
     * @param ucd  (e.g. {@code ivoa:phot.mag;em.opt.B})
     * @return  
The first word of the return string is an error code, possibly followed by an explanation of the error. 
A return value of 0 indicates no error (valid UCD).

The error-code results from the combination (logical OR) of the following values:
    <ul>
    <li>1: warning indicating use of non-standard namespace (not ivoa:)</li>
    <li>2: use of deprecated word</li>
    <li>4: use of non-existing word</li>
    <li>8: syntax error (extra space or unallowed character)</li> 
    </ul>
     * @throws ServiceException
     */
    public java.lang.String validate(java.lang.String ucd)throws ServiceException;
    /** Find the description of a UCD1+
     * @param ucd (e.g. {@code ivoa:phot.mag;em.opt.B})
     * @return  This function gives a human-readable explanation for a UCD composed of one or several words
     * @throws ServiceException
     * @equivalence assign(explain(ucd) == ucd
     */
    public java.lang.String explain(java.lang.String ucd) throws ServiceException;
    /** Find the UCD1+ associated with a description.
     * @param descr  Plain text description of a parameter to be described                      
     * @return This function returns the UCD1+ corresponding to the description
     * @throws ServiceException
     * @equivalence explain(assign(description) = description
     */
    public java.lang.String assign(java.lang.String descr)throws ServiceException;
}
