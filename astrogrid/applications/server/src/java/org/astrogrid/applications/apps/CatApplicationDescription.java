/*$Id: CatApplicationDescription.java,v 1.2 2004/08/17 15:07:25 nw Exp $
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.AbstractApplication.IDs;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.Cardinality;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.io.Piper;
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
        setName("org.astrogrid.localhost/concat");
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
        this.addInterface(intf);
        
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Cat application\n" + this.toString();
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
        String newID = env.getIdGen().getNewID();
        final DefaultIDs ids = new DefaultIDs(callerAssignedID,newID,user);
        ApplicationInterface iface = this.getInterface(tool.getInterface());
        return new CatApplication(ids,tool,iface,env.getProtocolLib());
        
    }
    
    public static class CatApplication extends AbstractApplication {

        /** Construct a new CatApplication
         * @param ids
         * @param tool
         * @param applicationInterface
         * @param lib
         */
        public CatApplication(IDs ids, Tool tool, ApplicationInterface applicationInterface, ProtocolLibrary lib) {
            super(ids, tool, applicationInterface, lib);
        }

        /**
         * @see org.astrogrid.applications.AbstractApplication#execute()
         */
        public boolean execute() throws CeaException {
            createAdapters();
            setStatus(Status.INITIALIZED);
            setStatus(Status.RUNNING);
            List streams = new ArrayList();
            for (Iterator i = inputParameterAdapters(); i.hasNext();) {
                ParameterAdapter input = (ParameterAdapter)i.next();
                streams.add(input.process());
            }            
            setStatus(Status.WRITINGBACK);
            ParameterAdapter out= (ParameterAdapter)outputParameterAdapters().next(); // we know there's just the one.
            out.writeBack(streams);
            setStatus(Status.COMPLETED);
            return true;
                        
        }
        protected ParameterAdapter instantiateAdapter(ParameterValue pval,
                ParameterDescription descr, ExternalValue indirectVal) {
            return new StreamParameterAdapter(pval, descr, indirectVal);
        }
    }

    /** parameter adapter that produces and consumes streams, rather than string values - for efficiency when handling large amounts of data.
     * @todo maybe a candidate for factoring out into a package of useful parameter adapters later.*/
    public static class StreamParameterAdapter extends DefaultParameterAdapter {
        /** always returns an InputStream */
        public Object process() throws CeaException {
            if (externalVal == null) {
                return new ByteArrayInputStream(val.getValue().getBytes());
            } else {
                return externalVal.read();
            }
        }
        
        /** expects a list of input streams */
        public void writeBack(Object o) throws CeaException {
            if (! (o instanceof List)) {
                throw new CeaException("Programming error - expected List of Streams, got " + o.getClass().getName());                
            }
            OutputStream os = null;
            if (externalVal == null) {
                os = new ByteArrayOutputStream();
            } else {
                os  = externalVal.write();
            }
            try {
                for (Iterator i = ((List)o).iterator(); i.hasNext(); ) {
                    InputStream is = (InputStream)i.next();                
                    Piper.pipe(is,os); // hope this doesn't close the os.
                }
                os.close();
                if (externalVal == null) {
                    val.setValue(os.toString()); // uses byteArrayOutputStream's overloaded toString().
                }
            } catch (IOException e) {
                throw new CeaException("Faled to write back",e);
            }
        }
        public StreamParameterAdapter(ParameterValue val, ParameterDescription description, ExternalValue externalVal) {
            super(val, description, externalVal);
        }


    }

}


/* 
$Log: CatApplicationDescription.java,v $
Revision 1.2  2004/08/17 15:07:25  nw
added concat application

Revision 1.1  2004/08/16 11:03:46  nw
first stab at a cat application
 
*/