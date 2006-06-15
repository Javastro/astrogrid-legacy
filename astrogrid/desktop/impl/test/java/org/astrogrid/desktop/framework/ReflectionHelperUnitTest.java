/*$Id: ReflectionHelperUnitTest.java,v 1.1 2006/06/15 09:18:24 nw Exp $
 * Created on 25-Jul-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Jul-2005
 *
 */
public class ReflectionHelperUnitTest extends TestCase {

    public void testGetMethodByName() throws NoSuchMethodException {
       Method m =  ReflectionHelper.getMethodByName(Object.class,"equals");
       assertNotNull(m);
       Method m1 = ReflectionHelper.getMethodByName(Object.class," EqualS"); // check still works for poorly entered data.
       assertNotNull(m1);
       assertEquals(m,m1);
              
     
    }
    
    public void testGetMethodByNameFails() throws NoSuchMethodException {
        try {
        Method m =  ReflectionHelper.getMethodByName(Object.class,"equals1");
        fail("expected to throw");
        } catch (NoSuchMethodException e) {
            // expeced.
        }
    }

}


/* 
$Log: ReflectionHelperUnitTest.java,v $
Revision 1.1  2006/06/15 09:18:24  nw
improved junit tests

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.
 
*/