import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

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
import org.astrogrid.registry.beans.cea.ApplicationDefinition;
import org.astrogrid.registry.beans.cea.CeaApplicationType;
import org.astrogrid.registry.beans.cea.Parameters;
import org.astrogrid.registry.beans.resource.ContactType;
import org.astrogrid.registry.beans.resource.CurationType;
import org.astrogrid.registry.beans.resource.IdentifierType;
import org.astrogrid.registry.beans.resource.ResourceReferenceType;
import org.astrogrid.registry.beans.resource.SummaryType;
import org.astrogrid.registry.beans.resource.VODescription;

import junit.framework.TestCase;

/*
 * $Id: CEARegistryRoundTripTest.java,v 1.2 2004/03/26 12:31:10 pah Exp $
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
      SummaryType summary = new SummaryType();
      ResourceReferenceType publisher = new ResourceReferenceType();
      ContactType contact = new ContactType();
      CurationType curation = new CurationType();
      IdentifierType identifier = new IdentifierType();
      VODescription description = new VODescription();
      CeaApplicationType applicationType = new CeaApplicationType();
      description.addResource(applicationType);


      identifier.setAuthorityID("authority");
      identifier.setResourceKey("resourcekey");
      applicationType.setIdentifier(identifier);
      applicationType.setTitle("title");
      publisher.setTitle("titlw");
      curation.setPublisher(publisher);
      contact.setName("me");
      contact.setEmail("email");
      curation.setContact(contact);
      
      applicationType.setCuration(curation);
      
      applicationType.setShortName("short");
      summary.setDescription("desc");
      summary.setReferenceURL("refurl");
      applicationType.setSummary(summary);

      ApplicationDefinition applicationDefinition = new ApplicationDefinition();
      applicationType.setApplicationDefinition(applicationDefinition);
      
      Parameters parameters = new Parameters();
      applicationDefinition.setParameters(parameters);
      BaseParameterDefinition vParameterDefinition = new BaseParameterDefinition();
      parameters.addParameterDefinition(vParameterDefinition);
      vParameterDefinition.setName("par1");
      vParameterDefinition.setType(ParameterTypes.FILEREFERENCE);
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
      
      File outfile = new File("/tmp/test.xml");
  
         FileWriter writer = new FileWriter(outfile);
         Marshaller.marshal(description, writer);
         
       FileInputStream istream = new FileInputStream(outfile);
       Unmarshaller um = new Unmarshaller(VODescription.class);
       InputSource is = new InputSource(istream);
       VODescription indescr = (VODescription)um.unmarshal(is);

   }

}
