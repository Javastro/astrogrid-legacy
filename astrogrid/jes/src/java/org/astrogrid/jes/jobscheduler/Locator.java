/*$Id: Locator.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 12-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler;

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.job.JobStep;

/** Interface to a component that will retreive details / location for a tool
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public interface Locator {
    String locateTool(JobStep js) throws JesException;
    String getToolInterface(JobStep js) throws JesException;
}


/* 
$Log: Locator.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:28:14  nw
rearranging code

Revision 1.1.2.3  2004/02/19 13:38:17  nw
started implementation of an alternative tool locator

Revision 1.1.2.2  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.1.2.1  2004/02/12 12:56:39  nw
factored out two components, concerned with accessing tool details
and talking with the application controller
 
*/