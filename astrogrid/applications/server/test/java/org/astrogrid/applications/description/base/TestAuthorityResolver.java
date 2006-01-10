/*$Id: TestAuthorityResolver.java,v 1.4 2006/01/10 11:26:52 clq2 Exp $
 * Created on 23-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description.base;

import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;



public class TestAuthorityResolver implements BaseApplicationDescriptionLibrary.AppAuthorityIDResolver {
    public String getAuthorityID() {
        return "org.astrogrid.test";
    }
}

/* 
$Log: TestAuthorityResolver.java,v $
Revision 1.4  2006/01/10 11:26:52  clq2
rolling back to before gtr_1489

Revision 1.2  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.1.2.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.3  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:42:46  nw
final version, before merge
 
*/