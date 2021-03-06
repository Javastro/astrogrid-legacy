/*$Id: Locator.java,v 1.7 2005/03/13 07:13:39 clq2 Exp $
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
import org.astrogrid.workflow.beans.v1.Tool;

/** Interface to a component that will retreive details / location for a tool
 * @author Noel Winstanley nw@jb.man.ac.uk 12-Feb-2004
 *
 */
public interface Locator {
    /** resolve a tool name into a list of equivalent endpoints of services that provides the tool 
     * @todo return url? or something more abstract
     * */
    String[] locateTool(Tool  js) throws JesException;
}


/* 
$Log: Locator.java,v $
Revision 1.7  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.6.92.1  2005/03/11 15:21:35  nw
adjusted locator so that it returns a list of endpoints to connect to.
we can get round-robin by shuffling the list.
dispatcher tries each endpoint in the list until can connect to one wihout throwing an exception.

Revision 1.6  2004/08/13 09:07:58  nw
tidied imports

Revision 1.5  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.4  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.3  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

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