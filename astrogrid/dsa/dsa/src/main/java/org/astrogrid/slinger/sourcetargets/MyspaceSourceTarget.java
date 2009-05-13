/*
 * $Id: MyspaceSourceTarget.java,v 1.1 2009/05/13 13:20:41 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.sourcetargets;

import java.io.*;

import java.net.URISyntaxException;
import java.net.URLConnection;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.store.Ivorn;
import org.astrogrid.slinger.agfm.FileManagerConnection;
import org.astrogrid.slinger.homespace.HomespaceName;
/*
import org.astrogrid.community.resolver.CommunityAccountSpaceResolver;
import org.astrogrid.community.common.exception.CommunityServiceException;
import org.astrogrid.community.common.exception.CommunityIdentifierException;
*/


/**
 * Homespace targets and sources should normally be represented as UrlSourceTarget with
 * the string 'homespace:individual@community/path/file'. However in some circumstances
 * the URL plugin handler mechanism doesn't work very well - notably in tomcat, where
 * the handlers must be included in the endorsed directory, and therefore also all the
 * dependent client, axis, etc classes, and this can raise conflict problems.
 * Rather than try and solve all these, this class provides a mechanism for specifying
 * a homepsace source/target without using the URL plugin mechanism, and so is
 * suitable for webapps.  It wraps the homespace Handler.
 *
 */

public class MyspaceSourceTarget implements SourceTargetIdentifier {

   String name;
//   HomespaceName name;

   LoginAccount login = LoginAccount.ANONYMOUS;

   URLConnection connection = null;
   
   public MyspaceSourceTarget(String name) throws URISyntaxException {
      this(name, LoginAccount.ANONYMOUS);
      //this.name = name;
   }

   public MyspaceSourceTarget(String name, LoginAccount userLogin) {
      //name = new HomespaceName(homespace);
      this.name = name;
      login = userLogin;
   }

   public String toURI() {
      //return name.toString();
      return name;
   }
   
   /** lazy connection creation, so it's only created when we start writing to it.
    * Note that, like the homespace.Handler, this naughtily assumes that the
    * homespace will resolve to a filemanager because filemanager can't handle
    * anything else :-( */
   private synchronized URLConnection getConnection() throws IOException {
      if (connection == null) {
         try {
            connection =  new FileManagerConnection(new Ivorn(name));
         }
         catch (Exception e) {
            throw new RuntimeException("Failed to open FileManagerConnection for ivorn "+name, e);
         }
      }
      return connection;
   }
   
   /** opens output stream to the URL */
   public OutputStream openOutputStream() throws IOException {
      return getConnection().getOutputStream();
   }

   /** Used to set the mime type of the data about to be sent to the target. . */
   public void setMimeType(String mimeType) throws IOException {
      getConnection().setRequestProperty ("Content-Type", mimeType);
   }

   public String toString() {
      return "[Destination: '"+name.toString()+"']";
   }

   /** Resolves writer as a wrapper around resolved outputstream */
   public Writer openWriter() throws IOException {
      return new OutputStreamWriter(openOutputStream());
   }
   
   public InputStream openInputStream() throws IOException {
      return getConnection().getInputStream();
   }

   public String getMimeType() throws IOException {
      return getConnection().getContentType();
   }
   
   /** Returns an OutputStreamWrapper around the resolved stream */
   public Reader openReader() throws IOException {
      return new InputStreamReader(openInputStream());
   }

   
}

