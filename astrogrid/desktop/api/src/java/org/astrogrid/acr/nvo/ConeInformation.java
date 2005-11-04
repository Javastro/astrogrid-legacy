/*$Id: ConeInformation.java,v 1.3 2005/11/04 14:38:58 nw Exp $
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
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.InterfaceBean;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.astrogrid.ParameterReferenceBean;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/** Information bean that describes a registered cone server
 * <p>
 * Adds fields for the extra service information provided by a cone search registry entry. 
 * @see http://www.ivoa.net/xml/ConeSearch/v0.3 for definition of this information
 * @xmlrpc returned as a struct, with keys corresponding to bean names.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Oct-2005
 *
 */
public class ConeInformation extends ApplicationInformation {

    /** Construct a new ConeInformation
     * @param ivorn
     * @param title
     * @param description
     * @param url
     */
    public ConeInformation(URI ivorn, String title, String description, URL url, URL logo, float maxSR,int maxRecords, boolean verbosity) {
        super(ivorn, title, description,parameters,ifaces,url,logo);
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
    
    private static Map parameters;
    private static InterfaceBean[] ifaces;
    static { // build static info for a cone applicaiton.
        // missing paramers are ucd, default value, units, type, subtype,options
        ParameterBean ra = new ParameterBean("ra","Right Ascension","right ascension of the position",null,null,null,"float",null,null); // @todo improve the metadata
        ParameterBean dec = new ParameterBean("dec","Declination","declination of the position",null,null,null,"float",null,null);
        ParameterBean sz = new ParameterBean("sz","Size","size of search radius",null,"1.0",null,"float",null,null);
        ParameterBean result = new ParameterBean("result","Result","Votable of results",null,null,null,"votable",null,null);
        parameters = new HashMap();
        parameters.put(ra.getName(),ra);
        parameters.put(dec.getName(),dec);
        parameters.put(sz.getName(),sz);
        parameters.put(result.getName(),result);
        
        ifaces = new InterfaceBean[] {
              new InterfaceBean(
                       "Cone"
                       , new ParameterReferenceBean[] {
                               new ParameterReferenceBean(ra.getName(),1,1)
                               , new ParameterReferenceBean(dec.getName(),1,1)
                               , new ParameterReferenceBean(sz.getName(),1,1)
                       }
                       , new ParameterReferenceBean[] {
                               new ParameterReferenceBean(result.getName(),1,1)
                       }
               )
        };
    }    
}


/* 
$Log: ConeInformation.java,v $
Revision 1.3  2005/11/04 14:38:58  nw
added logo field

Revision 1.2  2005/10/19 18:07:06  nw
changed inheritance hierarchy - now is a kind of application

Revision 1.1  2005/10/18 16:51:41  nw
added information beans for siap and cone registry entries.
 
*/