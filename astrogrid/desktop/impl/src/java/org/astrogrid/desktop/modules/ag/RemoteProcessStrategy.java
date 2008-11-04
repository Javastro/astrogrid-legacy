/*$Id: RemoteProcessStrategy.java,v 1.9 2008/11/04 14:35:47 nw Exp $
 * Created on 08-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.ServiceException;
import org.w3c.dom.Document;

/** Something that knows how to manage a particular kind of remote process.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Nov-2005
 *
 */
public interface RemoteProcessStrategy{
    /** returns true of this strategy can manage a task described by the parameter exec id. */
    boolean canProcess(URI execId) ;
    /** dual-use method, for efficiency.
     * returns null if can't process. If can process, return value is used for snitching - extract some kind of name from the document
     * 
     *  @return the application being invoked / workflow name / somesuch.*/
    String canProcess(Document doc);
    
    public ProcessMonitor create(Document doc) throws InvalidArgumentException, ServiceException;
    
    

    
}


/* 
$Log: RemoteProcessStrategy.java,v $
Revision 1.9  2008/11/04 14:35:47  nw
javadoc polishing

Revision 1.8  2008/07/18 17:15:52  nw
Complete - task 433: Strip out unused internal CEA

Revision 1.7  2007/09/21 16:35:14  nw
improved error reporting,
various code-review tweaks.

Revision 1.6  2007/07/30 17:59:55  nw
RESOLVED - bug 2257: More feedback, please
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2257

Revision 1.5  2007/07/13 23:14:53  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

Revision 1.4  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.3  2006/05/26 15:23:45  nw
implemented snitching.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.34.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/