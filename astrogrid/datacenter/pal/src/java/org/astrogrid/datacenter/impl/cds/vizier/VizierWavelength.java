/*$Id: VizierWavelength.java,v 1.1 2004/11/03 05:14:33 mch Exp $
 * Created on 11-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.impl.cds.vizier;

/** Static enumeration class of supported wavelengths
 *
 */
import org.astrogrid.util.TypeSafeEnumerator;

public class VizierWavelength extends TypeSafeEnumerator {

    public final static VizierWavelength RADIO = new VizierWavelength("Radio");
    public final static VizierWavelength IR = new VizierWavelength("IR");
    public final static VizierWavelength OPTICAL = new VizierWavelength("optical");
    public final static VizierWavelength UV = new VizierWavelength("UV");
    public final static VizierWavelength EUV = new VizierWavelength("EUV");
    public final static VizierWavelength X_RAY = new VizierWavelength("X-ray");
    public final static VizierWavelength GAMMA_RAY = new VizierWavelength("Gamma-ray");

    private VizierWavelength(String name) {
        super(name);
    }

    /** Convenience routine to get unit enumerator from identifying string */
    public static VizierWavelength getFor(String unit) {
      return (VizierWavelength) getFor( VizierWavelength.class, unit);
    }
}


/*
$Log: VizierWavelength.java,v $
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
