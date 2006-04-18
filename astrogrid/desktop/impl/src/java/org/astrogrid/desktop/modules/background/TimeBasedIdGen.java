/*$Id: TimeBasedIdGen.java,v 1.2 2006/04/18 23:25:43 nw Exp $
 * Created on 23-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.applications.manager.idgen.IdGen;

public class TimeBasedIdGen implements IdGen {
    public String getNewID() {
        return Long.toString(System.currentTimeMillis()); // simple  - don't need to be globally unique.
    }
}

/* 
$Log: TimeBasedIdGen.java,v $
Revision 1.2  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.1.2.1  2006/03/28 13:47:35  nw
first webstartable version.
 
*/