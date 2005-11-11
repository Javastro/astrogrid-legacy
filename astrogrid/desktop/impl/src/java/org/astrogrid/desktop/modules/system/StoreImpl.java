/*$Id: StoreImpl.java,v 1.4 2005/11/11 10:08:18 nw Exp $
 * Created on 25-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.system.Configuration;

import org.picocontainer.Startable;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.RecordManagerOptions;

/** Implementation of a storage mechanism - based on jdbm
 * intended for storing largeish chunks of data. smaller snippets of info are better stored in cache.
 *  - no public inteface, just an internal one.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Oct-2005
 *
 */
public class StoreImpl implements Startable, StoreInternal{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(StoreImpl.class);

    /** Construct a new StoreImpl
     * @throws IOException
     * 
     */
    public StoreImpl(Configuration conf) {
        super();
        this.conf = conf;

    }
    private final Configuration conf;
    private RecordManager manager;
    // unique key that defines the format of store, plus all classes within it.
    // whenever a binary incompatible change is made, change the version string
    // this will be detected, and a new store created (all old data lost - will do for now
    // later will add migration functions.
    public static final String STORE_FORMAT_VERSION = "9";

    public RecordManager getManager() {
        return manager;
    }
    
    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        try {
        File workDir = new File(conf.getKey(ConfigurationKeys.WORK_DIR_KEY));
        // can't assume it exists
        if (!workDir.exists()) {
            workDir.mkdirs();
        }
        File f = new File(workDir,"store-" + STORE_FORMAT_VERSION);
        Properties props = new Properties();
        props.setProperty(RecordManagerOptions.THREAD_SAFE,"true"); // assume this is what we need to do.
        //@todo review configuration later.
        manager = RecordManagerFactory.createRecordManager(f.getAbsolutePath(),props);     
        } catch (IOException e) {
            logger.fatal("Failed to create JDBM file",e);
        }
    }

    
    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        try {
            if (manager != null) {
                manager.close();
            }
        } catch (IOException e) {            
            logger.warn("Failed to close JDBM file",e);
        }
    }

}


/* 
$Log: StoreImpl.java,v $
Revision 1.4  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.3  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.2  2005/11/10 12:05:53  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/