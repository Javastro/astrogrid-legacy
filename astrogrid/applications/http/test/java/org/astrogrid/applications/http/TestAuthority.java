/*$Id: TestAuthority.java,v 1.6 2006/03/11 05:57:54 clq2 Exp $
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


//@TODO jdt check out the significance of this...can we push it up?
public class TestAuthority implements HttpApplicationDescriptionLibrary.AppAuthorityIDResolver {
    public String getAuthorityID() {
        return "org.astrogrid.test";
    }

}

/* 
$Log: TestAuthority.java,v $
Revision 1.6  2006/03/11 05:57:54  clq2
roll back to before merged apps_gtr_1489, tagged as rolback_gtr_1489

Revision 1.4  2006/01/10 11:26:52  clq2
rolling back to before gtr_1489

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