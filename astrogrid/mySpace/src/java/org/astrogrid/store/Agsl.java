/*
 * $Id: Agsl.java,v 1.1 2004/03/01 16:24:05 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.astrogrid.community.Account;
import org.astrogrid.log.Log;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;

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
 *
 * The path can be null, ie referring just to a store service
 */

public class AGSL
{
   private URL url = null;
   private Msrl msrl = null;
   
   public static final String SCHEME = "astrogrid:store";
   
   public static final String FORM = SCHEME+":[<Msrl>|<URL>]";
   
   public AGSL(URL aUrl)
   {
      this.url = aUrl;
   }

   public AGSL(Msrl aMyspaceResourceLocation)
   {
      this.msrl = aMyspaceResourceLocation;
   }
   
   /** Make a reference from the given string representation. Takes agsl forms,
    * but also URLs and MSRLs. Also, for the moment, takes the deprecated VospaceRL
    * from It4.1
    */
   public AGSL(String rl) throws MalformedURLException
   {
      if (rl.toLowerCase().startsWith("vospace:"))
      {
         try {
            URI asUri = new URI(rl);
         
            //break down authority into delegate protocol and authority
            String auth = asUri.getAuthority();
            int dot = auth.indexOf(".");
            String delegateEndpoint = auth.substring(0,dot)+"://"+auth.substring(dot+1)+asUri.getPath();
            msrl = new Msrl(new URL(delegateEndpoint), asUri.getFragment());
         }
         catch (URISyntaxException use) {
            throw new MalformedURLException(use+": "+rl);
         }
      }
      else
      {
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
      return agsl.toLowerCase().startsWith(SCHEME+":");
   }
   
   /** Returns the myspace reference */
   public Msrl getMsrl() { return msrl; }
   
   /** Returns the path */
   public String getPath() {
      if (url != null) {
         return url.getPath();
      }
      else {
         return msrl.getPath();
      }
   }
   
   /** Opens an inputstream to the file.  Just like url.openStream()....
    */
   public InputStream openStream() throws IOException {
      if (url != null) {
         return url.openStream();
      }
      else {
         return msrl.openStream();
      }
   }
   
   /**
    * Returns a standard URL to the file
    */
   public URL resolveURL() throws IOException {
      
      if (url != null) {
         return url;
      }
      else {
         return msrl.resolveURL();
      }
   }


}

/*
$Log: Agsl.java,v $
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

