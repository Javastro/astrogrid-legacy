/*
 * $Id: MSRL.java,v 1.3 2005/01/26 17:31:56 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.slinger.myspace;
import org.astrogrid.slinger.myspace.it05.*;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import javax.xml.rpc.ServiceException;
import org.astrogrid.community.User;
import org.astrogrid.slinger.SRL;
import org.astrogrid.slinger.StoreException;
import org.astrogrid.slinger.sources.SourceIdentifier;
import org.astrogrid.slinger.targets.TargetIdentifier;

/**
 * MySpace Resource Locator.  An Myspace-specific way of identifying a file (or
 the location to create a file) in MySpace
 * <p>
 * It is of the form:
 * <pre>
 *    myspace:{delegateendpoint}[#{myspacepath}[!{serverId}]]
 * </pre>
 * or
 * <pre>
 *    myspace:{ivorn}[#{myspacepath}[!{serverId}]]
 * </pre>
 * If the path fragment is empty, the MSRL refers to a myspace store service
 * <p>
 */

public class MSRL implements SRL, TargetIdentifier, SourceIdentifier
{
   private URL delegateEndpoint = null;
   private String filepath = null;
   
   /** When set it identifies which filestore the manager should look at */
   private String storeId = null;
   
   public static final String SCHEME = "myspace";
   
   //for error messages
   public static final String FORM = "myspace:<delegateEndPoint>[#<MySpacePath>[!<Server>]]";
   
   /** Make a single myspace:// reference string out of a delegate endpoint */
   public MSRL(URL aDelegateEndpoint)
   {
      this.delegateEndpoint = aDelegateEndpoint;
   }

   /** Make a single myspace:// reference string out of a delegate endpoint
    * (eg a myspace manager service) and a path to the file   */
   public MSRL(URL aDelegateEndpoint, String myspacePath)
   {
      this.delegateEndpoint = aDelegateEndpoint;
      this.filepath = myspacePath;
   }
   
   /** Make a single myspace:// reference string out of a delegate endpoint
    * (eg a myspace manager service) and a path to the file.  In this case
    * we also define explicitly which myspace server to use - which should
    * only apply to put-to-myspace calls  */
   public MSRL(URL aDelegateEndpoint, String myspacePath, String aStore)
   {
      this.delegateEndpoint = aDelegateEndpoint;
      this.filepath = myspacePath;
      this.storeId = aStore;
   }

   /** Make a reference from the given string representation
    */
   public MSRL(String msrl) throws MalformedURLException {
      
      assert msrl.toLowerCase().startsWith(SCHEME+":") : msrl+" is not a MySpace RL - should be of the form "+FORM;

      //?? the above doesn't seem to work on twmberlwm; do ordinary check
      if (!msrl.toLowerCase().startsWith(SCHEME+":")) {
         throw new IllegalArgumentException(msrl+" is not a MySpace RL - should be of the form "+FORM);
      }
      
      //look for path + server fragment
      int hashPos = msrl.indexOf('#');
      if (hashPos > -1) {
         String fragment = msrl.substring(hashPos+1);
         msrl = msrl.substring(0,hashPos);
         
         //look for server id
         int atPos = fragment.indexOf("!");
         if (atPos > -1) {
            String givenServer = fragment.substring(atPos+1);
            fragment = fragment.substring(0,atPos);
            
            this.storeId = givenServer;
         }
         
         this.filepath = fragment;
      }

      //look for whether start is an Ivorn or URL
      String rl = msrl.substring(SCHEME.length()+1);
      //assume URL
      this.delegateEndpoint = new URL(rl);
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
      String msrl = SCHEME+":"+delegateEndpoint;
      if (filepath != null) { msrl = msrl +"#"+filepath; }
      if (storeId != null) { msrl = msrl +"!"+storeId; }
      return msrl;
   }
   
   /** Returns just the delegate end point */
   public URL getDelegateEndpoint()   {   return delegateEndpoint;   }
   
   /** Returns the myspace filepath */
   public String getPath()             {  return filepath; }
   
   /** Returns the server ID */
   public String getServer()           { return storeId; }
      
   /** Returns the file part of the reference - ie the text after the last slash,
    * which may be an empty string if this is a directory
    */
   public String getFilename()
   {
      String path = getPath();
      if (path != null) { //might refer to a server, ie no path
         int slash = path.lastIndexOf("/");
         return path.substring(slash+1);
      }
      else {
         return null;
      }
   }
   
   /** Returns the name part of the reference - ie the last token broken by
    * slashes.  If the last character is a slash, this indicates a directory
    * and the previous token is returned
    */
   public String getName()
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

   /**
    * Returns the manager location as an MSRL
    */
   public MSRL getManagerMsrl()
   {
      return new MSRL(delegateEndpoint);
   }
   
