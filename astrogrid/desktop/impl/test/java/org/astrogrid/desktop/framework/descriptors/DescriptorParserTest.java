/*$Id: DescriptorParserTest.java,v 1.1 2005/08/11 10:15:01 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework.descriptors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import junit.framework.TestCase;

/** Test the descriptor parser.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public class DescriptorParserTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.parser = new DigesterDescriptorParser();
    }
    
    protected DescriptorParser parser;
    
    public void testParse() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("module-descriptor-test1.xml");
        assertNotNull(is);
        ModuleDescriptor m = parser.parse(is);
        
        // test module.
        assertNotNull(m);
        assertNotNull(m.getName());
        assertEquals("test",m.getName());
        
        assertNotNull(m.getDescription());
        
        // test properties.
        assertNotNull(m.propertyIterator());
        assertTrue(m.propertyIterator().hasNext());
        assertEquals("<br>barney</br>",m.getProperty("fred"));
        
        
        // test componment
        assertNotNull(m.componentIterator());
        assertTrue(m.componentIterator().hasNext());

        ComponentDescriptor c1 = m.getComponent("component1");
        assertNotNull(c1);
        assertEquals("component1",c1.getName());
        assertNotNull(c1.getImplementationClass());
        assertNotNull(c1.getInterfaceClass());
        assertFalse(c1.propertyIterator().hasNext());   
        assertTrue(c1.methodIterator().hasNext());
                       
        ComponentDescriptor c2 = m.getComponent("component2");    
        assertNotNull(c2);
        assertEquals("component2",c2.getName());
        assertNotNull(c2.getImplementationClass());
        assertNotNull(c2.getInterfaceClass());  
        assertFalse(c2.methodIterator().hasNext());
        assertTrue(c2.propertyIterator().hasNext());
        assertEquals("pling",c2.getProperty("wibble"));
        
        
        // test method.
        MethodDescriptor m1 = c1.getMethod("list");
        assertNotNull(m1);
        assertEquals("list",m1.getName());
        // check defaults work.
        assertNotNull(m1.getReturnValue());
        assertFalse(m1.parameterIterator().hasNext());

        MethodDescriptor m2 = c1.getMethod("getJob");
        assertNotNull(m2);
        assertEquals("getJob",m2.getName());
        //check return value.
        assertEquals("document",m2.getReturnValue().getName());
        assertNotNull(m2.getReturnValue().getDescription());
        
        
        // check order of parameters.
        Iterator i = m2.parameterIterator();
        assertTrue(i.hasNext());
        ValueDescriptor one = (ValueDescriptor)i.next();
        assertNotNull(one);
        assertEquals("id",one.getName());
   
        assertTrue(i.hasNext());
        ValueDescriptor two = (ValueDescriptor)i.next();
        assertNotNull(one);
        assertEquals("User",two.getName());      
   
        
    }

    public void testDescriptorToString() throws Exception{
        InputStream is = this.getClass().getResourceAsStream("module-descriptor-test1.xml");
        assertNotNull(is);
        ModuleDescriptor m = parser.parse(is);     
        System.out.println(m);
    }
    
    public void testDescriptorSerializable() throws Exception{
        InputStream is = this.getClass().getResourceAsStream("module-descriptor-test1.xml");
        assertNotNull(is);
        ModuleDescriptor m = parser.parse(is);     
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(m);
        os.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        Object o = ois.readObject();
        assertNotNull(o);
        assertNotSame(o,m);
        assertEquals(o,m);
    }

}


/* 
$Log: DescriptorParserTest.java,v $
Revision 1.1  2005/08/11 10:15:01  nw
finished split

Revision 1.3  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.2  2005/04/13 12:59:18  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/