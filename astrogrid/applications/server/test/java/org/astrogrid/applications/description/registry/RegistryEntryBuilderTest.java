/*$Id: RegistryEntryBuilderTest.java,v 1.3 2004/11/27 13:20:03 pah Exp $
 * Created on 26-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description.registry;

import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.TestApplicationDescriptionLibrary;
import org.astrogrid.registry.beans.resource.VODescription;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import junit.framework.TestCase;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 *
 */
public class RegistryEntryBuilderTest extends TestCase {
    /**
     * Constructor for RegistryEntryBuilderNewTest.
     * @param arg0
     */
    public RegistryEntryBuilderTest(String arg0) {
        super(arg0);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ApplicationDescriptionLibrary lib = new TestApplicationDescriptionLibrary("astrogrid.org/testapp");
        RegistryEntryBuilder.URLs urls = new RegistryEntryBuilder.URLs() {
            URL serviceURL =  new URL("http://locahost:8080/astrogrid-applications-SNAPSHOT/services/CommonExecutionConnectorService");                
           
            public URL getRegistryTemplate() {
                URL template = this.getClass().getResource("/CEARegistryTemplate.xml");
                assertNotNull(template);
                return template;        
             }

            public URL getServiceEndpoint() {
                return serviceURL;
                 }
        };
        builder = new RegistryEntryBuilder(lib,urls);
    }
    
    protected RegistryEntryBuilder builder;
    
    public void testGetDescription() throws Exception{
        VODescription desc =  builder.getVODescription();
        assertNotNull(desc);
        assertTrue(desc.isValid()      );  
    }
    public void testRoundTrip() throws Exception {
      VODescription entry = builder.makeEntry();
      assertNotNull(entry);
      StringWriter writer = new StringWriter();

      Marshaller mar = new Marshaller(writer);
      mar.setDebug(true);
      mar.setMarshalExtendedType(true);
      mar.setSuppressXSIType(false);
      mar.setLogWriter(new PrintWriter(System.out));
      mar.setMarshalAsDocument(true);
//    TODO write a castor wiki page about this....      
      mar.setNamespaceMapping("cea", "http://www.ivoa.net/xml/CEAService/v0.1");


      mar.marshal(entry);
     
      writer.close();
      Unmarshaller um = new Unmarshaller(VODescription.class);
     
      //TODO Castor bug -will not round trip....
      VODescription reentry = (VODescription)um.unmarshal(new StringReader(writer.toString()));
     assertNotNull(reentry);
     
      //TODO - should make more extensive tests....
    }    
}


/* 
$Log: RegistryEntryBuilderTest.java,v $
Revision 1.3  2004/11/27 13:20:03  pah
result of merge of pah_cea_bz561 branch

Revision 1.2.90.1  2004/11/04 16:49:17  pah
put in the authorityid into the testapp name

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.7.4.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/