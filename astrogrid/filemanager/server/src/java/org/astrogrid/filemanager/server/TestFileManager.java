/*$Id: TestFileManager.java,v 1.3 2005/04/28 20:42:04 clq2 Exp $
 * Created on 18-Feb-2005
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
import org.astrogrid.filemanager.BaseTest;
import org.astrogrid.filemanager.common.FileManagerPortType;
import org.astrogrid.filemanager.datastore.DefaultStoreFacade;
import org.astrogrid.filemanager.datastore.StoreFacade;
import org.astrogrid.filemanager.nodestore.CautiousNodeStoreDecorator;
import org.astrogrid.filemanager.nodestore.NodeIvornFactory;
import org.astrogrid.filemanager.nodestore.NodeStore;
import org.astrogrid.filemanager.nodestore.file.TransactionalFileNodeStore;
import org.astrogrid.filestore.client.FileStoreMockDelegate;
import org.astrogrid.filestore.common.exception.FileStoreServiceException;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverMock;
import org.astrogrid.io.Piper;

import org.apache.axis.client.AdminClient;
import org.apache.axis.client.Call;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.transaction.file.ResourceManagerException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/** Composition of file manager components for testing.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Feb-2005
 * <p>
 * odd - seems like the local:/// protocol doesn't respect service-scope - 
 * in particular it seems to be instantiating a new server for each call. This fouls
 * up all the backend persistence. - so make a singleton of this instead.
 *
 */
public class TestFileManager extends CautiousFileManagerDecorator {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TestFileManager.class);

    /** Construct a new SoapTestFilemanager
     * @throws IOException
     * @throws ResourceManagerException
     * 
     */
    public TestFileManager() throws IOException, ResourceManagerException {
        super(createTestFileManager());
    }
    
    /** create instance of the test file manager 
     * - mock resolver, with 2 filestore mock delegates..
     * @return
     * @throws IOException
     * @throws ResourceManagerException
     */
    public static final FileManagerPortType createTestFileManager() throws IOException, ResourceManagerException {
        if (theInstance == null) {
            logger.info("Creating new TestFileManager");
            FileManagerConfig config = new HardCodedFileManagerConfig();
            File baseDir = config.getBaseDir();
            //          NameGen nameGen = new InMemoryNameGen();
            File nameGenDir = new File(baseDir,"nameGen");
            NameGen nameGen = new FileNameGen(nameGenDir,TheFileManager.NODE_SEQUENCE_NAME);
//          NodeStore store = new CautiousNodeStoreDecorator(new InMemoryNodeStore(new NodeIvornFactory( nameGen,config)));

            File nodeDir = new File(baseDir,"nodes");        
            //    NodeStore store = new CautiousNodeStoreDecorator(new FileNodeStore(new NodeIvornFactory( nameGen,config),nodeDir));
            File workingDir = new File(baseDir,"working");
            NodeStore store = new CautiousNodeStoreDecorator(new TransactionalFileNodeStore(new NodeIvornFactory(nameGen,config),config.getDefaultStorageServiceURI(),nodeDir,workingDir));
            
            FileStoreDelegateResolverMock resolver = new FileStoreDelegateResolverMock();
            try {
                resolver.register(new FileStoreMockDelegate(BaseTest.FILESTORE_1));
                resolver.register(new FileStoreMockDelegate(BaseTest.FILESTORE_2))  ;   
            } catch (FileStoreServiceException e) {
                throw new RuntimeException("Could not register delegates in mock resolver",e);
            }
        
            StoreFacade facade = new DefaultStoreFacade(resolver);
        
            theInstance = new CoreFileManager(config,store,facade);
        } 
        return theInstance;
    }
    private static FileManagerPortType theInstance;
    

    
    /** write altered version of the wsdd to a known file location */
    public static final void prepareWsdd() throws Exception {
        // store in temporary in-memory buffer along the way, so we can do a search-n-replace to set the implementation class
        Reader r = new InputStreamReader( TestFileManager.class.getResourceAsStream("/wsdd/FileManager.wsdd"));
        Writer buff = new StringWriter();
        Piper.pipe(r,buff);
        r.close();
        //@hack this will break if wsdd is edited to point to another impl class
        Reader buff1 = new StringReader(buff.toString().replaceFirst("org\\.astrogrid\\.filemanager\\.common\\.FileManagerBindingImpl",TestFileManager.class.getName()));
        File wsddFile = getWsddFile();     
        // write wsdd from classpath into this file.
        Writer w = new FileWriter(wsddFile);
        Piper.pipe(buff1,w);
        w.close();        
     
    }

    
    /** @pre - prepareWsdd has been called */
    public static final void deployLocal() throws Exception{
        // iniitialize the service 
        // Initialise the Axis 'local:' URL protocol.        
        Call.initialize();
         
        // fire off a local service from this file.
        File wsddFile = getWsddFile();
        AdminClient.main(new String[]{"-l", "local:///AdminService",wsddFile.toString()});

    }

    /**
     * @return
     */
    private static File getWsddFile() {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        File wsddFile = new File(tmpDir,"TestFileManager.wsdd");
        return wsddFile;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[TestFileManager:");
        buffer.append(super.toString());
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: TestFileManager.java,v $
Revision 1.3  2005/04/28 20:42:04  clq2
1035

Revision 1.2.22.1  2005/04/11 11:30:59  nw
refactored nameGen into a component component

Revision 1.2  2005/03/11 13:37:06  clq2
new filemanager merged with filemanager-nww-jdt-903-943

Revision 1.1.2.1  2005/03/01 23:43:36  nw
split code inito client and server projoects again.

Revision 1.1.2.1  2005/03/01 15:07:35  nw
close to finished now.

Revision 1.1.2.3  2005/02/27 23:03:12  nw
first cut of talking to filestore

Revision 1.1.2.2  2005/02/25 12:33:27  nw
finished transactional store

Revision 1.1.2.1  2005/02/18 15:50:14  nw
lots of changes.
introduced new schema-driven soap binding, got soap-based unit tests
working again (still some failures)
 
*/