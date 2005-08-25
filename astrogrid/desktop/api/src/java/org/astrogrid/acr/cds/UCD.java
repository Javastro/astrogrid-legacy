/**
 * UCD.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.acr.cds;

import org.astrogrid.acr.ServiceException;
/**
 * This XML Web Service provides a few methods to ease manipulation of the
Unified Content Descriptors (UCD).

 * @author CDS
 *@see http://cdsweb.u-strasbg.fr/cdsws/ucdClient.gml
 *@service cds.ucd
 */
public interface UCD  {
    /** list of UCD1
     * @return html document containing all ucd1
     * @throws ServiceException
     */
    public java.lang.String UCDList()throws ServiceException;
    /** resolve a UCD1 (wont work with UCD1+)
     * @param ucd             ucd  the UCD1 to resolve (example : PHOT_JHN_V)

     * @return  sentence corresponding to the UCD1 (example : Johnson magnitude V (JHN))

     * @throws ServiceException
     */
    public java.lang.String resolveUCD(java.lang.String ucd) throws ServiceException;
    /**
     * @param catalog_designation designes the catalog (example : I/239)

     * @return list of UCD1 (in raw text) contained in a given catalog

     * @throws ServiceException
     */
    public java.lang.String UCDofCatalog(java.lang.String catalog_designation) throws ServiceException;
    /** makes the translation of old-style UCD1 into the newer UCD1+ easier:
     * @param ucd The argument is a UCD1 (not UCD1+ !).

     * @return String ucd. This function returns the default UCD1+ corresponding to an old-style UCD1.

     * @throws ServiceException
     */
    public java.lang.String translate(java.lang.String ucd) throws ServiceException;
    /** upgrade a ucd
     * @param ucd a deprecated UCD1+ (word or combination).
                    Useful when the 'validate' method returns with code 2.

     * @return  String ucd. This function returns a valid UCD1+ corresponding to a deprecated word.
                      It is useful when some reference words of the UCD1+ vocabulary are changed,
                      and ensures backward compatibility.

     * @throws ServiceException
     */
    public java.lang.String upgrade(java.lang.String ucd) throws ServiceException;
    /** validate a ucd
     * @param ucd  (e.g. ivoa:phot.mag;em.opt.B)
     * @return  String, this function checks that a UCD is well-formed
<pre>
The first word of the string is an error code, possibly followed by an explanation of the error. 
A return value of 0 indicates no error (valid UCD).

The error-code results from the combination (logical OR) of the following values:

    * 1: warning indicating use of non-standard namespace (not ivoa:)
    * 2: use of deprecated word
    * 4: use of non-existing word
    * 8: syntax error (extra space or unallowed character) 
</pre>
     * @throws ServiceException
     */
    public java.lang.String validate(java.lang.String ucd)throws ServiceException;
    /** returns description of a ucd
     * @param ucd (e.g. ivoa:phot.mag;em.opt.B)

     * @return  String, this function gives a human-readable explanation for a UCD composed of one or several words

     * @throws ServiceException
     */
    public java.lang.String explain(java.lang.String ucd) throws ServiceException;
    /** Find the UCD associated with a description
     * @param descr  Plain text description of a parameter to be described                      
     * @return  String ucd. This function returns the UCD1+ corresponding to the description

     * @throws ServiceException
     */
    public java.lang.String assign(java.lang.String descr)throws ServiceException;
}
