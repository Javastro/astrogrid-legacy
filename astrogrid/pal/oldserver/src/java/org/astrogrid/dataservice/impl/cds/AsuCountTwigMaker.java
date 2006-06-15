/*$Id: AsuCountTwigMaker.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.impl.cds;

import java.io.IOException;
import org.astrogrid.query.returns.ReturnSpec;

/** Makes an ASU URL 'twig' that can be attached to a URL stem to query
 * an ASU-compatible server for returning the number of matches.
 * <p>
 * @see http://vizier.u-strasbg.fr/doc/asu.html
 * <p>
 * @author M Hill
 */
public class AsuCountTwigMaker extends AsuTwigMaker  {

   /** do nothing - don't limit counts :-) */
   public void visitLimit(long limit) throws IOException {
      if (limit>0) {
         asuTwig.append("-out.max="+limit);
      }
   }
   
   /** Return spec is to return a count */
   public void visitReturnSpec(ReturnSpec spec) {
      asuTwig.append("&-out.exists");
   }
   
}


/*
 $Log: AsuCountTwigMaker.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/20 15:23:08  kea
 Checking old sources in in oldserver directory (rather than just
 deleting them, might still be useful).

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.1  2004/12/09 10:21:16  mch
 added count asu maker and asu conditions

 
 */





