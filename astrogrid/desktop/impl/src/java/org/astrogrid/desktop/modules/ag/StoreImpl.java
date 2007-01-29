/*$Id: StoreImpl.java,v 1.4 2007/01/29 11:11:35 nw Exp $
 * Created on 25-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.RecordManagerOptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.builtin.ShutdownListener;

/** Implementation of a storage mechanism - based on jdbm
 * intended for storing largeish chunks of data. smaller snippets of info are better stored in cache.
 *  - no public inteface, just an internal one.
 *  
 *  uses community, but doesn't implement UserLoginListener - instead,
 *  cleanup at logout of the store is done by MessageRecorderImpl - as
 *  has to do some other work before closing the user's store.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 25-Oct-2005
 */
public class StoreImpl implements StoreInternal, ShutdownListener{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(StoreImpl.class);

    /** Construct a new StoreImpl
     * @throws IOException
     * 
     */
    public StoreImpl(Community comm) {
        super();
        this.comm= comm;

    }
    // dunno if finalize() is guaranteed to be called on system shutdown, but it's worth a try..
    //might mean that we manage to close the store cleanly more often.
    protected void finalize() throws Throwable {
    	halting();
    }
    private final Community comm;
    private RecordManager manager;
    private File workDir = new File(new File(System.getProperty("user.home")),".workbench");
    
    public void setWorkDir(String location) {
        workDir = new File(location);
    }
    // unique key that defines the format of store, plus all classes within it.
    // whenever a binary incompatible change is made, change the version string
    // this will be detected, and a new store created (all old data lost - will do for now
    // later will add migration functions.
    public static final String STORE_FORMAT_VERSION = "20";

    public synchronized RecordManager getManager() {
        if (manager == null) {
            createManager();
        } 
        return manager;
    }
    

    // create a manager - requires user login.
    private void createManager() {
        try {
        String storeId = URLEncoder.encode(comm.getUserInformation().getId().toString() + "-store-" + STORE_FORMAT_VERSION,"UTF-8");
        if (!workDir.exists()) {
            workDir.mkdirs();
        }        
        File f = new File(workDir,storeId);
        Properties props = new Properties();
        
       props.setProperty(RecordManagerOptions.THREAD_SAFE,"true"); // @todo verify that we do need thread safety.
        // turn off transactions - as we never use rollback.
       props.setProperty(RecordManagerOptions.DISABLE_TRANSACTIONS,"true");
        //woud like to make cache soft - so it doesn't eat memory.
        // however, doesn't seem to be implemented - it throws.
        //props.setProperty(RecordManagerOptions.CACHE_TYPE,RecordManagerOptions.SOFT_REF_CACHE);
        // instead, all we can do is just turn down the size of the MRU cache. y code reading, seems to default to 1000 (objects I guess).
       // as each of my objects is a tree, this is quite bit. will shrink down to 100.
       props.setProperty(RecordManagerOptions.CACHE_SIZE,"50");
       
        manager = RecordManagerFactory.createRecordManager(f.getAbsolutePath(),props);
        logger.info("Using record manager: " + manager.getClass().getName() + " to read store " + storeId);
        } catch (IOException e) {
            logger.fatal("Failed to create JDBM file",e);
        }
    }

    
 
    public void halting() {
        try { //' think this is the cause of problems - if the system goes down quickly, the manager isn't necessarily closed.
            if (manager != null) {
                manager.close();
            }
        } catch (IOException e) {            
            logger.warn("Failed to close JDBM file",e);
        }        
    }

    public String lastChance() {
        return null;
    }

}


/* 
$Log: StoreImpl.java,v $
Revision 1.4  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.30.2  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.2.30.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.2  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.1.2.2  2005/11/23 18:09:50  nw
turned down cache - think this was the problem.

Revision 1.1.2.1  2005/11/17 21:06:26  nw
moved store to be user-dependent
debugged message monitoring.

Revision 1.4  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.3  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.2  2005/11/10 12:05:53  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/