/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/ResourceManagerTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/08/12 16:08:47 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerTest.java,v $
 *   Revision 1.5  2005/08/12 16:08:47  clq2
 *   com-jl-1315
 *
 *   Revision 1.4.80.1  2005/07/26 11:30:19  jl99
 *   Tightening up of unit tests for the server subproject
 *
 *   Revision 1.4  2004/11/22 13:03:04  jdt
 *   Merges from Comm_KMB_585
 *
 *   Revision 1.3.22.1  2004/11/05 08:55:49  KevinBenson
 *   Moved the GroupMember out of PolicyManager in the commons and client section.
 *   Added more unit tests for GroupMember and PermissionManager for testing.
 *   Still have some errors that needs some fixing.
 *
 *   Revision 1.3  2004/09/16 23:18:08  dave
 *   Replaced debug logging in Community.
 *   Added stream close() to FileStore.
 *
 *   Revision 1.2.82.1  2004/09/16 09:58:48  dave
 *   Replaced debug with commons logging ....
 *
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

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.community.common.policy.data.ResourceData ;

import org.astrogrid.community.common.exception.CommunityResourceException   ;
import org.astrogrid.community.common.exception.CommunityIdentifierException ;

import org.astrogrid.community.common.service.CommunityServiceTest ;

public class ResourceManagerTest
    extends CommunityServiceTest
    {
    /**
     * Our debug logger.
     *
     */
    private static Log log = LogFactory.getLog(ResourceManagerTest.class);

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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest.setResourceManager()") ;
        log.debug("  Manager : " + manager.getClass()) ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testRegisterValid()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testGetValid()") ;
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
     * Check we can get a list of resources
     *
     */
    public void testGetResources()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testGetResources()") ;
        //
        // Create some resources.
        ResourceData created01 = this.resourceManager.addResource() ;
        assertNotNull( "Null resource", created01 ) ;
        ResourceData created02 = this.resourceManager.addResource() ;
        assertNotNull( "Null resource", created02 ) ;
 
        //
        // Try getting a list
        Object found[] = this.resourceManager.getResources() ;
        assertNotNull(
            "Null resource array",
            found
            ) ;
 
        }
    
    /**
     * Check we can find a known Resource.
     *
     */
    public ResourceData testGetValidResourceData()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testGetValid()") ;
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
            return found;
        }
    

    /**
     * Check the service won't find an unknown Resource.
     *
     */
    public void testGetUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testGetUnknown()") ;
        //
        // Try finding an unknown resource.
        try {
            this.resourceManager.getResource("unknown") ;
            fail("Expected CommunityResourceException") ;
            }
        catch (CommunityResourceException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check the service won't find a null Resource.
     *
     */
    public void testGetNullIdent()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testGetNullIdent()") ;
        //
        // Try finding a resource with a null ident.
        try {
            this.resourceManager.getResource(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can update a known Resource.
     *
     */
    public void testSetValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testSetValid()") ;
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
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testSetNull()") ;
        //
        // Try changing a null resource.
        try {
            this.resourceManager.setResource(null) ;
            fail("Expected CommunityResourceException") ;
            }
        catch (CommunityResourceException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can't update a Resource with a null ident.
     *
     */
    public void testSetNullIdent()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testSetNullIdent()") ;
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
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can't update a Resource with an invalid ident.
     *
     */
    public void testSetUnknownIdent()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testSetUnknownIdent()") ;
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
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check we can delete a known Resource.
     *
     */
    public void testDelValid()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testDelValid()") ;
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
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check the service won't delete an unknown Resource.
     *
     */
    public void testDelUnknown()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testDelUnknown()") ;
        //
        // Try deleting an unknown resource.
        try {
            this.resourceManager.delResource("unknown") ;
            fail("Expected CommunityResourceException") ;
            }
        catch (CommunityResourceException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }

    /**
     * Check the service won't delete a null Resource.
     *
     */
    public void testDelNullIdent()
        throws Exception
        {
        log.debug("") ;
        log.debug("----\"----") ;
        log.debug("ResourceManagerTest:testDelNullIdent()") ;
        //
        // Try deleting a resource with a null ident.
        try {
            this.resourceManager.delResource(null) ;
            fail("Expected CommunityIdentifierException") ;
            }
        catch (CommunityIdentifierException ouch)
            {
            log.debug("Caught expected Exception") ;
            log.debug("Exception : " + ouch) ;
            log.debug("Class     : " + ouch.getClass()) ;
            }
        }
    }
