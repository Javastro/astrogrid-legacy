/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/ResourceManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerTest.java,v $
 *   Revision 1.2  2004/06/18 13:45:20  dave
 *   Merged development branch, dave-dev-200406081614, into HEAD
 *
 *   Revision 1.1.2.6  2004/06/17 15:53:22  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.1.2.5  2004/06/17 15:00:21  dave
 *   Fixed debug flag
 *
 *   Revision 1.1.2.4  2004/06/17 14:50:03  dave
 *   Removed unused imports (PMD report).
 *
 *   Revision 1.1.2.3  2004/06/17 13:38:59  dave
 *   Tidied up old CVS log entries
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;


import org.astrogrid.community.common.policy.data.ResourceData ;

import org.astrogrid.community.common.exception.CommunityResourceException   ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

public class ResourceManagerTest
    extends CommunityServiceTest
    {
    /**
     * Switch for our debug statements.
     * @todo Refactor to use the common logging.
     *
     */
    private static final boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public ResourceManagerTest()
        {
        }

    /**
     * Our target ResourceManager.
     *
     */
    private ResourceManager resourceManager ;

    /**
     * Get our target GroupManager.
     *
     */
    public ResourceManager getResourceManager()
        {
        return this.resourceManager ;
        }

    /**
     * Set our target ResourceManager.
     *
     */
    public void setResourceManager(ResourceManager manager)
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest.setResourceManager()") ;
        if (DEBUG_FLAG) System.out.println("  Manager : " + manager.getClass()) ;
        //
        // Set our ResourceManager reference.
        this.resourceManager = manager ;
        //
        // Set our CommunityService reference.
        this.setCommunityService(manager) ;
        }

    /**
     * Check we can create a Resource.
     *
     */
    public void testRegisterValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testRegisterValid()") ;
        //
        // Try creating the Resource.
        assertNotNull(
            "Null resource",
            this.resourceManager.addResource()
            ) ;
        }

    /**
     * Check we can find a known Resource.
     *
     */
    public void testGetValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testGetValid()") ;
        //
        // Try creating the Resource.
        ResourceData created = this.resourceManager.addResource() ;
        assertNotNull(
            "Null resource",
            created
            ) ;
        //
        // Get the resource ident.
        String ident = created.getIdent() ;
        assertNotNull(
            "Null resource ident",
            ident
            ) ;
        //
        // Try getting the resource details.
        ResourceData found = this.resourceManager.getResource(ident) ;
        assertNotNull(
            "Null resource",
            found
            ) ;
        //
        // Check that the identifiers are the same.
        assertEquals(
            "Identifiers don't match",
            created.getIdent(),
            found.getIdent()
            ) ;
        }

    /**
     * Check the service won't find an unknown Resource.
     *
     */
    public void testGetUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testGetUnknown()") ;
        //
        // Try finding an unknown resource.
        try {
            this.resourceManager.getResource("unknown") ;
            fail("Expected CommunityResourceException") ;
            }
        catch (CommunityResourceException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check the service won't find a null Resource.
     *
     */
    public void testGetNullIdent()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testGetNullIdent()") ;
        //
        // Try finding a resource with a null ident.
        try {
            this.resourceManager.getResource(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can update a known Resource.
     *
     */
    public void testSetValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testSetValid()") ;
        //
        // Try creating the Resource.
        ResourceData created = this.resourceManager.addResource() ;
        assertNotNull(
            "Null resource",
            created
            ) ;
        //
        // Get the resource ident.
        String ident = created.getIdent() ;
        assertNotNull(
            "Null resource ident",
            ident
            ) ;
        //
        // Try changing the resource details.
        String description = "Test description" ;
        created.setDescription(description) ;
        ResourceData modified = this.resourceManager.setResource(created) ;
        assertNotNull(
            "Null resource",
            modified
            ) ;
        //
        // Check the resource was updated.
        assertEquals("Descriptions don't match",
            description,
            modified.getDescription()
            ) ;
        //
        // Try getting the resource details.
        ResourceData found = this.resourceManager.getResource(ident) ;
        assertNotNull(
            "Null resource",
            found
            ) ;
        //
        // Check the resource was updated.
        assertEquals(
            "Descriptions don't match",
            description,
            found.getDescription()
            ) ;
        }

    /**
     * Check we can't update a null Resource.
     *
     */
    public void testSetNull()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testSetNull()") ;
        //
        // Try changing a null resource.
        try {
            this.resourceManager.setResource(null) ;
            fail("Expected CommunityResourceException") ;
            }
        catch (CommunityResourceException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can't update a Resource with a null ident.
     *
     */
    public void testSetNullIdent()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testSetNullIdent()") ;
        //
        // Try changing a null resource.
        try {
            this.resourceManager.setResource(
                new ResourceData()
                ) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can't update a Resource with an invalid ident.
     *
     */
    public void testSetUnknownIdent()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testSetUnknownIdent()") ;
        //
        // Try changing an unknwon resource.
        try {
            this.resourceManager.setResource(
                new ResourceData("unknown")
                ) ;
            fail("Expected CommunityResourceException") ;
            }
        catch (CommunityResourceException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can delete a known Resource.
     *
     */
    public void testDelValid()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testDelValid()") ;
        //
        // Try creating the Resource.
        ResourceData created = this.resourceManager.addResource() ;
        assertNotNull(
            "Null resource",
            created
            ) ;
        //
        // Get the resource ident.
        String ident = created.getIdent() ;
        assertNotNull(
            "Null resource ident",
            ident
            ) ;
        //
        // Try finding the resource details.
        ResourceData found = this.resourceManager.getResource(ident) ;
        assertNotNull(
            "Null resource",
            found
            ) ;
        //
        // Try deleting the resource.
        ResourceData deleted = this.resourceManager.delResource(ident) ;
        assertNotNull(
            "Null resource",
            deleted
            ) ;
        //
        // Try finding the resource details.
        try {
            this.resourceManager.getResource(ident) ;
            fail("Expected CommunityResourceException") ;
            }
        catch (CommunityResourceException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check the service won't delete an unknown Resource.
     *
     */
    public void testDelUnknown()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testDelUnknown()") ;
        //
        // Try deleting an unknown resource.
        try {
            this.resourceManager.delResource("unknown") ;
            fail("Expected CommunityResourceException") ;
            }
        catch (CommunityResourceException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check the service won't delete a null Resource.
     *
     */
    public void testDelNullIdent()
        throws Exception
        {
        if (DEBUG_FLAG) System.out.println("") ;
        if (DEBUG_FLAG) System.out.println("----\"----") ;
        if (DEBUG_FLAG) System.out.println("ResourceManagerTest:testDelNullIdent()") ;
        //
        // Try deleting a resource with a null ident.
        try {
            this.resourceManager.delResource(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            if (DEBUG_FLAG) System.out.println("Caught expected Exception") ;
            if (DEBUG_FLAG) System.out.println("Exception : " + ouch) ;
            if (DEBUG_FLAG) System.out.println("Class     : " + ouch.getClass()) ;
            }
        }
    }
