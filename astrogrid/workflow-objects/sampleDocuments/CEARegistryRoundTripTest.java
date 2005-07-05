import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import org.astrogrid.applications.beans.v1.Input;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.Output;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.registry.beans.v10.cea.ApplicationDefinition;
import org.astrogrid.registry.beans.v10.cea.CeaApplicationType;
import org.astrogrid.registry.beans.v10.cea.Parameters;
import org.astrogrid.registry.beans.v10.resource.Contact;
import org.astrogrid.registry.beans.v10.resource.Content;
import org.astrogrid.registry.beans.v10.resource.Creator;
import org.astrogrid.registry.beans.v10.resource.Curation;
import org.astrogrid.registry.beans.v10.resource.Date;
import org.astrogrid.registry.beans.v10.resource.Resource;
import org.astrogrid.registry.beans.v10.resource.ResourceName;
import org.astrogrid.registry.beans.v10.resource.types.ContentLevel;
import org.astrogrid.registry.beans.v10.resource.types.Type;

import junit.framework.TestCase;

/*
 * $Id: CEARegistryRoundTripTest.java,v 1.5 2005/07/05 08:26:56 clq2 Exp $
 * 
 * Created on 15-Mar-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

/**
 * update to version 10 schema
 * @author Paul Harrison (pah@jb.man.ac.uk) 15-Mar-2004
 * @version $Name:  $
 * @since iteration5
 */
public class CEARegistryRoundTripTest extends TestCase {

   /**
    * Constructor for CEARegistryEntryGenTest.
    * @param arg0
    */
   public CEARegistryRoundTripTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(CEARegistryRoundTripTest.class);
   }

   public void testwrite() throws IOException, MarshalException, ValidationException {
      Contact contact = new Contact();
      ResourceName resourcenameother = new ResourceName();
      resourcenameother.setContent("other");
      resourcenameother.setIvoId("ivo://false/other");
      
      contact.setName(resourcenameother);
      contact.setEmail("email");
      Curation curation = new Curation();
      curation.setContact(contact);
      Creator creator = new Creator();
      creator.setLogo("nologo");
      creator.setName(resourcenameother);
      curation.setCreator(creator);
      curation.setPublisher(resourcenameother);
      curation.setVersion("0.0001");
      curation.addContributor(resourcenameother);
      Date cdate = new Date();
      short[] ss={1,2,3,4};//silly date values
      org.exolab.castor.types.Date dd = new org.exolab.castor.types.Date(ss);
      
      cdate.setContent(dd );
      curation.addDate(cdate);
            
      
      CeaApplicationType application = new CeaApplicationType();


      application.setIdentifier("ivo://authority/resourcekey");
      application.setTitle("title");
      ResourceName resourcename = new ResourceName();
      resourcename.setContent("me");
      resourcename.setIvoId("ivo://false/me");
      application.setCuration(curation);
      
      application.setShortName("short");

      ApplicationDefinition applicationDefinition = new ApplicationDefinition();
      application.setApplicationDefinition(applicationDefinition);
      
      Parameters parameters = new Parameters();
      applicationDefinition.setParameters(parameters);
      BaseParameterDefinition vParameterDefinition = new BaseParameterDefinition();
      parameters.addParameterDefinition(vParameterDefinition);
      vParameterDefinition.setName("par1");
      vParameterDefinition.setType(ParameterTypes.BINARY);
      vParameterDefinition.setUCD("sillyucd");
      
      InterfacesType interfaces = new InterfacesType();
      applicationDefinition.setInterfaces(interfaces);
      Interface v_interface = new Interface();
      v_interface.setName("i1");
      interfaces.add_interface(v_interface);

      Output output = new Output();
      Input input = new Input();
      v_interface.setInput(input);
      v_interface.setOutput(output);
      
      ParameterRef vPref = new ParameterRef();
      vPref.setRef("par1");
      input.addPref(vPref);
      output.addPref(vPref);
      
      Content content = new Content();
      content.addContentLevel(ContentLevel.VALUE_6);
      content.setDescription("this is the main description");
      content.setReferenceURL("refurl");
      content.addSubject("one of the subjects");
      
     
      content.addType(Type.ANIMATION);
      application.setContent(content);
      
      File outfile = new File("/tmp/test.xml");
  
         FileWriter writer = new FileWriter(outfile);
      Marshaller mar = new Marshaller(writer);
      mar.setDebug(true);
      mar.setMarshalExtendedType(true);
      mar.setSuppressXSIType(false);
      mar.setLogWriter(new PrintWriter(System.out));
      mar.setMarshalAsDocument(true);
      mar.setDoctype("http://www.ivoa.net/xml/CEAService/v0.2", null);
      
//    TODO Castor bug? write a castor wiki page about this....      
      mar.setNamespaceMapping("cea", "http://www.ivoa.net/xml/CEAService/v0.2");


      mar.marshal(application);
         
       FileInputStream istream = new FileInputStream(outfile);
       Unmarshaller um = new Unmarshaller(CeaApplicationType.class);
      
     
       InputSource is = new InputSource(istream);
       CeaApplicationType indescr = (CeaApplicationType)um.unmarshal(is);

   }

}
