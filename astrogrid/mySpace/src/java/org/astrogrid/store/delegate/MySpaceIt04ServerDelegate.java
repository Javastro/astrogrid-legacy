/*
 * $Id: MySpaceIt04ServerDelegate.java,v 1.2 2004/03/01 15:15:04 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate;




/**
 * Delegate to the MySpace VoSpace servers; this one provides an interface
 * to the 'old' Iteration 4 servers.
 *
 * @author M Hill
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.util.StringTokenizer;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.community.Account;
import org.astrogrid.log.Log;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.store.Msrl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MySpaceIt04ServerDelegate implements StoreClient
{
   private MySpaceClient depIt04Delegate = null;//deprecated It04 delegate
   private Msrl serverMsrl = null; //location of server

   //the person/account using this delegate
   private Account operator = null;

   public MySpaceIt04ServerDelegate(Account anOperator, String endPoint) throws IOException
   {
      operator = anOperator;
      
      if (endPoint.startsWith("http") && (endPoint.indexOf("/services/MySpaceManager") == -1))
      {
         endPoint = endPoint + "/services/MySpaceManager";
      }
      depIt04Delegate = MySpaceDelegateFactory.createDelegate(endPoint);
      serverMsrl = new Msrl(new URL(endPoint));
   }
   
   /**
    * Returns the user of this delegate - ie the account it is being used by
    */
   public Account getOperator() { return operator; }
   

   /**
    * Puts the given string into the given location
    */
   public void putString(String contents, String targetPath, boolean append) throws StoreException {
      
      try {
         String action = MySpaceClient.OVERWRITE;
         if (append) action = MySpaceClient.APPEND;
         
         Log.trace("saveDataHolding("+operator.getIndividual()+","+operator.getCommunity()+","+operator.getToken()+","+targetPath+","+contents+",not used,"+action);

         depIt04Delegate.saveDataHolding(operator.getIndividual(),
                                     operator.getCommunity(),
                                     operator.getToken(),
                                     targetPath,
                                     contents,
                                     "not used",
                                     action
                                    );
      }
      catch (Exception e) {
         throw new StoreException("Failed to putString", e);
      }
   }
   
   /**
    * Returns a list of all the files that match the expression
    */
   public StoreFile[] listFiles(String filter) throws StoreException {
      // TODO
         throw new UnsupportedOperationException();
   }
   
   /**
    * Returns the StoreFile representation of the file at the given AGSL
    */
   public StoreFile getFile(String path) throws StoreException {
      try {
         
         String entries = depIt04Delegate.getDataHolding(
                              operator.getIndividual(),
                              operator.getCommunity(),
                              operator.getToken(),
                              path);
         
         throw new UnsupportedOperationException();
      }
      catch (Exception e) {
            throw new StoreException("Failed to get File at '"+path+"'", e);
      }
      
   }
   
    /**
    * Returns a tree representation of the files that match the expression
    */
   
   public StoreFile getFiles(String filter) throws StoreException {

      MySpaceFolder rootFolder = new MySpaceFolder(serverMsrl, "");
      
      try {
         
         String entries = null;
         
         entries = (String) depIt04Delegate.listDataHoldingsGen(
                  operator.getIndividual(),
                  operator.getCommunity(), operator.getToken(),
                  filter).elementAt(0);
            
         Element entryDom = XMLUtils.newDocument(new StringBufferInputStream(entries)).getDocumentElement();
            
            NodeList records = entryDom.getElementsByTagName("dataItemRecord");
            
            //represent entries
            for (int r=0;r<records.getLength();r++) {
               
               Element e = (Element) records.item(r);
               MySpaceFileType type = MySpaceFileType.getForHoldingRef(e.getElementsByTagName("type").item(0).getFirstChild().getNodeValue());
               
               String path = e.getElementsByTagName("dataItemName").item(0).getFirstChild().getNodeValue().trim();
               String name = path.substring(path.lastIndexOf('/')+1);
               
               //locate parent
               StringTokenizer dirTokens = new StringTokenizer(path, "/");
               MySpaceFolder folder = rootFolder;
               while (dirTokens.hasMoreTokens())
               {
                  String dir = dirTokens.nextToken();
                  if (dirTokens.hasMoreTokens()) { //not last one - which is the filename
                     if (folder.getChild(dir) == null) {
                        MySpaceFolder newFolder = new MySpaceFolder(folder, dir);
                        folder.add(newFolder);
                     }
                     folder = (MySpaceFolder) folder.getChild(dir);
                  }
               }
               
               if (type == MySpaceFileType.FOLDER)
               {
                  MySpaceFolder file = new MySpaceFolder(folder, name);
                  folder.add(file);
               }
               else
               {
                  MySpaceFile file = new MySpaceFile(
                     folder,
                     name,
                     e.getElementsByTagName("ownerID").item(0).getFirstChild().getNodeValue(),
                     e.getElementsByTagName("creationDate").item(0).getFirstChild().getNodeValue(),
                     e.getElementsByTagName("expiryDate").item(0).getFirstChild().getNodeValue(),
                     e.getElementsByTagName("size").item(0).getFirstChild().getNodeValue(),
                     e.getElementsByTagName("permissionsMask").item(0).getFirstChild().getNodeValue(),
                     type
                  );
                  folder.add(file);
               }
            }
            return rootFolder;
         }
         catch (Exception e) {
            throw new StoreException("Failed to getEntries tree for filter '"+filter+"'", e);
         }
   }
   
   /**
    * Copies the contents of the file at the given source url to the given location
    */
   public void putUrl(URL source, String targetPath, boolean append) throws StoreException {

      String action = MySpaceClient.OVERWRITE;
      if (append) action = MySpaceClient.APPEND;
         
      try {
         Log.trace("saveDataHoldingURL("+operator.getIndividual()+","+operator.getCommunity()+","+operator.getToken()+","+targetPath+","+source+",not used,"+action);

         depIt04Delegate.saveDataHoldingURL(
                                     operator.getIndividual(),
                                     operator.getCommunity(),
                                     operator.getToken(),
                                     targetPath,
                                     source.toString(),
                                     "not used",
                                     action
                                    );
      }
      catch (Exception e) {
         throw new StoreException("Failed to saveDataHoldingURL from source '"+source+"' to '"+targetPath, e);
      }
   }

   /**
    * Special OutputStream which writes to a string, then sends it when the stream is closed
    * @todo: could send every n bytes and append to the other end
    */
   private class MySpaceOutputStream extends OutputStream
   {
      StringBuffer buffer = new StringBuffer();
      String targetPath = null;
      
      public MySpaceOutputStream(String aTargetPath)
      {
         this.targetPath = aTargetPath;
      }
      
      public void write(int i)
      {
         buffer.append( (char) i);
      }
      
      public void close() throws IOException
      {
         putString(buffer.toString(), targetPath, false);
      }
   }
   
   /**
    * Streaming output - returns a stream that can be used to output to the given
    * location
    */
   public OutputStream putStream(String targetPath) throws StoreException {
      return new MySpaceOutputStream(targetPath);
   }

   /**
    * Gets a file's contents as a stream
    */
   public InputStream getStream(String sourcePath) throws IOException {
      URL url = getUrl(sourcePath);
      
      if (url == null)
         throw new FileNotFoundException("Could not find url for '"+sourcePath+"'");
      
      return url.openStream();
   }

   /**
    * Gets the url to the given file
    */
   public URL getUrl(String sourcePath) throws StoreException {

      try {
         String source = depIt04Delegate.getDataHoldingUrl(operator.getIndividual(), operator.getCommunity(), operator.getToken(),
                                                           sourcePath);

         if (source == null)
         {
            throw new FileNotFoundException("Could not find myspace file '"+sourcePath+"'");
         }
         return new URL(source);
      }
      catch (Exception e) {
         throw new StoreException("Failed to get url to '"+sourcePath+"'", e);
      }
   }

   /**
    * Deletes the given file
    */
   public void delete(String deletePath) throws IOException {

      String status;
      try {
         status = depIt04Delegate.deleteDataHolding(operator.getIndividual(), operator.getCommunity(), operator.getToken(),
                                                           "/"+deletePath);

      }
      catch (Exception e) {
         throw new StoreException("Failed to delete '"+deletePath+"'", e);
      }

      if (status.indexOf(">FAULT<") >-1) {
         String msg = status.substring(status.indexOf("<details>")+9);
         msg = msg.substring(0, msg.indexOf("</"));
         throw new StoreException("Fault ["+msg+"] deleting '"+deletePath+"'");
      }
         
      
   }
   
   /**
    * Copies the given file to the given location
    */
   public void copy(String originalPath, String copyPath) throws IOException {

      try {
         depIt04Delegate.copyDataHolding(operator.getIndividual(), operator.getCommunity(), operator.getToken(),
                                                           originalPath, copyPath);
      }
      catch (Exception e) {
         throw new StoreException("Failed to copy '"+originalPath+"' to '"+copyPath+"'", e);
      }
   }
   
   /**
    * Creates a new folder
    */
   public void newFolder(String newFolderPath) throws IOException {
      try {
         depIt04Delegate.createContainer(operator.getIndividual(), operator.getCommunity(), operator.getToken(),
                                                           newFolderPath);
      }
      catch (Exception e) {
         throw new StoreException("Failed to create new folder '"+newFolderPath+"'", e);
      }
   }
      
   
}

/*
$Log: MySpaceIt04ServerDelegate.java,v $
Revision 1.2  2004/03/01 15:15:04  mch
Updates to Store delegates after myspace meeting

Revision 1.1  2004/02/24 15:59:56  mch
Moved It04.1 Datacenter VoSpaceClient stuff to myspace as StoreClient stuff

Revision 1.3  2004/02/17 03:37:27  mch
Various fixes for demo

Revision 1.2  2004/02/16 23:33:42  mch
Changed to use Account and AttomConfig

Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

