/*
 * $Id: Vorl.java,v 1.1 2004/03/09 23:18:58 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.StringTokenizer;

/**
 * It4.1 Vospace Resource Locator.  This is deprecated, but exists to handle
 * 4.1 clients using newer components.
 * <p>
 * It is of the form:
 *    vospace:<delegateendpoint>[#<path>]
 *
 * where the path is given as what was going to be an ivo, ie:
 *
 *    /community/user/path/path/filename.ext
 * @deprecated
 */

public class Vorl
{
   private URL delegateEndpoint = null;
   private String filepath = null;
   
   public static final String SCHEME = "vospace";
   
   //for error messages
   public static final String FORM = "vospace:<delegateEndPoint>[#/community/individual/path/path/filename.ext]";
   
   /** Make a reference from the given string representation
    */
   public Vorl(String vorl) throws MalformedURLException {
      
      assert vorl.toLowerCase().startsWith(SCHEME+":") : vorl+" is not a VoSpace RL - should be of the form "+FORM;
      
         try {
            URI asUri = new URI(vorl);
         
            //break down authority into delegate protocol and authority, and restore delegate protocol
            String auth = asUri.getAuthority();
            int dot = auth.indexOf(".");
            delegateEndpoint = new URL(auth.substring(0,dot)+"://"+auth.substring(dot+1)+asUri.getPath());

            filepath = asUri.getFragment();
         }
         catch (URISyntaxException use) {
            throw new MalformedURLException(use+": "+vorl);
         }
   }

   /**
    * Returns string that should be same form as constructor parameter
    */
   public String toString() {
      String s = SCHEME+"://"+delegateEndpoint.getProtocol()+"."+delegateEndpoint.getAuthority()+delegateEndpoint.getPath()+
            "#"+filepath;
      return s;
   }
   
   /** Returns the AGSL version of this reference */
   public Agsl toAgsl() throws MalformedURLException
   {
      //extract community & individual for swapping
      StringTokenizer tokenizer = new StringTokenizer(filepath, "/");
      String community = tokenizer.nextToken();
      String individual = tokenizer.nextToken();
      String path = tokenizer.nextToken("####"); //shouldn't be one of these so should get rest
      
      return new Agsl("myspace:"+delegateEndpoint.toString(), individual+"@"+community+path);
      
   }
   
}

/*
$Log: Vorl.java,v $
Revision 1.1  2004/03/09 23:18:58  mch
Added Vorl for 4.1 access

Revision 1.3  2004/03/01 22:38:46  mch
Part II of copy from It4.1 datacenter + updates from myspace meetings + test fixes

Revision 1.2  2004/03/01 16:38:58  mch
Merged in from datacenter 4.1 and odd cvs/case problems

Revision 1.1  2004/03/01 15:15:33  mch
Updates to Store delegates after myspace meeting

 */

