/*
 * $Id: StoreFileResolver.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import javax.xml.rpc.ServiceException;
import org.astrogrid.slinger.file.LocalFile;
import org.astrogrid.slinger.ftp.FtpFile;
import org.astrogrid.slinger.myspace.MSRL;
import org.astrogrid.slinger.myspace.MySpaceFile;
import org.astrogrid.slinger.srb.JargonFileAdaptor;
import org.astrogrid.slinger.vospace.HomespaceName;
import org.astrogrid.slinger.vospace.IVOSRN;
import org.astrogrid.slinger.vospace.VoSpaceResolver;

/**
 * Resolves the appropriate StoreFile implementation from a *locator*, eg
 * URL, MSRL
 */

public class StoreFileResolver {
   

   public static StoreFile resolveStoreFile(URL url, Principal user) throws IOException {
      if (url.getProtocol().equals("file")) {
         return new LocalFile(url);
      }
      else if (url.getProtocol().equals("ftp")) {
         return new FtpFile(url, user);
      }
      else {
         throw new UnsupportedOperationException("No StoreFile implemented for "+url.getProtocol());
      }
   }

   public static StoreFile resolveStoreFile(MSRL myspace, Principal user) throws IOException {
      try {
         return new MySpaceFile(myspace, user);
      }
      catch (ServiceException se) {
         IOException ioe = new IOException(se+" connecting to "+myspace);
         ioe.setStackTrace(se.getStackTrace());
         throw ioe;
      }
   }

   public static StoreFile resolveStoreFile(HomespaceName homespace, Principal user) throws IOException {
      return resolveStoreFile(VoSpaceResolver.resolveIvosrn(homespace), user);
   }

   /** Assumes ivosrn resolves to a myspace - bad... */
   public static StoreFile resolveStoreFile(IVOSRN ivosrn, Principal user) throws IOException {
      MSRL myspace = VoSpaceResolver.resolveMsrl(ivosrn);
      try {
         return new MySpaceFile(myspace, user);
      }
      catch (ServiceException se) {
         IOException ioe = new IOException(se+" connecting to "+myspace);
         ioe.setStackTrace(se.getStackTrace());
         throw ioe;
      }
   }

   public static StoreFile resolveStoreFile(String uri, Principal user) throws IOException, URISyntaxException {
      try {
         //see if it's a URL
         return resolveStoreFile(new URL(uri), user);
      }
      catch (MalformedURLException mue) {
         //ignore, and go on to try other forms
      }
      if (IVOSRN.isIvorn(uri)) {
         return resolveStoreFile(new IVOSRN(uri), user);
      }
      else if (MSRL.isMsrl(uri)) {
         return resolveStoreFile(new MSRL(uri), user);
      }
      else if (HomespaceName.isHomespaceName(uri)) {
         return resolveStoreFile(new HomespaceName(uri), user);
      }
      else if (JargonFileAdaptor.isSRB(uri)) {
         return new JargonFileAdaptor(new URI(uri));
      }
      else {
         throw new IllegalArgumentException("Can't interpret URI "+uri);
      }
   }
   
}

/*
$Log: StoreFileResolver.java,v $
Revision 1.2  2004/12/07 01:33:36  jdt
Merge from PAL_Itn07

Revision 1.1.2.6  2004/12/07 00:02:37  mch
added resolve from IVOSRN

Revision 1.1.2.5  2004/12/06 02:39:58  mch
a few bug fixes

Revision 1.1.2.4  2004/12/05 19:09:26  mch
Added store cookie reader and SRB store

Revision 1.1.2.3  2004/12/03 18:11:09  mch
Adding SRB support

Revision 1.1.2.2  2004/12/03 11:50:19  mch
renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

Revision 1.1.2.1  2004/11/22 00:46:28  mch
New Slinger Package


 */




