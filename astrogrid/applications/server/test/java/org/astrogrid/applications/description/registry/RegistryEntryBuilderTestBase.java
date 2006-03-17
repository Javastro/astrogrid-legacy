/*
 * $Id: RegistryEntryBuilderTestBase.java,v 1.5 2006/03/17 17:50:58 clq2 Exp $
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
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import junit.framework.TestCase;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.TestApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.BaseConfiguration;
import org.astrogrid.applications.manager.DefaultMetadataService;
import org.astrogrid.common.bean.v1.Namespaces;
import org.astrogrid.registry.beans.v10.wsinterface.VOResources;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.test.schema.SchemaMap;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.TestApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.BaseConfiguration;
import org.astrogrid.applications.manager.DefaultMetadataService;
import org.astrogrid.common.bean.v1.Namespaces;
import org.astrogrid.registry.beans.v10.wsinterface.VOResources;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.test.schema.SchemaMap;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
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
       = LogFactory.getLog(RegistryEntryBuilderTest.class);
   
   protected void setUp() throws Exception {
     super.setUp();
     ApplicationDescriptionLibrary lib = createDesciptionLibrary();
     Configuration configuration = new BaseConfiguration();
     builder = new DefaultMetadataService(lib,configuration);
   }
   
   protected DefaultMetadataService builder;
   
   /** test to see if get description as objects and if castor thinks that they are valid.*/
   public void testGetDescription() throws Exception {
        VOResources desc =  builder.getVODescription();
        assertNotNull(desc);
        // this tests castor's idea of validity
        assertTrue(desc.isValid());  
    }
   
   /** test to see if get description as DOM and if XML valid from schema directly */
   public void testDescriptionValidity() throws Exception {
       Document desc =  builder.returnRegistryEntry();
       assertNotNull(desc);
       //this tests xml validity via xml parser
       StringWriter writer = new StringWriter();
       XMLUtils.PrettyDocumentToWriter(desc, writer);
       //note that the pretty print will not necessarily be valid as it puts new lines in all the string only content elements.
       logger.info("pretty print of xml to be VALIDATED\n"+writer.toString());
       AstrogridAssert.assertSchemaValid(desc,"VOResources",SchemaMap.ALL);
    }
   public void RoundTrip() throws Exception {
         VOResources entry = builder.makeEntry();
         assertNotNull(entry);
         StringWriter writer = new StringWriter();
   
         Marshaller mar = new Marshaller(writer);
         mar.setDebug(true);
         mar.setMarshalExtendedType(true);
         mar.setSuppressXSIType(false);
         mar.setLogWriter(new PrintWriter(System.out));
         mar.setMarshalAsDocument(true);
   //    TODO write a castor wiki page about this....      
         mar.setNamespaceMapping("cea", Namespaces.VOCEA);
         mar.setNamespaceMapping("vr", Namespaces.VORESOURCE);
   
   
         mar.marshal(entry);
         writer.close();
         logger.info("marshalled object\n"+writer.toString());
        
         Unmarshaller um = new Unmarshaller(VOResources.class);
        
         //TODO Castor bug -will not round trip....
         VOResources reentry = (VOResources)um.unmarshal(new StringReader(writer.toString()));
        assertNotNull(reentry);
        
         //TODO - should make more extensive tests....
       }
}


/*
 * $Log: RegistryEntryBuilderTestBase.java,v $
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
