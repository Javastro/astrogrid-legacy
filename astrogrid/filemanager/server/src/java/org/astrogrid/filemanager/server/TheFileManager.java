/*$Id: TheFileManager.java,v 1.4 2005/04/28 20:42:04 clq2 Exp $
 * Created on 28-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.filemanager.server;

import org.astrogrid.common.namegen.FileNameGen;
import org.astrogrid.common.namegen.NameGen;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.filemanager.datastore.DefaultStoreFacade;
import org.astrogrid.filemanager.datastore.StoreFacade;
import org.astrogrid.filemanager.nodestore.CautiousNodeStoreDecorator;
import org.astrogrid.filemanager.nodestore.NodeIvornFactory;
import org.astrogrid.filemanager.nodestore.NodeStore;
import org.astrogrid.filemanager.nodestore.file.TransactionalFileNodeStore;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolver;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.ResourceManagerException;

import java.io.File;

/** Production File manager.
 * @author Noel Winstanley nw@jb.man.ac.uk 28-Feb-2005
 *
 */
public class TheFileManager extends SecurityGuardFileManagerDecorator {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TheFileManager.class);

    /** Construct a new TheFileManager
     * @param fm
     */
    public TheFileManager() {
        super(buildFileManager());
    }
    
    public static final String FILEMANAGER_BASE_DIR_KEY  = "org.astrogrid.filemanager.basedir";
    public static final String NODE_SEQUENCE_NAME = "node-ivorn-sequence";
    
    protected static final FileManagerPortType buildFileManager() {
        try {
        // access config
        logger.info("Assembling file manager");
        logger.info("Creating config");
        Config c = SimpleConfig.getSingleton();
        FileManagerConfig config = new FileManagerConfigImpl(c);
        logger.info(config);
        
        // create file objects for various directories used, based on single base-dir key.
        File baseDir = config.getBaseDir();
        logger.info("Base dir will be " + baseDir);
        File nameGenDir = new File(baseDir,"nameGen");
        logger.info(nameGenDir);
        File nodeDir = new File(baseDir,"nodes");
        logger.info(nodeDir);
        File workingDir = new File(baseDir,"working");
        logger.info(workingDir);
        
        //assemble node storage.
        NodeStore nodeStore = null;
        
            logger.info("Creating nameGen");
            NameGen nameGen = new FileNameGen(nameGenDir, NODE_SEQUENCE_NAME);   
            logger.info(nameGen);
            
            logger.info("Creating NodeStore");
            NodeStore innerStore =  new TransactionalFileNodeStore(
                    new NodeIvornFactory(nameGen,config)
                    , config.getDefaultStorageServiceURI(), nodeDir, workingDir
                    );
            nodeStore = new CautiousNodeStoreDecorator(innerStore);
            logger.info(nodeStore);
            
            
            //assemble delegates to data storage.
            logger.info("Creating dataStore");
            FileStoreDelegateResolver resolver = new FileStoreDelegateResolverImpl();        
            StoreFacade dataStore = new DefaultStoreFacade(resolver);
            logger.info(dataStore);
            return new CoreFileManager(config,nodeStore,dataStore);   
            
        } catch (ResourceManagerException e) {
            logger.fatal("Could not create node storage",e);
            throw new RuntimeException("Fatal: Could not create node storage",e);
        } catch (FileManagerFault e) {
            logger.fatal("Could not ascertain default file store URI",e);
            throw new RuntimeException("Fatal: Could not ascertain default file store URI",e);
        } catch (Throwable t) {
            logger.fatal("Encountered throwable from the depths",t);
            throw new RuntimeException("Fatal: Encountered throwable from the depths",t);
        }
                        
    }

}


/* 
$Log: TheFileManager.java,v $
Revision 1.4  2005/04/28 20:42:04  clq2
1035

Revision 1.3.8.1  2005/04/11 11:30:58  nw
refactored nameGen into a component component

Revision 1.3  2005/03/31 14:56:08  nw
improved error trapping

Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:37  nw
split code inito client and server projoects again.

Revision 1.1.2.1  2005/03/01 15:07:34  nw
close to finished now.
 
*/