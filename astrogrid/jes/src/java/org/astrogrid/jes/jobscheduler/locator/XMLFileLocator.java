/*$Id: XMLFileLocator.java,v 1.2 2004/02/27 00:46:03 nw Exp $
 * Created on 25-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler.locator;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/** Extension of map locator that uses digester to read in an xml configuration file giving tool details.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2004
 *
 */
public class XMLFileLocator extends MapLocator {
    public static final String DEFAULT_CONFIG_LOCATION = "tools.xml";
    /** Construct a new XMLFileLocator, read config from default location on classpath
     * @see #DEFAULT_CONFIG_LOCATION
     */
    public XMLFileLocator() throws IOException , SAXException{
        this(XMLFileLocator.class.getResourceAsStream(DEFAULT_CONFIG_LOCATION));
    }
    /** Construct a new XMLFileLocator, read config from parameter URL
     * @param url url to gain config from
     */
    public XMLFileLocator(URL url) throws IOException, SAXException{
        this(url.openStream());
    }
    
    /** construct a new XMLFileLOcator, read config from stream 
     *  Construct a new XMLFileLocator
     * @param is stream to read from
     */
    public XMLFileLocator(InputStream is) throws IOException, SAXException{
        super();
        // run digester.
        Digester dig = new Digester();
        dig.push(this);
        dig.addObjectCreate("tools/tool",MapLocator.ToolInfo.class);
        dig.addSetProperties("tools/tool");
        dig.addSetNext("tools/tool","addTool");
        dig.parse(is);
    }
}


/* 
$Log: XMLFileLocator.java,v $
Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:27:21  nw
rearranging code
 
*/