/*$Id: DeployedServicesTest.java,v 1.4 2004/11/08 02:56:13 mch Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.deployed;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.applications.delegate.DelegateFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.ConeSearcher;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterException;
import org.astrogrid.datacenter.query.Adql074Writer;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.VoSpaceResolver;
import org.astrogrid.util.DomHelper;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.SAXException;

/**
 * Tests to see if the real deployed services are up.  This isn't really an
 * integration test, but it seems a convenient place to have it run and notify
 * us if services go down
 *
 */
public class DeployedServicesTest extends TestCase {

   /** Reads a file from myspace */
   public void testMySpaceRead() throws URISyntaxException, IOException {
      SimpleConfig.getSingleton().setProperty("org.astrogrid.sc2004/frog","astrogrid:store:myspace:http://twmbarlwm.roe.ac.uk:8080/astrogrid-mySpace-SNAPSHOT/services/Manager");
      
      Ivorn file = new Ivorn("ivo://org.astrogrid.sc2004/frog#frog/queries/6df.q");
      
      InputStream i = VoSpaceResolver.resolveInputStream(Account.ANONYMOUS.toUser(), file);
   }
      
   
      /** Runs a CEA test */
   public void doCea(String endpoint, String toolName, double ra, double dec, double radius) throws IOException, SAXException, IOException, ParserConfigurationException, CEADelegateException, MarshalException, ValidationException  {
      
      CommonExecutionConnectorClient client = DelegateFactory.createDelegate(endpoint);

      Tool dsaTool = new Tool();
      dsaTool.setName(toolName);
      dsaTool.setInterface("adql");
//    ParameterValue query
//    i.addParameter(new ParameterValue(

      ParameterValue query= new ParameterValue();
      query.setName("Query");
      query.setIndirect(false);
      Query q = SimpleQueryMaker.makeConeQuery(ra,dec,radius);
      query.setValue(Adql074Writer.makeAdql(q));
      

        ParameterValue format= new ParameterValue();
      format.setName("Format");
        format.setIndirect(false);
        format.setValue("VOTABLE");// rely on default values.
        ParameterValue result= new ParameterValue();
      result.setName("Result");
      result.setIndirect(false);
      
      Input i = new Input();
      i.addParameter(query);
      i.addParameter(format);
      Output o = new Output();
      o.addParameter(result);

      dsaTool.setInput(i);
      dsaTool.setOutput(o);

      String id = client.init(dsaTool, new JobIdentifierType("DeployedServicesTestJesID1"));
      boolean success = client.execute(id);

      ExecutionSummaryType summary = client.getExecutionSumary(id);
      long start = System.currentTimeMillis();
      while ((summary.getStatus().getType() < summary.getStatus().COMPLETED_TYPE) && (System.currentTimeMillis() < start+10000)) {
         summary = client.getExecutionSumary(id);
      }
      java.io.StringWriter sw = new java.io.StringWriter();
      summary.marshal(sw);
      System.out.println(sw.toString());
      
      assertTrue(success);
   }

   public void doCone(String endpoint, double ra, double dec, double radius) throws MalformedURLException, DatacenterException, IOException, IOException, ParserConfigurationException, SAXException {
     ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         endpoint,
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);

