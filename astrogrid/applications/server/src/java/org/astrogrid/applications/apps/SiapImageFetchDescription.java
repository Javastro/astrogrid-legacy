/*$Id: SiapImageFetchDescription.java,v 1.2 2004/11/29 20:00:56 clq2 Exp $
 * Created on 15-Nov-2004
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.AbstractApplication.IDs;
import org.astrogrid.applications.apps.CatApplicationDescription.CatApplication;
import org.astrogrid.applications.apps.CatApplicationDescription.StreamParameterAdapter;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;
import org.astrogrid.util.TimeStamp;
import org.astrogrid.workflow.beans.v1.Tool;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.util.DataSource;

import junit.framework.Test;

/** @todo implementation is a bit tatty - could do with refactoring a bunch of these applications,
 * improving the internal design, and extracting generally useful helper classes.
 * for astrogrid 2 - this code is ugly, but works for now.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Nov-2004
 *
 */
public class SiapImageFetchDescription extends AbstractApplicationDescription
        implements ComponentDescriptor {
    static final String URLS = "urls";
    static final String IVORNS="ivorns";
    static final String BASEIVORN = "baseIvorn";
    static final String TABLE = "table";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(SiapImageFetchDescription.class);

    /** Construct a new SiapImageFetchDescription
     * @param env
     */
    public SiapImageFetchDescription(ApplicationDescriptionEnvironment env) {
        super(env);
        this.setMetaData();
    }
    
    /** set up metadata for this instance */
    private final void setMetaData() {
        setName("org.astrogrid.localhost/siap-image-fetch");
        BaseParameterDescription src = new BaseParameterDescription();
        src.setName(TABLE);
        src.setDisplayName("Image List");
        src.setDisplayDescription("VOTable containing URIs of images");
        src.setType(ParameterTypes.VOTABLE);
        this.addParameterDescription(src);
        
        BaseParameterDescription baseDir = new BaseParameterDescription();
        baseDir.setName(BASEIVORN);
        baseDir.setDisplayName("Save To");
        baseDir.setDisplayDescription("Location of a directory in myspace in which to save fetched images");
        baseDir.setType(ParameterTypes.ANYURI);
        baseDir.setSubType("ivo://..");
        this.addParameterDescription(baseDir);
        
        BaseParameterDescription urls = new BaseParameterDescription();
        urls.setName(URLS);
        urls.setDisplayName("URLs of files to fetch");
        urls.setDisplayDescription("List of  the urls  of the fetched files");
        urls.setType(ParameterTypes.TEXT);
        this.addParameterDescription(urls);
        
        BaseParameterDescription ivorns = new BaseParameterDescription();
        ivorns.setName(IVORNS);
        ivorns.setDisplayName("Ivorns of fetched Files");
        ivorns.setDisplayDescription("List of  the ivorns of the fetched files");
        ivorns.setType(ParameterTypes.TEXT);
        this.addParameterDescription(ivorns);
        
        BaseApplicationInterface intf = new BaseApplicationInterface("basic",this);
        try {
            intf.addInputParameter(src.getName());
            intf.addInputParameter(baseDir.getName());
            intf.addOutputParameter(urls.getName());
            intf.addOutputParameter(ivorns.getName());
        } catch (ParameterDescriptionNotFoundException e) {
            logger.fatal("Programming error");
            throw new RuntimeException("Programming Error",e);
        }
        this.addInterface(intf);
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Application that parses results from a siap query and saves to myspace";
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
        return new SiapImageFetchApplication(ids,tool,iface,env.getProtocolLib());
    }
    
    public static class ParameterAdapterDataSource extends DataSource {
        public ParameterAdapterDataSource(CatApplicationDescription.StreamParameterAdapter p) {
            this.p = p;
        }
        CatApplicationDescription.StreamParameterAdapter p;
        protected InputStream getRawInputStream() throws IOException {
            
            try {
                return (InputStream)p.process();
            } catch (CeaException e) {
             throw new IOException(e.getMessage());
            }
        }
    }
    

}


/* 
$Log: SiapImageFetchDescription.java,v $
Revision 1.2  2004/11/29 20:00:56  clq2
nww-itn07-684

Revision 1.1.2.2  2004/11/24 00:14:58  nw
factored the application class out of the descripiton - makes for
more manageable code.

Revision 1.1.2.1  2004/11/22 14:06:13  nw
start on implementing these.
 
*/