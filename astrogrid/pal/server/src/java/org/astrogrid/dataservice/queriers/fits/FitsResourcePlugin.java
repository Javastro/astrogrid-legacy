/*$Id: FitsResourcePlugin.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.queriers.fits;

import java.io.IOException;
import org.astrogrid.dataservice.metadata.VoResourcePlugin;

/** returns resources for Vizier service
 */
public class FitsResourcePlugin implements VoResourcePlugin {

   
   /**
    * Returns the VOResource element of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public String[] getVoResources() throws IOException {
      throw new UnsupportedOperationException("todo");

   }
   
   
}


/*
 $Log: FitsResourcePlugin.java,v $
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.6  2004/11/03 12:13:26  mch
 Fixes to branch cockup, plus katatjuta Register and get cone (for examples)

 */
