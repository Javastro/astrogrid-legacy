/*
 * $Id: JaxbReadImplTest.java,v 1.6 2008/09/24 13:40:50 pah Exp $
 * 
 * Created on 11 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.impl;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import junit.framework.TestCase;
import net.ivoa.resource.Resource;

import org.astrogrid.applications.description.cea.CeaApplication;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

public class JaxbReadImplTest extends TestCase {

    public JaxbReadImplTest(String name) {
	super(name);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
    }

    public void testRead() throws JAXBException, SAXException
    {
	  JAXBContext jc = CEAJAXBContextFactory.newInstance();
	  Unmarshaller um = jc.createUnmarshaller();
        System.out.println(jc.toString());
	      javax.xml.validation.SchemaFactory sf = SchemaFactory.newInstance(
	    	      javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
	      
	      LSResourceResolver resourceResolver = new LSResourceResolver(){

		public LSInput resolveResource(String type,
			String namespaceURI, String publicId, String systemId,
			String baseURI) {
		    System.out.println("ns="+namespaceURI+" base="+baseURI);
		    LSInput retval = null;
		    return retval;
		}
		  
	      };
	    sf.setResourceResolver(resourceResolver);
	  Source schemas = new StreamSource(this.getClass().getResourceAsStream("/schema/cea/CEAImplementation/v2.0/CEAImplementation.xsd"),this.getClass().getResource("/schema/cea/CEAImplementation/v2.0/CEAImplementation.xsd").toExternalForm()) ;
	  Schema schema = sf.newSchema(schemas);
	  um.setSchema(schema);
	  um.setEventHandler(new DefaultValidationEventHandler());  
	  //Unmarshall the file into a content object
//	  JAXBElement<?> o = (JAXBElement<?>) um.unmarshal(getClass().getResourceAsStream("/CeaApplicationConfig.xml"));
	  CECConfig c = (CECConfig) um.unmarshal(getClass().getResourceAsStream("/TestApplicationConfig.xml"));
	  assertTrue("should be more than one appliccation", c.getCeaApplicationOrCeaDALServiceOrDBDefinition().size() > 0);
          CeaApplication appdesc = (CeaApplication) c.getCeaApplicationOrCeaDALServiceOrDBDefinition().get(0);
          
          Marshaller m = jc.createMarshaller();
          m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, 
                Boolean.TRUE );
          
          //TODO - perhaps the easiest thing is to do a quick xslt transformation to get this back to a resource
          m.marshal( new JAXBElement(new QName("http://www.ivoa.net/xml/RegistryInterface/v1.0","Resource"),Resource.class,appdesc), System.out);
    }

    
}


/*
 * $Log: JaxbReadImplTest.java,v $
 * Revision 1.6  2008/09/24 13:40:50  pah
 * package naming changes
 *
 * Revision 1.5  2008/09/13 09:51:06  pah
 * code cleanup
 *
 * Revision 1.4  2008/09/10 23:27:20  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
 * Revision 1.3  2008/09/04 19:10:53  pah
 * ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
 * Added the basic implementation to support VOSpace  - however essentially untested on real deployement
 *
 * Revision 1.2  2008/09/03 14:19:08  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.2  2008/08/02 13:33:56  pah
 * safety checkin - on vacation
 *
 * Revision 1.1.2.1  2008/03/19 23:10:55  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
