/*$Id: DatacenterApplicationDescription.java,v 1.1 2004/07/13 17:11:09 nw Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.service.v06;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.community.User;
import org.astrogrid.datacenter.service.DataServer;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationDescription extends AbstractApplicationDescription {
    public static final String CONE_IFACE = "cone";
    public static final String ADQL_IFACE = "adql";
    public static final String RA = "RA";
    public static final String DEC = "DEC";
    public static final String RADIUS = "Radius";
    public static final String RESULT = "Result";
    public static final String QUERY = "Query";
    public static final String FORMAT = "Format";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DatacenterApplicationDescription.class);

    /** Construct a new DatacenterApplicationDescription
     * @param arg0
     */
    public DatacenterApplicationDescription(DataServer ds,ApplicationDescriptionEnvironment arg0) {
        super(arg0);
        this.ds = ds;        
            try {
                this.createMetadata();
            }
            catch (ParameterDescriptionNotFoundException e) {
                // really should not happen
                logger.fatal("Programming error",e);
                throw new RuntimeException("Programming error",e);
            }
        
    }
    
    /** adds self-description bits to the application description
     *@todo get an astronomer-type to check over this metadata, verify it is sensible, etc.
     */
    protected final void createMetadata() throws ParameterDescriptionNotFoundException {
        this.setName("Datacenter");
        BaseParameterDescription query = new BaseParameterDescription();
        query.setDisplayDescription("Astronomy Data Query Language that defines the search criteria");
        query.setDisplayName(QUERY);
        query.setName(QUERY);
        query.setType(ParameterTypes.ADQL);
        this.addParameterDescription(query);
        
        
        BaseParameterDescription ra = new BaseParameterDescription();
        ra.setDisplayDescription("Right-Ascension of cone");
        ra.setDisplayName(RA);
        ra.setName(RA);
        ra.setType(ParameterTypes.RA);
        this.addParameterDescription(ra);
       
       BaseParameterDescription dec = new BaseParameterDescription();
       dec.setDisplayDescription("Declination of cone");
       dec.setDisplayName(DEC);
       dec.setName(DEC);
       dec.setType(ParameterTypes.DEC);
       this.addParameterDescription(dec);
       
       BaseParameterDescription radius = new BaseParameterDescription();
       radius.setDisplayDescription("Radius of cone");       radius.setDisplayName(RADIUS);
       radius.setName(RADIUS);
       radius.setType(ParameterTypes.DOUBLE); // assume this is correct.
       this.addParameterDescription(radius);
        
        BaseParameterDescription format = new BaseParameterDescription();
        format.setDisplayDescription("How the results are to be returned.  VOTABLE or CSV for now");
        format.setDisplayName(FORMAT);
        format.setName(FORMAT);
        format.setType(ParameterTypes.TEXT);        
        this.addParameterDescription(format);
        
        BaseParameterDescription result = new BaseParameterDescription();
        result.setDisplayDescription("Query results");
        result.setDisplayName(RESULT);
        result.setName(RESULT);
        result.setType(ParameterTypes.TEXT); // probably votable.
        this.addParameterDescription(result);
        
        BaseApplicationInterface adql = new BaseApplicationInterface(ADQL_IFACE,this);        
        this.addInterface(adql);
        adql.addInputParameter(FORMAT);
        adql.addInputParameter(QUERY);
        adql.addOutputParameter(RESULT);
        
        BaseApplicationInterface cone = new BaseApplicationInterface(CONE_IFACE,this);
        this.addInterface(cone);
        cone.addInputParameter(FORMAT);
        cone.addInputParameter(RA);
        cone.addInputParameter(DEC);
        cone.addInputParameter(RADIUS);
        cone.addOutputParameter(RESULT);
       
    }
    
    protected final DataServer ds;
    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String id, User user, Tool tool) throws Exception {
        String newID = env.getIdGen().getNewID();
        final DefaultIDs ids = new DefaultIDs(id,newID,user);
        ApplicationInterface interf = this.getInterface(tool.getInterface());    
        return new DatacenterApplication(ids,tool,interf,env.getProtocolLib(),ds);
    }
    
}


/* 
$Log: DatacenterApplicationDescription.java,v $
Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/