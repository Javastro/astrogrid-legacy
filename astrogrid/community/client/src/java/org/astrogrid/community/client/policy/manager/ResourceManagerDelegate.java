/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/client/src/java/org/astrogrid/community/client/policy/manager/ResourceManagerDelegate.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/03/19 14:43:14 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManagerDelegate.java,v $
 *   Revision 1.5  2004/03/19 14:43:14  dave
 *   Merged development branch, dave-dev-200403151155, into HEAD
 *
 *   Revision 1.4.2.1  2004/03/19 00:18:09  dave
 *   Refactored delegate Exception handling
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.client.policy.manager ;

import org.astrogrid.community.client.service.CommunityServiceDelegate ;

import org.astrogrid.community.common.policy.data.ResourceData ;
import org.astrogrid.community.common.policy.manager.ResourceManager ;
import org.astrogrid.community.common.service.data.ServiceStatusData ;

/**
 * Interface for our ResourceManager delegate.
 * This mirrors the ResourceManager interface, without the RemoteExceptions.
 *
 */
public interface ResourceManagerDelegate
    extends CommunityServiceDelegate
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
