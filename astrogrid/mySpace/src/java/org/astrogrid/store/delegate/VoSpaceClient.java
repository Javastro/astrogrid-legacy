/*
 * $Id: VoSpaceClient.java,v 1.1 2004/03/01 22:38:46 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store.delegate;

import java.io.IOException;
import java.io.InputStream;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreDelegateFactory;

/**
 * A VoSpaceClient is used to access storepoints anywhere on the Virtual Observatory.
 * As such it is a 'higher' level delegate than the StoreClient implemenations
 * which only handle particular service types.
 * This class takes IVO ids and resolves these to StoreClients that can be used
 * to access the the store services or files.  As such it is really a factory,
 * making appropriate delegates as required, but it offers convenient methods
 * for, eg,  returning streams directly to IVO-identified files.
 *
 */

public class VoSpaceClient {
   
   /**
    * Given an IVO Resource Name, resolves the AGSL
    * @todo implement
    */
   public Agsl resolveAgsl(Ivorn ivorn) throws IOException {
      //we need some kind of 'type' information here...
      throw new UnsupportedOperationException();
      
   }
   
   /**
    * Given an IVO Resource Name, looks it up in the community/registry, and
    * returns the appropriate StoreClient to access it. Ignores any fragment
    * given.  The user is the account attempting to access the store.
    */
   public StoreClient resolveStore(User user, Ivorn ivorn) throws IOException {
      return StoreDelegateFactory.createDelegate(user, resolveAgsl(ivorn));
   }
   
   /**
    * Given an IVO Resource Name, resolves the Stream.  The user is the account
    * attempting to access the file.
    */
   public InputStream resolveStream(User user, Ivorn ivorn) throws IOException {
      return resolveAgsl(ivorn).openStream(user);
   }
   

   
}

/*
$Log: VoSpaceClient.java,v $
Revision 1.1  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

 */

