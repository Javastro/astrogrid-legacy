/*$Id: ManagingFileStoreExecutionHistory.java,v 1.2 2006/03/22 17:24:39 nw Exp $
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

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.manager.persist.FileStoreExecutionHistory;

import java.io.File;
import java.net.URLEncoder;

/** Extension to standard filestore exec history that allows old jobs tobe cleaned up.
 * 
 * bit nasrty - original class isn't easy to extend, so have had to reverse-engineer - making it a little brittle
 * to change. 
 * @todo use own persistence mechanism- like jdbm for example?
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Nov-2005
 *
 */
public class ManagingFileStoreExecutionHistory extends FileStoreExecutionHistory {

    /** Construct a new ManagingFileStoreExecutionHistory
     * @param arg0
     */
    public ManagingFileStoreExecutionHistory(Configuration conf) {
        super(conf);
        root = conf.getRecordsDirectory();
    }
    private File root;
    
    
    public void delete(String execId) {
        File f = new File(root,URLEncoder.encode(execId));
        if (f.exists()){
            f.delete();
        }
    }

}


/* 
$Log: ManagingFileStoreExecutionHistory.java,v $
Revision 1.2  2006/03/22 17:24:39  nw
fixes necessary for upgrade to 2006.1 libs

Revision 1.1  2005/11/11 17:53:27  nw
added cea polling to lookout.
 
*/