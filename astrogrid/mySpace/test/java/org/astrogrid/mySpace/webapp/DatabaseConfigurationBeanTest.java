/*
 * Created on Feb 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.astrogrid.mySpace.webapp;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

/**
 * @author jdt
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DatabaseConfigurationBeanTest extends TestCase {
	private static Log log = LogFactory.getLog(DatabaseConfigurationBeanTest.class);
	private File testRegistry = new File("testRegistry");
	private File testRegistryProperties = new File(testRegistry + ".db.properties");
	private File testRegistryScript = new File(testRegistry + ".db.script");
	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		if (testRegistryProperties.exists()) {
			testRegistryProperties.delete();
		}
		if (testRegistryScript.exists()) {
			testRegistryScript.delete();
		}
	}





	public void testCreateRegistry() {
		DatabaseConfigurationBean db = new DatabaseConfigurationBean();
		
		db.setHost("http://um01.astrogrid.org:8080");
		log.debug("testRegistry: "+testRegistry);
		log.debug("testRegistry absolute path: "+testRegistry.getAbsolutePath());
		db.setPathToRegistry(testRegistry.getAbsolutePath());
		db.setFolderName("/home/tomcat");
		db.setFolderName("ROOT/mySpaceConfig");
		db.createRegistry();
		assertTrue(testRegistryProperties.exists());
		assertTrue(testRegistryScript.exists());
	}



	public void testSetGetExpiry() {
		DatabaseConfigurationBean db = new DatabaseConfigurationBean();
		final int foo = 1;
		db.setExpiry(foo);
		assertEquals(foo, db.getExpiry());
	}


	public void testSetGetHost() {
		DatabaseConfigurationBean db = new DatabaseConfigurationBean();
		final String foo = "foo";
		db.setHost(foo);
		assertEquals(foo, db.getHost());
	}

	public void testSetGetPathToRegistry() {
		DatabaseConfigurationBean db = new DatabaseConfigurationBean();
		final String foo = "foo";
		db.setPathToRegistry(foo);
		assertEquals(foo, db.getPathToRegistry());
	}

	public void testSetGetServerName() {
		DatabaseConfigurationBean db = new DatabaseConfigurationBean();
		final String foo = "foo";
		db.setServerName(foo);
		assertEquals(foo, db.getServerName());
	}

	public void testSetGetTomcatRoot() {
		DatabaseConfigurationBean db = new DatabaseConfigurationBean();
		final String foo = "foo";
		db.setTomcatRoot(foo);
		assertEquals(foo, db.getTomcatRoot());
	}


	public void testSetGetWebAppName() {
		DatabaseConfigurationBean db = new DatabaseConfigurationBean();
		final String foo = "foo";
		db.setFolderName(foo);
		assertEquals(foo, db.getFolderName());
	}
	
	public void testGetURL(){
		DatabaseConfigurationBean db = new DatabaseConfigurationBean();
		db.setHost("http://localhost:8080");
		db.setFolderName("mySpaceConfig");
		assertEquals("http://localhost:8080/mySpaceConfig/", db.getURL());
	}
	public void testGetDirectory(){
		DatabaseConfigurationBean db = new DatabaseConfigurationBean();
		db.setHost("http://localhost:8080");
		db.setFolderName("mySpaceConfig");
		db.setTomcatRoot("/home/tomcat");
		assertEquals("/home/tomcat/webapps/mySpaceConfig/", db.getDirectory());
	}
}
