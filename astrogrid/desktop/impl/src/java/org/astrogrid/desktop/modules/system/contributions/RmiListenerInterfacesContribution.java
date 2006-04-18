/*$Id: RmiListenerInterfacesContribution.java,v 1.2 2006/04/18 23:25:43 nw Exp $
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
/** another hivmind configuration bean - for rmi listener interfaces. */
public class RmiListenerInterfacesContribution extends ArrayList {

 
    
    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

}


/* 
$Log: RmiListenerInterfacesContribution.java,v $
Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.1  2006/03/22 18:01:31  nw
merges from head, and snapshot of development
 
*/