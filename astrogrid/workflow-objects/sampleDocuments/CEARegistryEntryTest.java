import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.stream.FileImageInputStream;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import org.astrogrid.registry.beans.resource.VODescription;

import junit.framework.TestCase;

/*
 * $Id: CEARegistryEntryTest.java,v 1.1 2004/03/23 16:34:16 pah Exp $
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
public class CEARegistryEntryTest extends TestCase {

   /**
    * Constructor for CEARegistryEntryTest.
    * @param arg0
    */
   public CEARegistryEntryTest(String arg0) {
      super(arg0);
   }

   public static void main(String[] args) {
      junit.textui.TestRunner.run(CEARegistryEntryTest.class);
   }

   public void testRead() throws MarshalException, ValidationException, IOException, MappingException
   {
        Mapping mapping = new Mapping();
        InputStream i2 = this.getClass().getResourceAsStream("mapping.xml");
        InputSource s2 = new InputSource(i2);
        mapping.loadMapping(s2);
        Unmarshaller um = new Unmarshaller(VODescription.class);
        um.setMapping(mapping);
//        InputStream istream = this.getClass().getResourceAsStream("/CEARegistyEntry.xml");
        File f1 = new File("/tmp/test.xml");
        FileInputStream istream = new FileInputStream(f1);
        InputSource saxis = new InputSource(istream);
        VODescription description =  (VODescription)um.unmarshal( saxis);
   }
}
