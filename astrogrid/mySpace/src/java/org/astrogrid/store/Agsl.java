/*
 * $Id: Agsl.java,v 1.10 2004/03/15 18:18:31 mch Exp $
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

/**
 * AstroGrid Storepoint Locator.  An astrogrid-specific way of *locating* a
 * file (or the location to create a file) completely.
 * <p>
 * This contains all the information you
 * need to identify a file in 'VoSpace' that can be reached through one of
 * the StoreClient delegates
 *
 * It is of the form:
 *    astrogrid:store:<url>
 *or
 *    astrogrid:store:myspace:[<serverId>@]<delegateendpoint>#myspacepath
 *or (experimental)
 *    astrogrid:store:mailto:<email address>
 *
 * The path can be null, ie referring just to a store service
 */

public class Agsl
{
   /** Note that this is not a url to the file - as an ftp reference is split by fragment ftp://server/path#fileonserver
    */
   private URL url = null;
   private Msrl msrl = null;
   
   public static final String SCHEME = "astrogrid:store";
   
   public static final String FORM = SCHEME+":[<Msrl>|<URL>]";
   
   public Agsl(URL aUrl)
   {
      this.url = aUrl;
   }

   public Agsl(Msrl aMyspaceResourceLocation)
   {
      this.msrl = aMyspaceResourceLocation;
   }

   /** Makes a reference from the given endpoint (eg myspace:http://asdfasdf or ftp://)
    * and filepath
    */
   public Agsl(String endpoint, String path) throws MalformedURLException
   {
      this(SCHEME+":"+endpoint+"#"+path);
   }
   
   /** Make a reference from the given string representation. Takes agsl forms,
    * but also URLs and MSRLs. Also, for the moment, takes the deprecated VospaceRL
    * from It4.1
    */
   public Agsl(String rl) throws MalformedURLException
   {
      if (rl.toLowerCase().startsWith("mailto:")) {
         rl = "astrogrid:store:"+rl;
      }
      
      if (rl.toLowerCase().startsWith(Vorl.SCHEME+":"))
      {
         rl = new Vorl(rl).toAgsl().toString();
      }

      if (rl.toLowerCase().startsWith(SCHEME+":"))
      {
         rl = rl.substring(SCHEME.length()+1);
      }
      
      if (Msrl.isMsrl(rl))
      {
         msrl = new Msrl(rl);
      }
      else
      {
         url = new URL(rl);
         
         if (url.getAuthority() != null) {
            //for some reason picks up # in authority if there's no slashes...
            if (url.getAuthority().indexOf('#')>-1) {
               url = new URL(url.getProtocol(),
                             url.getHost().substring(0,url.getHost().indexOf('#')),
                             url.getPort(),
                             "#"+url.getRef());
            }
         }
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
   
   /** Returns the path to the file on the server */
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
      if (url != null) {
         return url.openStream();
      }
      else {
         return msrl.openInputStream();
      }
   }

   /** Opens an outputstream to the file.
    */
   public OutputStream openOutputStream(User user) throws IOException {
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
   

}

/*
$Log: Agsl.java,v $
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

