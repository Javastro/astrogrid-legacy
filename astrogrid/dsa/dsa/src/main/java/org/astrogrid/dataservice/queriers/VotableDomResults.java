/*$Id: VotableDomResults.java,v 1.1 2009/05/13 13:20:26 gtr Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.dataservice.queriers;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.dataservice.queriers.TableResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.tableserver.out.TableWriter;
import org.astrogrid.tableserver.out.VoTableWriter;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** Trivial Implementation of {@link org.astrogrid.datacenter.queriers.QueryResults} backed by a
 * document object model containing a VOTable document
 *
 */
public class VotableDomResults extends TableResults {
   
    protected final Document doc;

    /**
     * Std constructor
     */
    public VotableDomResults(Querier querier, Document doc) {
      super(querier);
      this.doc = doc;
    }

    /**
     * construct from document in given string
     */
    public VotableDomResults(Querier querier, String doc) throws SAXException, IOException, ParserConfigurationException {
      super(querier);
       if (doc == null) throw new IllegalArgumentException("Can't make a VoTable from a null string");
      this.doc = DomHelper.newDocument(doc);
    }
    
    public int getCount() {
       return doc.getDocumentElement().getElementsByTagName("TR").getLength();
    }
    
   /** Writes results to the given TableWriter    */
   public void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException {
       if (tableWriter instanceof VoTableWriter) {
          DomHelper.DocumentToWriter(doc, ((VoTableWriter) tableWriter).getOut());
       }
       else {
          throw new UnsupportedOperationException("Can only write to votables");
       }
   }

}


/*
$Log: VotableDomResults.java,v $
Revision 1.1  2009/05/13 13:20:26  gtr
*** empty log message ***

Revision 1.2  2005/03/21 18:45:55  mch
Naughty big lump of changes

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.5.10.4  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

Revision 1.5.10.3  2004/11/30 01:04:02  mch
Rationalised tablewriters, reverted AxisDataService06 to string

Revision 1.5.10.2  2004/11/22 00:57:16  mch
New interfaces for SIAP etc and new slinger package

Revision 1.5.10.1  2004/11/17 17:56:07  mch
set mime type, switched results to taking targets

Revision 1.5  2004/11/08 03:00:14  mch
Removed assert and replaced with proper if

Revision 1.4  2004/11/03 05:14:33  mch
Bringing Vizier back online

Revision 1.3  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.2.6.1  2004/10/27 00:43:39  mch
Started adding getCount, some resource fixes, some jsps

Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:05  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.2  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

Revision 1.1  2004/09/06 21:37:26  mch
Factored out VotableResults

Revision 1.3  2004/09/01 12:10:58  mch
added results.toHtml

Revision 1.2  2004/03/14 03:04:57  mch
Added CSV writer

Revision 1.1  2004/03/13 23:40:59  mch
Changes to adapt to It05 refactor

Revision 1.2  2003/12/09 16:25:08  nw
wrote plugin documentation

Revision 1.1  2003/11/18 11:23:49  nw
mavenized cds delegate

Revision 1.1  2003/11/18 11:10:05  nw
mavenized cds delegate
 
*/
