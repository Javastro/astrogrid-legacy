/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/loader/junit/Attic/JUnitLoaderTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/11/06 15:35:26 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: JUnitLoaderTest.java,v $
 *   Revision 1.2  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.1  2003/09/24 15:47:38  dave
 *   Added policy database loader tools.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.loader.junit ;

import org.jconfig.Configuration ;

import java.io.File ;

import junit.framework.TestCase ;

import org.astrogrid.community.common.util.xml.sax.SAXElementHandler ;
import org.astrogrid.community.common.util.xml.sax.SAXDocumentHandler ;

import org.astrogrid.community.policy.loader.* ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 * JUnit test for the CommunityConfig.
 *
 */
public class JUnitLoaderTest
    extends TestCase
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Switch for our assert statements.
     *
     */
    private static final boolean ASSERT_FLAG = false ;

    /**
     * Our manager locator.
     *
     */
    private PolicyManagerService locator ;

    /**
     * Our manager.
     *
     */
    private PolicyManager manager ;

    /**
     * Setup our tests.
     *
     */
    protected void setUp()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("setUp()") ;

        //
        // Create our manager locator.
        locator = new PolicyManagerServiceLocator() ;
        assertNotNull("Null manager locator", locator) ;
        //
        // Create our manager.
        manager = locator.getPolicyManager() ;
        assertNotNull("Null manager", manager) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Test we can load accounts.
     *
     */
    public void testLoadAccounts()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testLoadAccounts()") ;

        //
        // Get the location of our data files.
        String path = System.getProperty("org.astrogrid.community.data") ;
        if (DEBUG_FLAG) System.out.println("  Path : " + path) ;

        //
        // Load our data file.
        File file = new File(path, "accounts.xml") ;
        //
        // Create a commnity data parser.
        SAXDocumentHandler parser = new SAXDocumentHandler() ;
        parser.addElementHandler(
            new PolicyDataParser(manager)
            ) ;
        //
        // Parse our data file.
        parser.parse(file) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Test we can load groups.
     *
     */
    public void testLoadGroups()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testLoadGroups()") ;

        //
        // Get the location of our data files.
        String path = System.getProperty("org.astrogrid.community.data") ;
        if (DEBUG_FLAG) System.out.println("  Path : " + path) ;

        //
        // Load our data file.
        File file = new File(path, "groups.xml") ;
        //
        // Create a commnity data parser.
        SAXDocumentHandler parser = new SAXDocumentHandler() ;
        parser.addElementHandler(
            new PolicyDataParser(manager)
            ) ;
        //
        // Parse our data file.
        parser.parse(file) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Test we can load resources.
     *
     */
    public void testLoadResources()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testLoadResources()") ;

        //
        // Get the location of our data files.
        String path = System.getProperty("org.astrogrid.community.data") ;
        if (DEBUG_FLAG) System.out.println("  Path : " + path) ;

        //
        // Load our data file.
        File file = new File(path, "resources.xml") ;
        //
        // Create a commnity data parser.
        SAXDocumentHandler parser = new SAXDocumentHandler() ;
        parser.addElementHandler(
            new PolicyDataParser(manager)
            ) ;
        //
        // Parse our data file.
        parser.parse(file) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Test we can load group members.
     *
     */
    public void testLoadMembers()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testLoadMembers()") ;

        //
        // Get the location of our data files.
        String path = System.getProperty("org.astrogrid.community.data") ;
        if (DEBUG_FLAG) System.out.println("  Path : " + path) ;

        //
        // Load our data file.
        File file = new File(path, "members.xml") ;
        //
        // Create a commnity data parser.
        SAXDocumentHandler parser = new SAXDocumentHandler() ;
        parser.addElementHandler(
            new PolicyDataParser(manager)
            ) ;
        //
        // Parse our data file.
        parser.parse(file) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Test we can load communities.
     *
     */
    public void testLoadCommunities()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testLoadCommunities()") ;

        //
        // Get the location of our data files.
        String path = System.getProperty("org.astrogrid.community.data") ;
        if (DEBUG_FLAG) System.out.println("  Path : " + path) ;

        //
        // Load our data file.
        File file = new File(path, "communities.xml") ;
        //
        // Create a commnity data parser.
        SAXDocumentHandler parser = new SAXDocumentHandler() ;
        parser.addElementHandler(
            new PolicyDataParser(manager)
            ) ;
        //
        // Parse our data file.
        parser.parse(file) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Test we can load permissions.
     *
     */
    public void testLoadPermissions()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testLoadPermissions()") ;

        //
        // Get the location of our data files.
        String path = System.getProperty("org.astrogrid.community.data") ;
        if (DEBUG_FLAG) System.out.println("  Path : " + path) ;

        //
        // Load our data file.
        File file = new File(path, "permissions.xml") ;
        //
        // Create a commnity data parser.
        SAXDocumentHandler parser = new SAXDocumentHandler() ;
        parser.addElementHandler(
            new PolicyDataParser(manager)
            ) ;
        //
        // Parse our data file.
        parser.parse(file) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }

    /**
     * Test we can load the complete set.
     *
     */
    public void testLoadCombined()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("testLoadCombined()") ;

        //
        // Get the location of our data files.
        String path = System.getProperty("org.astrogrid.community.data") ;
        if (DEBUG_FLAG) System.out.println("  Path : " + path) ;

        //
        // Load our data file.
        File file = new File(path, "combined.xml") ;
        //
        // Create a commnity data parser.
        SAXDocumentHandler parser = new SAXDocumentHandler() ;
        parser.addElementHandler(
            new PolicyDataParser(manager)
            ) ;
        //
        // Parse our data file.
        parser.parse(file) ;

        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("") ;
        }
    }
