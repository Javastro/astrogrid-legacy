/*$Id: ManagingFileStoreExecutionHistory.java,v 1.4 2007/01/29 11:11:36 nw Exp $
 * Created on 11-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;

import org.astrogrid.applications.manager.persist.FileStoreExecutionHistory;

/** Extension to standard filestore exec history that allows old jobs tobe cleaned up.
 * 
 * bit nasrty - original class isn't easy to extend, so have had to reverse-engineer - making it a little brittle
 * to change. 
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 11-Nov-2005
 *
 */
public class ManagingFileStoreExecutionHistory extends FileStoreExecutionHistory {

    /** Construct a new ManagingFileStoreExecutionHistory
     * @param arg0
     */
    public ManagingFileStoreExecutionHistory(String dir) {
        super(new MyConf(dir));
        root = new File(dir);
    }
    private File root;
    
    
    private static class MyConf implements  org.astrogrid.applications.contracts.Configuration {
        public MyConf(String dir) {
            this.workDir = new File(dir);
            if (!workDir.exists()) {
                workDir.mkdirs();
            }              
        }
        private final File workDir;
        public File getBaseDirectory() {
            return null;
        }
        // only need to implement this one.
        public File getRecordsDirectory() {
              
            return workDir;
        }

        public File getTemporaryFilesDirectory() {
            return null;
        }

        public URL getRegistryTemplate() {
            return null;
        }

        public URL getServiceEndpoint() {
            return null;
        }
    }

    
    public void delete(String execId) {
        File f = new File(root,URLEncoder.encode(execId));
        if (f.exists()){
            f.delete();
        }
    }

}


/* 
$Log: ManagingFileStoreExecutionHistory.java,v $
Revision 1.4  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.3  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.34.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1.34.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.1.34.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development

Revision 1.2  2006/03/22 17:24:39  nw
fixes necessary for upgrade to 2006.1 libs

Revision 1.1  2005/11/11 17:53:27  nw
added cea polling to lookout.
 
*/