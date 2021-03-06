/*$Id: IdGen.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 27-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.idgen;
/** Interface to a component that generates unique ids.
 * @author Noel Winstanley nw@jb.man.ac.uk 27-May-2004
 */
public interface IdGen {
    /**
    * Get a new executionID. implementations should be thread safe - every call to this method must return a new id. 
    * @return a unique execution identifier.
    */
    public abstract String getNewID();
}
/* 
$Log: IdGen.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/