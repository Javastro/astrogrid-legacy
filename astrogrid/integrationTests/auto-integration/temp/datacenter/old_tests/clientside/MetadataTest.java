/*$Id: MetadataTest.java,v 1.2 2005/08/01 08:15:52 clq2 Exp $
 * Created on 23-Jan-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.integration.clientside;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.integration.StdKeys;

/**
 * Test metadata integration stuff
 *
 */
public class MetadataTest extends TestCase implements StdKeys {


   public void testStdJspPush() throws IOException {
      jspPush(PAL_STD_STEM);
   }
   
   public void testFitsJspPush() throws IOException {
      jspPush(PAL_FITS_STEM);
   }
   public void testSecJspPush() throws IOException {
      jspPush(PAL_SEC_STEM);
   }
   public void testVizierJspPush() throws IOException {
      jspPush(PAL_VIZIER_STEM);
   }

   /** Tests push to registry */
   public void jspPush(String stem) throws IOException  {

      URL url = new URL(stem+"/admin/pushMetadata.jsp");
      InputStream i = url.openStream();
      
      assertNotNull("No results from pushing", i);
   }
   
   /** Tests push to registry
   public void testServletPush() throws RegistryException, IOException  {

      URL url = new URL(PAL_STD_STEM+"/admin/pushMetadata.jsp");
      InputStream i = url.openStream();
      
      assertNotNull("No results from pushing", i);
   }
    */

   public void testStdSoapGet() throws ServiceException, RemoteException {
      soapGet(PAL_STD_STEM);
   }
   
   public void testFitsSoapGet() throws ServiceException, RemoteException {
      soapGet(PAL_FITS_STEM);
   }
   
   public void testSecSoapGet() throws ServiceException, RemoteException {
      soapGet(PAL_SEC_STEM);
   }
   
   public void testVizierSoapGet() throws ServiceException, RemoteException {
      soapGet(PAL_VIZIER_STEM);
   }
   
   /** Calls SOAP getRegistry() and getMetadata() methods on the web interface */
   public void soapGet(String stem) throws ServiceException, RemoteException {
      Service  service = new Service();
      Call     call    = (Call) service.createCall();
      
      call.setTargetEndpointAddress( stem+"/services/AxisDataService06");
      call.setReturnType( XMLType.XSD_STRING );

      //call getReg method
      call.setOperationName("getRegistration");
      Object response = call.invoke( new Object[] {  } );
      
      //now ought to examine the response...
      
      
      //call getMetadata method
      call.setOperationName("getMetadata");
      
      response = call.invoke( new Object[] {  } );
      
      //examine results?
   }

   /** Only test the standard url, as if the SOAP stuff works above then we
    * only need to test that the delegate code works */
   public void testStdDelegate() throws ServiceException, IOException {
      delegateGet(PAL_STD_STEM);
   }
   
   /** Gets metadata using the delegate */
   public void delegateGet(String stem) throws ServiceException, IOException {
      QuerySearcher searcher = DatacenterDelegateFactory.makeQuerySearcher(stem+"/services/AxisDataService05");
      String metadata = searcher.getMetadata();
      
      //examine results?
      
   }
   
   /** To run standalone */
   public static void main(String[] args) {
      SimpleConfig.setProperty("tomcat.root", "http://twmbarlwm.star.le.ac.uk:8888");
      junit.textui.TestRunner.run(new TestSuite(MetadataTest.class));
   }
}


/*
$Log: MetadataTest.java,v $
Revision 1.2  2005/08/01 08:15:52  clq2
Kmb 1293/1279/intTest1 FS/FM/Jes/Portal/IntTests

Revision 1.1.2.1  2005/07/12 11:21:06  KevinBenson
old datacenter moved out of the test area

Revision 1.2  2004/10/08 15:59:22  mch
made stdkey strings non-static

Revision 1.1  2004/10/08 15:52:18  mch
More tests for Registry push etc

Revision 1.3  2004/10/07 10:48:03  mch
Reintroduced push test

Revision 1.2  2004/09/09 11:04:05  mch
Removed test for the moment

Revision 1.1  2004/09/08 20:06:11  mch
Added metadat push test

 
*/

