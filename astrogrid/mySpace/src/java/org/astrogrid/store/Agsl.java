/*
 * $Id: Agsl.java,v 1.18 2004/05/19 16:24:33 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.astrogrid.community.User;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;

/**
 * AstroGrid Storepoint Locator.  An astrogrid-specific way of <b>locating</b> a
 * file (or the location to create a file) completely.
 * <p>
 * This contains all the information you
 * need to identify a file in 'VoSpace' that can be reached through one of
 * the StoreClient delegates
 * <p>
 * It is of the form:
 * <pre>
 *    astrogrid:store:{url}
 * </pre>
 *or
 * <pre>
 *    astrogrid:store:myspace:[{serverId}]{delegateendpoint}#{myspacepath}
 * </pre>
 *or (experimental)
 * <pre>
 *    astrogrid:store:mailto:{email address}
 * </pre>
 * <p>
 * The path can be null, ie referring just to a store <i>service</i> rather than
 * a file/folder on that service.
 */

public class Agsl
{
   /** Note that this is not a url to the file - as an ftp reference is split by fragment ftp://server/path#fileonserver
    */
   private URL url = null;
   private Msrl msrl = null;
   
   public static final String SCHEME = "astrogrid:store";
   
   public static final String FORM = SCHEME+":[<Msrl>|<URL>][#path]";
   
   /** Create an AGSL for a URL-based store point */
   public Agsl(URL aUrl)
   {
      this.url = aUrl;
   }

   /** Create an AGSL for a myspace-based store point (might include file path) */
   public Agsl(Msrl aMyspaceResourceLocation)
   {
      this.msrl = aMyspaceResourceLocation;
   }

   /** Makes a reference from the given endpoint (eg myspace:http://asdfasdf or ftp://)
    * and filepath
    * @deprecated - use typed ones
    *
   public Agsl(String endpoint, String path) throws MalformedURLException
   {
      if (endpoint == null) throw new IllegalArgumentException("endpoint must not be null");
      
      if (path == null) {
         init(endpoint);
      }
      else {
         init(endpoint.trim()+"#"+path.trim());
      }
   }
   
   /** Makes a reference from the given endpoint (eg myspace:http://asdfasdf or ftp://)
    * and filepath
    */
   public Agsl(Msrl myspaceEndpoint, String path) throws MalformedURLException
   {
      init(myspaceEndpoint, path);
   }

   /** Makes a reference from the given endpoint (eg myspace:http://asdfasdf or ftp://)
    * and filepath
    */
   public Agsl(URL storepoint, String path) throws MalformedURLException
   {
      init(storepoint, path);
   }

   /** Makes a reference from the given storepoint location (eg astrogrid:store:myspace:http://asdfasdf or astrogrid:store:ftp://)
    * and filepath
    */
   public Agsl(Agsl storepoint, String path) throws MalformedURLException
   {
      init(storepoint, path);
   }

   /** Make a reference from the given string representation. Takes agsl forms,
    * but also URLs and MSRLs. Also, for the moment, takes the deprecated VospaceRL
    * from It4.1
    * @deprecated - use typed ones
    */
   public Agsl(String rl) throws MalformedURLException
   {
      rl = rl.trim();
      
      //allow for mailto
      if (rl.toLowerCase().startsWith("mailto:")) {
         rl = "astrogrid:store:"+rl;
      }
      
      if (rl.toLowerCase().startsWith(Vorl.SCHEME+":"))
      {
         //work out agsl from vorl
         rl = new Vorl(rl).toAgsl().toString();
      }

      //check it's valid
      if (!rl.toLowerCase().startsWith(SCHEME+":")) {
         throw new MalformedURLException("AGSL '"+rl+"' is not of the right form: "+FORM);
      }

      rl = rl.substring(SCHEME.length()+1);
      
      if (Msrl.isMsrl(rl)) {
         this.msrl = new Msrl(rl);
      }
      else {
         this.url = new URL(rl);
         
         //for some reason if there is no slash in the authority, it picks up the first bit of the reference - up to the first #
         if ((url.getAuthority() != null) && (url.getAuthority().indexOf("#")>-1)) {
            url = new URL(url.getProtocol(),
                          url.getHost().substring(0, url.getHost().indexOf('#')),
                          url.getPort(),
                          "#"+url.getRef());
         }
      }
   }
   
