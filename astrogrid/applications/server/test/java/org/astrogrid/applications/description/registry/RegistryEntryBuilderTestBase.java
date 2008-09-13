/*
 * $Id: RegistryEntryBuilderTestBase.java,v 1.9 2008/09/13 09:51:02 pah Exp $
 * 
 * Created on 02-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.registry;

import java.io.IOException;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.ServiceDefinitionFactory;
import org.astrogrid.applications.description.TemplatedServiceDefinition;
import org.astrogrid.applications.manager.DefaultMetadataService;
import org.astrogrid.contracts.SchemaMap;
import org.astrogrid.test.AstrogridAssert;
import org.w3c.dom.Document;

/**
 * Encapuslates the common parts of a registry entry builder test case.
 * @author Paul Harrison (pharriso@eso.org) 02-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public abstract class RegistryEntryBuilderTestBase extends TestCase {

   /**
    * 
    */
   public RegistryEntryBuilderTestBase() {
      super();
   }
   /**
    * @param arg0
    */
   public RegistryEntryBuilderTestBase(String arg0) {
      super(arg0);
   }
   /** create a description library - this should be overwritten in the subclass 
    * @throws IOException
    * @throws Exception*/
   protected abstract ApplicationDescriptionLibrary createDesciptionLibrary() throws  Exception;
   /**
    * Logger for this class
    */
   private static final Log logger 
       = LogFactory.getLog(RegistryEntryBuilderTestBase.class);
   
   @Override
protected void setUp() throws Exception {
     super.setUp();
     ApplicationDescriptionLibrary lib = createDesciptionLibrary();
     Configuration configuration = new MockNonSpringConfiguredConfig();
     ServiceDefinitionFactory fac = new TemplatedServiceDefinition(configuration);
    builder = new DefaultMetadataService(lib,configuration, fac);
   }
   
   protected DefaultMetadataService builder;
   
   /** test to see if get description as objects and if castor thinks that they are valid.*/
   public void testGetDescription() throws Exception {
        Document desc =  builder.getServerDescription();
        assertNotNull(desc);
        XMLUtils.PrettyDocumentToStream(desc, System.out);
        // this tests castor's idea of validity
        AstrogridAssert.assertSchemaValid(desc,"Resource",SchemaMap.ALL);
    }
   
/** test to see if get description as DOM and if XML valid from schema directly */
   public void testDescriptionValidity() throws Exception {
       Document desc =  builder.getServerDescription();
       assertNotNull(desc);
       //this tests xml validity via xml parser
       StringWriter writer = new StringWriter();
       XMLUtils.PrettyDocumentToWriter(desc, writer);
       //note that the pretty print will not necessarily be valid as it puts new lines in all the string only content elements.
       logger.info("pretty print of xml to be VALIDATED\n"+writer.toString());
       AstrogridAssert.assertSchemaValid(desc,"Resource",SchemaMap.ALL);
    }
   //FIXME - will want to reimplement something like this....
//  public void RoundTrip() throws Exception {
//         VOResources entry = builder.makeEntry();
//         assertNotNull(entry);
//         StringWriter writer = new StringWriter();
//   
//         Marshaller mar = new Marshaller(writer);
//         mar.setDebug(true);
//         mar.setMarshalExtendedType(true);
//         mar.setSuppressXSIType(false);
//         mar.setLogWriter(new PrintWriter(System.out));
//         mar.setMarshalAsDocument(true);
//   //    TODO write a castor wiki page about this....      
//         mar.setNamespaceMapping("cea", Namespaces.VOCEA);
//         mar.setNamespaceMapping("vr", Namespaces.VORESOURCE);
//   
//   
//         mar.marshal(entry);
//         writer.close();
//         logger.info("marshalled object\n"+writer.toString());
//        
//         Unmarshaller um = new Unmarshaller(VOResources.class);
//        
//         //TODO Castor bug -will not round trip....
//         VOResources reentry = (VOResources)um.unmarshal(new StringReader(writer.toString()));
//        assertNotNull(reentry);
//        
//         //TODO - should make more extensive tests....
//       }
}


/*
 * $Log: RegistryEntryBuilderTestBase.java,v $
 * Revision 1.9  2008/09/13 09:51:02  pah
 * code cleanup
 *
 * Revision 1.8  2008/09/03 14:19:00  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.7.2.2  2008/05/01 15:22:47  pah
 * updates to tool
 *
 * Revision 1.1.2.4  2008/04/04 15:46:08  pah
 * Have got bulk of code working with spring - still need to remove all picocontainer refs
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 * Revision 1.1.2.3  2008/04/01 13:50:07  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.1.2.2  2008/03/26 17:15:39  pah
 * Unit tests pass
 *
 * Revision 1.1.2.1  2008/03/19 23:10:55  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 * Revision 1.1  2008/02/12 12:10:56  pah
 * build with 1.0 registry and filemanager clients
 *
 * Revision 1.6  2007/01/16 09:54:55  gtr
 * I replaced org.astrogrid.comon.test.SchemaMap with org.astrogrid.contracts.SchemaMap. This seems to fix the unit test failure in BZ2051.
 *
 * Revision 1.5  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.3  2006/03/07 21:45:26  clq2
 * gtr_1489_cea
 *
 * Revision 1.2.38.1  2006/01/26 13:18:42  gtr
 * Refactored.
 *
 * Revision 1.2  2005/07/05 08:27:00  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.2  2005/06/08 22:10:46  pah
 * make http applications v10 compliant
 *
 * Revision 1.1.2.1  2005/06/02 14:57:28  pah
 * merge the ProvidesVODescription interface into the MetadataService interface
 *
 */
