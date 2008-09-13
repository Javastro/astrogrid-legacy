/*$Id: FileStoreExecutionHistory.java,v 1.9 2008/09/13 09:51:02 pah Exp $
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import junit.framework.Test;
import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.contracts.Namespaces;

/** Execution history that persists archives as xml documents to disk.
 * Current Set of executing applications still only kept in memory.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Apr 2008 changed to be jaxb
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
         * @throws ValidationException 
         * @throws MarshalException 
         * @see org.astrogrid.applications.manager.persist.InMemoryExecutionHistory.SimpleMap#put(java.lang.Object, java.lang.Object)
         */
        public void put(String key,ExecutionSummaryType value) throws  IOException{ 
            File f = mkFile(key);
            FileWriter fw = new FileWriter(f);
            try {
        	JAXBContext jc = CEAJAXBContextFactory.newInstance();
                 Marshaller m = jc.createMarshaller();
                 m.marshal(new JAXBElement(new QName(Namespaces.CEAT
          		.getNamespace(), "executionSummary"), ExecutionSummaryType.class, value), fw);
                 
            } catch (JAXBException e) {
		logger.error("problem writing execution history for "+key, e);
		//TODO action for this error?
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
         * @throws ValidationException 
         * @throws MarshalException 
         * @see org.astrogrid.applications.manager.persist.InMemoryExecutionHistory.SimpleMap#get(java.lang.Object)
         */
        public ExecutionSummaryType get(String key) throws  FileNotFoundException {
            File f = mkFile(key);
            if (!f.exists()) {
                return null;
            }
            FileReader fr = new FileReader(f);
            try {
        	
                return CEAJAXBUtils.unmarshall(fr, ExecutionSummaryType.class);
            } catch (Exception e) {
		logger.error("problem unmarshalling for "+key, e);
		ExecutionSummaryType es = new ExecutionSummaryType();
		es.setApplicationName("failed to retrieve execution summary");
		return es;
	    } finally {
                try {
                    fr.close();
                } catch (IOException e){
                    logger.debug("failed to close file" );
                }
            }
        }
	public Set<String> keys() {
	    Set<String> set = new HashSet<String>();
	    
	    File[] files = baseDir.listFiles();
	    for (int i = 0; i < files.length; i++) {
		File file = files[i];
		String s =  file.getName();
		set.add(s);
	    }
	    return set;
	}
	public boolean delete(String key) {
	          File f = mkFile(key);
	            if (f.exists()) {
	                return f.delete();
	            }
	            return true; // file already not there...
	 
	}

    }// end inner class
    
    
    // componentDescritpin interface 
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    @Override
    public String getDescription() {
        return "Execution History Store that persists records as xmldocuments on disk"
         + "\n store directory " + baseDir.getAbsolutePath()
         + "\n # records " + baseDir.list().length
         ;
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    @Override
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
    @Override
    public String getName() {
        return "FileStoreExecutionHistory";
    }



}


/* 
$Log: FileStoreExecutionHistory.java,v $
Revision 1.9  2008/09/13 09:51:02  pah
code cleanup

Revision 1.8  2008/09/03 14:18:48  pah
result of merge of pah_cea_1611 branch

Revision 1.7.54.4  2008/08/29 07:28:30  pah
moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration

Revision 1.7.54.3  2008/05/08 22:40:53  pah
basic UWS working

Revision 1.7.54.2  2008/04/17 16:08:32  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.7.54.1  2008/03/19 23:10:54  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

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