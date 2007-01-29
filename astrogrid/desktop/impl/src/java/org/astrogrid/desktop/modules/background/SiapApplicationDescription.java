/*$Id: SiapApplicationDescription.java,v 1.4 2007/01/29 11:11:36 nw Exp $
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.acr.ivoa.Siap;
import org.astrogrid.acr.ivoa.resource.SiapService;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.MandatoryParameterNotPassedException;
import org.astrogrid.applications.Status;
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

/** implementation of a siap search proxy.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 19-Oct-2005
 *
 */
public class SiapApplicationDescription extends AbstractApplicationDescription {



    /** Construct a new SiapApplicationDescription
     * @param arg0
     */
    public SiapApplicationDescription(ApplicationDescriptionEnvironment arg0,  Siap siap, MyspaceInternal ms) {        
        super(arg0);
        this.siap = siap;
        this.ms = ms;
        setName(SiapService.class.getName());
        iface = new BaseApplicationInterface("default",this);
    }
    final Siap siap;
    final MyspaceInternal ms;
    final ApplicationInterface iface;

    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String arg0, User arg1, Tool arg2) throws Exception {
        String newId = env.getIdGen().getNewID();
        final DefaultIDs ids = new DefaultIDs(arg0,newId,arg1);
        return new SiapApplication(ids,arg2,env.getProtocolLib());
    }
    
    public class SiapApplication extends AbstractApplication {

        public SiapApplication(DefaultIDs ids, Tool arg2, ProtocolLibrary protocolLib) {
            super(ids,arg2,iface,protocolLib);
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
                         String[] size = StringUtils.split(args.get("SIZE").toString(),","); // may contain one element, or two.
                         String[] pos = StringUtils.split(args.get("POS").toString(),",");
                         double ra = Double.parseDouble(pos[0].trim());
                         double dec = Double.parseDouble(pos[1].trim());
                         URL query;
                         if (size.length == 1) {
                             query = siap.constructQuery(endpoint,ra,dec,Double.parseDouble(size[0].trim()));
                         } else {
                             query = siap.constructQueryS(endpoint, ra,dec
                                         ,Double.parseDouble(size[0].trim())
                                         ,Double.parseDouble(size[1].trim())
                                         );
                         }

                         // process any remaining args..
                         for (Iterator i = args.entrySet().iterator(); i.hasNext(); ) {
                             Map.Entry e = (Map.Entry)i.next();
                             query = siap.addOption(query,e.getKey().toString(),e.getValue().toString());
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
    } //end inner class siap application.

}


/* 
$Log: SiapApplicationDescription.java,v $
Revision 1.4  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.3  2006/08/15 10:15:34  nw
migrated from old to new registry models.

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.42.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/