   /**
    * Initialises an AGSL out of the given myspace & path
    */
   private void init(Msrl myspacestore, String path)   {
      if (myspacestore == null) throw new IllegalArgumentException("endpoint must not be null");
      
      if (myspacestore.getPath() != null) throw new IllegalArgumentException("endpoint '"+myspacestore+"' includes a file path");

      if (path == null) {
         this.msrl = myspacestore;
      }
      else {
         this.msrl = new Msrl(myspacestore.getDelegateEndpoint(), path);
      }
   }

   /**
    * Initialises an AGSL from the given URL & path
    */
   private void init(URL storepoint, String path) throws MalformedURLException {
      if (storepoint == null) throw new IllegalArgumentException("endpoint must not be null");
      
      if (storepoint.getRef() != null) throw new IllegalArgumentException("endpoint '"+storepoint+"' includes a fragment/reference");

      if (path==null) {
         this.url = storepoint;
      }
      else {
         this.url = new URL(storepoint+"#"+path);
      }
   }

   /**
    * Initialises an AGSL from the given AGSL storepoint & path
    */
   private void init(Agsl storepoint, String path) throws MalformedURLException {
      if (storepoint == null) throw new IllegalArgumentException("endpoint must not be null");
      
      if (storepoint.getPath() != null) throw new IllegalArgumentException("endpoint '"+storepoint+"' includes a path");

      if (Msrl.isMsrl(storepoint.getEndpoint())) {
         init(new Msrl(storepoint.getEndpoint()), path);
      }
      else {
         init(new URL(storepoint.getEndpoint()), path);
      }
   }

   /**
    * This string must be reversable through the above constructor, ie for agsl string s:
    *   new Vorl(s).toString().equals(s);
    * must be true.  However, we can pass in ftp:// and myspace:// etc to the constructor
    * so it won't always work...
    */
   public String toString() {
      if (msrl != null) {
         return SCHEME+":"+msrl.toString();
      }
      else {
         return SCHEME+":"+url.toString();
      }
   }
 
   /** Returns true if the given string is an attempt to be an agsl */
   public static boolean isAgsl(String agsl)
   {
      return agsl.toLowerCase().startsWith(SCHEME+":") ||
         agsl.toLowerCase().startsWith("vospace:"); //it04.1 scheme
   }
   
   /** Returns the myspace reference */
//   public Msrl getMsrl() { return msrl; }

   /** Returns the delegate endpoint */
   public String getEndpoint() {
      if (url != null) {
         return url.getProtocol()+"://"+url.getAuthority()+url.getPath();
      }
      else {
         return Msrl.SCHEME+":"+msrl.getDelegateEndpoint().toString();
      }
   }
   
   /** Returns the path (including filename) to the file on the server */
   public String getPath() {
      if (url != null) {
         return url.getRef();
      }
      else {
         return msrl.getPath();
      }
   }
   
   /** Returns the filename */
   public String getFilename() {
      if (url != null) {
         String path = url.getRef();
         if (path != null) { //might refer to a server, ie no path
            if (path.endsWith("/")) {
               path = path.substring(0,path.length()-1); //chop off last slash
            }
            int slash = path.lastIndexOf("/");
            return path.substring(slash+1);
         }
         else {
            return null;
         }
      }
      else {
         return msrl.getFilename();
      }
   }
   
