/*$Id: VizierUnit.java,v 1.1 2004/11/03 05:14:33 mch Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.impl.cds.vizier;

import org.astrogrid.util.TypeSafeEnumerator;

/** enumeration class representing different units of measurement.
 *
 */

public class VizierUnit extends TypeSafeEnumerator {
   
    public static final VizierUnit ARCMIN = new VizierUnit("arcmin");
    public static final VizierUnit ARCSEC = new VizierUnit("arcsec");
    public static final VizierUnit DEG = new VizierUnit("deg");
    
    private VizierUnit(String unit) {
        super(unit);
    }

    /** Convenience routine to get unit enumerator from identifying string */
    public static VizierUnit getFor(String unit) {
      return (VizierUnit) getFor( VizierUnit.class, unit);
    }
}


/*
$Log: VizierUnit.java,v $
Revision 1.1  2004/11/03 05:14:33  mch
Bringing Vizier back online

Revision 1.1  2004/10/05 19:19:18  mch
Merged CDS implementation into PAL

Revision 1.2  2003/12/01 16:50:30  nw
first working tested version

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/
