/*$Id: SolarEventKeys.java,v 1.6 2004/11/19 10:27:29 clq2 Exp $
 * Created on 12-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.workflow.integration.itn6.solarevent;

/**defines registry key constants for the solar event science case.
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Aug-2004
 *@todo check whether these constants need to be querying config properties for authorityID.
 *
 */
public interface SolarEventKeys {
    /** registry key for the sec application */
    public static final String SEC_APP = "org.astrogrid.localhost/sec_dsa";
    /** registry key for the trace / fits application */
    public static final String FITS_APP = "org.astrogrid.localhost/trace_dsa";
    /** registry key for the movemaker application */
    public static final String MPEG_APP = "org.astrogrid.localhost/CallMakeMPEGFitsImages";
    /** registry key for the concat application - a simplified mock of the moviemaker application */
    public static final String CONCAT_APP = "org.astrogrid.localhost/concat";
}


/* 
$Log: SolarEventKeys.java,v $
Revision 1.6  2004/11/19 10:27:29  clq2
nww-itn07-659

Revision 1.5.66.1  2004/11/18 10:52:01  nw
javadoc, some very minor tweaks.

Revision 1.5  2004/08/17 13:35:18  nw
added key for concat app

Revision 1.4  2004/08/12 21:30:07  nw
got it working. nice.

Revision 1.3  2004/08/12 15:54:06  nw
fixed duff key

Revision 1.2  2004/08/12 14:30:03  nw
constructed workflows to call fits and sec. need to check the results next

Revision 1.1  2004/08/12 13:33:34  nw
added framework of classes for testing the solar event science case.
 
*/