/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/server/Attic/ResourceManager.java,v $</cvs:source>
 * <cvs:author>$Author: KevinBenson $</cvs:author>
 * <cvs:date>$Date: 2003/09/09 19:13:32 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: ResourceManager.java,v $
 *   Revision 1.1  2003/09/09 19:13:32  KevinBenson
 *   New resource managerr stuff
 *
 *   Revision 1.2  2003/09/08 20:28:50  dave
 *   Added CommunityIdent, with isLocal() and isValid()
 *
 *   Revision 1.1  2003/09/06 20:10:07  dave
 *   Split PolicyManager into separate components.
 *
 * </cvs:log>
 *
 */
package org.astrogrid.community.policy.server ;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import org.astrogrid.community.policy.data.ResourceData ;

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
