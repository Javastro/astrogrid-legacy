/*$Id: FullSearcherTest.java,v 1.2 2004/01/22 16:16:40 nw Exp $
 * Created on 22-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.integrationTests.datacenter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.TestCase;

import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.*;
import org.astrogrid.datacenter.query.QueryStatus;
import org.astrogrid.integrationTests.common.ConfManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
/** Exercises methods of the full searcher delegate interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 22-Jan-2004
 *
 */
public class FullSearcherTest extends TestCase {
   /**
    * Constructor for FullSearcherTest.
    * @param arg0
    */
   public FullSearcherTest(String arg0) {
      super(arg0);
   }
   public static void main(String[] args) {
      junit.textui.TestRunner.run(FullSearcherTest.class);
   }
   
   private static final Log log = LogFactory.getLog(FullSearcherTest.class);
   
   protected void setUp() throws Exception {
      String endpoint = ConfManager.getInstance().getMerlinDatacenterEndPoint();
      delegate = DatacenterDelegateFactory.makeFullSearcher(endpoint);
      assertNotNull("delegate was null",delegate);
      
      InputStream is = this.getClass().getResourceAsStream("query-merlin.xml");
      assertNotNull("query document not found",is);
      Select adqlSelect = Select.unmarshalSelect(new InputStreamReader(is));
      assertNotNull("failed to created adql object model",adqlSelect);
      queryElement = ADQLUtils.toQueryBody(adqlSelect);
      assertNotNull("failed to create query element",queryElement);
      
      myspaceEndpoint = new URL(ConfManager.getInstance().getMySpaceEndPoint());
   }
   protected FullSearcher delegate;
   protected Element queryElement;
   protected URL myspaceEndpoint;
   
   /** test we can get the metadata from the server. not much we can test here */
   public void testGetMetadata() throws Exception {
      Metadata m = delegate.getMetadata();
      assertNotNull("metadata was null",m);
      Document doc = m.getDocument();
      assertNotNull("document in metadata was null",doc);
      Element voreg = m.getVoRegistryMetadata();
      assertNotNull("no voregistry element in metadata",voreg); // check merlin dataserver has a voregistry entry.
   }
   
   /** test the count query method. expect this to fail at the moment - don't think the server implements this yet*/
   public void testCountQuery() throws Exception {
      int i = delegate.countQuery(queryElement);
      assertEquals("query expected to return a single row",i,1);
   }
   
   /** do a blocking query */
   public void testBlockingQuery() throws Exception {
      DatacenterResults results = delegate.doQuery(FullSearcher.VOTABLE,queryElement);
      assertNotNull("blocking query returned null results",results);
      assertTrue("expected votable",results.isVotable());
      assertFalse("expected votable",results.isFits());
      assertFalse("expected votable",results.isUrls());
      
      Element votable = results.getVotable();
      assertNotNull("result votable document is null",votable);
      validateVOTableResult(votable); 
   }
   
   /** test the result is a votable, and that it contains the expected data
    * @todo fill in. 
    */
   private void validateVOTableResult(Element votable) {
      
   }
   
   /** do a non-blocking query, where results are left in myspace. 
    * retreive from myspace, check they're what we expect
    * @throws Exception
    * @todo - currently fails because it gets duff urls back. fix this.
    */
   public void testNonBlockingQuery() throws Exception {
      DatacenterQuery q = delegate.makeQuery(queryElement);
      assertNotNull("datacenter query is null",q);
      q.setResultsDestination(myspaceEndpoint);
      /* returns unknown, whcihc can't be right?     - expected status code behaviour needs to be specified somewhere.
      QueryStatus stat = q.getStatus();
      assertNotNull(stat);
      assertEquals(QueryStatus.CONSTRUCTED,stat);
      */
      q.start();
      QueryStatus stat = q.getStatus();
      assertTrue("Status is" + stat.getText(),! stat.isBefore(QueryStatus.CONSTRUCTED));
      DatacenterResults results = null;
      do {         
         stat = q.getStatus();
         results = q.getResultsAndClose();
         if (results == null) {
            assertTrue("Status is" + stat.getText(),stat.isBefore(QueryStatus.FINISHED));
         }
      } while (results == null /* need some extra timout here too */);
      assertNotNull("query timed out",results);
      assertTrue("expected urls",results.isUrls());
      assertFalse("expected urls",results.isFits());
      assertFalse("expected urls",results.isVotable());
     
      String[] urls = results.getUrls();
      assertNotNull("null url results",urls);
      assertTrue("no url results",urls.length > 0);
      assertNotNull("null url results",urls[0]);
      System.out.println(urls[0]);      
      URL resultURL = new URL(urls[0]);
      Document resultDoc = XMLUtils.newDocument(resultURL.openStream());
      assertNotNull("null result document",resultDoc);
      validateVOTableResult(resultDoc.getDocumentElement());   
      
   }
   
   
}


/* 
$Log: FullSearcherTest.java,v $
Revision 1.2  2004/01/22 16:16:40  nw
minor doc fix

Revision 1.1  2004/01/22 16:14:59  nw
added integration test for datacenter full searcher.
 
*/