/*
 * $Id: StoreAdminClient.java,v 1.2 2004/03/19 12:39:37 mch Exp $
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
Revision 1.2  2004/03/19 12:39:37  mch
Added StoreAdminClient implementation to LocalFileStore

Revision 1.1  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

 */

