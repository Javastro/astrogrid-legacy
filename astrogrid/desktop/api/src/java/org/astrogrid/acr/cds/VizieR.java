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
 * Access VizieR catalogues from CDS
 * @author CDS
 *@see http://cdsweb.u-strasbg.fr/cdsws/vizierAccess.gml
 *@service cds.vizier
 */
public interface VizieR {
    /** get metadata about catalogues.
     * @param target  (example : M32)
     * @param radius  (example : 0.1)
     * @param unit   (example : arcsec)
     * @param text   (author, ..., example : Ochsenbein)
     * @return  metadata about catalogues depending on the given parameters (VOTable format)

     * @throws ServiceException
     */
    public Document cataloguesMetaData(java.lang.String target, double radius, java.lang.String unit, java.lang.String text)throws ServiceException;
    /** get metadata about catalogues
     * @param target  (example : M32)
     * @param radius  (example : 0.1)
     * @param unit   (example : arcsec)
     * @param text   (author, ..., example : Ochsenbein)
     * @param wavelength (example : optical, Radio, like in the VizieR Web interface)

     * @return  metadata about catalogues depending on the given parameters (VOTable format)

     * @throws ServiceException
     */
    public Document cataloguesMetaDataWavelength(java.lang.String target, double radius, java.lang.String unit, java.lang.String text, java.lang.String wavelength) throws ServiceException;
    /**  get catalogue data
     * @param target  (example : M32)
     * @param radius  (example : 0.1)
     * @param unit   (example : arcsec)
     * @param text   (author, ..., example : Ochsenbein)
     * @return  data about catalogues depending on the given parameters (VOTable format)

     * @throws ServiceException
     */
    public Document cataloguesData(java.lang.String target, double radius, java.lang.String unit, java.lang.String text)throws ServiceException;
    /** get catalogue data for a wavelength
     * @param target  (example : M32)
     * @param radius  (example : 0.1)
     * @param unit   (example : arcsec)
     * @param text   (author, ..., example : Ochsenbein)
     * @param wavelength (example : optical, Radio, like in the VizieR Web interface)
     * @return data about catalogues depending on the given parameters (VOTable format)

     * @throws ServiceException
     */
    public Document cataloguesDataWavelength(java.lang.String target, double radius, java.lang.String unit, java.lang.String text, java.lang.String wavelength)throws ServiceException;
    /** get metadata for all catalogues
     * @return  all metadata about catalogues in VizieR (VOTable format)

     * @throws ServiceException
     */
    public Document metaAll() throws ServiceException;
}
