/*$Id: Initialise.java,v 1.2 2004/09/07 00:54:20 mch Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.sec.initialiser;

import java.io.IOException;
import org.astrogrid.datacenter.queriers.fits.IndexGenerator;
import org.astrogrid.log.Log;


/** Quickfire class that runs the IndexGenerator on the urls in the pakcage
 *
 */

public class Initialise {

   /**
    *
    */
   public static void main(String[] args) throws IOException {
       Log.traceOn();
      Log.logToConsole();
      IndexGenerator generator = new IndexGenerator();
      generator.generateIndex(Initialise.class.getResourceAsStream("secUrls.txt"), System.out);
   }
}


/*
$Log: Initialise.java,v $
Revision 1.2  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

Revision 1.1  2004/09/06 21:36:28  mch
QuickFire initialiser

 
*/
