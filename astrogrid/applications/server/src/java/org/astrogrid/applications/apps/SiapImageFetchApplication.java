/*$Id: SiapImageFetchApplication.java,v 1.7 2011/09/02 21:55:54 pah Exp $
 * Created on 23-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.apps;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.apps.CatApplicationDescription.StreamParameterAdapter;
import org.astrogrid.applications.apps.SiapImageFetchDescription.ParameterAdapterDataSource;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterDirection;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Nov-2004
 * @TODO FIXME - this is broken at the moment.
 */
public class SiapImageFetchApplication extends AbstractApplication {

    /** Construct a new SiapImageFetchApplication
     * @param tool
     * @param applicationInterface
     * @param env 
     * @param lib
     */
    public SiapImageFetchApplication(Tool tool, ApplicationInterface applicationInterface, ApplicationEnvironment env, ProtocolLibrary lib) {
        super(tool, applicationInterface, env, lib);
    }
    /** create streaming parameter adapter for table (as may be large). 
     * standard strings for the other ones.
     * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue, org.astrogrid.applications.description.ParameterDescription, org.astrogrid.applications.parameter.protocol.ExternalValue)
     */
    @Override
    protected ParameterAdapter instantiateAdapter(ParameterValue pval,
            ParameterDescription descr, ParameterDirection dir, ExternalValue indirectVal) {
        if (descr.getId().equals(SiapImageFetchDescription.TABLE)) {
            return new CatApplicationDescription.StreamParameterAdapter(pval,descr,dir, applicationEnvironment);
        } else {
            return super.instantiateAdapter(pval, descr, dir, indirectVal);
        }
    }
    /** runnable object that actually does the work */
    final class ImageFetchRunnable implements Runnable {
        private static final String REFERENCE_UCD = "VOX:Image_AccessReference";
        protected final StarTableFactory factory = new StarTableFactory();
        protected final FileManagerClient vospace = (new FileManagerClientFactory()).login(); // default user - should be sufficient for now.



        public void run() {
            try {
                setStatus(Status.RUNNING);
                StreamParameterAdapter tableStream = null;
                String baseDir = null;
                // extract and process parameters
                for (Iterator i = inputParameterAdapters(); i.hasNext(); ) {
                    ParameterAdapter input = (ParameterAdapter)i.next();
                    String name = input.getWrappedParameter().getId();
                    if (SiapImageFetchDescription.TABLE.equalsIgnoreCase(name)) {
                        tableStream = (CatApplicationDescription.StreamParameterAdapter) input;
                    } else if (SiapImageFetchDescription.BASEIVORN.equalsIgnoreCase(name)) {
                        baseDir = input.getInternalValue().asString();
                        if (baseDir == null || baseDir.trim().length() ==0) {
                            reportError("Empty BASEDIR parameter");
                        }
                    } else {
                        reportError("Unknown input parameter " + name);
                    }
                }
                // set up output folder.
                Date now = new Date();
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy--HHmm");
                String unq = df.format(now);
                                        
                Ivorn dirIvorn = new Ivorn(baseDir + (baseDir.endsWith("/") ? "" : "/") + unq);
                reportMessage("Images will be saved in folder " + dirIvorn);
                FileManagerNode dir = vospace.createFolder(dirIvorn);
                
                // save the votable itself.
                Ivorn votable = new Ivorn(dirIvorn.toString() + "/" + "votable.vot");
                FileManagerNode votableNode = dir.addFile("votable.vot");
                InputStream vin = (InputStream)tableStream.getInternalValue();                        
                OutputStream vout = votableNode.writeContent(); 
                Piper.pipe(vin,vout);
                vin.close();
                vout.close();
                reportMessage("Saved source votable as " + votable);
                
                //now do the work on the table..
                StarTable table = factory.makeStarTable(
                        new ParameterAdapterDataSource(tableStream),"votable");
                // first find correct metadata column
                int referenceCol = -1;
                for (int col = 0; col < table.getColumnCount(); col++) {
                    ColumnInfo colInfo = table.getColumnInfo(col);
                    if (REFERENCE_UCD.equals(colInfo.getUCD())){
                        referenceCol = col;
                    }
                }
                if (referenceCol == -1) {
                    reportError("Couldn't find column with UCD :" + REFERENCE_UCD);
                }
                
                setStatus(Status.WRITINGBACK);

                // iterate round, fetching and saving files.                        
                // process each row in turn.
                List urls = new ArrayList();
                List ivorns = new ArrayList();                        
                int count = 0;
                RowSequence rs = table.getRowSequence();
                  while(  rs.next()){
                    URL url = new URL(rs.getCell(referenceCol).toString());  
                    FileManagerNode target = dir.addFile((count ++) + ".fits");
                    reportMessage("Saving " + url  + " to " + target.getName());
                    target.copyURLToContent(url);
                    urls.add(url);
                    ivorns.add(target);
                }                        
                
                //return output parameters.
                for (Iterator i = outputParameterAdapters(); i.hasNext(); ) {
                    StreamParameterAdapter p = (StreamParameterAdapter)i.next();
                    String outputName = p.getWrappedParameter().getId();
                    if (SiapImageFetchDescription.URLS.equalsIgnoreCase(outputName)) {
                        p.getInternalValue().setValue(listToString(urls));
                        p.writeBack();
                    } else if (SiapImageFetchDescription.IVORNS.equalsIgnoreCase(outputName)) {
                        p.getInternalValue().setValue(listToString(ivorns));
                        p.writeBack();
                    } else {
                        reportError("Unknown output parameter " + outputName);
                    }                            
                }
                
                setStatus(Status.COMPLETED);
            } catch (URISyntaxException e) {
                reportError("Storage Ivorn not in correct format",e);
            } catch (CeaException e) {
                reportError("something failed",e);
            } catch (Exception e) {
                reportError("something failed",e);
            }
        }

