/*
 * $Id: StoreFileResolver.java,v 1.1 2005/03/31 19:25:39 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.storeclient;
import org.astrogrid.file.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import javax.xml.rpc.ServiceException;
import org.astrogrid.slinger.agfm.FileManagerId;
import org.astrogrid.slinger.myspace.MSRL;
import org.astrogrid.slinger.vospace.HomespaceName;
import org.astrogrid.slinger.vospace.IVOSRN;
import org.astrogrid.storeclient.myspace.FileManagerFile;
import org.astrogrid.file.LocalFile;
import org.astrogrid.storeclient.ftp.FtpStoreFile;
import org.astrogrid.storeclient.myspace.MySpaceFile;
import org.astrogrid.storeclient.srb.JargonFileAdaptor;

/**
 * Resolves the appropriate StoreFile implementation from a *locator*, eg
 * URL, MSRL
 */

public class StoreFileResolver {
   

   public static FileNode resolveStoreFile(URL url, Principal user) throws IOException {
      if (url.getProtocol().equals("file")) {
         return new LocalFile(url);
      }
      else if (url.getProtocol().equals("ftp")) {
         return new FtpStoreFile(url, user);
      }
      else {
         throw new UnsupportedOperationException("No StoreFile implemented for "+url.getProtocol());
      }
   }

   public static FileNode resolveStoreFile(MSRL myspace, Principal user) throws IOException {
      try {
         return new MySpaceFile(myspace);
      }
      catch (ServiceException se) {
         IOException ioe = new IOException(se+" connecting to "+myspace);
         ioe.setStackTrace(se.getStackTrace());
         throw ioe;
      }
   }

   /** assumes homespaces are filemanagers... bad... */
   public static FileNode resolveStoreFile(HomespaceName homespace, Principal user) throws IOException, URISyntaxException {
      return resolveStoreFile(new FileManagerId(homespace.toIvoForm()), user);
   }

   public static FileNode resolveStoreFile(FileManagerId fileManagerId, Principal user) throws IOException, URISyntaxException {
      return new FileManagerFile(fileManagerId.getId(), user);
   }

   /** Assumes ivosrn resolves to a myspace - bad... */
   public static FileNode resolveStoreFile(IVOSRN ivosrn, Principal user) throws IOException {
      MSRL myspace = new MSRL(new URL(ivosrn.resolve()), ivosrn.getFragment());
      return resolveStoreFile(myspace, user);
   }
   
   public static FileNode resolveStoreFile(String uri, Principal user) throws IOException, URISyntaxException {
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
      else if (FileManagerId.isFileManagerId(uri)) {
         return resolveStoreFile(new FileManagerId(uri), user);
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
Revision 1.1  2005/03/31 19:25:39  mch
semi fixed a few threading things, introduced sort order to tree

Revision 1.4  2005/03/28 02:06:35  mch
Major lump: split picker and browser and added threading to seperate UI interations from server interactions

Revision 1.3  2005/03/26 13:09:57  mch
Minor fixes for accessing FileManager

Revision 1.2  2005/03/25 16:19:57  mch
Added FIleManger suport

Revision 1.1.1.1  2005/02/16 19:57:05  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 15:02:46  mch
Initial Checkin

Revision 1.1.2.1  2005/01/26 14:42:58  mch
Separating slinger and scapi

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




