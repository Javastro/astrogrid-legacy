/*
 * $Id: Ucd1Dictionary.java,v 1.1 2009/05/13 13:20:53 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.ucd;



/**
 * Used to access/use UCDs
 * <p>
 * @author M Hill
 */

public class Ucd1Dictionary
{
   public static final String REF = "http://cdsweb.u-strasbg.fr/UCD/old/";
   public static final String LIST = "http://cdsweb.u-strasbg.fr/viz-bin/UCDs";

   /** Should return true only if the given candidate string is a ucd, but parsing
    * the dictionary page is a pain so just returns true anyway */
   public boolean isUCD(String candidate) {
      return true;
   }
   
}


