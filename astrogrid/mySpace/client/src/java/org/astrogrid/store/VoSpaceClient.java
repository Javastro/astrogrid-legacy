/*
 * $Id: VoSpaceClient.java,v 1.2 2004/06/14 23:08:53 jdt Exp $
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
import org.astrogrid.store.delegate.ResolverException;
import java.net.URISyntaxException;
import org.astrogrid.community.common.ivorn.CommunityIvornParser;
import org.astrogrid.store.delegate.StoreException;

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
   
   /**
    * Creates a new user (identified by the given user ivorn) on the storepoint given by
    * the 'target' Ivorn.
    */
   public Ivorn createUser(Ivorn targetVoSpace, Ivorn user) throws IOException, URISyntaxException {
      //Agsl vospaceTarget = VoSpaceResolver.resolveAgsl(target);
      StoreAdminClient client = VoSpaceResolver.resolveStoreAdmin(operator, targetVoSpace);
      CommunityIvornParser ci = null;
      try {
         ci = new CommunityIvornParser(user);
      }catch(Exception e) {
         throw new StoreException("Could not parse ivorn user ",e);
      }
            
      client.createUser(new User(ci.getAccountName(),ci.getCommunityName(),"Anonymous","None"));
      return new Ivorn(targetVoSpace.toString());
   }
   
   public void deleteUser(Ivorn target, Ivorn user) throws IOException {
      //Agsl vospaceTarget = VoSpaceResolver.resolveAgsl(target);
      StoreAdminClient client = VoSpaceResolver.resolveStoreAdmin(operator, target);
      CommunityIvornParser ci = null;
      try {
         ci = new CommunityIvornParser(user);
      }catch(Exception e) {
         throw new ResolverException("Could not parse ivorn user ",e);
      }
      client.deleteUser(new User(ci.getAccountName(),ci.getCommunityName(),"Anonymous","None"));
   }
   
}

/*
$Log: VoSpaceClient.java,v $
Revision 1.2  2004/06/14 23:08:53  jdt
Merge from branches
ClientServerSplit_JDT
and
MySpaceClientServerSplit_JDT

MySpace now split into a client/delegate jar
astrogrid-myspace-<version>.jar
and a server/manager war
astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:21  jdt
Split into delegate jar and server war.  
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.10  2004/04/21 09:41:38  mch
Added softwired shortcut for auto-integration tests

Revision 1.9  2004/04/20 15:26:46  mch
More complete error reporting and simplified resolving (not as featureful ? as before)

Revision 1.8  2004/04/20 12:35:32  mch
Moved std.out stacktrace to wrapping exception

Revision 1.7  2004/04/16 10:54:31  KevinBenson
*** empty log message ***

Revision 1.6  2004/04/16 08:17:10  KevinBenson
*** empty log message ***

Revision 1.5  2004/04/16 08:15:38  KevinBenson
small change to split out the reolving of a "myspace" resourcekey.

Revision 1.4  2004/04/15 16:41:41  KevinBenson
new way of using the user object for createUser.

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



