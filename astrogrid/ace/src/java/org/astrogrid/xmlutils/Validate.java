//-------------------------------------------------------------------------

// FILE:    Validate.java

// PACKAGE: org.astrogrid.xmlutils

//

// DATE       AUTHOR    NOTES

// ----       ------    -----

// 14/10/02   KEA       Initial prototype

//-------------------------------------------------------------------------





package org.astrogrid.xmlutils;



import java.io.*;

import org.astrogrid.xmlutils.XmlValidatorIfc;

import org.astrogrid.xmlutils.XmlValidatorXercesImpl;



import java.util.Map;

import java.util.TreeMap;



/**

 * <p>Command-line harness for XML Validation functionality.

 * <ul>

 * <li>Checks the syntax of the XML input file for validity.

 * <li>Validates the XML input file against its relevant schema(s),

 *     which can either be specified in the <tt>xsi:schemaLocation</tt>

 *     attribute of the toplevel document element, or provided in

 *     a namespace map.

 * </ul>

 *

 * <p>If validation is successful, produces no output.  If validation

 * fails, an exception reports the validation error encountered.

 *

 * <p>Usage:  java org.astrogrid.xmlutils.Validate filename.xml

 *

 * <p>See XmlValidatorIfc documentation for usage example with a namespace

 * map.

 *

 * <p>TO DO:  <ul>

 * <li>In schema-validation mode, the Xerces parser can produce pretty

 * cryptic parse-error messages - is there an alternative parser we

 * can use?  XML4j seems not much better (v. similar to Xerces).

 * </ul>

 *

 *

 * @see org.astrogrid.xmlutils.XmlValidatorIfc

 * @see org.astrogrid.xmlutils.XmlValidatorXercesImpl

 *

 *

 * @author Kona Andrews,

 * <a href="mailto:kea@ast.cam.ac.uk">kea@ast.cam.ac.uk</a>

 * @version 1.0

 *

 *

 * (c) Copyright Astrogrid 2002; all rights reserved.

 * See http://www.astrogrid.org/code_licence.html for terms of usage.

 */

public class Validate

{

   /**

    * Dummy constructor - does nothing.

    */

    public Validate()

    {

    }



   /**

    * Validates the XML file named in args[0] for syntactic

    * correctness, and for semantic correctness against its

    * schema (whic must be specified in the

    * <tt>xsi:schemaLocation</tt> attribute of the toplevel

    * document element.

    */

   public static void main(String [] args) throws Exception {



      if (args.length != 1)

      {

         // Print some usage information

         System.out.println("\nUSAGE: \n"

                  + "java org.astrogrid.xmlutils.Validate <inputXMLFile>\n");

      }

      else

      {

         // Xerces-specific version

         XmlValidatorXercesImpl validator = new XmlValidatorXercesImpl();

         validator.setFeature("ValidateSchema",true);

         validator.setFeature("SuppressWarnings",false);

         validator.setFeature("UseNamespaces",true);



         validator.validate(args[0]);



/* **************************

   //FOLLOWING EXAMPLE SHOWS USE WITH FIXED NAMESPACE MAP



         Map namespaceMap = new TreeMap();

         namespaceMap.put(

               "http://www.astrogrid.org/namespace/AceInput-1_0",

               "http://astrogrid.ast.cam.ac.uk/namespace/AceInput-1_0.xsd"

         );

         namespaceMap.put(

               "http://www.astrogrid.org/namespace/AceInput",

               "http://astrogrid.ast.cam.ac.uk/namespace/AceInput.xsd"

         );

         namespaceMap.put(

               "http://www.astrogrid.org/namespace/SExtractor_2_2_2",

               "http://astrogrid.ast.cam.ac.uk/namespace/SExtractor_2_2_2.xsd"

         );



         validator.validate(args[0],namespaceMap);



   **************************/

      }

   }

}

//-------------------------------------------------------------------------

