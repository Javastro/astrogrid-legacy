/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/ResourceManagerMock.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/15 07:49:30 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerMock.java,v $
 *   Revision 1.4  2004/03/15 07:49:30  dave
 *   Merged development branch, dave-dev-200403121536, into HEAD
 *
 *   Revision 1.3.12.2  2004/03/13 17:57:20  dave
 *   Remove RemoteException(s) from delegate interfaces.
 *   Protected internal API methods.
 *
 *   Revision 1.3.12.1  2004/03/13 16:08:08  dave
 *   Added CommunityAccountResolver and CommunityEndpointResolver.
 *
 *   Revision 1.3  2004/03/08 13:42:33  dave
 *   Updated Maven goals.
 *   Replaced tabs with Spaces.
 *
 *   Revision 1.2.2.1  2004/03/08 12:53:17  dave
 *   Changed tabs to spaces
 *
 *   Revision 1.2  2004/03/05 17:19:59  dave
 *   Merged development branch, dave-dev-200402211936, into HEAD
 *
 *   Revision 1.1.2.1  2004/03/04 13:26:17  dave
 *   1) Added Delegate interfaces.
 *   2) Added Mock implementations.
 *   3) Added MockDelegates
 *   4) Added SoapDelegates
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.util.Map ;
import java.util.HashMap ;

import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.service.CommunityServiceMock ;

/**
 * Mock implementation of our ResourceManager service.
 *
 */
public class ResourceManagerMock
    extends CommunityServiceMock
    implements ResourceManager
    {
    /**
     * Switch for our debug statements.
     *
     */
    private static boolean DEBUG_FLAG = true ;

    /**
     * Public constructor.
     *
     */
    public ResourceManagerMock()
        {
        super() ;
        }

    /**
     * Our hash table of values.
     *
     */
    private Map map = new HashMap() ;

   /**
    * Create a new Resource.
    *
    */
   public ResourceData addResource(String ident)
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            // TODO
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Check if we already have an existing Resource.
        if (null != this.getResource(ident))
            {
            // TODO
            // Throw a duplicate exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Create a new Resource.
        ResourceData resource = new ResourceData(ident) ;
        //
        // Add it to our map.
        map.put(resource.getIdent(), resource) ;
        //
        // Return the new Resource.
        return resource ;
        }

   /**
    * Request an Resource details.
    *
    */
   public ResourceData getResource(String ident)
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            // TODO
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Lookup the Resource in our map.
        return (ResourceData) map.get(ident) ;
        }

   /**
    * Update an Resource details.
    *
    */
   public ResourceData setResource(ResourceData resource)
        {
        //
        // Check for null param.
        if (null == resource)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // If we don't have an existing Resource.
        if (null == this.getResource(resource.getIdent()))
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            return null ;
            }
        //
        // Replace the existing Resource with the new data.
        map.put(resource.getIdent(), resource) ;
        //
        // Return the new Resource.
        return resource ;
        }

   /**
    * Delete a Resource.
    *
    */
   public boolean delResource(String ident)
        {
        //
        // Check for null ident.
        if (null == ident)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            // TODO - refactor API
            return false ;
            }
        //
        // Try to find the Resource.
        ResourceData resource = this.getResource(ident) ;
        //
        // If we didn't find a matching Resource.
        if (null == resource)
            {
            //
            // Throw an exception ?
            //
            // Return null for now.
            // TODO - refactor API
            return false ;
            }
        //
        // Remove the Resource from our map.
        map.remove(resource.getIdent()) ;
        //
        // Return the old Resource.
        // TODO - refactor API
        return true ;
        }

   /**
    * Request a list of Resources.
    *
    */
   public Object[] getResourceList()
        {
        return map.values().toArray() ;
        }

   }
