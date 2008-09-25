/**
 * Jglu.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.acr.cds;

import org.astrogrid.acr.ServiceException;

/** Webservice to resolve  GLU  (Generateur de Liens Uniformes).tags.
 * @see <a href='http://cdsweb.u-strasbg.fr/cdsws/glu_resolver.gml'>Webservice Descritption</a>
 * @author CDS
 *@service cds.glu
 *@exclude 
 *@deprecated the webservice interface at CDS this client calls doesn't seem to be maintained, and this component of AR is no longer provided
 */
@Deprecated
public interface Glu {
    /** Resolve a tag.
     * @param id  a tag to resolve (example : VizieR.MetaCat)
 
     * @return Result :  URL corresponding to the tag (example : http://vizier.u-strasbg.fr/cgi-bin/votable?-meta)

     * @throws ServiceException if fails to connect to service at CDS.
     */
    public java.lang.String getURLfromTag(java.lang.String id) throws ServiceException;
}
