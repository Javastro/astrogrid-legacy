/*$Id: ObjectBuilderTest.java,v 1.3 2004/11/30 15:39:56 clq2 Exp $
 * Created on 22-Nov-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.scripting;

import junit.framework.TestCase;

/**@todo add test to check that values are being converted between the three representations correctly.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Nov-2004
 *
 */
public class ObjectBuilderTest extends TestCase {

    protected void setUp() {
        ob = new ObjectBuilder();
    }
    protected ObjectBuilder ob;
    
    public void testCreateAccount() {
        assertNotNull(ob.newAccount("frog","wibble.com"));
    }

    public void testCreateGroup() {
        assertNotNull(ob.newGroup("frog","wibble.com"));
    }



    /*
     * Class under test for User createUser()
     */
    public void testCreateUser() {
        assertNotNull(ob.user());
    }

    /*
     * Class under test for User createUser(String, String, String, String)
     */
    public void testCreateUserStringStringStringString() {
        assertNotNull(ob.newUser("frog","wibble","amphibians","null"));
    }


}


/* 
$Log: ObjectBuilderTest.java,v $
Revision 1.3  2004/11/30 15:39:56  clq2
scripting-nww-777

Revision 1.2.2.1  2004/11/26 15:38:16  nw
improved some names, added some missing methods.

Revision 1.2  2004/11/22 18:26:54  clq2
scripting-nww-715

Revision 1.1.2.1  2004/11/22 15:54:51  nw
deprecated existing scripting interface (which includes service lists).
produced new scripting interface, with more helpler objects.
 
*/