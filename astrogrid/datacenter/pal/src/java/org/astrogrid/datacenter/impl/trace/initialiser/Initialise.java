/*$Id: Initialise.java,v 1.1 2004/10/05 16:10:43 mch Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.impl.trace.initialiser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import org.astrogrid.datacenter.queriers.fits.IndexGenerator;


/** Quickfire class that runs the IndexGenerator on the urls in the pakcage
 *
 */

public class Initialise {

   /**
    *
    */
   public static void main(String[] args) throws IOException {
      IndexGenerator generator = new IndexGenerator();
      generator.checkForExtensions = false;
      generator.fitsDateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
      generator.generateIndex(Initialise.class.getResourceAsStream("secUrls.txt"), System.out);
   }
}


/*
$Log: Initialise.java,v $
Revision 1.1  2004/10/05 16:10:43  mch
Merged with PAL

Revision 1.5  2004/09/07 14:52:19  mch
Fixes etc for SEC

Revision 1.4  2004/09/07 09:48:56  mch
Logging updates

Revision 1.3  2004/09/07 01:39:27  mch
Moved email keys from TargetIndicator to Slinger

Revision 1.2  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

Revision 1.1  2004/09/06 21:36:28  mch
QuickFire initialiser

 
*/
