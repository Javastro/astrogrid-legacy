/*$Id: SimpleTranslatorMapTest.java,v 1.3 2004/01/13 00:33:14 nw Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.queriers.spi;

import junit.framework.TestCase;

import org.astrogrid.datacenter.axisdataserver.types.Language;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 27-Nov-2003
 *
 */
public class SimpleTranslatorMapTest extends TestCase {

 
    /**
     * Constructor for SimpleTranslatorMap.
     * @param arg0
     */
    public SimpleTranslatorMapTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(SimpleTranslatorMapTest.class);
    }
    
    protected void setUp() {
        map = new SimpleTranslatorMap();
        map.add(KNOWN_KEY,SCHEMA_URL,new IdTranslator());
    }
    protected SimpleTranslatorMap map;
    public static final String KNOWN_KEY = "urn:fooble";
    public static final  String SCHEMA_URL = "http://www.slashdot.org" ;
    public void testRetreiveUnknown() {
        Translator trans = map.lookup("Unknown");
        assertNull(trans);
        
    }
    
    public void testRetreiveKnown() {
        Translator trans = map.lookup(KNOWN_KEY);
        assertNotNull(trans);
    }
    
    public void testList() {
        Language[] lang = map.list();
        assertNotNull(lang);
        assertEquals(1,lang.length);
        assertNotNull(lang[0]);
        assertEquals(KNOWN_KEY,lang[0].getNamespace().toString());
        assertEquals(IdTranslator.class.getName(),lang[0].getImplementingClass());
        assertEquals(SCHEMA_URL,lang[0].getSchemaLocation().toString());
    }
    
    public void testFaultyAdd() {
       try {
           map.add(null,new IdTranslator());
           fail("didn't thrown");
       } catch (IllegalArgumentException expected) {
       }
       try {
           map.add("foo",null);
           fail("didn't throw");
       } catch (IllegalArgumentException expected) {
       }
       // should succeed
       map.add("foo",null,new IdTranslator());
       
     
    }
    
    
   
    
}


/* 
$Log: SimpleTranslatorMapTest.java,v $
Revision 1.3  2004/01/13 00:33:14  nw
Merged in branch providing
* sql pass-through
* replace Certification by User
* Rename _query as Query

Revision 1.2.10.1  2004/01/07 11:51:07  nw
found out how to get wsdl to generate nice java class names.
Replaced _query with Query throughout sources.

Revision 1.2  2003/11/28 16:10:30  nw
finished plugin-rewrite.
added tests to cover plugin system.
cleaned up querier & queriermanager. tested

Revision 1.1  2003/11/27 17:28:09  nw
finished plugin-refactoring
 
*/