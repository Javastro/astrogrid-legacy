/*$Id: SiapInformation.java,v 1.1 2005/10/18 16:51:41 nw Exp $
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

import org.astrogrid.acr.astrogrid.ResourceInformation;

import java.net.URI;
import java.net.URL;

/**Information bean that describes a registered siap service
 *  * <p>
 * Adds fields for the extra service information provided by a SIAP registry entry. 
 * @see http://www.ivoa.net/xml/SIA/v0.7 for definition of this information
 * @xmlrpc returned as a struct, with keys corresponding to bean names.
 
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Oct-2005
 *
 */
public class SiapInformation extends ResourceInformation {

    /** Construct a new SiapInformation
     * @param ivorn
     * @param title
     * @param description
     * @param url
     */
    public SiapInformation(URI ivorn, String title, String description, URL url, 
            String imageServiceType, float maxQueryRegionSizeRa, float maxQueryRegionSizeDec,
            float maxImageExtentRa, float maxImageExtentDec, int maxImageSizeRa, int maxImageSizeDec,
            int maxFileSize, int maxRecords) {
        super(ivorn, title, description, url);
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
}


/* 
$Log: SiapInformation.java,v $
Revision 1.1  2005/10/18 16:51:41  nw
added information beans for siap and cone registry entries.
 
*/