/*
 * $Id: StoreAdminClient.java,v 1.2 2004/06/14 23:08:52 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.astrogrid.community.User;

/**
 * These are the methods that administration delegates to storage
 * mechanisms used by Astrogrid must implement.
 * <p>
 * StoreAdminClients are those that offer administration operations on storage
 * services.
 */

public interface StoreAdminClient {
   
   /**
    * Returns the user of this delegate - ie the account it is being used by
    */
   public User getOperator();
   
   /**
    * Create a new user
    */
   public void createUser(User newUser) throws IOException;

   /**
    * Delete a user
    */
   public void deleteUser(User delUser) throws IOException;
   
   
}

/*
$Log: StoreAdminClient.java,v $
Revision 1.2  2004/06/14 23:08:52  jdt
Merge from branches
ClientServerSplit_JDT
and
MySpaceClientServerSplit_JDT

MySpace now split into a client/delegate jar
astrogrid-myspace-<version>.jar
and a server/manager war
astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:20  jdt
Split into delegate jar and server war.  
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.2  2004/03/19 12:39:37  mch
Added StoreAdminClient implementation to LocalFileStore

Revision 1.1  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

 */

