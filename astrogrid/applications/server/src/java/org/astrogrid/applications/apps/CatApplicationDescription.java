/*$Id: CatApplicationDescription.java,v 1.1 2004/08/16 11:03:46 nw Exp $
 * Created on 16-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.apps;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.Application;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.Cardinality;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.community.User;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.workflow.beans.v1.Tool;

import junit.framework.Test;

/** Simple application that behaves a bit like unix 'cat' - concatenates a bunch of files together.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Aug-2004
 *
 */
public class CatApplicationDescription extends AbstractApplicationDescription
        implements ComponentDescriptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(CatApplicationDescription.class);

    /** Construct a new CatApplicationDescription
     * @param env
     */
    public CatApplicationDescription(ApplicationDescriptionEnvironment env) {
        super(env);
        this.setMetaData();        
    }
    
    /** set up metadata for this instance */
    private final void setMetaData() {
        setName("org.astrogrid.localhost/cea");
        BaseParameterDescription result = new BaseParameterDescription();
        result.setName("result");
        result.setDisplayName("Result");
        result.setDisplayDescription("result of concatenating data together");
        result.setType(ParameterTypes.BINARY);
        this.addParameterDescription(result);
        
        BaseParameterDescription src = new BaseParameterDescription();
        src.setName("src");
        src.setDisplayName("Source");
        src.setDisplayDescription("an input to concatenate");
        src.setType(ParameterTypes.BINARY);
        this.addParameterDescription(src);
        
        BaseApplicationInterface intf = new BaseApplicationInterface("basic",this);
        try {
            intf.addInputParameter(src.getName(),Cardinality.MANDATORY_REPEATED);
            intf.addOutputParameter(result.getName());
            
        } catch (ParameterDescriptionNotFoundException e) {
            logger.fatal("Programming error",e); // really shouldn't happen
            throw new RuntimeException("Programming Error",e);
        }
        
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return null;
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }

    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String callerAssignedID,
            User user, Tool tool) throws Exception {
        return null;
    }

}


/* 
$Log: CatApplicationDescription.java,v $
Revision 1.1  2004/08/16 11:03:46  nw
first stab at a cat application
 
*/