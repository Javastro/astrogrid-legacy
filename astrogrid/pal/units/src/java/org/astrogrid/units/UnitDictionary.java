/*
 * $Id: UnitDictionary.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.units;


import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used to access/use UCDs
 * <p>
 * @author M Hill
 */

public class UnitDictionary
{
   public static final String UNIT_REF = "http://vizier.u-strasbg.fr/cgi-bin/Unit";
   public static final String UNIT_LIST = "http://vizier.u-strasbg.fr/cgi-bin/Unit?%3f";

   protected static Log log = LogFactory.getLog(UnitDictionary.class);

   public UnitDictionary() {
      init();
   }

   /** Initialise dictionary - load list of keywords and descriptions from vizier
    * @todo
    */
   public void init() {
   }
   
}