   /** Returns true if the given string is an attempt to be a vospace reference */
   public static boolean isMsrl(String msrl)
   {
      return msrl.toLowerCase().startsWith(SCHEME+":");
   }

   public OutputStream resolveOutputStream(Principal user) throws IOException {
       //can't use MySpaceFile for this as this may be a new file
//        return new MySpaceFile(msrl, user).resolveOutputStream(user);
      try {
         MySpaceIt05Delegate temp = new MySpaceIt05Delegate(User.ANONYMOUS, getDelegateEndpoint().toString());
         return temp.putStream(getPath(), false);
      }
      catch (StoreException se) {
         StoreException newse = new StoreException(se+" connecting to "+this+", user "+user);
         newse.setStackTrace(se.getStackTrace());
         throw newse;
      }
   }

   
   /** Used to set the mime type of the data about to be sent to the target. */
   public void setMimeType(String aMimeType, Principal user) {
//@todo      this.mimeType = aMimeType;
   }

   public InputStream resolveInputStream(Principal user) throws IOException {
      try {
         MySpaceIt05Delegate temp = new MySpaceIt05Delegate(User.ANONYMOUS, getDelegateEndpoint().toString());
         return temp.getStream(getPath());
      }
      catch (StoreException se) {
         IOException ioe = new IOException(se+" connecting to "+this+", user "+user);
         ioe.setStackTrace(se.getStackTrace());
         throw ioe;
      }
   }
   
   public Reader resolveReader(Principal user) throws IOException {
      return new InputStreamReader(resolveInputStream(user));
   }
   
   public Writer resolveWriter(Principal user) throws IOException {
      return new OutputStreamWriter(resolveOutputStream(user));
   }
   
   /** Used to set the mime type of the data about to be sent to the source. Does nothing. */
   public String getMimeType(Principal user) throws IOException {
      try {
         MySpaceIt05Delegate temp = new MySpaceIt05Delegate(User.ANONYMOUS, getDelegateEndpoint().toString());
         return temp.getFile(getPath()).getMimeType();
      }
      catch (StoreException se) {
         IOException ioe = new IOException(se+" connecting to "+this+", user "+user);
         ioe.setStackTrace(se.getStackTrace());
         throw ioe;
      }
   }
   

}

/*
$Log: MSRL.java,v $
Revision 1.3  2005/01/26 17:31:56  mch
Split slinger out to scapi, swib, etc.

Revision 1.1.2.4  2005/01/26 14:35:17  mch
Separating slinger and scapi

Revision 1.1.2.3  2004/12/08 18:37:11  mch
Introduced SPI and SPL

Revision 1.1.2.2  2004/12/06 03:17:38  mch
added info to error report

Revision 1.1.2.1  2004/12/03 11:50:19  mch
renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

Revision 1.1.2.1  2004/11/22 00:46:28  mch
New Slinger Package

Revision 1.4  2004/07/07 14:02:14  mch
Fix to parsing out URL

Revision 1.3  2004/07/06 20:19:06  mch
Added Itn06 file identifiers

Revision 1.2  2004/06/14 23:08:53  jdt
Merge from branches

ClientServerSplit_JDT

and

MySpaceClientServerSplit_JDT



MySpace now split into a client/delegate jar

astrogrid-myspace-<version>.jar

and a server/manager war

astrogrid-myspace-server-<version>.war

Revision 1.1.2.1  2004/06/14 22:33:21  jdt
Split into delegate jar and server war.
Delegate: astrogrid-myspace-SNAPSHOT.jar
Server/Manager: astrogrid-myspace-server-SNAPSHOT.war

Package names unchanged.
If you regenerate the axis java/wsdd/wsdl files etc you'll need
to move some files around to ensure they end up in the client
or the server as appropriate.
As of this check-in the tests/errors/failures is 162/1/22 which
matches that before the split.

Revision 1.9  2004/05/12 09:00:16  mch
Added extra check for illegal string on creation

Revision 1.8  2004/03/25 12:27:19  mch
Tidied doc

Revision 1.7  2004/03/25 12:21:59  mch
Tidied doc

Revision 1.6  2004/03/22 10:25:42  mch
Added VoSpaceClient, StoreDelegate, some minor changes to StoreClient interface

Revision 1.5  2004/03/14 03:31:54  mch
Added openOutputStream

Revision 1.4  2004/03/10 00:20:22  mch
Changed @ to ! in MSRL to be compatible with existing individaul@community myspace servers

Revision 1.3  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

Revision 1.2  2004/03/01 16:38:58  mch
Merged in from datacenter 4.1 and odd cvs/case problems

Revision 1.1  2004/03/01 15:15:33  mch
Updates to Store delegates after myspace meeting

 */

