/*
 * $Id: VospaceRL.java,v 1.1 2004/02/17 03:37:27 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.vospace;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.astrogrid.community.Account;
import org.astrogrid.log.Log;
import org.astrogrid.vospace.delegate.VoSpaceClient;
import org.astrogrid.vospace.delegate.VoSpaceDelegateFactory;

/**
 * A VoSpace Resource Locator.  This contains all the information you
 * need to identify a file in VoSpace that can be reached through one of
 * the VoSpaceClient delegates
 *
 * It is of the form:
 *   vospace://protocol.delegate.end.point#individual@community/path/path/file
 *
 * The path can be null, giving you a reference to a vospace service
 */

public class VospaceRL
{
   private URL delegateEndpoint;
   private IvoRN ivorn;
   
   /** Make a single myspace:// reference string out of a delegate endpoint
    * (eg a myspace manager service) and an ivo file reference
    */
   public VospaceRL(URL aDelegateEndpoint, IvoRN anIvorn) throws MalformedURLException
   {
      this.delegateEndpoint = aDelegateEndpoint;
      this.ivorn = anIvorn;
   }
   
   /** Make a reference from the given string representation
    */
   public VospaceRL(String vorl) throws MalformedURLException
   {
      try
      {
         URI asUri = new URI(vorl);
         if (!asUri.getScheme().equals("vospace")) throw new MalformedURLException("Not a Vospace URL");
      
         //break down authority into delegate protocol and authority
         String auth = asUri.getAuthority();
         int dot = auth.indexOf(".");
         this.delegateEndpoint = new URL(auth.substring(0,dot)+"://"+auth.substring(dot+1)+asUri.getPath());
         
         if (asUri.getFragment() != null) {
            this.ivorn = new IvoRN("ivo://"+asUri.getFragment());
         }
      }
      catch (URISyntaxException use)      {
         throw new MalformedURLException("Syntax Error: "+use.getMessage());
      }
      catch (IndexOutOfBoundsException ie)
      {
         throw new MalformedURLException("Syntax Error: "+ie.getMessage());
      }
   }

   /**
    * This string must be reversable through the above constructor, ie for string s:
    *   new Vorl(s).toString().equals(s);
    * must be true.
    */
   public String toString() {
//      return "vospace://"+delegateEndpoint.getAuthority()+delegateEndpoint.getPath()+"#"+ivorn.getIndividual()+"@"+ivorn.getCommunity()+ivorn.getPath();
      String vorl = "vospace://"+delegateEndpoint.getProtocol()+"."+delegateEndpoint.getAuthority()+delegateEndpoint.getPath();
      if (ivorn != null) {
         vorl = vorl +"#"+ivorn.getCommunity()+"/"+ivorn.getIndividual()+ivorn.getPath();
      }
      return vorl;
   }
   /*
      //tidy up ivoRef
      ivoRef = ivoRef.replaceAll("\\\\", "/");
      if (ivoRef.toLowerCase().startsWith("ivo://")) {
         ivoRef = ivoRef.substring(6);
      }

      //tidy up service address - remove http (as it confuses uri) and endpoint
      if (delegateEndpoint.toLowerCase().startsWith("http://")) {
         delegateEndpoint = delegateEndpoint.substring(7);
      }

      return "myspace://"+delegateEndpoint+";"+ivoRef;
   }

   public static void assertValid(String myspaceRef)
   {
      assert myspaceRef != null : "MySpace Reference is null";
      assert myspaceRef.startsWith("myspace://") : "Does not start with 'myspace://'";
   }
    */
   
   public URL getDelegateEndpoint()
   {
      return delegateEndpoint;
   }
   
   /**
    * Returns whatever is required by the delegate to refer to the file.
    * This is currently /individual@community/path/path/filename.ext
    */
   public String getDelegateFileRef()
   {
      return "/"+ivorn.getIndividual()+"@"+ivorn.getCommunity()+ivorn.getPath();
   }
   
   /** Returns the IVO reference to the file */
   public IvoRN getIvoRef()
   {
      return ivorn;
   }
   
   /** Returns the path part of the reference - ie the list of directories
    * following the individual@community
    */
   public String getPath()
   {
      return ivorn.getPath();
   }
      
   /** Returns the file part of the reference - ie the text after the last slash
    */
   public String getFilename()
   {
      String path = getPath();
      int slash = path.lastIndexOf("/");
      return path.substring(slash+1);
   }
   
   /** Returns true if the given string is an attempt to be a vospace reference */
   public static boolean isVoRL(String vorl)
   {
      return vorl.toLowerCase().startsWith("vospace://");
   }
   
   /** Opens an inputstream to the file.  Just like url.openStream()....
    */
   public InputStream openStream() throws IOException {
      return resolveURL().openStream();
   }
   
   /**
    * Returns a standard URL to the file
    */
   public URL resolveURL() throws IOException {
      VoSpaceClient vospace = VoSpaceDelegateFactory.createDelegate(Account.ANONYMOUS, getDelegateEndpoint().toString());
         
      URL url = vospace.getUrl(getDelegateFileRef());

      Log.trace("Loc '"+getDelegateEndpoint()+"' + path '"+getDelegateFileRef()+"' -> URL '"+url+"'");
      
      return url;
   }

   /**
    * Test harness
    */
   public static void main(String[] args) throws MalformedURLException {
      
      String validVorl = "vospace://http.grendel12.roe.ac.uk:8080/astrogrid-mySpace#test.astrogrid.org/avodemo/serv1/query/mch-6dF-query.xml";
      
      System.out.println(" In: "+validVorl);
      
      VospaceRL vorl = new VospaceRL(validVorl);

      System.out.println("Out: "+vorl.toString());
      
      assert vorl.toString().equals(validVorl);

      vorl = new VospaceRL(new URL("http://grendel12.roe.ac.uk:8080/astrogrid-mySpace"), new IvoRN("test.astrogrid.org", "avodemo", "/serv1/query/mch-6dF-query.xml"));
      
      System.out.println("Out: "+vorl.toString());
      
      assert vorl.toString().equals(validVorl);

   }

}

/*
$Log: VospaceRL.java,v $
Revision 1.1  2004/02/17 03:37:27  mch
Various fixes for demo

Revision 1.1  2004/02/16 23:31:21  mch
Temporary Vospace Resource Location representation

Revision 1.1  2004/02/15 23:16:06  mch
New-style VoSpace delegates.  Not agreed so private to datacenter for the moment

 */

