/**
 * Sesame.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.acr.cds;

import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;
/**
 * Resolve object  names to position by querying  Simbad and/or NED and/or VizieR.
 * @see http://cdsweb.u-strasbg.fr/cdsws/name_resolver.gml
 * @author CDS
 *@service cds.sesame
 *@todo produce an informationBean variant of these methods?
 *@see http://vizier.u-strasbg.fr/xml/sesame_1.dtd
 *@see http://vizier.u-strasbg.fr/xml/sesame_1.xsd
 */
public interface Sesame {

    /** resolve a name to position.
  * @param name the name to resolve
     * @param resultType
     * <pre>
     *           u = usual (corresponding to the deprecated Sesame(String name) output)
                H = html 
                x = XML (XSD at http://vizier.u-strasbg.fr/xml/sesame_1.xsd, DTD at http://vizier.u-strasbg.fr/xml/sesame_1.dtd) 
                p (for plain (text/plain)) and i (for all identifiers) options can be added to H or x
                </pre>
     * @return
     * @throws ServiceException
     */
    public java.lang.String sesame(java.lang.String name, java.lang.String resultType)throws ServiceException;
    /** resolve a name, selecing which services to use.
  * @param name the name to resolve
     * @param resultType      * <pre>
     *           u = usual (corresponding to the deprecated Sesame(String name) output)
                H = html 
              x = XML (XSD at http://vizier.u-strasbg.fr/xml/sesame_1.xsd, DTD at http://vizier.u-strasbg.fr/xml/sesame_1.dtd) 
               p (for plain (text/plain)) and i (for all identifiers) options can be added to H or x
                </pre>

     * @param all true if all identifiers wanted 
     * @param service
     * <pre>  
     * S=Simbad
        N=NED 
        V=VizieR
        A=all
        </pre>
        (examples : S to query in Simbad, NS to query in Ned if not found in Simbad,
        NS to query in Ned and Simbad, A to query in Ned, Simbad and VizieR, ...)

     * @return format depending on the resultTtpe parameter

     * @throws ServiceException
     */
    public java.lang.String sesameChooseService(java.lang.String name, java.lang.String resultType, boolean all, java.lang.String service) throws ServiceException;
 
}
