/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/ResourceManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/08 13:42:33 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerDelegate.java,v $
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
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.manager.ResourceManager ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Interface for our ResourceManager delegate.
 * This extends the ResourceManager interface, without the RemoteExceptions.
 *
 */
public interface ResourceManagerDelegate
    extends ResourceManager
    {
   /**
    * Create a new Resource.
    *
    */
   public ResourceData addResource(String ident) ;

   /**
    * Request an Resource details.
    *
    */
   public ResourceData getResource(String ident) ;

   /**
    * Update an Resource details.
    *
    */
   public ResourceData setResource(ResourceData resource) ;

   /**
    * Delete an Resource.
    *
    */
   public boolean delResource(String ident) ;

   /**
    * Request a list of Resources.
    *
    */
   public Object[] getResourceList() ;

    }
