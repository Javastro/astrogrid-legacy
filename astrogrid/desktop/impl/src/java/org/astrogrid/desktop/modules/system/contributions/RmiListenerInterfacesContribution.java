/*$Id: RmiListenerInterfacesContribution.java,v 1.5 2008/11/04 14:35:53 nw Exp $
 * Created on 20-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system.contributions;

import java.util.ArrayList;
/** A RMI interface that needs to be declared to the system */
public class RmiListenerInterfacesContribution extends ArrayList {

 
    
    private String id;

    /** the id of a service that has listener interfaces that need to be registered
     * with the rmi machinery
     */
    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

}


/* 
$Log: RmiListenerInterfacesContribution.java,v $
Revision 1.5  2008/11/04 14:35:53  nw
javadoc polishing

Revision 1.4  2007/06/18 16:55:51  nw
javadoc fixes.

Revision 1.3  2006/06/15 09:55:55  nw
doc fix

Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/