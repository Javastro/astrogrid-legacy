/*
 * $Id: Ivorn.java,v 1.1 2004/03/01 16:38:58 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.store;


/**
 * IVO Resource Name.  A URI used to name specific IVO resources.
 * I think there's some plugin mechanism to register this wuth URIs but not sure
 * what it is... and anyway in the meantime we want to pass around things that
 * are definitely IVORNs not any other URI.
 *
 * They act as keys to VO Registries; give the registry an IVORN and it will
 * return some value (that might be another IVORN...)
 *
 * IVORNs are of the form:
 *
 * ivo://something/anything/athing/etc#somethingwithmeaningtothiscontext
 *
 * For example:
 *
 * ivo://roe.ac.uk#mch
 *
 * might resolve to an Community web service endpoint, that takes 'mch' to return
 * an account, which <i>might</i> be of the form:
 *
 * ivo://roe.ac.uk/mch/
 *
 * Or:
 *
 * ivo://roe.ac.uk#mch/myspace
 *
 * might resolve to a Community web service as above, that takes 'mch/myspace' to
 * return that accounts myspace, probably as another ivo identifier:
 *
 * ivo://roe.ac.uk/myspace
 *
 * that resolves to a myspace delegate endpoint through the registry.
 *
 * So from the above:
 *
 * ivo://roe.ac.uk/myspace#roe.ac.uk/mch/famousData/BestResults.vot
 *
 * would resolve to a myspace delegate endpoint and a path to give it - ie
 * it refers to a file in myspace
 *
 * IVORNs are immutable - ie once created, they cannot be changed.  You can
 * make new ones out of old ones.
 *
 *
 * @author MCH, KMB, KTN, DM, ACD
 */

import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

public class Ivorn
{
   public final static String SCHEME = "ivo";
   private String path;
   private String fragment;
   
   /** Construct from given string */
   public Ivorn(String ivorn) throws URISyntaxException
   {
      assert ivorn.startsWith(SCHEME+":") : "Scheme should be "+SCHEME+":";

      URI uri = new URI(ivorn);
      
      path = uri.getAuthority()+uri.getPath();
      fragment = uri.getFragment();
   }

   public Ivorn(String aPath, String aFragment)
   {
      this.path = aPath;
      this.fragment =aFragment;
   }
   
   /** Returns identifier scheme */
   public String getScheme() {      return SCHEME;  }

   /** Returns ivo id */
   public String getPath() {  return path;   }

   /** Returns specific-to-service - ie fragment */
   public String getFragment() {  return fragment;   }

   /** String representation */
   public String toString() {
      return SCHEME+"://"+path+"#"+getFragment();
   }

   /**
    * Basic tests
    */
   public static void main(String[] args) throws URISyntaxException {
      
      String validirn = "ivo://test.astrogrid.org/avodemo/serv1/query/mch-6dF-query.xml";
      
      System.out.println(" In: "+validirn);
      
      IvoRN irn = new IvoRN(validirn);

      System.out.println("Out: "+irn.toString());
      
      assert irn.toString().equals(validirn);

      irn = new IvoRN("test.astrogrid.org", "avodemo", "/serv1/query/mch-6dF-query.xml");
      
      System.out.println("Out: "+irn.toString());
      
      assert irn.toString().equals(validirn);

   }
   
}

/*
$Log: Ivorn.java,v $
Revision 1.1  2004/03/01 16:38:58  mch
Merged in from datacenter 4.1 and odd cvs/case problems

Revision 1.1.2.1  2004/02/23 12:54:42  mch
It04 vospace identifiers

Revision 1.1  2004/02/16 23:31:47  mch
IVO Resource Name representation

 */
