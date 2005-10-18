/*$Id: ConeInformation.java,v 1.1 2005/10/18 16:51:41 nw Exp $
 * Created on 18-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.nvo;

import org.astrogrid.acr.astrogrid.AbstractInformation;
import org.astrogrid.acr.astrogrid.ResourceInformation;

import java.net.URI;
import java.net.URL;

/** Information bean that describes a registered cone server
 * <p>
 * Adds fields for the extra service information provided by a cone search registry entry. 
 * @see http://www.ivoa.net/xml/ConeSearch/v0.3 for definition of this information
 * @xmlrpc returned as a struct, with keys corresponding to bean names.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Oct-2005
 *
 */
public class ConeInformation extends ResourceInformation {

    /** Construct a new ConeInformation
     * @param ivorn
     * @param title
     * @param description
     * @param url
     */
    public ConeInformation(URI ivorn, String title, String description, URL url,float maxSR,int maxRecords, boolean verbosity) {
        super(ivorn, title, description, url);
        this.maxSR = maxSR;
        this.maxRecords = maxRecords;
        this.verbosity = verbosity;
    }

    protected final float maxSR;
    protected final int maxRecords;
    protected final boolean verbosity;
    

    /** access the maximum number of records this service will return */
    public int getMaxRecords() {
        return this.maxRecords;
    }
    /** access the maximum search radius, in degrees,  this service will accept */
    public float getMaxSR() {
        return this.maxSR;
    }
    /** true if this service supports the <tt>VERB</tt> keyword */
    public boolean isVerbosity() {
        return this.verbosity;
    }
}


/* 
$Log: ConeInformation.java,v $
Revision 1.1  2005/10/18 16:51:41  nw
added information beans for siap and cone registry entries.
 
*/