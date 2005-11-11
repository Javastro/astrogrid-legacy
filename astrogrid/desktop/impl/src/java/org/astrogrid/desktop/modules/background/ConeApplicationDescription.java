/*$Id: ConeApplicationDescription.java,v 1.3 2005/11/11 10:08:18 nw Exp $
 * Created on 19-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.acr.nvo.Cone;
import org.astrogrid.acr.nvo.ConeInformation;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.MandatoryParameterNotPassedException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.io.Piper;
import org.astrogrid.workflow.beans.v1.Tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** implementation of a cone-search cea proxy.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Oct-2005
 *
 */
public class ConeApplicationDescription extends AbstractApplicationDescription {

    /** Construct a new ConeApplicationDescription
     * @param arg0
     */
    public ConeApplicationDescription(ApplicationDescriptionEnvironment arg0, Cone cone, MyspaceInternal ms) {
        super(arg0);
        this.cone = cone;
        this.ms = ms;
        setName(ConeInformation.class.getName());
        iface = new BaseApplicationInterface("default",this);
    }
    final Cone cone;
    final MyspaceInternal ms;
    final ApplicationInterface iface;

    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String arg0, User arg1, Tool arg2) throws Exception {
        String newId = env.getIdGen().getNewID();
        final DefaultIDs ids  = new DefaultIDs(arg0,newId,arg1);
        return new ConeApplication(ids,arg2,env.getProtocolLib());
    }
    public class ConeApplication extends AbstractApplication {
        
        public ConeApplication(IDs arg0, Tool arg1,ProtocolLibrary arg3) {
            super(arg0, arg1, iface, arg3); // circumventing application interface here.
        }
        public boolean checkParameterValues() throws ParameterNotInInterfaceException,
                MandatoryParameterNotPassedException, ParameterDescriptionNotFoundException {
            return true; // don't care about parameter checking - wantto be freer in what we accept.
        }

         
        public Runnable createExecutionTask() throws CeaException {
           // createAdapters(); - nope, we'll do this by hand.
            getResult().clearResult();
            setStatus(Status.INITIALIZED);
            Runnable r = new Runnable() {
                public void run() {
                    final Map args= new HashMap();
                    OutputStream os = null;
                    try {
                        for (Iterator i = inputParameterValues(); i.hasNext();) {
                                ParameterValue input = (ParameterValue)i.next();
                                String val;
                                if (input.getIndirect()) {
                                    os = new ByteArrayOutputStream();
                                    InputStream is = ms.getInputStream(new URI(input.getValue()));
                                    Piper.pipe(is,os);
                                    is.close();
                                    val = os.toString();
                                } else {
                                    val = input.getValue();
                                }
                                 args.put(input.getName(),val);
                        }            
                        setStatus(Status.RUNNING);
                        URI endpoint = new URI(
                                (getTool().getName().startsWith("ivo://") ? "" : "ivo://") + getTool().getName()
                                );
                        URL query = cone.constructQuery(endpoint
                                ,Double.parseDouble(args.get("ra").toString())
                                ,Double.parseDouble(args.get("dec").toString())
                                ,Double.parseDouble(args.get("sz").toString())
                                );
                        args.remove("ra");
                        args.remove("dec");
                        args.remove("sz");
                        // process any remaining args..
                        for (Iterator i = args.entrySet().iterator(); i.hasNext(); ) {
                            Map.Entry e = (Map.Entry)i.next();
                            query = cone.addOption(query,e.getKey().toString(),e.getValue().toString());
                        }
                        //do call - behaviouor here depends on how the result is to be saved..
                        ParameterValue out = findOutputParameter("result");
                        if (out.getIndirect()) {
                       URI outUri = new URI(out.getValue());
                       setStatus(Status.WRITINGBACK);
                       // if it's a write to myspace, handle differently.
                        if (out.getValue().startsWith("ivo://")) {
                            ms.copyURLToContent(query,outUri);
                        } else {
                           os =ms.getOutputStream(outUri);
                           InputStream is = query.openStream();
                           Piper.pipe(is,os);
                           is.close();                           
                        }
                        } else { // inline result
                            os = new ByteArrayOutputStream();
                            InputStream is = query.openStream();
                            Piper.pipe(is,os);
                            is.close();
                            out.setValue(os.toString());
                        }
                        // set results
                        getResult().addResult(out);
                        setStatus(Status.COMPLETED);
                    } catch (Exception e) {
                        reportError("Failed to query siap service",e);
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // oh well.
                            }
                        }
                    }
                }
            };
            return r;            
        }
    }

}


/* 
$Log: ConeApplicationDescription.java,v $
Revision 1.3  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.2  2005/11/10 10:46:58  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/