/*$Id: SiapInformation.java,v 1.4 2006/02/02 14:19:47 nw Exp $
 * Created on 18-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ivoa;

import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.ResourceInformation;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/** Description of the registry entry for a Simple Image Access (SIAP) service.
 *  
 * Adds fields for the extra service information provided by a SIAP registry entry. 
 * @see http://www.ivoa.net/xml/SIA/v0.7 for definition of this information
 * @xmlrpc returned as a struct, with keys corresponding to bean names.
 
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Oct-2005
 *
 */
public class SiapInformation extends ApplicationInformation {

    /** Construct a new SiapInformation
     * @param ivorn
     * @param title
     * @param description
     * @param url
     */
    public SiapInformation(URI ivorn, String title, String description, URL url, URL logo, 
            String imageServiceType, float maxQueryRegionSizeRa, float maxQueryRegionSizeDec,
            float maxImageExtentRa, float maxImageExtentDec, int maxImageSizeRa, int maxImageSizeDec,
            int maxFileSize, int maxRecords) {
        super(ivorn, title, description, parameters,ifaces,url,logo);
        this.imageServiceType = imageServiceType;
        this.maxQueryRegionSizeRa = maxQueryRegionSizeRa;
        this.maxQueryRegionSizeDec = maxQueryRegionSizeDec;
        this.maxImageExtentRa = maxImageExtentRa;
        this.maxImageExtentDec = maxImageExtentDec;
        this.maxImageSizeRa = maxImageSizeRa;
        this.maxImageSizeDec = maxImageSizeDec;
        this.maxFileSize = maxFileSize;
        this.maxRecords = maxRecords;
        
    }
    protected final String imageServiceType ;
    protected final float maxQueryRegionSizeRa;
    protected final float maxQueryRegionSizeDec;
    protected final float maxImageExtentRa;
    protected final float maxImageExtentDec;
    protected final int maxImageSizeRa;
    protected final int maxImageSizeDec;
    protected final int maxFileSize;
    protected final int maxRecords;

    public String getImageServiceType() {
        return this.imageServiceType;
    }
    public int getMaxFileSize() {
        return this.maxFileSize;
    }
    public float getMaxImageExtentDec() {
        return this.maxImageExtentDec;
    }
    public float getMaxImageExtentRa() {
        return this.maxImageExtentRa;
    }
    public int getMaxImageSizeDec() {
        return this.maxImageSizeDec;
    }
    public int getMaxImageSizeRa() {
        return this.maxImageSizeRa;
    }
    public float getMaxQueryRegionSizeDec() {
        return this.maxQueryRegionSizeDec;
    }
    public float getMaxQueryRegionSizeRa() {
        return this.maxQueryRegionSizeRa;
    }
    public int getMaxRecords() {
        return this.maxRecords;
    }
    
