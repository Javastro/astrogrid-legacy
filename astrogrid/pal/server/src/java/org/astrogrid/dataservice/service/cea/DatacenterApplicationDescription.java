/*$Id: DatacenterApplicationDescription.java,v 1.2 2005/11/21 12:54:18 clq2 Exp $
 * Created on 12-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.dataservice.service.cea;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.astrogrid.dataservice.service.DataServer;
import org.astrogrid.workflow.beans.v1.Tool;

/** Descrption object for the datacenter 'application'.
 * Supports two interfaces - a cone search interface, and a full ADQL.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Jul-2004
 *
 */
public class DatacenterApplicationDescription extends AbstractApplicationDescription {
    /** name of the cone search interface */
    public static final String CONE_IFACE = "cone";
    /** name of the adql search interface */
    public static final String ADQL_IFACE = "adql";
    /** name of the 'ra' parameter */
    public static final String RA = "RA";
    /** name of the 'dec' parameter */
    public static final String DEC = "DEC";
    /** name of the 'radius' parameter */
    public static final String RADIUS = "Radius";
    /** name of the 'result' output parameter */
    public static final String RESULT = "Result";
    /** name of the 'query' parameter */
    public static final String QUERY = "Query";
    /** name of the 'format' parameter */
    public static final String FORMAT = "Format";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DatacenterApplicationDescription.class);

    /** Construct a new DatacenterApplicationDescription
     * @param arg0
     */
    public DatacenterApplicationDescription(String name,DataServer ds,ApplicationDescriptionEnvironment arg0, Executor exec) {
        super(arg0);
        this.ds = ds;
        this.exec = exec;
            try {
                this.createMetadata(name);
            }
            catch (ParameterDescriptionNotFoundException e) {
                // really should not happen - as statically build.
                logger.fatal("Programming error",e);
                throw new RuntimeException("Programming error",e);
            }
        
    }
    
    /** adds self-description bits to the application description
     *@todo get an astronomer-type to check over this metadata, verify it is sensible, etc.
     */
    protected final void createMetadata(String name) throws ParameterDescriptionNotFoundException {
        this.setName(name);
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
       radius.setDisplayDescription("Radius of cone");
       radius.setDisplayName(RADIUS);
       radius.setName(RADIUS);
       radius.setType(ParameterTypes.DOUBLE); // assume this is correct.
       this.addParameterDescription(radius);
        
        BaseParameterDescription format = new BaseParameterDescription();
        format.setDisplayDescription("How the results are to be returned.  VOTABLE or CSV for now");
        format.setDisplayName(FORMAT);
        format.setName(FORMAT);
        format.setDefaultValue("VOTABLE");
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
   
       //removed cone altogether for the moment, otherwise it turns up before the adql i/f on the portal
       //UI, which at the omment can only handle one i/f
       // PUT BACK BY KEA WHEN FIXING UNIT TESTS - JDT says portal can now cope 
        BaseApplicationInterface cone = new BaseApplicationInterface(CONE_IFACE,this);
        this.addInterface(cone);
        cone.addInputParameter(FORMAT);
        cone.addInputParameter(RA);
        cone.addInputParameter(DEC);
        cone.addInputParameter(RADIUS);
        cone.addOutputParameter(RESULT);
    }
    
    protected final DataServer ds;
    protected final Executor exec;
    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String id, User user, Tool tool) throws Exception {
        String newID = env.getIdGen().getNewID();
        logger.debug("Initializing new datacenter application " + newID + " " + id);
        final DefaultIDs ids = new DefaultIDs(id,newID, user);
        ApplicationInterface interf = this.getInterface(tool.getInterface());
        return new DatacenterApplication(ids,tool,interf,env.getProtocolLib(),ds,exec);
    }
    
}


/*
$Log: DatacenterApplicationDescription.java,v $
Revision 1.2  2005/11/21 12:54:18  clq2
DSA_KEA_1451

Revision 1.1.1.1.66.1  2005/11/15 15:33:34  kea
Re-instated cone-search interface (in addition to existing adql interface)
as JDT says portal can now cope with >1 interface.

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.2.10.1  2004/11/22 00:57:16  mch
New interfaces for SIAP etc and new slinger package

Revision 1.2  2004/11/08 02:58:44  mch
Various fixes and better error messages

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.3  2004/09/17 01:27:21  nw
added thread management.

Revision 1.2  2004/07/20 02:14:48  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/
