/*
   LocalTermDictionary.java

   Date        Author      Changes
   1 Oct 2002  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.wrapper;

import java.util.Hashtable;
import org.astrogrid.common.ucd.*;
import org.astrogrid.common.units.*;

import org.astrogrid.log.Log;

/**
 * LocalTermDictionary is used to describe the local terms used in a wrapper
 * service that correspond to configuration file/etc parameters used by the
 * wrapped application.
 * <p>
 * The dictionary includes ways of validating local terms, converting to/from
 * UCDs, and ensuring units are correct
 * <p>
 * For example, the SExtractor native
 * configuration file uses key words that are not UCDs.  An instance of this
 * class could be constructed to provide that translation, and to ensure that
 * only the relevent words are provided, and that all required words are
 * provided.
 * <p>
 *
 * @version %I%
 * @author M Hill
 */

public class LocalTermDictionary
{
   private Hashtable ucdKeyedDictionary = new Hashtable();
   private Hashtable localKeyedDictionary = new Hashtable();
   private Hashtable localTermUnits = new Hashtable(); //units of each local term
   private Hashtable localTermRequired = new Hashtable(); //booleans marking if a local term is required

   public LocalTermDictionary()
   {
   }

   public void addTerm(String ucd, String localTerm, String units, boolean isRequired)
   {
      Log.affirm(localTerm != null, "Local term must not be null");

      localTermRequired.put(localTerm, new Boolean(isRequired));

      if (ucd != null)
      {
         ucdKeyedDictionary.put(ucd, localTerm);
         localKeyedDictionary.put(localTerm, ucd);
      }
      if (units != null)
      {
         localTermUnits.put(localTerm, units);
      }
   }

   /**
    * Returns the string used by the local wrapped application to designate
    * the same value as the given ucd
    */
   public String getLocalTermFor(String ucd)
   {
      String localTerm = (String) ucdKeyedDictionary.get(ucd);
      if (localTerm == null)
      {
         throw new IllegalArgumentException("UCD '"+ucd+"' not found in dictionary");
      }
      return localTerm;
   }

   public String getUcdFor(String localTerm)
   {
      return (String) localKeyedDictionary.get(localTerm);
   }

   public String getUnitsFor(String localTerm)
   {
      return (String) localTermUnits.get(localTerm);
   }

   public boolean isLocalTermRequired(String localTerm)
   {
      return ((Boolean) localTermRequired.get(localTerm)).booleanValue();
   }

   /** Returns true if the given string is a ucd or local term in the
    * dictionary
    */
   public boolean isInDictionary(String term)
   {
      return (localTermRequired.get(term) != null)
           || (ucdKeyedDictionary.get(term) != null);
   }
}