    private static Map parameters;
    private static InterfaceBean[] ifaces;
    static { // build static info for a siap applicaiton - doesn't mention optional parameters.
        // missing paramers are ucd, default value, units, type, subtype,options
        ParameterBean pos = new ParameterBean("POS","Position",
                "The position of the region of interest, expressed as the right-ascension and declination of the field center, in decimal degrees" +
                " using the ICRS coordinate system. A comma should delimit the two values; embedded whitespace is not permitted." +
                " Example: '12.821,-33.4",null,null,"decimal degrees","float,float",null,null);
        ParameterBean sz = new ParameterBean("SIZE","Size","The angular size of the region",null,null,"decimal degrees","float",null,null);
        ParameterBean format = new ParameterBean("FORMAT","Format","desired format(fs) of images",null,null,"comma-delimited list of: mime type|ALL|GRAPHIC|METADATA","String",null,null);
        ParameterBean intersect = new ParameterBean("INTERSECT","Intersect","indicates how matched images should intersec the  region of interest",
                null,"OVERLAPS",null,"String",null,new String[]{"COVERS","ENCLOSED","CENTER","OVERLAPS"});
        
        ParameterBean naxis = new ParameterBean("NAXIS","Naxis","Size of the output image in pixels",null,null,null,"int,int",null,null);
        ParameterBean cframe = new ParameterBean("CFRAME","CFrame","Coordinate system reference frame",null,"ICRS",null,"String",null,new String[]{"ICRS","FK5","FK4","ECL","GAL","SGAL"});
        ParameterBean equinox = new ParameterBean("EQUINOX","Equinox","Epoch of the mean equator and equinox for the specified CFrame",null,null,null,"String",null,null);
        ParameterBean crpix = new ParameterBean("CRPIX","CRPix","Coordinates of the reference pixel, expressed in the pixel coordinates of the output image",null,null,null,"int,int",null,null);
        ParameterBean crval = new ParameterBean("CRVAL","CRVal","World coordinates relative to CFrame at the reference point",null,null,null,"coordinates",null,null);
        ParameterBean cdelt = new ParameterBean("CDELT","CDelt","The scale of the output image",null,null,"decimal degrees per pixel","int,int",null,null);
        ParameterBean rotang = new ParameterBean("ROTANG","Rotation angle", "Rotation angle of the image in degrees relative to CFrame",null,"0",null,"angle",null,null);
        ParameterBean proj = new ParameterBean("PROJ","Projection","Celestial projection of the output image",null,"TAN",null,"String",null,null);
        ParameterBean verb = new ParameterBean("VERB","Verbose","Desired level of information to be returned",null,",1",null,"int",null,new String[]{"0","1","2","3"});
        
        ParameterBean result = new ParameterBean("result","Result","Votable of results",null,null,null,null,null,null);
        
        parameters = new HashMap();
        parameters.put(pos.getName(),pos);
        parameters.put(sz.getName(),sz);
        parameters.put(result.getName(),result);
        parameters.put(format.getName(),format);
        parameters.put(intersect.getName(),intersect);
        parameters.put(naxis.getName(),naxis);
        parameters.put(cframe.getName(),cframe);
        parameters.put(equinox.getName(),equinox);
        parameters.put(crpix.getName(),crpix);
        parameters.put(crval.getName(),crval);
        parameters.put(cdelt.getName(),cdelt);
        parameters.put(rotang.getName(),rotang);
        parameters.put(proj.getName(),proj);
        parameters.put(verb.getName(),verb);
        
        ifaces = new InterfaceBean[] {
                new InterfaceBean(
                        "Minimal"
                        , new ParameterReferenceBean[] {
                                new ParameterReferenceBean(pos.getName(),1,1)
                                , new ParameterReferenceBean(sz.getName(),1,1)
                        }
                        , new ParameterReferenceBean[] {
                                new ParameterReferenceBean(result.getName(),1,1)
                        }
                )                
               ,new InterfaceBean(
                       "Full"
                       , new ParameterReferenceBean[] {
                               new ParameterReferenceBean(pos.getName(),1,1)
                               , new ParameterReferenceBean(sz.getName(),1,1)
                               , new ParameterReferenceBean(intersect.getName(),1,0)
                               ,new ParameterReferenceBean(format.getName(),1,0)
                             ,new ParameterReferenceBean(naxis.getName(),1,0)
                             ,new ParameterReferenceBean(cframe.getName(),1,0)
                             ,new ParameterReferenceBean(equinox.getName(),1,0)
                             ,new ParameterReferenceBean(crpix.getName(),1,0)
                             ,new ParameterReferenceBean(crval.getName(),1,0)
                             ,new ParameterReferenceBean(cdelt.getName(),1,0)
                             ,new ParameterReferenceBean(rotang.getName(),1,0)
                             ,new ParameterReferenceBean(proj.getName(),1,0) 
                             ,new ParameterReferenceBean(verb.getName(),1,0)
                       }
                       , new ParameterReferenceBean[] {
                               new ParameterReferenceBean(result.getName(),1,1)
                       }
               )
        };
    }        
}


/* 
$Log: SiapInformation.java,v $
Revision 1.4  2006/02/02 14:19:47  nw
fixed up documentation.

Revision 1.3  2005/11/04 14:38:58  nw
added logo field

Revision 1.2  2005/10/19 18:07:06  nw
changed inheritance hierarchy - now is a kind of application

Revision 1.1  2005/10/18 16:51:41  nw
added information beans for siap and cone registry entries.
 
*/