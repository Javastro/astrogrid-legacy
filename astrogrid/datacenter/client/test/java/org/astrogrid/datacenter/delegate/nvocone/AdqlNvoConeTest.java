/*
 * $Id NvoConeTest.java $
 *
 */

package org.astrogrid.datacenter.delegate.nvocone;


/**
 *
 * @author M Hill
 */

import java.io.*;

import VOTableUtil.Votable;
import com.tbf.xml.XmlElement;
import com.tbf.xml.XmlParser;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import javax.xml.rpc.ServiceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.query.QueryStatus;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

public class AdqlNvoConeTest extends TestCase
{
   
   /**
    * Tests the factory produces the right class
    */
   public void testFactory() throws MalformedURLException, IOException, ServiceException
   {
      AdqlQuerier querier = DatacenterDelegateFactory.makeAdqlQuerier("http://dummy.nvoconesearch/cone?cat=mycatalogue");
      
      assertTrue(querier instanceof AdqlNvoConeDelegate);
   }
   
   
   /**
    * Tests the format of the url is right
    */
   public void testUrlFormat() throws IOException, ServiceException, MarshalException, ValidationException
   {
      AdqlQuerier querier = DatacenterDelegateFactory.makeAdqlQuerier("http://dummy.nvoconesearch/cone?cat=mycatalogue");
      
      //this should throw an exception as the given url is wrong
      try
      {
         Select adql = loadAdql("coneQuery2.xml");
         querier.doQuery(querier.VOTABLE, adql);
         
         fail("Should have thrown an exception trying to run cone search on "+querier);
      }
      catch (DatacenterException de)
      {
         //look for the url
         Throwable cause = de.getCause();
         
         assertTrue(cause instanceof UnknownHostException);
      }
      
   }
   
   /**
    * Loads adql from the given filename in the directory of this class
    */
   private Select loadAdql(String filename) throws MarshalException, ValidationException
   {
      InputStream is = this.getClass().getResourceAsStream(filename);
      assertNotNull(is);
      Reader reader = new InputStreamReader(is);
      Select query = Select.unmarshalSelect(reader);
      assertNotNull(query);
      assertTrue(query.isValid());
      return query;
   }
   
   /**
    * Tests a blocking query against a real service - Messier
    * @see http://voservices.org/cone/register/showlist.asp for a list of nvo cone search implementations
    */
   public void testMessier() throws MalformedURLException, IOException, ServiceException, MarshalException, ValidationException
   {
      AdqlQuerier querier = DatacenterDelegateFactory.makeAdqlQuerier("http://virtualsky.org/servlet/cover?CAT=messier");
      
      Select adql = loadAdql("coneQuery2.xml");
      
      DatacenterResults results = querier.doQuery(querier.VOTABLE, adql);
      
      assertNotNull(results);
      
      assertNotNull(results.getVotable());
      
      String doc = XMLUtils.DocumentToString(results.getVotable().getOwnerDocument());
      
      checkResults(new ByteArrayInputStream(doc.getBytes()));
   }

   /**
    * Tests an async query against a real service - NCSA Radio
    * @see http://voservices.org/cone/register/showlist.asp for a list of nvo cone search implementations
    */
   public void testNcsa() throws MalformedURLException, IOException, ServiceException, MarshalException, ValidationException
   {
      AdqlQuerier querier = DatacenterDelegateFactory.makeAdqlQuerier("http://adil.ncsa.uiuc.edu/cgi-bin/vocone?survey=f");
      
      Select adql = loadAdql("coneQuery3.xml");
      
      DatacenterQuery query = querier.makeQuery(adql);
      
      query.start();

      assertTrue(query.getStatus() == QueryStatus.FINISHED);
      
      DatacenterResults results = query.getResultsAndClose();
      
      assertNotNull(results);
      
      assertNotNull(results.getVotable());
      
      String doc = XMLUtils.DocumentToString(results.getVotable().getOwnerDocument());
      
      checkResults(new ByteArrayInputStream(doc.getBytes()));
      
   }
   
   
   
   public void checkResults(InputStream results)
   {
      //horrible horrible Javot requiring a particular parser...
      XmlParser parser = new XmlParser();
      XmlElement rootNode = parser.parse(new BufferedInputStream(results));
      
      assertNotNull(rootNode);
      
      Votable votable = new Votable(rootNode);
      
      //check there were no errors
      /* MCH - at the moment there seem to be some services that return bad votables
      Vector errors = votable.getValidationErrors();
      if (errors != null) {
         assertTrue(errors.size() == 0);
      }
       */
      
      //check there is a resource component
      assertTrue(votable.getResourceCount()!=0);
      
   }
   
   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(AdqlNvoConeTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[])
   {
      junit.textui.TestRunner.run(suite());
   }
   
}

/*
 $Log: AdqlNvoConeTest.java,v $
 Revision 1.2  2003/11/18 00:53:51  mch
 New Adql-compliant cone search

 Revision 1.1  2003/11/18 00:35:32  mch
 New Adql-compliant cone search

 Revision 1.1  2003/11/17 17:11:12  mch
 Added cone search test

 */
