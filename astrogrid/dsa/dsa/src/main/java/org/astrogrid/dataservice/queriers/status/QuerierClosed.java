/*
 * $Id: QuerierClosed.java,v 1.1 2009/05/13 13:20:26 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.status;



/** Final state, everything is finished and no more work can/will be done
 * This might be implemented by error, aborted or complete */

public interface QuerierClosed
{
}

/*
$Log: QuerierClosed.java,v $
Revision 1.1  2009/05/13 13:20:26  gtr
*** empty log message ***

Revision 1.2  2007/03/21 18:59:41  kea
Preparatory work for v1.0 resources (not yet supported);  and also
cleaning up details of completed jobs to save memory.

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.1.30.1  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 */
