/*$Id: ToolListFromConfig.java,v 1.3 2004/03/15 01:30:06 nw Exp $
 * Created on 07-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component.production;

import org.astrogrid.config.Config;
import org.astrogrid.jes.component.descriptor.SimpleComponentDescriptor;
import org.astrogrid.jes.jobscheduler.locator.XMLFileLocator.ToolList;

import java.net.URL;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 */
public class ToolListFromConfig extends SimpleComponentDescriptor implements ToolList {

    public static final String XML_LOCATOR_URL = "jes.locator.xml.url";
    /** @todo change this */
    public static final String DEFAULT_XML_LOCATOR_URL = "/org/astrogrid/jes/jobscheduler/locator/tools.xml";
    public ToolListFromConfig(Config conf) {
        url = conf.getUrl(XML_LOCATOR_URL,this.getClass().getResource(DEFAULT_XML_LOCATOR_URL));
        name = "get url for xml tool list from config";
             description = "Loads url of tool list document from Config\n" +
                 "key :" + XML_LOCATOR_URL 
                 + "\n default value: " + DEFAULT_XML_LOCATOR_URL + " on classpath" 
                 + "\n current value:" + url.toString();

    }
    
    protected final URL url; 
    
    
    public URL getURL() {
        return url;
    }
}


/* 
$Log: ToolListFromConfig.java,v $
Revision 1.3  2004/03/15 01:30:06  nw
factored component descriptor out into separate package

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:26  nw
added implementation of a self-configuring production set of component
 
*/