/*
 * $Id: IvoRN.java,v 1.1 2004/02/16 23:31:47 mch Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 */

package org.astrogrid.vospace;


/**
 * IVO Resource Name.  A URI used to name specific IVO resources.
 * I think there's some plugin mechanism to register this wuth URIs but not sure what it is...
 *
 * IVO Resource Names are of the form:
 *
 * ivo://community[/individual[/path]]
 *
 * @author M Hill
 */

import java.net.URISyntaxException;
import java.util.StringTokenizer;

public class IvoRN
{
   private String scheme;
   private String community;
   private String individual;
   private String path;
   
   /** Hmmm should really through a MalformedUR*I*Exception */
   public IvoRN(String ivori) throws URISyntaxException
   {
      int colon = ivori.indexOf(":");
      if (colon == -1) {
         throw new URISyntaxException(ivori, "No protocol given - should be of form ivo://community[/individual[/path]]");
      }
      scheme = ivori.substring(0,colon).toLowerCase();
      if (!scheme.equals("ivo")) {
         throw new URISyntaxException(ivori, "Unknown scheme '"+scheme+"'");
      }

      if (!ivori.substring(colon,colon+3).equals("://")) {
         throw new URISyntaxException(ivori, "Badly formed - should be of form ivo://community[/individual[/path]]");
      }
      
      StringTokenizer tokenizer = new StringTokenizer(ivori.substring(colon+3), "/");

      if (!tokenizer.hasMoreTokens()) {
         throw new URISyntaxException(ivori, "No community given - should be of form ivo://community[/individual[/path]]");
      }
      
      community = tokenizer.nextToken();
      
      if ((community == null) || (community.trim().length() == 0)) {
         throw new URISyntaxException(ivori, "No community given - should be of form ivo://community[/individual[/path]]");
      }

      if (tokenizer.hasMoreTokens()) {
         
         individual = tokenizer.nextToken();
         
         //read path
         path = "";
         while (tokenizer.hasMoreTokens()) {
            path = path+"/"+tokenizer.nextToken();
         }
      }
   }

   public IvoRN(String aCommunity, String anIndividual, String aPath)
   {
      this.scheme = "ivo";
      this.community = aCommunity;
      this.individual = anIndividual;
      this.path = aPath;
   }
   
   /** Returns identifier scheme */
   public String getScheme() {      return scheme;  }

   /** Returns community */
   public String getCommunity() {   return community; }

   /** Returns individual within community */
   public String getIndividual() {  return individual;   }

   /** Returns filepath */
   public String getPath() {  return path;   }

   /** String representation */
   public String toString() {
      return scheme+"://"+community+"/"+individual+path;
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
$Log: IvoRN.java,v $
Revision 1.1  2004/02/16 23:31:47  mch
IVO Resource Name representation

 */

