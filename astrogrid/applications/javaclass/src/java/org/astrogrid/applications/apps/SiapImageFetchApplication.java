/*$Id: SiapImageFetchApplication.java,v 1.2 2007/02/19 16:20:22 gtr Exp $
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

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.apps.CatApplicationDescription.StreamParameterAdapter;
import org.astrogrid.applications.apps.SiapImageFetchDescription.ParameterAdapterDataSource;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.io.Piper;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Tool;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Nov-2004
 *
 */
public class SiapImageFetchApplication extends AbstractApplication {

    /** Construct a new SiapImageFetchApplication
     * @param ids
     * @param tool
     * @param applicationInterface
     * @param lib
     */
    public SiapImageFetchApplication(IDs ids, Tool tool, ApplicationInterface applicationInterface, ProtocolLibrary lib) {
        super(ids, tool, applicationInterface, lib);
    }
    /** create streaming parameter adapter for table (as may be large). 
     * standard strings for the other ones.
     * @see org.astrogrid.applications.AbstractApplication#instantiateAdapter(org.astrogrid.applications.beans.v1.parameters.ParameterValue, org.astrogrid.applications.description.ParameterDescription, org.astrogrid.applications.parameter.protocol.ExternalValue)
     */
    protected ParameterAdapter instantiateAdapter(ParameterValue pval,
            ParameterDescription descr, ExternalValue indirectVal) {
        if (descr.getName().equals(SiapImageFetchDescription.TABLE)) {
            return new CatApplicationDescription.StreamParameterAdapter(pval,descr,indirectVal);
        } else {
            return super.instantiateAdapter(pval, descr, indirectVal);
        }
    }
    public Runnable createExecutionTask() throws CeaException {
        createAdapters();
        setStatus(Status.INITIALIZED);
        Runnable r = new ImageFetchRunnable();
        return r;
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
                    String name = input.getWrappedParameter().getName();
                    if (SiapImageFetchDescription.TABLE.equalsIgnoreCase(name)) {
                        tableStream = (CatApplicationDescription.StreamParameterAdapter) input;
                    } else if (SiapImageFetchDescription.BASEIVORN.equalsIgnoreCase(name)) {
                        baseDir = (String)input.process();
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
                InputStream vin = (InputStream)tableStream.process();                        
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
                for(RowSequence rs = table.getRowSequence(); rs.hasNext(); ) {
                    rs.next();
                    URL url = new URL(rs.getCell(referenceCol).toString());  
                    FileManagerNode target = dir.addFile((count ++) + ".fits");
                    reportMessage("Saving " + url  + " to " + target.getName());
                    target.copyURLToContent(url);
                    urls.add(url);
                    ivorns.add(target);
                }                        
                
                //return output parameters.
                for (Iterator i = outputParameterAdapters(); i.hasNext(); ) {
                    ParameterAdapter p = (ParameterAdapter)i.next();
                    String outputName = p.getWrappedParameter().getName();
                    if (SiapImageFetchDescription.URLS.equalsIgnoreCase(outputName)) {
                        p.writeBack(listToString(urls));
                    } else if (SiapImageFetchDescription.IVORNS.equalsIgnoreCase(outputName)) {
                        p.writeBack(listToString(ivorns));
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

        private Object listToString(List items) {
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
}

/* 
$Log: SiapImageFetchApplication.java,v $
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