/*$Id: XMLFileLocator.java,v 1.3 2004/03/07 21:04:38 nw Exp $
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Extension of map locator that uses digester to read in an xml configuration file giving tool details.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Feb-2004
 *
 */
public class XMLFileLocator extends MapLocator {
    public static interface ToolList {
        public URL getURL();
    }

    /** Construct a new XMLFileLocator, read config from parameter URL
     * @param url url to gain config from
     */
    public XMLFileLocator(ToolList t) throws IOException, SAXException{
        this.url = t.getURL();
        assert url != null;
        InputStream is = url.openStream(); 
        Digester dig = new Digester();
        dig.push(this);
        dig.addObjectCreate("tools/tool",MapLocator.ToolInfo.class);
        dig.addSetProperties("tools/tool");
        dig.addSetNext("tools/tool","addTool");
        dig.parse(is);
    }
    
    protected final URL url;
    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Tool locator that populates internal map from an xml document at a URL\n"
         + "Current Settings\n"
         + "file location :" + url.toString()
         + "\nmap contents :\n" 
         + m.toString();
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {        
        TestSuite suite  = new TestSuite("Tests for XML File Locator");
        suite.addTest(new InstallationTest("testCanReadFromURL"));
        suite.addTest(super.getInstallationTest());        
        return suite;    
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "XML File Locator";
    }
    
    protected class InstallationTest extends TestCase {
        public InstallationTest(String s) {
            super(s);
        }
        
        public void testCanReadFromURL() throws Exception{
            InputStream is = url.openStream();
            assertNotNull(is);
            // try creating another of self, see if barfs.
            MapLocator loc = new XMLFileLocator(new ToolList() {

                public URL getURL() {
                    return url;
                }
            });
            assertNotNull(loc);
        }
    }

}


/* 
$Log: XMLFileLocator.java,v $
Revision 1.3  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.2.4.1  2004/03/07 20:41:06  nw
added component descriptor interface impl,
refactored any primitive types passed into constructor

Revision 1.2  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.1.2.1  2004/02/27 00:27:21  nw
rearranging code
 
*/