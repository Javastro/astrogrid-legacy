/*
 * $Id: UcdDictionary.java,v 1.1 2004/09/02 11:22:31 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.ucd;

import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Used to access/use UCDs
 * <p>
 * @author M Hill
 */

public class UcdDictionary
{
   public static final String UCD1REF = "http://cdsweb.u-strasbg.fr/UCD/old/";
   public static final String UCD1LIST = "http://cdsweb.u-strasbg.fr/viz-bin/UCDs";

   public static final String UCD1PREF = "http://vizier.u-strasbg.fr/doc/UCD/inVizieR.htx";
   public static final String UCD1PLIST = "http://vizier.u-strasbg.fr/UCD/ucd1p-words.txt";
   
   protected static Log log = LogFactory.getLog(UcdDictionary.class);

   public UcdDictionary() {
      init();
   }

   /** Initialise dictionary - load list of keywords and descriptions from vizier
    * @todo
    */
   public void init() {
   }
   
}


