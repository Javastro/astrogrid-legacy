/*$Id: MapLocator.java,v 1.8 2004/03/16 01:21:12 nw Exp $
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

import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**Tool locator that accepts a map of tool information.
 * Interim solution - eventually will find this information from the registry. 
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
 *
 */
public class MapLocator implements Locator, ComponentDescriptor {
    protected static final Log logger = LogFactory.getLog(MapLocator.class);
    /** Construct a new MapToolLocator
     * 
     */
    public MapLocator() {
        m = new HashMap();
    }

    protected final Map m;

    /** add a tool to the map */
    public void addTool(ToolInfo info) {
        m.put(info.getName(),info);
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.ToolLocator#locateTool(org.astrogrid.jes.job.JobStep)
     */
    public String locateTool(Step js) throws JesException{
        ToolInfo nfo = getToolInfo(js);
        logger.debug("tool location for " + js.getTool().getName() + " :=" + nfo.getLocation());
        return nfo.getLocation();
        
    }
    /** return tool for this job step, or throw not found excepton */
    private ToolInfo getToolInfo(Step js) throws JesException {
        Tool tool = js.getTool();
        if (tool == null) {
            throw new JesException("Job Step contains no tool");
        }
        String toolName = tool.getName();
        ToolInfo nfo = (ToolInfo)m.get(toolName);
        if (nfo == null) {
            throw new NotFoundException("No information for tool named " + toolName);
        }
        return nfo;
    }
    /**
     * @see org.astrogrid.jes.jobscheduler.ToolLocator#getToolInterface(org.astrogrid.jes.job.JobStep)
     */
    public String getToolInterface(Step js) throws JesException {
        ToolInfo nfo = getToolInfo(js);
        return nfo.getInterface();
    }
    
    public static class ToolInfo {
        private String location;
        private String interfaceName;
        private String name;

        public void setLocation(String endpoint) {
            this.location = endpoint;
        }

        public String getLocation() {
            return location;
        }

        public void setInterface(String interfaceName) {
            this.interfaceName = interfaceName;
        }

        public String getInterface() {
            return interfaceName;
        }
        /**
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * @param string
         */
        public void setName(String string) {
            name = string;
        }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ToolInfo:");
        buffer.append(" location: ");
        buffer.append(location);
        buffer.append(" interfaceName: ");
        buffer.append(interfaceName);
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append("]");
        return buffer.toString();
    }
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "MapToolLocator";
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Empty map of tool locations, that can be extended prorammatically\n" + 
        "current contents:\n" + m.toString();
    }
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {        
        TestSuite suite  = new TestSuite("Tests for Map Locator");
        suite.addTest(new InstallationTest("testCanResolveToolEndpoints"));
        return suite;    
    }

    
    protected class InstallationTest extends TestCase {
        public InstallationTest(String s) {
            super(s);
        }
        /** @todo more checking of url endpoint here.. */
        public void testCanResolveToolEndpoints() {
            Iterator i = m.values().iterator();
            while (i.hasNext()) {
                ToolInfo nfo = (ToolInfo)i.next();
                assertNotNull(nfo);
                try {                 
                    URL url = new URL(nfo.getLocation());
                    URLConnection conn = url.openConnection();
                    assertNotNull(conn);
                    conn.connect();
                } catch (MalformedURLException e) {
                    fail(nfo.getName() + " " + e.getMessage());
                } catch(IOException e) {
                    fail("could not connect to " + nfo.getName() + ": " + e.getMessage());
                }
            }
        }
    }

}


/* 
$Log: MapLocator.java,v $
Revision 1.8  2004/03/16 01:21:12  nw
fixed bug

Revision 1.7  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.6  2004/03/15 01:30:45  nw
factored component descriptor out into separate package

Revision 1.5  2004/03/12 15:32:14  nw
improved logging

Revision 1.4  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.3.4.1  2004/03/07 20:41:06  nw
added component descriptor interface impl,
refactored any primitive types passed into constructor

Revision 1.3  2004/03/04 01:57:35  nw
major refactor.
upgraded to latest workflow object model.
removed internal facade
replaced community snippet with objects

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:27:21  nw
rearranging code

Revision 1.1.2.1  2004/02/19 13:38:17  nw
started implementation of an alternative tool locator
 
*/