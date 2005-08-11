/*$Id: MutableACRTest.java,v 1.1 2005/08/11 10:15:00 nw Exp $
 * Created on 17-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import org.astrogrid.acr.builtin.Module;
import org.astrogrid.desktop.framework.descriptors.ComponentDescriptor;
import org.astrogrid.desktop.framework.descriptors.DescriptorParser;
import org.astrogrid.desktop.framework.descriptors.DigesterDescriptorParser;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Mar-2005
 *
 */
public class MutableACRTest extends TestCase {

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.reg = new ACRImpl();
        this.parser = new DigesterDescriptorParser();
    }
    
    protected MutableACR reg;
    protected DescriptorParser parser;
       
        /** test that the builtin module, although created in a different way, obeys the
         * same behaviour as other modules
         * @throws Exception
         */
     public void testBuiltin() throws Exception {
        DefaultModule builtin = (DefaultModule)reg.getModule("builtin");
        assertNotNull(builtin);
        ComponentDescriptor cd =builtin.getDescriptor().getComponent("modules");
        assertNotNull(cd);
        assertEquals("modules", cd.getName());
        Object o = builtin.getComponent("modules");
        assertNotNull(o);
        assertSame(o,reg);
        
        // test how we can access the registry..
        //however, can get it by using interfaces as types, rather than keys.       
        //assertNotNull(builtin.getComponentInstanceOfType(ModuleRegistry.class));     
       // assertNotNull(builtin.getComponentInstanceOfType(MutableModuleRegistry.class));
        
        // test iterator.
        Iterator i = reg.moduleIterator();
        assertTrue(i.hasNext());
        assertSame(i.next(),builtin);
        assertFalse(i.hasNext());
        
        i = builtin.componentIterator();
        assertTrue(i.hasNext());
        while( i.hasNext()) {
            assertNotNull(i.next());
        }
     }
     
     
     public void testRegister() throws Exception {
         Listener l = new Listener();
         reg.addNewModuleListener(l);         
        // test a new module.
        reg.register(parseTestModule());
        DefaultModule mod = (DefaultModule)reg.getModule("test");
        assertNotNull(mod);
        assertEquals("test",mod.getDescriptor().getName());
                
        // test component is present
        Object o = mod.getComponent("component1");
        assertNotNull(o);
        // assert the thing we geet back supports the published interface
        assertTrue(CharSequence.class.isAssignableFrom(o.getClass()));
        //but isn't actually the implementation object - it's wrapped in proxies, etc.
        assertFalse(o.getClass().equals(String.class));
        //@todo add testing of optional components here too.
        // test listener
        assertEquals(2,l.seen);                
    }

    public void testRemovedListener() throws Exception{
        Listener removed = new Listener();
        reg.addNewModuleListener(removed);
        reg.removeNewModuleListener(removed);
        reg.register(parseTestModule());
        // test removed listener -- will just have seen builtin.
        assertEquals(1,removed.seen);
    }
    
    public void testLateListener() throws Exception {
        reg.register(parseTestModule());
        // test late listener
        Listener late = new Listener();
        reg.addNewModuleListener(late);
        assertEquals(2,late.seen);        
    }

    public void testGetDescriptors() throws Exception {
        Map m = reg.getDescriptors();
        assertNotNull(m);
    }
    
    
    
    /**
     * @return
     * @throws IOException
     * @throws SAXException
     */
    private ModuleDescriptor parseTestModule() throws IOException, SAXException {
        InputStream is = this.getClass().getResourceAsStream("test-module.xml");
        assertNotNull(is);
        ModuleDescriptor desc = parser.parse(is);
        return desc;
    }

    
    private  class Listener implements NewModuleListener {
        public int seen = 0;
    /**
     * @see org.astrogrid.desktop.framework.NewModuleListener#newModuleRegistered(org.astrogrid.desktop.framework.NewModuleEvent)
     */
    public void newModuleRegistered(NewModuleEvent e) {
        assertNotNull(e);
        assertEquals(reg,e.getSource());
        Module m = e.getModule();
        assertNotNull(m);
        seen++;
    }
    }


    
    
}


/* 
$Log: MutableACRTest.java,v $
Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.1  2005/08/05 11:46:56  nw
reimplemented acr interfaces, added system tests.

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:17  nw
checkin from branch desktop-nww-998

Revision 1.1.2.2  2005/04/01 19:03:10  nw
beta of job monitor

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/