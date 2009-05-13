/*$Id: VoTableTestHelper.java,v 1.1 2009/05/13 13:21:05 gtr Exp $
 * Created on 04-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.tableserver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.xml.ErrorRecorder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.contracts.SchemaMap;


/** Helper for testing votables
 *
 */
public class VoTableTestHelper extends TestCase {

   public static Document assertIsVotable(String candidate) throws IOException, SAXException {

     // Check that the VOTable response is schema-valid XML
     // NB: This doesn't actually test that the returned VOTable
     // contains real data rather than e.g. an error message
     // or no data
     Document doc = DomHelper.newDocument(candidate);
     String rootElement = doc.getDocumentElement().getLocalName();
     if(rootElement == null) {
        rootElement = doc.getDocumentElement().getNodeName();
     }
     try {
       AstrogridAssert.assertSchemaValid(doc,rootElement,SchemaMap.ALL);
     }
     catch (Exception e) {
        fail("VOTable produced is not valid: "+e.getMessage());
     }
     assertTrue("Root element of results is not VOTABLE", doc.getDocumentElement().getLocalName().equals("VOTABLE"));
     return doc;
   }
}


