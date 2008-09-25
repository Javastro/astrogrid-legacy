/**
 * VizieR.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.acr.cds;

import org.astrogrid.acr.ServiceException;
import org.w3c.dom.Document;
/**
 * AR Service: Access VizieR catalogues, from CDS
 * @author CDS
 *@see <a href="http://cdsweb.u-strasbg.fr/cdsws/vizierAccess.gml">Webservice Description</a>
 *@service cds.vizier
 */
public interface VizieR {
    /** Get metadata about catalogues.
     * @param target  (example : {@code M32})
     * @param radius  (example : {@code 0.1})
     * @param unit   (example : {@code arcsec})
     * @param text   (author, ..., example : {@code Ochsenbein})
     * @return  metadata about catalogues depending on the given parameters (VOTable format)
     * @see <a href='http://www.ivoa.net/Documents/latest/VOT.html'>VOTable Specification</a>
     * @throws ServiceException
     */
    public Document cataloguesMetaData(java.lang.String target, double radius, java.lang.String unit, java.lang.String text)throws ServiceException;
    /** Get metadata about catalogues, with wavelength criteria.
     * @param target  (example : {@code M32})
     * @param radius  (example : {@code 0.1})
     * @param unit   (example : {@code arcsec})
     * @param text   (author, ..., example : {@code Ochsenbein})
     * @param wavelength (example : {@code optical, Radio} like in the VizieR Web interface)

     * @return  metadata about catalogues depending on the given parameters (VOTable format)
     * @see <a href='http://www.ivoa.net/Documents/latest/VOT.html'>VOTable Specification</a>
     * @throws ServiceException
     */
    public Document cataloguesMetaDataWavelength(java.lang.String target, double radius, java.lang.String unit, java.lang.String text, java.lang.String wavelength) throws ServiceException;
    /**  Get catalogue data.
     * @param target  (example : {@code M32})
     * @param radius  (example : {@code 0.1})
     * @param unit   (example : {@code arcsec})
     * @param text   (author, ..., example : {@code Ochsenbein})
     * @return  data about catalogues depending on the given parameters (VOTable format)
     * @see <a href='http://www.ivoa.net/Documents/latest/VOT.html'>VOTable Specification</a>
     * @throws ServiceException
     */
    public Document cataloguesData(java.lang.String target, double radius, java.lang.String unit, java.lang.String text)throws ServiceException;
    /** Get catalogue data, with wavelength criteria.
     * @param target  (example : {@code M32})
     * @param radius  (example : {@code 0.1})
     * @param unit   (example : {@code arcsec})
     * @param text   (author, ..., example : {@code Ochsenbein})
     * @param wavelength (example : {@code optical, Radio}, like in the VizieR Web interface)
     * @return data about catalogues depending on the given parameters (VOTable format)
     * @see <a href='http://www.ivoa.net/Documents/latest/VOT.html'>VOTable Specification</a>
     * @throws ServiceException
     */
    public Document cataloguesDataWavelength(java.lang.String target, double radius, java.lang.String unit, java.lang.String text, java.lang.String wavelength)throws ServiceException;
    /** get metadata for all catalogues.
     * {@stickyWarning Will return a large amount of data. }
     * @return  all metadata about catalogues in VizieR (VOTable format)
     * @see <a href='http://www.ivoa.net/Documents/latest/VOT.html'>VOTable Specification</a>
     * @throws ServiceException
     */
    public Document metaAll() throws ServiceException;
}
