/*
 * $Id: Agfi.java,v 1.1 2004/07/06 20:19:06 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import org.astrogrid.community.User;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;

/**
 * AstroGrid File Identifier.  An astrogrid-specific way of identifying a file
 * wherever it may be (conceptually), eg in myspace, on an ftp server, on a local
 * hard disk, in an account's homespace, etc.
 *
 * It is of the form:
 * <pre>
 *    astrogrid:file:<something>
 * </pre>
 * where <something> is a URL, a Myspace Identifier, an IVORN, etc.
 * <p>
 * there is probably a fancy plugin mechanism for handling all these subschemes,
 * or better still it should probably be typed, but for the moment this will do. - MCH
 */

public class Agfi
{
   private URL url = null;
   private Msrl msrl = null;
   private Ivorn ivorn = null;
   private String account = null;
   private String email = null;
   
   public static final String SCHEME = "astrogrid:file";
   
   public static final String FORM = SCHEME+":[<Msrl>|<URL>|<IVORN>][#path]";
   
   /** Create an AGFI for a URL-located file */
   public Agfi(URL aUrl)
   {
      this.url = aUrl;
   }

   /** Create an AGFI for an Registered file (ie with the given IVORN) */
   public Agfi(Ivorn anIvorn)
   {
      this.ivorn = anIvorn;
   }

   /** Create an AGSL for a myspace-based store point (might include file path) */
   public Agfi(Msrl aMyspaceResourceLocation)
   {
      this.msrl = aMyspaceResourceLocation;
   }

   /** Parse an AGFI from the given string representation. See class javadoc.
    */
   public Agfi(String rl) throws URISyntaxException
   {
      rl = rl.trim();
      
      //check it's a valid scheme
      if (!rl.toLowerCase().startsWith(SCHEME+":")) {
         throw new URISyntaxException(rl, "AGFI is not of the right form: "+FORM);
      }

      //remove AGFI scheme
      rl = rl.substring(SCHEME.length()+1);
      
      //allow for mailto
      if (rl.toLowerCase().startsWith("mailto:")) {
         email = rl.substring("mailto".length()+1);
      }
      else if (Msrl.isMsrl(rl)) {
         try {
            this.msrl = new Msrl(rl);
         }
         catch (MalformedURLException mue) {
            throw new URISyntaxException(rl, mue.toString());
         }
      }
      else if (rl.equals("null")) {
         //fine, leave everything null
      }
      else if (Ivorn.isIvorn(rl)) {
         this.ivorn = new Ivorn(rl);
      }
      else {
         try {
            //assume URL
            this.url = new URL(rl);
            
            //for some reason if there is no slash in the authority, it picks up the first bit of the reference - up to the first #
            if ((url.getAuthority() != null) && (url.getAuthority().indexOf("#")>-1)) {
               url = new URL(url.getProtocol(),
                             url.getHost().substring(0, url.getHost().indexOf('#')),
                             url.getPort(),
                             "#"+url.getRef());
            }
         }
         catch (MalformedURLException mue) {
            throw new URISyntaxException(rl, mue.toString());
         }
      }
   }
   

   /**
    * This string must be reversable through the above constructor, ie for AGFI string s:
    *   new Agfi(s).toString().equals(s);
    * must be true.
    */
   public String toString() {
      if (msrl != null) {
         return SCHEME+":"+msrl.toString();
      }
      else if (url != null) {
         return SCHEME+":"+url.toString();
      }
      else if (ivorn != null) {
         return SCHEME+":"+ivorn.toString();
      }
      else if (email != null) {
         return SCHEME+":mailto:"+email;
      }
      else if (account != null) {
         throw new UnsupportedOperationException("Not done yet");
      }
      else {
         //assume null file
         return SCHEME+":null";
      }
   }
 
   /** Returns true if the given string is an attempt to be an agsl */
   public static boolean isAgfi(String agfi)
   {
      return agfi.toLowerCase().startsWith(SCHEME+":");
   }

   /** Returns true if this is the null file */
   public boolean isNull() {
      return (url==null) && (msrl==null) && (ivorn==null) && (email==null) && (account==null);
   }
   
   /** Returns URL */
   public URL getUrl() { return url; }
   
   /** Returns Myspace reference */
   public Msrl getMsrl() { return msrl; }
   
   /** Returns email address */
   public String getEmail() { return email; }
   
   /** Returns IVORN-based file id */
   public Ivorn getIvorn() { return ivorn; }
   
   /** Returns account-based file id */
   public String getAccount() { return account; }
     
   
   /** Tests that an AGSL is the same as another AGSL */
   public boolean equals(Object anAgsl) {
      return toString().equals(anAgsl.toString());
   }
   
}

/*
$Log: Agfi.java,v $
Revision 1.1  2004/07/06 20:19:06  mch
Added Itn06 file identifiers


 */


