/*$Id: TestCommunity.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 23-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.javaclass;

import org.astrogrid.applications.javaclass.JavaClassApplicationDescriptionLibrary.Community;


public class TestCommunity implements JavaClassApplicationDescriptionLibrary.Community {
    public String getCommunity() {
        return "org.astrogrid.test";
    }
}

/* 
$Log: TestCommunity.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:42:46  nw
final version, before merge
 
*/