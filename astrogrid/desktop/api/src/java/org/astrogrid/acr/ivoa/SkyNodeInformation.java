/*$Id: SkyNodeInformation.java,v 1.2 2006/04/18 23:25:45 nw Exp $
 * Created on 21-Feb-2006
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

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Description of the registry entry for a SkyNode service
 * 
 * Adds fields for the extra service information provided by a SkyNode registry entry.
 * @see 
http://www.ivoa.net/Documents/latest/SkyNodeInterface.html for definition of this information
@xmlrpc returned as a struct, with keys corresponding to bean names.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Feb-2006
 *
 */
public class SkyNodeInformation extends ApplicationInformation {

    public SkyNodeInformation(URI id, String name, String description, URL endpoint, URL logo
            ,String compliance, double latitude, double longitude, long maxRecords, String primaryTable, String primaryKey) {
        super(id, name, description, parameters, ifaces, endpoint, logo);
        this.compliance = compliance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxRecords = maxRecords;
        this.primaryTable = primaryTable;
        this.primaryKey = primaryKey;
    }
    

    static final long serialVersionUID = 6247601098828504183L;
    private final String compliance;
    private final double latitude;
    private final double longitude;
    private final long maxRecords;
    private final String primaryTable;
    private final String primaryKey;
   // later - accepts UCDs
    
    public final static String FULL = "FULL";
    public final static String PARTIAL = "PARTIAL";
    public final static String BASIC = "BASIC";
    
    
    
    
    private static Map parameters;
    private static InterfaceBean[] ifaces;
    static { // build up static info for a skynode application
        ParameterBean query = new ParameterBean("ADQL","Query"
                ,"An ADQL Query",null,null,null,"ADQL",null,null);
        ParameterBean format = new ParameterBean("FORMAT","Format","desired format of results",null,"VOTable",null,"String",null,null);
        ParameterBean result = new ParameterBean("result","Result","Votable of results",null,null,null,null,null,null);
        
        parameters = new HashMap();
        parameters.put(query.getName(),query);
        parameters.put(format.getName(),format);
        parameters.put(result.getName(),result);
        
        ifaces = new InterfaceBean[] {
                new InterfaceBean(
                        "Default"
                        ,new ParameterReferenceBean[]{
                                new ParameterReferenceBean(query.getName(),1,1)
                                , new ParameterReferenceBean(format.getName(),0,1)                                
                        }
                        ,new ParameterReferenceBean[] {
                                new ParameterReferenceBean(format.getName(),1,1)
                        }
                        )
        };
    }
    
    /** returns a description of the compliance of this service
     * 
     * @return FULL | PARTIAL | BASIC
     */
    public String getCompliance() {
        return this.compliance;
    }
    /**
     * gps latitude of server 
     * @return
     */
    public double getLattitude() {
        return this.latitude;
    }
    /** gps longitude of server
     * 
     * @return
     */
    public double getLongitude() {
        return this.longitude;
    }
    /** maximum number of records this server will return in response to a query
     * 
     * @return
     */
    public long getMaxRecords() {
        return this.maxRecords;
    }
    /** name of the primary key column of the primary table
     * 
     * @return
     */
    public String getPrimaryKey() {
        return this.primaryKey;
    }
    /** name of the primary table
     * 
     * @return
     */
    public String getPrimaryTable() {
        return this.primaryTable;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("SkyNodeInformation[");        
        sb.append(super.toString());
        sb.append(" complicance: ");
        sb.append(compliance);
        sb.append(" lattitude: ");
        sb.append(latitude);
        sb.append(" longitude: ");
        sb.append(longitude);
        sb.append(" maxRecords: ");
        sb.append(maxRecords);
        sb.append(" primaryKey: ");
        sb.append(primaryKey);
        sb.append("]");
        return sb.toString();
    }
}


/* 
$Log: SkyNodeInformation.java,v $
Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/