      delegate.setTimeout(60000);
      InputStream is = delegate.coneSearch(ra,dec,radius);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }

   public void test6dfCea() throws IOException, MarshalException, CEADelegateException, ParserConfigurationException, ValidationException, SAXException {
      doCea("http://grendel12.roe.ac.uk:8080/pal-6df/services/CommonExecutionConnectorService",
            "roe.ac.uk/DSA_6dF/ceaApplication",
            20,
            20,
            1);
   }
   
   public void testSsaCea() throws IOException, MarshalException, CEADelegateException, ParserConfigurationException, ValidationException, SAXException {
      doCea("http://astrogrid.roe.ac.uk:8080/pal-ssa/services/CommonExecutionConnectorService",
            "roe.ac.uk/DSA_SSA/ceaApplication",
            20,
            -20,
            0.1);
   }
   

   /** Runs a cone search on 6dF */
   public void test6dFCone() throws IOException, SAXException, IOException, ParserConfigurationException  {
      doCone("http://grendel12.roe.ac.uk:8080/pal-6df/services/AxisDataService05",
             10,
             10,
             2);
   }
   
   /** Runs a simple adql search on SEC proxy on grendel12. No point in running
    * a cone search - Solar Event Catalogues don't do cones
   public void testGrendelSecProxy() throws IOException, SAXException, IOException, ParserConfigurationException, ServiceException  {
     QuerySearcher delegate = DatacenterDelegateFactory.makeQuerySearcher(
         Account.ANONYMOUS,
         "http://grendel12.roe.ac.uk:8080/pal-sec/services/AxisDataService05",
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      delegate.setTimeout(60000);

      String adqls = "SELECT * FROM sgas_event AS s WHERE s.nar>9500 AND s.nar<9600";
      
      Query query = SqlQueryMaker.makeQuery(adqls);
      query.getResultsDef().setFormat(ReturnTable.VOTABLE);
      
      InputStream is = delegate.askQuery(query);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }

   /** Runs a cone search on Vizier proxy on grendel12
   public void testGrendelVizierProxy() throws IOException, SAXException, IOException, ParserConfigurationException  {
     ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         "http://grendel12.roe.ac.uk:8080/pal-vizier/services/AxisDataService05",
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      delegate.setTimeout(60000);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }

   /** Runs a cone search on SSA
   public void testSsaCone() throws IOException, SAXException, IOException, ParserConfigurationException  {
     ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         "http://astrogrid.roe.ac.uk:8080/pal-sss/services/AxisDataService05",
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      delegate.setTimeout(60000);

      InputStream is = delegate.coneSearch(10,-10,0.1);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }

   /** Runs a CEA test on SSA
   public void testSsaCea() throws IOException, SAXException, IOException, ParserConfigurationException, CEADelegateException  {
      
      CommonExecutionConnectorClient client = DelegateFactory.createDelegate("http://astrogrid.roe.ac.uk:8080/pal-sss/services/CommonExecutionConnectorService");

      Tool dsaTool = new Tool();
      dsaTool.setName("DSA_SSA/ceaApplication");
      dsaTool.setInterface("adql");
//    ParameterValue query
//    i.addParameter(new ParameterValue(

      ParameterValue query= new ParameterValue();
      query.setName("Query");
      query.setIndirect(false);
      Query q = SimpleQueryMaker.makeConeQuery(20,-10,0.1);
      query.setValue(Adql074Writer.makeAdql(q));
      

        ParameterValue format= new ParameterValue();
      format.setName("Format");
        format.setIndirect(false);
        format.setValue("VOTABLE");// rely on default values.
        ParameterValue result= new ParameterValue();
      result.setName("Result");
      result.setIndirect(false);
      
      Input i = new Input();
      i.addParameter(query);
      i.addParameter(format);
      Output o = new Output();
      o.addParameter(result);

      dsaTool.setInput(i);
      dsaTool.setOutput(o);
//    populateDsaTool(dsaTool);
      String id = client.init(dsaTool, new JobIdentifierType("DeployedServicesTestJesID1"));
      boolean success = client.execute(id);
      client.getResults(id);
      
      assertTrue(success);
   }
   
   public void populateDsaTool(Tool tool) throws IOException {
      
      ParameterValue query= (ParameterValue)tool.findXPathValue("input/parameter[name='Query']");
        query.setIndirect(false);
         
      Query q = SimpleQueryMaker.makeConeQuery(20,-10,0.1);
      query.setValue(Adql074Writer.makeAdql(q));
      

        ParameterValue format= (ParameterValue)tool.findXPathValue("input/parameter[name='Format']");
        format.setIndirect(false);
        format.setValue("VOTABLE");// rely on default values.
        ParameterValue target = (ParameterValue)tool.findXPathValue("output/parameter[name='Result']");
        target.setIndirect(false);
     }
   
   
   /** Runs a cone search on INT-WFS
   public void testIntWfs() throws IOException, SAXException, IOException, ParserConfigurationException  {
     ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(
         Account.ANONYMOUS,
         "http://ag01.ast.cam.ac.uk:8080/astrogrid-pal-Itn05_release/services/AxisDataService05",
         DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      delegate.setTimeout(60000);

      InputStream is = delegate.coneSearch(10,10,2);
      assertNotNull(is);
      DomHelper.newDocument(is);
   }
    */

   public static void main(String[] args) {
      junit.textui.TestRunner.run(new TestSuite(DeployedServicesTest.class));
   }
}


/*
$Log: DeployedServicesTest.java,v $
Revision 1.4  2004/11/08 02:56:13  mch
more tests

Revision 1.3  2004/11/06 19:30:34  mch
Added CEA tests

Revision 1.2  2004/11/03 19:35:41  mch
added CEA test (not working yet) and fixed SQL for SEC that didn't have table names/alias

Revision 1.1  2004/11/03 05:20:50  mch
Moved datacenter deployment tests out of standard tests

Revision 1.1.2.1  2004/11/02 21:51:03  mch
Moved deployment tests here - not exactly right either

Revision 1.6  2004/10/29 17:55:13  mch
Added timeouts

Revision 1.5  2004/10/06 22:03:45  mch
Following Query model changes in PAL

Revision 1.4  2004/09/29 18:42:46  mch
Changed SEC test

Revision 1.3  2004/09/29 16:58:34  mch
Added SEC & Vizier grendel12 proxy tests

Revision 1.2  2004/09/09 12:31:56  mch
Switched to using correct port

Revision 1.1  2004/09/09 11:18:45  mch
Moved DeployedServicesTest to separate package

Revision 1.1  2004/09/08 20:35:10  mch
Added tests against deployed services

Revision 1.1  2004/09/08 20:06:11  mch
Added metadat push test

 
*/
