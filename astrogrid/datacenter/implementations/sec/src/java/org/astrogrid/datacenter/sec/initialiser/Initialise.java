/*$Id: Initialise.java,v 1.1 2004/09/06 21:36:28 mch Exp $
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

import org.astrogrid.datacenter.queriers.fits.IndexGenerator;
import java.io.IOException;


/** Quickfire class that runs the IndexGenerator on the urls in the pakcage
 *
 */

public class Initialise {

   /**
    *
    */
   public static void main(String[] args) throws IOException {
      IndexGenerator generator = new IndexGenerator();
      generator.generateIndex(Initialise.class.getResourceAsStream("secUrls.txt"));
   }
}


/*
$Log: Initialise.java,v $
Revision 1.1  2004/09/06 21:36:28  mch
QuickFire initialiser

 
*/
