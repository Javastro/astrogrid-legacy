/*$Id: TestCommunity.java,v 1.4 2004/09/01 15:42:26 jdt Exp $
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
public class TestCommunity implements HttpApplicationDescriptionLibrary.Community {
    public String getCommunity() {
        return "org.astrogrid.test";
    }
}

/* 
$Log: TestCommunity.java,v $
Revision 1.4  2004/09/01 15:42:26  jdt
Merged in Case 3

Revision 1.1.2.1  2004/07/30 11:02:30  jdt
Added unit tests, refactored the RegistryQuerier anf finished off
HttpApplicationCEAComponentManager.


 
*/