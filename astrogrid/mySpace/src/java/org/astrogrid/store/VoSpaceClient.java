/*
 * $Id: VoSpaceClient.java,v 1.3 2004/04/06 09:00:46 KevinBenson Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.astrogrid.community.User;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreAdminClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.VoSpaceResolver;
import java.net.URISyntaxException;

/**
 * This delegate provides methods for operating on files in VoSpace - that is,
 * files that are on store points (accessible through <code>StoreClient</code> implementations)
 * that are Registered in IVO Registries and/or Communities.
 * <p>
 * Note that there is a difference between the user (termed <b>operator</b> here) of
 * this class, and the owner of the files that might be being browsed.
 */

public class VoSpaceClient {
   
   private User operator = null;
   
   /**
    * Constructor - instances of this class represent a particular user/account
    */
   public VoSpaceClient(User anOperator) {
      this.operator = anOperator;
   }
   
   /**
    * Returns the user of this delegate - ie the account it is being used by
    */
   public User getOperator() {
      return operator;
   }
   
   /**
    * Returns the StoreFile representation of the file at the given AGSL
    */
   public StoreFile getFile(Ivorn fileIvorn) throws IOException {
      Agsl fileAgsl = VoSpaceResolver.resolveAgsl(fileIvorn);
      StoreClient client = StoreDelegateFactory.createDelegate(operator, fileAgsl);
      return client.getFile(fileAgsl.getPath());
   }

   /**
    * Puts the given byte buffer from offset of length bytes, to the given target
    */
   public void putBytes(byte[] bytes, int offset, int length, Ivorn targetIvorn, boolean append) throws IOException {
      Agsl fileAgsl = VoSpaceResolver.resolveAgsl(targetIvorn);
      StoreClient client = StoreDelegateFactory.createDelegate(operator, fileAgsl);
      client.putBytes(bytes, offset, length, fileAgsl.getPath(), append);
   }

   /**
    * Copies the contents of the file at the given source url to the given location
    */
   public void putUrl(URL source, Ivorn targetIvorn, boolean append) throws IOException {
      Agsl fileAgsl = VoSpaceResolver.resolveAgsl(targetIvorn);
      StoreClient client = StoreDelegateFactory.createDelegate(operator, fileAgsl);
      client.putUrl(source, fileAgsl.getPath(), append);
   }

   /**
    * Streaming output - returns a stream that can be used to output to the given
    * location
    */
   public OutputStream putStream(Ivorn target) throws IOException {
      return VoSpaceResolver.resolveOutputStream(getOperator(), target);
   }

   /**
    * Gets a file's contents as a stream
    */
   public InputStream getStream(Ivorn source) throws IOException {
      return VoSpaceResolver.resolveInputStream(getOperator(), source);
   }
   
   /**
    * Delete a file
    */
   public void delete(Ivorn toDelete) throws IOException {
      Agsl fileAgsl = VoSpaceResolver.resolveAgsl(toDelete);
      StoreClient client = StoreDelegateFactory.createDelegate(operator, fileAgsl);
      client.delete(fileAgsl.getPath());
   }

   /**
    * Copy a file
    */
   public void copy(Ivorn source, Ivorn target) throws IOException
   {
      Agsl sourceAgsl = VoSpaceResolver.resolveAgsl(source);
      Agsl targetAgsl = VoSpaceResolver.resolveAgsl(target);
      StoreClient sourceClient = StoreDelegateFactory.createDelegate(operator, sourceAgsl);
      sourceClient.copy(sourceAgsl.getPath(), targetAgsl);
   }
   
   /**
    * Moves/Renames a file
    */
   public void move(Ivorn source, Ivorn target) throws IOException {
      Agsl sourceAgsl = VoSpaceResolver.resolveAgsl(source);
      Agsl targetAgsl = VoSpaceResolver.resolveAgsl(target);
      StoreClient sourceClient = StoreDelegateFactory.createDelegate(operator, sourceAgsl);
      sourceClient.move(sourceAgsl.getPath(), targetAgsl);
   }
   
   /**
    * Create a container
    */
   public void newFolder(Ivorn target) throws IOException {
      Agsl fileAgsl = VoSpaceResolver.resolveAgsl(target);
      StoreClient client = StoreDelegateFactory.createDelegate(operator, fileAgsl);
      client.newFolder(fileAgsl.getPath());
   }
   
   public Ivorn createUser(Ivorn target, String user) throws IOException, URISyntaxException {
      Agsl vospaceTarget = VoSpaceResolver.resolveAgsl(target);
      StoreAdminClient client = StoreDelegateFactory.createAdminDelegate(operator, vospaceTarget);      
      client.createUser(new User(user,null,null));
      return new Ivorn(target.toString() + "#" + user);
   }
   
   public void deleteUser(Ivorn target, String user) throws IOException, URISyntaxException {
      Agsl vospaceTarget = VoSpaceResolver.resolveAgsl(target);
      StoreAdminClient client = StoreDelegateFactory.createAdminDelegate(operator, vospaceTarget);      
      client.deleteUser(new User(user,null,null));
   }
   
}

/*
$Log: VoSpaceClient.java,v $
Revision 1.3  2004/04/06 09:00:46  KevinBenson
changed it around so that it calls community first then registry to figvure out endpoints

Revision 1.2  2004/03/25 12:21:59  mch
Tidied doc

Revision 1.1  2004/03/22 10:25:42  mch
Added VoSpaceClient, StoreDelegate, some minor changes to StoreClient interface

Revision 1.5  2004/03/17 15:17:29  mch
Added putBytes

Revision 1.4  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

 */

