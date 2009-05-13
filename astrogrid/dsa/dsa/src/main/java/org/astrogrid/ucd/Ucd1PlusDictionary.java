/*
 * $Id: Ucd1PlusDictionary.java,v 1.1 2009/05/13 13:20:54 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.ucd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Hashtable;

/**
 * Used to access/use UCDs version 1+.  The 'master' is at vizier, though there
 * is a local copy of it version controlled with this class too.
 * <p>
 * @author M Hill
 */

public class Ucd1PlusDictionary
{
   public static final String REF = "http://vizier.u-strasbg.fr/doc/UCD/inVizieR.htx";
   public static final String LIST = "http://vizier.u-strasbg.fr/UCD/ucd1p-words.txt";

   private static Hashtable ucds = null;
   
   /** Initialise dictionary - load list of keywords and descriptions from vizier.
    * Synchronized to prevent two threads calling at once after checking for ucds null
    * @todo
    */
   public static synchronized void loadDictionary() throws IOException {
      
      ucds = new Hashtable();
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(LIST).openStream()));
      
      String line = reader.readLine();
      while (line != null) {
         //parse line
         String ucd = line.substring(2, 38).trim();
         String description = line.substring(40).trim();
      
         ucds.put(ucd, description);
         
         line = reader.readLine();
      }
         
   }
   
   /** Returns true if the given string is a UCD in the dictionary, false if not.
    * */
   public static boolean isUcd(String candidate) throws IOException {
      
      if (ucds == null) {
         loadDictionary();
      }
      
      return ucds.get(candidate) != null;
   }

   
   /** Returns description of given UCD - null if it does not exist
    * */
   public static String getDescription(String ucd) throws IOException {
      if (ucds == null) {
         loadDictionary();
      }
      
      return ucds.get(ucd).toString();
   }
   
   /** Test/command line use */
   public static void main(String[] args) throws IOException {
      
      if ((args == null) || (args.length == 0)) {
         args = new String[] { "em.radio", "pos.eq.ra" };
      }
      
      for (int i = 0; i < args.length; i++) {
         System.out.println(args[i]+": "+getDescription(args[i]));
      }
   }
   
}


