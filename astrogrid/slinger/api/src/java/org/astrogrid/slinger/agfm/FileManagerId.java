/*
 * $Id: FileManagerId.java,v 1.5 2005/03/29 20:05:04 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */


package org.astrogrid.slinger.agfm;

import java.io.*;

import java.net.URISyntaxException;
import java.security.Principal;
import org.astrogrid.community.common.exception.CommunityException;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.slinger.SRL;
import org.astrogrid.slinger.StoreException;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.vospace.HomespaceName;
import org.astrogrid.slinger.vospace.IVOSRN;

/**
 * (AstroGrid) FileManager Id for identifying a file in the FileManager virtual
 * space.  Note that the ID is of the IVORN form, but is not resolvable directly through
 * the registry, which makes it a pain to
 * resolve unambiguously.  That is why this class 'has' an IVORN but 'is not' an IVORN.
 * <p>
 * It is of the form:
 * <pre>
 *    ivo://{community}/{individual}[#{path}[!{storeId}]]
 * </pre>
 * <p>
 */


public class FileManagerId implements SRL, TargetIdentifier, SourceIdentifier
{
   private IVOSRN id = null;
   
   /** When set it identifies which filestore the manager should look at */
   private String storeId = null;
   
   public static final String SCHEME = "agfm";
   
   //for error messages
   public static final String FORM = SCHEME+":ivo://{community}/{individual}[#<Path>[!<Store>]]";
   
   /** Make a single reference out of an identifier in the 'ivo' form */
   public FileManagerId(IVOSRN ivoFormId)
   {
      this.id = ivoFormId;
   }

  /** Make a single reference out of an identifier in the explicit 'homespace' form */
   public FileManagerId(HomespaceName homespaceName)
   {
      this.id = homespaceName.toIvoForm();
   }

   /** Make a single reference from a string */
   public FileManagerId(String fmid) throws URISyntaxException {
      assert isFileManagerId(fmid) : "Not a FileManagerID; should be of the form "+FORM;

      this.id = new IVOSRN(fmid.substring(SCHEME.length()+1));
   }
   
   /** Returns true if the given string looks like it might be a filemanager id */
   public static boolean isFileManagerId(String candidate) {
      return candidate.toLowerCase().startsWith(SCHEME);
   }
   
   public IVOSRN getId() {
      return id;
   }
   
   /**
    * This string must be reversable through the above constructor, ie for valid msrl string s:
    *   new Msrl(s).toString().equals(s);
    * must be true.
    */
   public String toString() {
      return toURI();
   }

   /** Returns URI instance of locator */
   public String toURI()  {
      return SCHEME+":"+id.toURI();
   }
   
   /** Returns the myspace filepath */
   public String getPath()             {  return id.getPath(); }
   
   /** Returns the name part of the reference - ie the last token broken by
    * slashes.  If the last character is a slash, this indicates a directory
    * and the previous token is returned
    */
   public String getFilename()
   {
      String path = getPath();

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

   public OutputStream resolveOutputStream(Principal user) throws IOException {
      try {
         FileManagerClientFactory factory = new FileManagerClientFactory();
         FileManagerClient client = factory.login();
         FileManagerNode node = null;
         
         //if the file doesn't exist, we need to make it
         if (client.exists(id.toOldIvorn()) == null) {
            node = client.createFile(id.toOldIvorn());
         }
         else {
            node = client.node(id.toOldIvorn());
         }
         return new FMCompleterStream(node, node.writeContent());
      }
      catch (IOException e) {
         throw new StoreException(e+" resolving output stream to "+id+" for "+user,e);
      }
      catch (CommunityException e) {
         throw new StoreException(e+" resolving output stream to "+id+" for "+user,e);
      }
      catch (RegistryException e) {
         throw new StoreException(e+" resolving output stream to "+id+" for "+user,e);
      }
      catch (URISyntaxException e) {
         throw new StoreException(e+" resolving output stream to "+id+" for "+user,e);
      }
      /**/
   }

   
   /** Used to set the mime type of the data about to be sent to the target. */
   public void setMimeType(String aMimeType, Principal user) throws IOException {
      /* not yet supported */
   }

   /** Used to set the mime type of the data about to be sent to the source. Does nothing. */
   public String getMimeType(Principal user) throws IOException {
      return null; /* not yet supported */
   }
   
   public InputStream resolveInputStream(Principal user) throws IOException {
      //return null;
      // /* temporarily commoneted as there appears to be a compile/maven bug in compiling the client.node() bit
      try {
         FileManagerClientFactory factory = new FileManagerClientFactory();
         FileManagerClient client = factory.login();
         return client.node(id.toOldIvorn()).readContent();
      }
      catch (CommunityException e) {
         throw new StoreException(e+" resolving input stream to "+id+" for "+user,e);
      }
      catch (RegistryException e) {
         throw new StoreException(e+" resolving input stream to "+id+" for "+user,e);
      }
      catch (URISyntaxException e) {
         throw new StoreException(e+" resolving input stream to "+id+" for "+user,e);
      }
      /**/
   }
   
   public Reader resolveReader(Principal user) throws IOException {
      return new InputStreamReader(resolveInputStream(user));
   }
   
   public Writer resolveWriter(Principal user) throws IOException {
      return new OutputStreamWriter(resolveOutputStream(user));
   }
   

}

/*

$Log: FileManagerId.java,v $
Revision 1.5  2005/03/29 20:05:04  mch
Added extra reporting info

Revision 1.4  2005/03/28 01:48:09  mch
Added socket source/target, and makeFile instead of outputChild

Revision 1.3  2005/03/22 12:58:18  mch
changed schemes to separate FileManagerId from MSRL

Revision 1.2  2005/03/21 16:10:43  mch
Fixes to compile (including removing refs to FileManager clients)

Revision 1.1  2005/03/15 12:07:28  mch
Added FileManager support


 */



