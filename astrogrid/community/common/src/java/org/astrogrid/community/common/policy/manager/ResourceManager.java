/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/common/src/java/org/astrogrid/community/common/policy/manager/ResourceManager.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2004/01/07 10:45:38 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManager.java,v $
 *   Revision 1.2  2004/01/07 10:45:38  dave
 *   Merged development branch, dave-dev-20031224, back into HEAD
 *
 *   Revision 1.1.2.2  2004/01/05 06:47:18  dave
 *   Moved policy data classes into policy.data package
 *
 *   Revision 1.1.2.1  2003/12/24 05:54:48  dave
 *   Initial Maven friendly structure (only part of the service implemented)
 *
 *   Revision 1.4  2003/11/06 15:35:26  dave
 *   Replaced tabs with spaces
 *
 *   Revision 1.3  2003/09/12 12:59:17  dave
 *   1) Fixed RemoteException handling in the manager and service implementations.
 *
 *   Revision 1.2  2003/09/10 00:08:45  dave
 *   Added getGroupMembers, ResourceIdent and JUnit tests for ResourceManager
 *
 *   Revision 1.1  2003/09/09 19:13:32  KevinBenson
 *   New resource managerr stuff
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.common.policy.manager ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.common.policy.data.ResourceData ;

public interface ResourceManager
    extends java.rmi.Remote
   {
   /**
    * Create a new Resource.
    *
    */
   public ResourceData addResource(String name)
        throws RemoteException ;

   /**
    * Request an Resource details.
    *
    */
   public ResourceData getResource(String ident)
        throws RemoteException ;

   /**
    * Update an Resource details.
    *
    */
   public ResourceData setResource(ResourceData resource)
        throws RemoteException ;

   /**
    * Delete an Resource.
    *
    */
   public boolean delResource(String ident)
        throws RemoteException ;

   /**
    * Request a list of Resources.
    *
    */
   public Object[] getResourceList()
        throws RemoteException ;

   }
