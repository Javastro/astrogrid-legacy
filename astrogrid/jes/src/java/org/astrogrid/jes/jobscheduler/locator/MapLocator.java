/*$Id: MapLocator.java,v 1.3 2004/03/04 01:57:35 nw Exp $
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
import org.astrogrid.jes.job.NotFoundException;
import org.astrogrid.jes.jobscheduler.Locator;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;

import java.util.HashMap;
import java.util.Map;

/**Tool locator that accepts a map of tool information.
 * Interim solution - eventually will find this information from the registry. 
 * @author Noel Winstanley nw@jb.man.ac.uk 19-Feb-2004
 *
 */
public class MapLocator implements Locator {
    /** Construct a new MapToolLocator
     * 
     */
    public MapLocator() {
        this(new HashMap());
    }
    public MapLocator(Map m) {
        this.m = m;
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

    }
}


/* 
$Log: MapLocator.java,v $
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