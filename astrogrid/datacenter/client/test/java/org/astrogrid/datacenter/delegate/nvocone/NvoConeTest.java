/*
 * $Id NvoConeTest.java $
 *
 */

package org.astrogrid.datacenter.delegate.nvocone;


/**
 *
 * @author M Hill
 */

import VOTableUtil.Votable;
import com.tbf.xml.XmlElement;
import com.tbf.xml.XmlParser;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterException;

public class NvoConeTest extends TestCase
{
   
   /**
    * Tests the factory produces the right class
    */
   public void testFactory() throws MalformedURLException, DatacenterException
   {
      ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher("http://dummy.nvoconesearch/cone?cat=mycatalogue");
      
      assertTrue(searcher instanceof NvoConeSearchDelegate);
   }
   
   
   /**
    * Tests the format of the url is right
    */
   public void testUrlFormat() throws IOException
   {
      NvoConeSearchDelegate searcher = new NvoConeSearchDelegate("http://dummy.nvoconesearch/cone?cat=mycatalogue");
      
      //this should throw an exception as the given url is wrong
      try {
         searcher.coneSearch(12.0,12.0,1.0);
         
         fail("Should have thrown an exception trying to run cone search on "+searcher);
      }
      catch (DatacenterException de)
      {
         //look for the url
         Throwable cause = de.getCause();
         
         assertTrue(cause instanceof UnknownHostException);
      }
      
   }
   
   /**
    * Tests against a real service - Messier
    * @see http://voservices.org/cone/register/showlist.asp for a list of nvo cone search implementations
    */
   public void testMessier() throws MalformedURLException, IOException
   {
      ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher(" http://virtualsky.org/servlet/cover?CAT=messier");
      
      InputStream results = searcher.coneSearch(20,-30,20);
      
      checkResults(results);
   }
   
   /**
    * Tests against a real service - NCSA Radio
    * @see http://voservices.org/cone/register/showlist.asp for a list of nvo cone search implementations
    */
   public void testNcsa() throws MalformedURLException, IOException
   {
      ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher("http://adil.ncsa.uiuc.edu/cgi-bin/vocone?survey=f");
      
      InputStream results = searcher.coneSearch(120,90,10);
      
      checkResults(results);
   }
   
   public void checkResults(InputStream results)
   {
      //horrible horrible Javot requiring a particular parser...
      XmlParser parser = new XmlParser();
      XmlElement rootNode = parser.parse(new BufferedInputStream(results));
      
      assertNotNull(rootNode);
      
      Votable votable = new Votable(rootNode);
      
      //check there wer no errors
      Vector errors = votable.getValidationErrors();
      if (errors != null) {
         assertTrue(errors.size() == 0);
      }
      
      //check there is a resource component
      assertTrue(votable.getResourceCount()!=0); //table has no resource
      
   }
   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(NvoConeTest.class);
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
 $Log: NvoConeTest.java,v $
 Revision 1.1  2003/11/17 17:11:12  mch
 Added cone search test

 */
