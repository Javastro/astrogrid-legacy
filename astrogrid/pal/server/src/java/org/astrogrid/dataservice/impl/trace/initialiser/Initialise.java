/*$Id: Initialise.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.dataservice.impl.trace.initialiser;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import org.astrogrid.dataservice.queriers.fits.IndexGenerator;


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
      generator.generateIndex(Initialise.class.getResourceAsStream("traceUrls.txt"), new OutputStreamWriter(System.out));
   }
}


/*
$Log: Initialise.java,v $
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.3  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.2.4.1  2004/10/15 19:59:05  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.2  2004/10/05 16:37:45  mch
Merged with PAL

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
