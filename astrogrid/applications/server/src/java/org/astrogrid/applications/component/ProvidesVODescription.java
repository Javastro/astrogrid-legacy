/*$Id: ProvidesVODescription.java,v 1.3 2004/07/23 13:21:21 nw Exp $
 * Created on 25-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.component;

import org.astrogrid.registry.beans.resource.VODescription;

/** interface to a component that provides the registry entry (vodescription) for this server 
 * @todo refactor into one of the description packages.*/

public interface ProvidesVODescription  {
    /** access the vodescription for this server */
    public VODescription getVODescription()  throws Exception;
}

/* 
$Log: ProvidesVODescription.java,v $
Revision 1.3  2004/07/23 13:21:21  nw
Javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)
 
*/