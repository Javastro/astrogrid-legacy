/*
   CdsUcdDictionary.java

   Date      Author      Changes
   $date$    M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.ucd;

import java.net.URL;

/**
 * CdsUcdDictionary.
 *
 * An implementation of a Ucd dictionary based on the web page provided by
 * CDS (Centre de Données Astronomiques de Strasbourg).  It will retrieve
 * that web page and parse it to check the UCD exists on it (and to get its
 * description if necessary).
 * <p>
 * A singleton - use getInstance().  This will allow us to cache the page, or
 * indeed cache all UCDs on that page when first instantiated.
 * <p>
 * Not yet implemented - currently returns 'true' for all strings.
 * <p>
 * @version %I%
 * @author M Hill
 */
   
public class CdsUcdDictionary implements UcdDictionary
{
   private static final String cdsPage = "http://cdsweb.u-strasbg.fr/viz-bin/UCDs";

   private static final UcdDictionary INSTANCE = new CdsUcdDictionary();

   /** Private constructor so that only getInstance() can be used to access
    * an instance, ensuring only one instance can be made (@see Singleton)
    */
   private CdsUcdDictionary()
   {
      super();
   }
      
   
   /** @see UcdDictionary.isUcdValid */
   public boolean isUcdValid(String ucd)
   {
      // Not yet implemented
      return true;
   }
   
   /** @see UcdDictionary.assertUcdValid */
   public void assertUcdValid(String ucd) throws IllegalUcdException
   {
      if (!isUcdValid(ucd))
         throw new IllegalUcdException(ucd, "UCD not found at CDS website '"+cdsPage+"'");
   }

   /** @see UcdDictionary.getUcdDescription */
   public String getUcdDescription(String ucd) throws IllegalUcdException
   {
      //not yet implemented
      return "<Unknown desc>";
   }

   /** Returns the singleton instance of this class. */
   public static UcdDictionary getInstance()
   {
      return INSTANCE;
   }
}

