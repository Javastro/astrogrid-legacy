/*
 * $Id: Msrl.java,v 1.4 2004/07/07 14:02:14 mch Exp $
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
import org.astrogrid.log.Log;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;

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

public class Msrl
{
   private String serverId = null;  //may be an IVORN?
   private URL delegateEndpoint = null;
   private Ivorn manager = null;
   private String filepath = null;
   
   public static final String SCHEME = "myspace";
   
   //for error messages
   public static final String FORM = "myspace:<delegateEndPoint>[#<MySpacePath>[!<Server>]]";
   
   /** Make a single myspace:// reference string out of a delegate endpoint */
   public Msrl(URL aDelegateEndpoint)
   {
      this.delegateEndpoint = aDelegateEndpoint;
   }

   /** Make a single myspace:// reference string out of a delegate endpoint
    * (eg a myspace manager service) and a path to the file   */
   public Msrl(URL aDelegateEndpoint, String myspacePath)
   {
      this.delegateEndpoint = aDelegateEndpoint;
      this.filepath = myspacePath;
   }
   
   /** Make a single myspace:// reference string out of a delegate endpoint
    * (eg a myspace manager service) and a path to the file   */
   public Msrl(Ivorn aManagerIvorn, String myspacePath)
   {
      this.manager = aManagerIvorn;
      this.filepath = myspacePath;
   }
   
   /** Make a single myspace:// reference string out of a delegate endpoint
    * (eg a myspace manager service) and a path to the file.  In this case
    * we also define explicitly which myspace server to use - which should
    * only apply to put-to-myspace calls (should this be an IVORN?) */
   public Msrl(URL aDelegateEndpoint, String myspacePath, String aServer)
   {
      this.delegateEndpoint = aDelegateEndpoint;
      this.filepath = myspacePath;
      this.serverId = aServer;
   }

   /** Make a reference from the given string representation
    */
   public Msrl(String msrl) throws MalformedURLException {
      
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
            
            this.serverId = givenServer;
         }
         
         this.filepath = fragment;
      }

      //look for whether start is an Ivorn or URL
      String rl = msrl.substring(SCHEME.length()+1);
      if (Ivorn.isIvorn(rl)) {
         try {
            this.manager = new Ivorn(rl);
         }
         catch (URISyntaxException use) {
            //should really throw this, but don't know what that will do to backwards compatibility
            throw new MalformedURLException(""+use);
         }
      }
      else {
         //assume URL
         this.delegateEndpoint = new URL(rl);
      }
   }

   /**
    * This string must be reversable through the above constructor, ie for valid msrl string s:
    *   new Msrl(s).toString().equals(s);
    * must be true.
    */
   public String toString() {
      String msrl = SCHEME+":";
      if (delegateEndpoint != null) {
         msrl = msrl + delegateEndpoint;
      }
      else {
         msrl = msrl + manager;
      }
      if (filepath != null) { msrl = msrl +"#"+filepath; }
      if (serverId != null) { msrl = msrl +"!"+serverId; }
      return msrl;
   }

   /** Returns just the delegate end point */
   public URL getDelegateEndpoint()   {   return delegateEndpoint;   }
   
   /** Returns just the ivorn to the manager */
   public Ivorn getManagerIvorn()   {   return manager;   }
   
   /** Returns the myspace filepath */
   public String getPath()             {  return filepath; }
   
   /** Returns the server ID */
   public String getServer()           { return serverId; }
      
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
   public Msrl getManagerMsrl()
   {
      return new Msrl(delegateEndpoint);
   }
   
   /** Returns true if the given string is an attempt to be a vospace reference */
   public static boolean isMsrl(String msrl)
   {
      return msrl.toLowerCase().startsWith(SCHEME+":");
   }

   /** Opens an inputstream to the file.  Just like url.openStream()....
    */
   public InputStream openInputStream() throws IOException {
      return resolveURL().openStream();
   }
   
   /** Opens an outputstream to the file.
    */
   public OutputStream openOutputStream() throws IOException {
      StoreClient myspace = StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl(this));
      return myspace.putStream(getPath(), false);
   }
   
   /**
    * Returns a standard URL to the file
    */
   public URL resolveURL() throws IOException {
      StoreClient myspace = StoreDelegateFactory.createDelegate(User.ANONYMOUS, new Agsl(this));
         
      URL url = myspace.getUrl(getPath());

      Log.trace("Loc '"+getDelegateEndpoint()+"' + path '"+getPath()+"' -> URL '"+url+"'");
      
      return url;
   }


}

/*
$Log: Msrl.java,v $
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

