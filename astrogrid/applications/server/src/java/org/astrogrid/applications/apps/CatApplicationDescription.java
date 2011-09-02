/*$Id: CatApplicationDescription.java,v 1.16 2011/09/02 21:55:54 pah Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.contracts.CEAConfiguration;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.Cardinality;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.InterfaceDefinition;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.description.cea.CeaApplication;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.description.intnl.InternallyConfiguredApplicationDescription;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.javainternal.JavaInternalApplication;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.MutableInternalValue;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterDirection;
import org.astrogrid.applications.parameter.StreamBasedInternalValue;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.io.Piper;
import org.astrogrid.security.SecurityGuard;

/** Simple application that behaves a bit like unix 'cat' - concatenates a bunch of files together.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Aug-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 13 Mar 2008
 *
 */
public class CatApplicationDescription extends InternallyConfiguredApplicationDescription
        implements ComponentDescriptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(CatApplicationDescription.class);
    private final static CeaApplication app = new CeaApplication();
    static {
	app.setIdentifier("ivo://org.astrogrid.unregistered/cat");
	addParameter(app,  "result", ParameterTypes.BINARY, "result", "result of concatenating data together");
	addParameter(app,  "src", ParameterTypes.BINARY, "src", "an input to concatenate");
	InterfaceDefinition intf = addInterface(app, "default");
	intf.addInputParameter("src", Cardinality.MANDATORY_REPEATED);
	intf.addOutputParameter("result");
   }

    /** Construct a new CatApplicationDescription
     * @param env
     * @param configuration 
     */
    public CatApplicationDescription( CEAConfiguration configuration) {
        super(app,  configuration);
               
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    @Override
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
     * @see org.astrogrid.applications.description.ApplicationDefinition#initializeApplication(java.lang.String, SecurityGuard, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String callerAssignedID,
            SecurityGuard secGuard, Tool tool) throws Exception {
        ApplicationInterface iface = this.getInterface(tool.getInterface());
        ApplicationEnvironment env = new ApplicationEnvironment(callerAssignedID, secGuard, getInternalComponentFactory().getIdGenerator(), conf);
	return new CatApplication(tool,iface,env , getInternalComponentFactory().getProtocolLibrary());
        
    }
    
    /**
     * A simple concatenation appliction.
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
     * @version $Name:  $
     * @since VOTech Stage 7
     */
    public static class CatApplication extends JavaInternalApplication {

        /** Construct a new CatApplication
         * @param ids
         * @param tool
         * @param applicationInterface
         * @param lib
         */
        public CatApplication( Tool tool, ApplicationInterface applicationInterface, ApplicationEnvironment env, ProtocolLibrary lib) {
            super( tool, applicationInterface, env, lib);
        }
        public void run() {
            try {
        	setStatus(Status.RUNNING);
        	List<InputStream> streams = new ArrayList<InputStream>();
        	for (Iterator i = inputParameterAdapters(); i.hasNext();) {
        	    ParameterAdapter input = (ParameterAdapter)i.next();
        	    reportMessage("reading in parameter " + input.getWrappedParameter().getValue());
        	    streams.add(input.getInternalValue().getStreamFrom());
        	}            
        	setStatus(Status.WRITINGBACK);
        	StreamParameterAdapter out= (StreamParameterAdapter)outputParameterAdapters().next(); // we know there's just the one.
        	out.writeBack(streams);
        	setStatus(Status.COMPLETED);
            } catch (CeaException e) {
        	reportError("something failed",e);
            }
        }
         @Override
	protected ParameterAdapter instantiateAdapter(ParameterValue pval,
                ParameterDescription descr, ParameterDirection dir, ExternalValue indirectVal) {
            return new StreamParameterAdapter(pval, descr, dir, applicationEnvironment);
        }

    }

    /** parameter adapter that produces and consumes streams, rather than string values - for efficiency when handling large amounts of data.
     * @todo maybe a candidate for factoring out into a package of useful parameter adapters later.*/
    public static class StreamParameterAdapter extends DefaultParameterAdapter {
        /** always returns an InputStream */
        @Override
	public MutableInternalValue getInternalValue() throws CeaException {
            if(internalVal == null){
            internalVal = new StreamBasedInternalValue(val, getProtocolLib(), env); 
            }
           return internalVal;
        }
        
        /** expects a list of input streams */
	public void writeBack(List<? extends InputStream> o) throws CeaException {
           OutputStream os = null;
           try {
                os = ((StreamBasedInternalValue)internalVal).getStreamTo();
                for (InputStream is : o) {                
                    try {
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
                throw new CeaException("Fialed to write back",e);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        logger.warn("failed to close output stream",e);
                }
            }
            }
                if (env == null) {
                    val.setValue(os.toString()); // uses byteArrayOutputStream's overloaded toString().
                }                
        }
        public StreamParameterAdapter(ParameterValue val, ParameterDescription description, ParameterDirection dir, ApplicationEnvironment env) {
            super(val, description, dir, env);
        }


    }

}


/* 
$Log: CatApplicationDescription.java,v $
Revision 1.16  2011/09/02 21:55:54  pah
result of merging the 2931 branch

Revision 1.15.2.2  2009/07/16 19:47:34  pah
ASSIGNED - bug 2950: rework parameterAdapter
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950

Revision 1.15.2.1  2009/07/15 09:46:14  pah
redesign of parameterAdapters

Revision 1.15  2009/02/26 12:45:55  pah
separate more out into cea-common for both client and server

Revision 1.14  2008/10/06 12:16:14  pah
factor out classes common to server and client

Revision 1.13  2008/09/24 13:40:49  pah
package naming changes

Revision 1.12  2008/09/18 09:13:39  pah
improved javadoc

Revision 1.11  2008/09/13 09:51:02  pah
code cleanup

Revision 1.10  2008/09/10 23:27:16  pah
moved all of http CEC and most of javaclass CEC code here into common library

Revision 1.3  2008/09/03 14:18:34  pah
result of merge of pah_cea_1611 branch

Revision 1.2.10.5  2008/09/03 12:01:56  pah
should perhaps be moved out of javaclass

Revision 1.2.10.4  2008/08/02 13:33:40  pah
safety checkin - on vacation

Revision 1.2.10.3  2008/05/13 15:14:07  pah
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708

Revision 1.2.10.2  2008/03/27 13:37:24  pah
now producing correct registry documents

Revision 1.2.10.1  2008/03/19 23:28:58  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

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