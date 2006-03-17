/*$Id: FileStoreExecutionHistory.java,v 1.7 2006/03/17 17:50:58 clq2 Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.persist;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

import junit.framework.Test;
import junit.framework.TestCase;

/** Execution history that persists archives as xml documents to disk.
 * Current Set of executing applications still only kept in memory.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class FileStoreExecutionHistory extends InMemoryExecutionHistory {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(FileStoreExecutionHistory.class);

    /** Construct a new FileStoreExecutionHistory
     * 
     */
    public FileStoreExecutionHistory(Configuration configuration) {
        super();
        this.baseDir = configuration.getRecordsDirectory();
        archive = new XMLFileMap();
    }
    private final File baseDir;
    
    /** implementation of the map interface over a directory of xml documents 
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
     *
     */
    class XMLFileMap implements SimpleMap {
        /**
         * Commons Logger for this class
         */

        public XMLFileMap() {
            logger.info("BaseDir set to " + baseDir.getAbsolutePath());
        }
        /**
         * @see org.astrogrid.applications.manager.persist.InMemoryExecutionHistory.SimpleMap#put(java.lang.Object, java.lang.Object)
         */
        public void put(String key,ExecutionSummaryType value) throws MarshalException, ValidationException, IOException{ 
            File f = mkFile(key);
            FileWriter fw = new FileWriter(f);
            try {
                value.marshal(fw);
            } finally {
                try {
                    fw.close();
                } catch (IOException e)  {
                    logger.debug("failed to close file" );
                }
            }
        }
        
        /** compute appropriate filename from key */
        protected File mkFile(Object key) {
            return new File(baseDir,URLEncoder.encode(key.toString()));
        }
        /**
         * @see org.astrogrid.applications.manager.persist.InMemoryExecutionHistory.SimpleMap#get(java.lang.Object)
         */
        public ExecutionSummaryType get(String key) throws MarshalException, ValidationException, FileNotFoundException {
            File f = mkFile(key);
            if (!f.exists()) {
                return null;
            }
            FileReader fr = new FileReader(f);
            try {
                return ExecutionSummaryType.unmarshalExecutionSummaryType(fr);
            } finally {
                try {
                    fr.close();
                } catch (IOException e){
                    logger.debug("failed to close file" );
                }
            }
        }

    }// end inner class
    
    
    // componentDescritpin interface 
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Execution History Store that persists records as xmldocuments on disk"
         + "\n store directory " + baseDir.getAbsolutePath()
         + "\n # records " + baseDir.list().length
         ;
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return new InstallationTest("testBaseDir");
    }

     /**
      * Installation Test for the FileStore
      * checks that the basedir exists and is writable 
      * @author Noel Winstanley nw@jb.man.ac.uk 26-Jul-2004
      *
      */
    public class InstallationTest extends TestCase {

        public InstallationTest(String arg0) {
            super(arg0);
        }

        public void testBaseDir() throws Exception {
            assertTrue("Base Directory does not exist",baseDir.exists());
            assertTrue("Base Directory isn't a directory",baseDir.isDirectory());
            assertTrue("Base Directory can't be read",baseDir.canRead());
            assertTrue("Base Directory can't be written to",baseDir.canWrite());
        }
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "FileStoreExecutionHistory";
    }

}


/* 
$Log: FileStoreExecutionHistory.java,v $
Revision 1.7  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.5  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.4.148.1  2006/01/25 17:04:32  gtr
Refactored: the configuration is now a fixed structure based at the configurable location cea.base.dir.

Revision 1.4  2004/09/17 10:17:09  nw
made sure streams are closed

Revision 1.3  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/