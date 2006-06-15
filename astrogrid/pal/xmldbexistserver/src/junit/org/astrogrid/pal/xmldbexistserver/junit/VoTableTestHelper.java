/*$Id: VoTableTestHelper.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 * Created on 04-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.pal.xmldbexistserver.junit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.astrogrid.xml.DomHelper;
import org.astrogrid.xml.ErrorRecorder;
import org.astrogrid.xml.Validator;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Helper for testing votables
 *
 */
public class VoTableTestHelper extends TestCase {

   public static Document assertIsVotable(String candidate) throws IOException, SAXException {

      //temporary to ignore this assertion failure while we look for other errors
      //return DomHelper.newDocument(candidate);

      ErrorRecorder recorder = Validator.isValid(new ByteArrayInputStream(candidate.getBytes()));
      if ((recorder != null ) && (recorder.hasErrors())) {
            fail("VOTable produced is not valid: "+recorder.listErrors());
      }
      Document doc = DomHelper.newDocument(candidate);
      assertTrue("Root element of results is not VOTABLE", doc.getDocumentElement().getLocalName().equals("VOTABLE"));
      return doc;
      /**/
   }
}


