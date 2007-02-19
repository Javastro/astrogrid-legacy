/*$Id: CatApplicationDescription.java,v 1.2 2007/02/19 16:20:23 gtr Exp $
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
        StringBuffer thename = new StringBuffer(env.getAuthIDResolver().getAuthorityID());
        thename.append("/concat");
        setName(thename.toString());
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
        public Runnable createExecutionTask() throws CeaException {
            createAdapters();
            setStatus(Status.INITIALIZED);
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        setStatus(Status.RUNNING);
                        List streams = new ArrayList();
                        for (Iterator i = inputParameterAdapters(); i.hasNext();) {
                                ParameterAdapter input = (ParameterAdapter)i.next();
                                reportMessage("reading in parameter " + input.getWrappedParameter().getValue());
                                streams.add(input.process());
                        }            
                        setStatus(Status.WRITINGBACK);
                        ParameterAdapter out= (ParameterAdapter)outputParameterAdapters().next(); // we know there's just the one.
                        out.writeBack(streams);
                        setStatus(Status.COMPLETED);
                    } catch (CeaException e) {
                        reportError("something failed",e);
                    }
                }
            };
            return r;            
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
                InputStream is = null;
                for (Iterator i = ((List)o).iterator(); i.hasNext(); ) {
                    try {
                        is = (InputStream)i.next();                
                        Piper.pipe(is,os); // hope this doesn't close the os.
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                logger.warn("failed to close input stream",e);
                            }
                    }
                }
                }
            } catch (IOException e) {
                throw new CeaException("Faled to write back",e);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        logger.warn("failed to close output stream",e);
                }
            }
            }
                if (externalVal == null) {
                    val.setValue(os.toString()); // uses byteArrayOutputStream's overloaded toString().
                }                
        }
        public StreamParameterAdapter(ParameterValue val, ParameterDescription description, ExternalValue externalVal) {
            super(val, description, externalVal);
        }


    }

}


/* 
$Log: CatApplicationDescription.java,v $
Revision 1.2  2007/02/19 16:20:23  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:17  gtr
no message

Revision 1.8  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.7.10.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.7  2004/09/22 10:52:50  pah
getting rid of some unused imports

Revision 1.6  2004/09/17 10:59:53  nw
made sure streams are closed

Revision 1.5  2004/09/17 01:21:12  nw
altered to work with new threadpool

Revision 1.4.10.1  2004/09/14 13:46:04  nw
upgraded to new threading practice.

Revision 1.4  2004/09/07 12:54:55  nw
put body in new thread - needs to be really.

Revision 1.3  2004/09/03 13:19:14  nw
added some progress messages

Revision 1.2  2004/08/17 15:07:25  nw
added concat application

Revision 1.1  2004/08/16 11:03:46  nw
first stab at a cat application
 
*/