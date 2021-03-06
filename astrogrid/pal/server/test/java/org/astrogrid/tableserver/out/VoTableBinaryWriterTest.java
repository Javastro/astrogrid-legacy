/*$Id: VoTableBinaryWriterTest.java,v 1.3 2007/06/08 13:16:11 clq2 Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.tableserver.out;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.slinger.targets.ByteArrayTarget;
import org.astrogrid.tableserver.VoTableTestHelper;
import org.astrogrid.tableserver.metadata.ColumnInfo;
import org.xml.sax.SAXException;

/**
 * tests the table metadata resources
 *
 */
public class VoTableBinaryWriterTest extends TestCase  {
   
   
   //checks that the metadoc is valid and reads OK
   public void testVoTableBinaryWriter() throws IOException, ParserConfigurationException, SAXException {
      
      ByteArrayTarget target = new ByteArrayTarget();
      VoTableBinaryWriter votWriter = new VoTableBinaryWriter(target, "Test VOTable", LoginAccount.ANONYMOUS);
      
      votWriter.open();
      
      ColumnInfo[] cols = new ColumnInfo[] {
         new ColumnInfo("RA", "RA_ID", "TABLE", "TABLE_ID", "Right Ascension", Double.class, "POS_EQ_RA_MAIN", "deg"),
         new ColumnInfo("ID", "ID_ID", "TABLE", "TABLE_ID", "Object unique identifier", "string", "ID", null)
      };
      
      votWriter.startTable(cols);
      
      //output some rows
      String[] row = new String[2];
      for (int i = 0; i < 12; i++) {
         row[0] = ""+(double) i/Math.PI;
         row[1] = ""+i;
//         row[2] = ""+new Date();
//         row[3] = ""+i;
         votWriter.writeRow(row);
      }

      votWriter.endTable();
      votWriter.close();
      
      String votable = target.getString();
      VoTableTestHelper.assertIsVotable(votable);
   }
   
   
   
   public static void main(String[] args) {
      junit.textui.TestRunner.run(suite());
   }
   
    public static Test suite() {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(VoTableBinaryWriterTest.class);
    }
   
}



