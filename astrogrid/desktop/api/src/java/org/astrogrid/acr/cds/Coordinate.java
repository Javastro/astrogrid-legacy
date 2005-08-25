/*$Id: Coordinate.java,v 1.1 2005/08/25 16:59:44 nw Exp $
 * Created on 16-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.cds;

import org.astrogrid.acr.ServiceException;

/** Interface to CDS's Astronomical Coordinate XML Web Service
 * @author CDS
 * @see http://cdsweb.u-strasbg.fr/cdsws/astroCoo.gml
 * @service cds.coordinate
 *
 */
public interface Coordinate {
    /**convert a coordinate
     * @param x (10.0)
     * @param y (15.0)
     * @param z (20.0)
     * @param precision (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)
     * @return  a String like 03 45 14.3838 +47 58 07.990 (J2000.0)
     * @throws ServiceException if fails to communicate with web service.
     */
    public java.lang.String convert(double x, double y, double z, int precision) throws ServiceException;
    /**convert a longitude-lattitude coordinate
     * @param lon (12.0)
     * @param lat  (45.0)
     * @param precision (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)
     * @return  a String like 04 21 34. +53 32.5 (J2000.0)

     * @throws ServiceException if fails to communicate with web service.
     */
    public java.lang.String convertL(double lon, double lat, int precision) throws ServiceException;
    /**convert a coordinate, considering equinox
     * @param frame1 (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)

     * @param frame2 (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)

     * @param x  (10.0)
     * @param y     (15.0)
     * @param z  (20.0)
     * @param precision (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)

     * @param equinox1 (Julian Years or Besselian, unused for GAL, SGAL, ICRS)

     * @param equinox2 (Julian Years or Besselian, unused for GAL, SGAL, ICRS)

     * @return a String like 150.4806267 -05.3873952 (Gal)


     * @throws ServiceException if fails to communicate with web service.
     */
    public java.lang.String convertE(int frame1, int frame2, double x, double y, double z, int precision, double equinox1, double equinox2) throws ServiceException;
    /** convert a longitude-latitude coordinate,  considering equinox
     * @param frame1 (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)

     * @param frame2 (1=FK4, 2=GAL, 3=SGAL, 4=ECL, 5=FK5, 6=ICRS)
     * @param lon (12.0)
     *
     * @param lat (45.0)
     * @param precision (0=NONE, 1=DEG, 3=ARCMIN, 5=ARCSEC, 8=MAS)

     * @param equinox1 (Julian Years or Besselian, unused for GAL, SGAL, ICRS)

     * @param equinox2 (Julian Years or Besselian, unused for GAL, SGAL, ICRS)
     * @return

     * @throws ServiceException if fails to communicate with web service.
     */
    public java.lang.String convertLE(int frame1, int frame2, double lon, double lat, int precision, double equinox1, double equinox2) throws ServiceException;
   
}


/* 
$Log: Coordinate.java,v $
Revision 1.1  2005/08/25 16:59:44  nw
1.1-beta-3
 
*/