   /** Opens an inputstream to the file.  Just like url.openStream().... but
    * need to give User to authorise/etc
    */
   public InputStream openInputStream(User user) throws IOException {
      StoreClient client = StoreDelegateFactory.createDelegate(user, this);
      return client.getStream(getPath());
      /**
      if (url != null) {
      not right
         if (url.getProtocol().equals("file")) {
            return new
         }
         else {
            return url.openStream();
         }
      }
      else {
         return msrl.openInputStream();
      }
       */
   }

   /** Opens an outputstream to the file.
    */
   public OutputStream openOutputStream(User user) throws IOException {
      StoreClient client = StoreDelegateFactory.createDelegate(user, this);
      return client.putStream(getPath(), false);
      /*
      if (url != null) {
         if (url.getProtocol().equals("file")) {
            File f = new File(url.getFile());
            return new FileOutputStream(f);
         }
         else {
            throw new UnsupportedOperationException("Cannot (currently) open outputs to URLs");
         }
      }
      else {
         return msrl.openOutputStream();
      }
       */
   }
   
   /**
    * Returns a standard URL to the file.
    */
   public URL resolveURL() throws IOException {
      
      if (url != null) {
         return url;
      }
      else {
         return msrl.resolveURL();
      }
   }

   
   /**
    * Returns the full scheme, eg astrogrid:store:myspace, or astrogrid:store:ftp
    */
   public String getScheme() {
      if (url != null) {
         return SCHEME+":"+url.getProtocol();
      }
      else {
         return SCHEME+":"+Msrl.SCHEME;
      }
   }
   

   /** Constructs an IVORN out of a user and this AGSL; ie assumes that
    * the user has an account on a community server that is resolvable through
    * a registry, and that the path on this agsl is the path to the storepoint
    * resolved to by that community server
    */
   public Ivorn toIvorn(User user) {
      return new Ivorn(user.getCommunity()+"/"+user.getUserId(), getPath());
   }
   
   
   /** Tests that an AGSL is the same as another AGSL */
   public boolean equals(Object anAgsl) {
      return toString().equals(anAgsl.toString());
   }
   
}

/*
$Log: Agsl.java,v $
Revision 1.18  2004/05/19 16:24:33  mch
Properly typed Agsl creation, some fixes to tests

Revision 1.17  2004/04/21 10:35:50  mch
Fixes to ivorn/fragment resolving

Revision 1.16  2004/04/06 15:39:53  mch
Fixes for creating with nul path

Revision 1.15  2004/04/01 15:16:36  mch
Added toIvorn() method

Revision 1.14  2004/04/01 14:49:23  mch
change to javadoc

Revision 1.13  2004/03/25 12:27:19  mch
Tidied doc

Revision 1.12  2004/03/25 12:21:59  mch
Tidied doc

Revision 1.11  2004/03/18 20:00:47  mch
Added trims

Revision 1.10  2004/03/15 18:18:31  mch
Added file output

Revision 1.9  2004/03/14 03:31:01  mch
Added openOutputStream

Revision 1.8  2004/03/14 03:09:16  mch
Fixed nulpointer exception for mailto:

Revision 1.7  2004/03/14 01:24:54  mch
Added experimental email target

Revision 1.6  2004/03/09 23:18:58  mch
Added Vorl for 4.1 access

Revision 1.5  2004/03/02 01:27:00  mch
Minor fixes

Revision 1.4  2004/03/01 23:35:40  mch
Added getFilename

Revision 1.3  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

Revision 1.2  2004/03/01 16:38:58  mch
Merged in from datacenter 4.1 and odd cvs/case problems

Revision 1.1  2004/03/01 16:24:05  mch
Merged in from datacenter 4.1

Revision 1.3  2004/03/01 14:40:54  mch
Added backwards VospaceRL compatibility

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.1  2004/02/17 03:37:27  mch
Various fixes for demo

Revision 1.1  2004/02/16 23:31:21  mch
Temporary Vospace Resource Location representation

Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */


