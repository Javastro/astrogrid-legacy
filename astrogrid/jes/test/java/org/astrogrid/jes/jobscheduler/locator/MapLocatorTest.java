/*$Id: MapLocatorTest.java,v 1.6 2004/08/13 09:09:40 nw Exp $
 * Created on 19-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.locator;

import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.workflow.beans.v1.Tool;

import junit.framework.TestCase;

/** test basics of map tool locator.
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
 *
 */
public class MapLocatorTest extends TestCase {
    /**
     * Constructor for MapToolLocatorTest.
     * @param arg0
     */
    public MapLocatorTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        l = new MapLocator();
        MapLocator.ToolInfo nfo = new MapLocator.ToolInfo();
        nfo.setLocation(KNOWN_TOOL_LOCATION);
        nfo.setInterface(KNOWN_TOOL_INTERFACE);
        nfo.setName(KNOWN_TOOL);
        l.addTool(nfo);

    }
    
    protected MapLocator l;
    
    protected final static String KNOWN_TOOL = "KNOWN_TOOL";
    protected final static String KNOWN_TOOL_LOCATION = "http://nowhere.org/";
    protected final static String KNOWN_TOOL_INTERFACE = "foo.bae.choo";
    protected final static String UNKNOWN_TOOL = "UNKNOWN_TOOL";
    
    public void testLocationSuccess() throws Exception {
        Tool js = buildTool(KNOWN_TOOL);
        assertNotNull(l.locateTool(js));
    }

    public void testLocationFailure() throws Exception {
        Tool js = buildTool(UNKNOWN_TOOL);
         try {
             l.locateTool(js);
             fail("expected to fail");
         } catch (NotFoundException e) {
             // ok.
         }        
    }

 
    
    /**
    Build a noddy instance of a tool for testing.
     */
    private Tool buildTool(final String name) {
         Tool t = new Tool();
         t.setName(name);
         return t;
    }
}


/* 
$Log: MapLocatorTest.java,v $
Revision 1.6  2004/08/13 09:09:40  nw
tidied imports

Revision 1.5  2004/08/03 16:31:25  nw
simplified interface to dispatcher and locator components.
removed redundant implementations.

Revision 1.4  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.3  2004/03/03 01:13:42  nw
updated jes to work with regenerated workflow object model

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:29:00  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:41:25  nw
added tests for everything :)
 
*/