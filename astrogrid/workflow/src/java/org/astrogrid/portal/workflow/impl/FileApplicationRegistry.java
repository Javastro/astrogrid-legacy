/*$Id: FileApplicationRegistry.java,v 1.3 2004/11/11 00:52:28 clq2 Exp $
 * Created on 09-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.ApplicationList;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.portal.workflow.intf.ApplicationDescription;
import org.astrogrid.portal.workflow.intf.ApplicationDescriptionSummary;
import org.astrogrid.portal.workflow.intf.ApplicationRegistry;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** Implememntation of an Application Registry based on a flat xml file. 
 * @see test-tool-list.xml for example of file.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 *
 */
public class FileApplicationRegistry implements ApplicationRegistry {
    private static final Log log = LogFactory.getLog(FileApplicationRegistry.class);

    /** Construct a new FileApplicationRegistry
     */
    public FileApplicationRegistry(URL regFile)  throws IOException, CastorException{
        log.info("Creating File-based Application Registry, from " + regFile.toString());
        Reader reader = new InputStreamReader(regFile.openStream());
        ApplicationList list = ApplicationList.unmarshalApplicationList(reader);       
        reader.close();  
        // populate a map, so its easier to look up later..
        map  = new HashMap();
        ApplicationBase[] arr = list.getApplicationDefn(); // todo get this from the application list, once its been fixed
        for (int i = 0; i < arr.length; i++) {
            map.put(arr[i].getName(),arr[i]);
        }  
    }
    protected final Map map;
       
    /**
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#listApplications()
     */
    public String[] listApplications() throws WorkflowInterfaceException {
        int size = map.keySet().size();
        String[] result = new String[size];
        Iterator i = map.keySet().iterator();
        for (int ix = 0; i.hasNext(); ix++) {
            result[ix] = i.next().toString(); // pretty sure they're strings already.
        } 
        return result;
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#getDescriptionFor(java.lang.String)
     */
    public ApplicationDescription getDescriptionFor(String applicationName)  throws WorkflowInterfaceException{
        ApplicationBase result = (ApplicationBase)map.get(applicationName);
        if (result == null) {
            throw new WorkflowInterfaceException("Could not find description for " + applicationName);
        }
        return new ApplicationDescription(result);
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.ApplicationRegistry#listUIApplications()
     */
    public ApplicationDescriptionSummary[] listUIApplications() throws WorkflowInterfaceException {
        int size = map.keySet().size();
        ApplicationDescriptionSummary[]  result = new ApplicationDescriptionSummary[size];
        Iterator i = map.entrySet().iterator();
        for (int ix = 0; i.hasNext(); ix++) {
            Map.Entry e = (Map.Entry)i.next();
            ApplicationBase b = (ApplicationBase)e.getValue();
            Interface[] intfs = b.getInterfaces().get_interface();
            String[] interfaces = new String[intfs.length];
            for (int j = 0; j < intfs.length; j++) {
                interfaces[j] =intfs[j].getName();
            }
            result[ix] = new ApplicationDescriptionSummary((String)e.getKey(),(String)e.getKey(),interfaces);
        }
        return result;
    }
}


/* 
$Log: FileApplicationRegistry.java,v $
Revision 1.3  2004/11/11 00:52:28  clq2
nww's bug590

Revision 1.2.116.1  2004/11/10 13:33:32  nw
added new method to ApplicationRegistry - listUIApplications

Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.2  2004/03/11 13:36:10  nw
added implementations for the workflow interfaces

Revision 1.1.2.1  2004/03/09 17:41:59  nw
created a bunch of implementations,
 
*/