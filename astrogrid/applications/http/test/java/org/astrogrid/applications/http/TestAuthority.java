/*$Id: TestAuthority.java,v 1.7 2006/03/17 17:50:58 clq2 Exp $
 * Created on 23-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.http;

import org.astrogrid.applications.manager.AppAuthorityIDResolver;

//@TODO jdt check out the significance of this...can we push it up?
public class TestAuthority implements AppAuthorityIDResolver {
    public String getAuthorityID() {
        return "org.astrogrid.test";
    }

}

/* 
$Log: TestAuthority.java,v $
Revision 1.7  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.5  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.2.114.1  2005/12/22 13:56:03  gtr
Refactored to match the other kinds of CEC.

Revision 1.2  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.1.2.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.4  2004/09/01 15:42:26  jdt
Merged in Case 3

Revision 1.1.2.1  2004/07/30 11:02:30  jdt
Added unit tests, refactored the RegistryQuerier anf finished off
HttpApplicationCEAComponentManager.


 
*/