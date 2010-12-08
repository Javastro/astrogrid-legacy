/*$Id: DatacenterApplicationDescription.java,v 1.3 2010/12/08 12:46:36 gtr Exp $
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
import org.astrogrid.applications.beans.v1.parameters.OptionList;
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
    public static final String CONE_IFACE = "ConeSearch";
    /** name of the adql search interface */
    public static final String ADQL_IFACE = "ADQL";
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
    public static final String[] FORMAT_OPTIONS = 
       {"VOTABLE", "VOTABLE-BINARY", "COMMA-SEPARATED", "HTML"};
    /** name of the 'catalog.table' parameter */
    public static final String CATTABLE = "CatTable";

    /* These additional parameters are for the "Multicone" 
     * simple crossmatch service */
    /** name of the simple crossmatch search interface */
    public static final String MULTICONE_IFACE = "MultiCone";
    /** Where the VOTable with rows to be matched is located */
    public static final String INPUT_VOTABLE = "Input_VOTable";
    /** name of the column (or expression) giving Right Ascension in degrees */
    public static final String RA_COL = "RA_Column_Name";
    /** name of the column (or expression) giving Declination in degrees */
    public static final String DEC_COL = "Dec_Column_Name";
    /** name of (optional) flag indicating match type */
    public static final String FIND_MODE = "Find_Mode";
    public static final String[] FIND_MODE_OPTIONS = {"BEST", "ALL"};

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
        format.setDisplayDescription("Format in which the results are to be returned.");
        format.setDisplayName(FORMAT);
        format.setName(FORMAT);
        format.setDefaultValue("VOTABLE");
        format.setType(ParameterTypes.TEXT);
        OptionList formatOptionList = new OptionList();
        formatOptionList.setOptionVal(FORMAT_OPTIONS);
        format.setOptionList(formatOptionList);
        this.addParameterDescription(format);

        BaseParameterDescription catTab = new BaseParameterDescription();
        catTab.setDisplayDescription("The table to be searched (with catalog prefix)");
        catTab.setDisplayName(CATTABLE);
        catTab.setName(CATTABLE);
        catTab.setDefaultValue("");
        catTab.setType(ParameterTypes.TEXT);
        this.addParameterDescription(catTab);
        
        BaseParameterDescription result = new BaseParameterDescription();
        result.setDisplayDescription("Query results");
        result.setDisplayName(RESULT);
        result.setName(RESULT);
        result.setType(ParameterTypes.TEXT); // probably votable.
        this.addParameterDescription(result);
        
        BaseParameterDescription inputVotable = new BaseParameterDescription();
        inputVotable.setDisplayDescription("Input VOTable of positions for matching against");
        inputVotable.setDisplayName(INPUT_VOTABLE);
        inputVotable.setName(INPUT_VOTABLE);
        inputVotable.setType(ParameterTypes.ANYURI);
        inputVotable.setType(ParameterTypes.TEXT);   
        this.addParameterDescription(inputVotable);

        BaseParameterDescription raExpr = new BaseParameterDescription();
        raExpr.setDisplayDescription("Name for input Right-Ascension column (or expression): column data in degrees");
        raExpr.setDisplayName(RA_COL);
        raExpr.setName(RA_COL);
        raExpr.setType(ParameterTypes.TEXT);
        this.addParameterDescription(raExpr);
       
        BaseParameterDescription decExpr = new BaseParameterDescription();
        decExpr.setDisplayDescription("Name for input Declination column (or expression): column data in degrees");
        decExpr.setDisplayName(DEC_COL);
        decExpr.setName(DEC_COL);
        decExpr.setType(ParameterTypes.TEXT);
        this.addParameterDescription(decExpr);

        BaseParameterDescription findMode = new BaseParameterDescription();
        findMode.setDisplayDescription("Find mode for matches");
        findMode.setDisplayName(FIND_MODE);
        findMode.setName(FIND_MODE);
        findMode.setType(ParameterTypes.TEXT);
        OptionList findModeOptionList = new OptionList();
        findModeOptionList.setOptionVal(FIND_MODE_OPTIONS);
        findMode.setOptionList(findModeOptionList);
        findMode.setDefaultValue("BEST");
        this.addParameterDescription(findMode);

        BaseApplicationInterface adql = new BaseApplicationInterface(ADQL_IFACE,this);
        this.addInterface(adql);
        adql.addInputParameter(FORMAT);
        adql.addInputParameter(QUERY);
        adql.addOutputParameter(RESULT);
   
        BaseApplicationInterface cone = new BaseApplicationInterface(CONE_IFACE,this);
        this.addInterface(cone);
        cone.addInputParameter(CATTABLE);
        cone.addInputParameter(FORMAT);
        cone.addInputParameter(RA);
        cone.addInputParameter(DEC);
        cone.addInputParameter(RADIUS);
        cone.addOutputParameter(RESULT);

        // Note: format of multicone output is fixed to VOTable right now
        BaseApplicationInterface multicone = new BaseApplicationInterface(MULTICONE_IFACE,this);
        this.addInterface(multicone);
        multicone.addInputParameter(CATTABLE);
        multicone.addInputParameter(RA_COL);
        multicone.addInputParameter(DEC_COL);
        multicone.addInputParameter(RADIUS);
        multicone.addInputParameter(FIND_MODE);
        multicone.addInputParameter(INPUT_VOTABLE);
        multicone.addOutputParameter(RESULT);
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
        return new DatacenterApplication(ids,tool,interf,env.getProtocolLib(),exec);
    }
    
}