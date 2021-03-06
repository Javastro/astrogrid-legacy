/*
 * $Id: UnitDictionary.java,v 1.3 2005/05/27 16:21:15 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.units;


import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used to lookup units and their meanings.  Just a place-holder at the moment.
 * <p>
 * @author M Hill
 */

public class UnitDictionary
{
   public static final String UNIT_REF = "http://vizier.u-strasbg.fr/cgi-bin/Unit";
   public static final String UNIT_LIST = "http://vizier.u-strasbg.fr/cgi-bin/Unit?%3f";

   public static final String MILLISECONDS = "ms";
   
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


