/*
 * $Id: CEAConfiguration.java,v 1.4 2008/09/18 09:13:39 pah Exp $
 * 
 * Created on 2 Apr 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.contracts;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.astrogrid.component.descriptor.ComponentDescriptor;

/**
 * Basic CEA configuration implementation.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class CEAConfiguration implements Configuration, ComponentDescriptor {



    protected File baseDirectory;
    protected File configurationDirectory;
    protected File recordsDirectory;
    protected File temporaryFilesDirectory;
    protected URL  serviceEndpoint;
    protected URL registryTemplate;
    public URL applicationDescriptionUrl;



    /**
     * @param configurationDirectory the configurationDirectory to set
     */
    public void setConfigurationDirectory(File configurationDirectory) {
	this.configurationDirectory = configurationDirectory;
    }

    /**
     * @return the recordsDirectory
     */
    public File getRecordsDirectory() {
	return recordsDirectory;
    }

    /**
     * @param recordsDirectory the recordsDirectory to set
     */
    public void setRecordsDirectory(File recordsDirectory) {
	this.recordsDirectory = recordsDirectory;
    }

    /**
     * @return the temporaryFilesDirectory
     */
    public File getTemporaryFilesDirectory() {
	return temporaryFilesDirectory;
    }

    /**
     * @param temporaryFilesDirectory the temporaryFilesDirectory to set
     */
    public void setTemporaryFilesDirectory(File temporaryFilesDirectory) {
	this.temporaryFilesDirectory = temporaryFilesDirectory;
    }

    /**
     * @return the serviceEndpoint
     */
    public URL getServiceEndpoint() {
	return serviceEndpoint;
    }

    /**
     * @param serviceEndpoint the serviceEndpoint to set
     */
    public void setServiceEndpoint(URL serviceEndpoint) {
	this.serviceEndpoint = serviceEndpoint;
    }

    /**
     * @return the registryTemplate
     */
    public URL getRegistryTemplate() {
	return registryTemplate;
    }

    /**
     * @param registryTemplate the registryTemplate to set
     */
    public void setRegistryTemplate(URL registryTemplate) {
	this.registryTemplate = registryTemplate;
    }

    /**
     * Describes the component and its current state.
     */
    public String getDescription() {
	StringBuffer sb = new StringBuffer();
	sb.append("Provides configuration for the CEC in a type-safe form.\n");
	sb.append("Manages directories where the CEC keeps its files.\n");
	//      sb.append("Base directory: ");
	//      sb.append(this.getBaseDirectory().getAbsolutePath());
	//      sb.append("\n");
	sb.append("\n");
	sb.append("Temporary-files directory: ");
	sb.append(this.getTemporaryFilesDirectory().getAbsolutePath());
	sb.append("\n");
	sb.append("Execution-records directory: ");
	sb.append(this.getRecordsDirectory().getAbsolutePath());
	sb.append("\n");
	sb.append("Registration-template URL: ");
	sb.append(this.getRegistryTemplate().toString());
	sb.append("\n");
	sb.append("CEC endpoint: ");
	sb.append(this.getServiceEndpoint().toString());
	sb.append("\n");
	sb.append("Application-description URL: ");
	sb.append(this.applicationDescriptionUrl.toString());
	sb.append("\n");

	return sb.toString();
    }

    public Test getInstallationTest() {
	TestSuite suite = new TestSuite(
	"Configuration Test");
        suite.addTest(new ConfigurationTest("testRecordsDirectoryOK"));
        suite.addTest(new ConfigurationTest("testWorkingDirectoryOK"));
        return suite;
	
	
    }

    /**
     * Installation test case for the configuration.
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
     * @version $Name:  $
     * @since VOTech Stage 7
     */
    public  class ConfigurationTest extends TestCase {
	
	public ConfigurationTest(String name) {
	    super(name);
	}
	public void testRecordsDirectoryOK() throws Exception {
	    assertTrue("records directory does not exist dir="+recordsDirectory.getAbsolutePath(), recordsDirectory.exists());
	    assertTrue("records directory is not a diretory dir="+recordsDirectory.getAbsolutePath(), recordsDirectory.isDirectory());
            assertTrue("records directory not writable dir="+recordsDirectory.getAbsolutePath(), recordsDirectory.canWrite());
            assertTrue("records directory not readable dir="+recordsDirectory.getAbsolutePath(), recordsDirectory.canRead());
        }
        public void testWorkingDirectoryOK() throws Exception {
            assertTrue("working directory does not exist dir="+temporaryFilesDirectory.getAbsolutePath(), temporaryFilesDirectory.exists());
            assertTrue("working directory is not a directoryt dir="+temporaryFilesDirectory.getAbsolutePath(), temporaryFilesDirectory.isDirectory());
            assertTrue("working directory not writable dir="+temporaryFilesDirectory.getAbsolutePath(), temporaryFilesDirectory.canWrite());
            assertTrue("working directory not readable dir="+temporaryFilesDirectory.getAbsolutePath(), temporaryFilesDirectory.canRead());
        }
    }

    public String getName() {
	return "CEA Configuration";
    }

    public URL getApplicationDescriptionUrl() {
	return applicationDescriptionUrl;
    }

    /**
     * @param applicationDescriptionUrl the applicationDescriptionUrl to set
     */
    public void setApplicationDescriptionUrl(URL applicationDescriptionUrl) {
	this.applicationDescriptionUrl = applicationDescriptionUrl;
    }

    /**
     * Creates a temporary directory. Java has an API to create a temporary
     * file, with safeguards against duplication, but doesn't have an API for a
     * temporary directory. This method uses the temporary file (name ending in
     * .tmp) as a lock and creates a directory based on that name. The simpler
     * technique of creating the file, deleting it from the file system and
     * replacing with a directory of the same name doesn't work reliably; it
     * defeats the locking in the JRE.
     * 
     * @return The directory.
     * @throw IOException If the lock file cannot be created (should never
     *        happen).
     */
    protected static synchronized File makeTemporaryDirectory() throws IOException {
        File lockFile = File.createTempFile("CecBaseConfiguration", ".tmp");
        lockFile.deleteOnExit();
        String fileName = lockFile.getAbsolutePath();
        String directoryName = fileName.substring(0,
        	fileName.lastIndexOf(".tmp")).toString();
        File directory = new File(directoryName);
        directory.mkdir();
        directory.deleteOnExit();
        return directory;
    }

}


/*
 * $Log: CEAConfiguration.java,v $
 * Revision 1.4  2008/09/18 09:13:39  pah
 * improved javadoc
 *
 * Revision 1.3  2008/09/13 09:51:06  pah
 * code cleanup
 *
 * Revision 1.2  2008/09/03 14:19:08  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.4  2008/09/03 12:22:55  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.3  2008/08/29 07:28:30  pah
 * moved most of the commandline CEC into the main server - also new schema for CEAImplementation in preparation for DAL compatible service registration
 *
 * Revision 1.1.2.2  2008/08/02 13:33:56  pah
 * safety checkin - on vacation
 *
 * Revision 1.1.2.1  2008/04/04 15:46:08  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