        private String listToString(List items) {
              StringBuffer buff = new StringBuffer();
              buff.append("[");
              boolean isFirst = true;
              for (Iterator i = items.iterator(); i.hasNext(); ) {
                  String value = i.next().toString();
                  if (!isFirst) {
                      buff.append(",");
                  }
                  buff.append("'");
                  buff.append(value);
                  buff.append("'");
                  isFirst = false;
              }
              buff.append("]");
              return buff.toString();
        }
    }
    @Override
    public Runnable createRunnable() {
	return new ImageFetchRunnable();
    }    
}

/* 
$Log: SiapImageFetchApplication.java,v $
Revision 1.7  2011/09/02 21:55:54  pah
result of merging the 2931 branch

Revision 1.6.6.2  2009/07/16 19:47:34  pah
ASSIGNED - bug 2950: rework parameterAdapter
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950

Revision 1.6.6.1  2009/07/15 09:46:14  pah
redesign of parameterAdapters

Revision 1.6  2008/09/13 09:51:02  pah
code cleanup

Revision 1.5  2008/09/10 23:27:16  pah
moved all of http CEC and most of javaclass CEC code here into common library

Revision 1.3  2008/09/03 14:18:34  pah
result of merge of pah_cea_1611 branch

Revision 1.2.10.3  2008/08/02 13:33:40  pah
safety checkin - on vacation

Revision 1.2.10.2  2008/05/13 15:14:07  pah
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708

Revision 1.2.10.1  2008/03/19 23:28:58  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.2  2007/02/19 16:20:22  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:17  gtr
no message

Revision 1.3  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.2.26.1  2005/03/11 11:21:19  nw
replaced VoSpaceClient with FileManagerClient

Revision 1.2  2004/11/29 20:00:56  clq2
nww-itn07-684

Revision 1.1.2.2  2004/11/26 15:17:28  nw
some extra error-checking logic.

Revision 1.1.2.1  2004/11/24 00:14:58  nw
factored the application class out of the descripiton - makes for
more manageable code.
 
*/