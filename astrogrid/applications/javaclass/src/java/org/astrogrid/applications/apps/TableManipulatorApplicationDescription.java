/*$Id: TableManipulatorApplicationDescription.java,v 1.2 2007/02/19 16:20:22 gtr Exp $
 * Created on 11-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.apps;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.apps.tables.TableConverter;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.StreamParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import uk.ac.starlink.table.StarTableFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Description of a generic table Manipulation application.
 * <P>
 * this application can
 * <ul>
 * <li> Transform tables from one representation to another - e.g. CSV to VOTable
 * <li> Manipulation of table columns within a single table
 * <li> limited cross matching facility
 * </ul>
 * <p> it is based on the Starlink STIL package for generic table manipulations.
 * @author Paul Harrison (pah@jb.man.ac.uk)
 *
 */
public class TableManipulatorApplicationDescription extends AbstractApplicationDescription implements ComponentDescriptor {
    private static final String INPUTTABLENAME = "INTABLE";
   private static final String INPUT2TABLENAME = "INTABLE2";
   private static final String OUTPUTTABLENAME = "OUTTABLE";
   private static final String CONVERT = "convert";
   private static final String OUTPUTFORMATNAME = "FORMAT";



   /** Construct a new Description
     * @param env
     */
    public TableManipulatorApplicationDescription(ApplicationDescriptionEnvironment env) {
        super(env);
        this.setMetaData();
    }
    
    /** set up metadata for this instance */
    private final void setMetaData() {
       StringBuffer thename = new StringBuffer(env.getAuthIDResolver().getAuthorityID());
       thename.append("/TableManipulator");
       setName(thename.toString()); 
       BaseParameterDescription inputTable = new BaseParameterDescription();
       inputTable.setName(INPUTTABLENAME);
       inputTable.setDisplayName("Input table");
       inputTable.setDisplayDescription("The table to be manipulated");
       inputTable.setType(ParameterTypes.BINARY);
       this.addParameterDescription(inputTable);
       
       BaseParameterDescription inputTable2 = new BaseParameterDescription();
       inputTable2.setName(INPUT2TABLENAME);
       inputTable2.setDisplayName("2nd Input table");
       inputTable2.setDisplayDescription("The table");
       inputTable2.setType(ParameterTypes.BINARY);
       this.addParameterDescription(inputTable);
       BaseParameterDescription outputTable = new BaseParameterDescription();
       outputTable.setName(OUTPUTTABLENAME);
       outputTable.setDisplayName("Output table");
       outputTable.setDisplayDescription("The output table");
       outputTable.setType(ParameterTypes.BINARY);
       this.addParameterDescription(inputTable);	
       BaseParameterDescription outputFormat = new BaseParameterDescription();
       outputFormat.setName(OUTPUTFORMATNAME);
       outputFormat.setDisplayName("format");
       outputFormat.setDisplayDescription("the format to output the table in");
       outputFormat.setType(ParameterTypes.TEXT);
       this.addParameterDescription(outputFormat);
 
        BaseApplicationInterface convertInterface = new BaseApplicationInterface(CONVERT,this);
        try {
            convertInterface.addInputParameter(inputTable.getName());
            convertInterface.addInputParameter(outputFormat.getName());
            convertInterface.addOutputParameter(outputTable.getName());
        } catch (ParameterDescriptionNotFoundException e) {
            logger.fatal("Programming error",e);// really shouldn't happen.
            throw new RuntimeException("Programming error",e);
        }
        this.addInterface(convertInterface);
    }

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TableManipulatorApplicationDescription.class);



    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Table Maipulation application\n" + this.toString();
    }

    /** installation test verifies mailer session object is present in jndi.
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {        
        return null;
    }
    
    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String callerAssignedID, User user, Tool tool) throws Exception {
        String newID = env.getIdGen().getNewID();
        final DefaultIDs ids = new DefaultIDs(callerAssignedID,newID,user);
        ApplicationInterface iface = this.getInterface(tool.getInterface());
        return new TableManipulatorApplication(ids,tool,iface,env.getProtocolLib());
    }

    /**The actual table manipulator application.
     * @TODO perhaps this needs to be in its own file.
    * @author Paul Harrison (pah@jb.man.ac.uk) 09-Nov-2004
    * @version $Name:  $
    * @since iteration6
    */
   public static class TableManipulatorApplication extends AbstractApplication {

        /** Construct a new SendMailApplication
         * @param ids
         * @param tool
         * @param applicationInterface
         * @param lib
         */
        public TableManipulatorApplication(IDs ids, Tool tool, ApplicationInterface applicationInterface, ProtocolLibrary lib) {
            super(ids, tool, applicationInterface, lib);
        }

      protected ParameterAdapter instantiateAdapter(ParameterValue pval,
            ParameterDescription descr, ExternalValue indirectVal) {
         if (pval.getName().equals(OUTPUTFORMATNAME)) {
            return new DefaultParameterAdapter(pval, descr, indirectVal);
         }
         else {
            return new StreamParameterAdapter(pval, descr, indirectVal);
         }
      }
        public Runnable createExecutionTask() throws CeaException {

            createAdapters();
            setStatus(Status.INITIALIZED);
            return new Runnable() {
                    public void run() {
                        final Map args = new HashMap();
                        try {
                        for (Iterator i = inputParameterAdapters(); i.hasNext();) {
                            ParameterAdapter a = (ParameterAdapter)i.next();
                            args.put(a.getWrappedParameter().getName(),a.process());
                        }                        
                        setStatus(Status.RUNNING);
                        // convert the table here
                        if(getApplicationInterface().getName().equals(CONVERT)){
                        TableConverter tableconverter = new TableConverter((InputStream)args.get(INPUTTABLENAME), (String)args.get(OUTPUTFORMATNAME));
                        }
                       // table = new StarTableFactory().makeStarTable(trans);
                         } catch (Throwable t) {
                            reportError("Something went wrong",t);
                        }
                    }
            };

            
        }
    }
    
}


/* 
$Log: TableManipulatorApplicationDescription.java,v $
Revision 1.2  2007/02/19 16:20:22  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:17  gtr
no message

Revision 1.2  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.1.2.1  2004/11/15 16:55:05  pah
initial thoughts on a table manipulator - stalled because want to make changes to STIL

Revision 1.5  2004/09/17 01:21:12  nw
altered to work with new threadpool

Revision 1.4.12.1  2004/09/14 13:46:04  nw
upgraded to new threading practice.

Revision 1.4  2004/09/03 13:19:14  nw
added some progress messages

Revision 1.3  2004/08/28 11:25:10  nw
tried to improve error trapping - seems to fail to complete at the moment.

Revision 1.2  2004/08/16 11:03:46  nw
first stab at a cat application

Revision 1.1  2004/08/11 17:40:49  nw
implemented send mail application
 
*/