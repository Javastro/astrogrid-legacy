/*
 * $Id: MySpaceIt04Delegate.java,v 1.11 2004/03/22 10:25:42 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store.delegate.myspace;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.StringTokenizer;
import javax.xml.rpc.ServiceException;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.mySpace.delegate.helper.MySpaceHelper;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerServiceLocator;
import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerSoapBindingStub;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Msrl;
import org.astrogrid.store.delegate.StoreDelegate;
import org.astrogrid.store.delegate.StoreException;
import org.astrogrid.store.delegate.StoreFile;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MySpaceIt04Delegate extends StoreDelegate
{
   private MySpaceClient depIt04Delegate = null;//deprecated It04 delegate
   private Msrl serverMsrl = null; //location of server

   Log log = LogFactory.getLog(MySpaceIt04Delegate.class);
   
   public MySpaceIt04Delegate(User anOperator, String endPoint) throws IOException
   {
      super(anOperator);
      
      if (endPoint.startsWith(Msrl.SCHEME)) {
         endPoint = endPoint.substring(Msrl.SCHEME.length()+1);
      }

      //@todo this is a Bad Assumption, remove during it06 - mch.
      if (endPoint.startsWith("http") && (endPoint.indexOf("/services/MySpaceManager") == -1))
      {
         endPoint = endPoint + "/services/MySpaceManager";
      }
      depIt04Delegate = MySpaceDelegateFactory.createDelegate(endPoint);
      serverMsrl = new Msrl(new URL(endPoint));
   }
   
   /**
    * Returns the endpoint
    */
   public Agsl getEndpoint() {
      return new Agsl(serverMsrl.getDelegateEndpoint());
   }

   /**
    * Returns the binding to the service; do this for some methods that
    * work direct rather than through the delegate
    */
   public MySpaceManagerSoapBindingStub getBinding() throws ServiceException {
      
       return (MySpaceManagerSoapBindingStub)
                     new MySpaceManagerServiceLocator().getMySpaceManager(serverMsrl.getDelegateEndpoint());
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
         
         String entry = depIt04Delegate.listDataHolding(
            getOperator().getUserId(),
            getOperator().getCommunity(),
            getOperator().getToken(),
            path);
         
         Element entryDom = XMLUtils.newDocument(new StringBufferInputStream(entry)).getDocumentElement();
         
         String filePath = entryDom.getElementsByTagName("dataItemName").item(0).getFirstChild().getNodeValue().trim();
         String name = filePath.substring(filePath.lastIndexOf('/')+1);
         
         MySpaceFile file = new MySpaceFile(null,  name);
         
         return file;
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
                  getOperator().getUserId(),
                  getOperator().getCommunity(),
                  getOperator().getToken(),
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
    * Puts the given byte array from the start to end points to the given location
    */
   public void putBytes(byte[] bytes, int offset, int length, String targetPath, boolean append) throws StoreException {

      String action = MySpaceClient.OVERWRITE;
      if (append) action = MySpaceClient.APPEND;

      String status = "";
      
        try {
            // Encodes the bytes as a series of space-separated numbers
            // in order to prevent an XML-snippet or line break from confusing Axis etc
            StringBuffer toSend = new StringBuffer();
            for (int loop = offset; loop<length; loop++) {
               toSend.append(bytes[loop]+" ");
            }
            { //debug
               String s = toSend.toString();
               if (s.length()>100) s = s.substring(0,100)+"...";
               log.debug("putBytes("+s+", "+targetPath+", "+append+")");
            }
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildSave(getOperator().getUserId(), getOperator().getCommunity(), getOperator().getToken(),
                                                 "/"+targetPath, toSend.toString(), "(not used)", action);
            status = getBinding().upLoad(jobDetails);
        }
        catch (ServiceException se) {
            log.error("putBytes("+targetPath+", "+action+")", se);
           throw new StoreException("Could not connect to "+getEndpoint());
        }
        catch (RemoteException se) {
            log.error("putBytes("+targetPath+", "+action+")", se);
           throw new StoreException("Error uploading to "+getEndpoint());
        }
        
      if (status.indexOf(">FAULT<") >-1) {
         String msg = status.substring(status.indexOf("<details>")+9);
         msg = msg.substring(0, msg.indexOf("</"));
         throw new StoreException("Fault ["+msg+"] putting bytes to '"+targetPath+"'");
      }
        
   }
   
   /**
    * Puts the given string into the given location
    * done in superclass
   public void putString(String contents, String targetPath, boolean append) throws StoreException {

      byte[] b = contents.getBytes();
      putBytes(b, 0, b.length, targetPath, append);
   }
   
   /**
    * Copies the contents of the file at the given source url to the given location
    */
   public void putUrl(URL source, String targetPath, boolean append) throws StoreException {

      String action = MySpaceClient.OVERWRITE;
      if (append) action = MySpaceClient.APPEND;
      boolean success = false;
      
      try {
         log.trace("putUrl:saveDataHoldingURL("+getOperator().getUserId()+","+getOperator().getCommunity()+","+getOperator().getToken()+",/"+targetPath+","+source+",not used,"+action);

         success = depIt04Delegate.saveDataHoldingURL(
                                     getOperator().getUserId(),
                                     getOperator().getCommunity(),
                                     getOperator().getToken(),
                                     "/"+targetPath,
                                     source.toString(),
                                     "not used",
                                     action
                                    );
      }
      catch (Exception e) {
         throw new StoreException("Failed to saveDataHoldingURL from source '"+source+"' to '"+targetPath, e);
      }
      if (!success) {
         throw new StoreException("Failed to putString to "+targetPath+"'");
      }
   }

   /**
    * Special OutputStream which writes to a string, then sends it when the stream is closed
    */
   private class MySpaceOutputStream extends OutputStream
   {
      private String targetPath = null;

      private byte[] buffer = new byte[32000];
      private int cursor = 0;  //insert point
      
      public MySpaceOutputStream(String aTargetPath, boolean append) throws IOException
      {
         this.targetPath = aTargetPath;
         if (!append) {
            //overwrite target path
            putString("", targetPath, false);
         }
      }
      
      public void write(int i) throws IOException
      {
         //get low byte - streams don't do words
         buffer[cursor] = (byte) (i & 0xFF);
         
         cursor++;
         if (cursor>=buffer.length) {
            flush();
         }
      }
      
      public void flush() throws IOException {
         //append string to file - cursor = length
         putBytes(buffer, 0, cursor, targetPath, true);
         cursor=0;
      }

      public void close() throws IOException {
         flush();
         super.close();
      }
      
   }
   
   /**
    * Streaming output - returns a stream that can be used to output to the given
    * location
    */
   public OutputStream putStream(String targetPath, boolean append) throws IOException {
      return new MySpaceOutputStream(targetPath, append);
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
         String source = depIt04Delegate.getDataHoldingUrl(getOperator().getUserId(), getOperator().getCommunity(), getOperator().getToken(),
                                                           "/"+sourcePath);

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

      String status = "";
      try {
            MySpaceHelper helper = new MySpaceHelper();
            String jobDetails = helper.buildDelete(getOperator().getUserId(), getOperator().getCommunity(), getOperator().getToken(),
                                                           "/"+deletePath);
            status = getBinding().deleteDataHolder(jobDetails);

      }
      catch (ServiceException e) {
         throw new StoreException("Failed to connect to '"+getEndpoint(), e);
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
   public void copy(String originalPath, Agsl targetPath) throws IOException {

      String status = "";
      try {
         log.debug("copy("+originalPath+", "+targetPath+")");
         MySpaceHelper helper = new MySpaceHelper();
         
         String jobDetails = helper.buildCopy(getOperator().getUserId(), getOperator().getCommunity(), getOperator().getToken(),
                                              "/"+originalPath, "/"+targetPath.getPath());
         
         status = getBinding().copyDataHolder(jobDetails);
         
         log.debug("copy->"+status);
      }
      catch (ServiceException se) {
         throw new StoreException("Failed to copy '"+originalPath+"' to '"+targetPath.getPath()+"'", se);
      }
      
      if (status.indexOf(">FAULT<") >-1) {
         String msg = status.substring(status.indexOf("<details>")+9);
         msg = msg.substring(0, msg.indexOf("</"));
         throw new StoreException("Fault ["+msg+"] copying '"+originalPath+" -> "+targetPath+"'");
      }
   }
   
   /**
    * Creates a new folder
    */
   public void newFolder(String newFolderPath) throws IOException {
      try {
         depIt04Delegate.createContainer(getOperator().getUserId(), getOperator().getCommunity(), getOperator().getToken(),
                                                           newFolderPath);
      }
      catch (Exception e) {
         throw new StoreException("Failed to create new folder '"+newFolderPath+"'", e);
      }
   }

}

/*
$Log: MySpaceIt04Delegate.java,v $
Revision 1.11  2004/03/22 10:25:42  mch
Added VoSpaceClient, StoreDelegate, some minor changes to StoreClient interface

Revision 1.10  2004/03/18 17:07:32  mch
Added debug

Revision 1.9  2004/03/17 23:37:22  mch
Removed debug print of encoded string

Revision 1.8  2004/03/16 22:42:18  mch
fixed upload speed

Revision 1.7  2004/03/16 12:57:11  mch
added more direct use of binding

Revision 1.6  2004/03/16 00:01:26  mch
Streaming is now a series of PutStrings

Revision 1.5  2004/03/16 00:01:09  mch
Streaming is now a series of PutStrings

Revision 1.4  2004/03/10 00:28:28  mch
Added root slash

Revision 1.3  2004/03/10 00:20:56  mch
Added comments to debug

Revision 1.2  2004/03/05 19:24:43  mch
Store delegates were moved

Revision 1.1  2004/03/04 12:51:31  mch
Moved delegate implementations into subpackages

Revision 1.2  2004/03/02 01:27:00  mch
Minor fixes

Revision 1.1  2004/03/02 00:15:39  mch
Renamed MyspaceIt04Delegate from misleading ServerDelegate

Revision 1.3  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

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


