/*
 * $Id: SkyNodeTest.java,v 1.1 2004/11/11 20:42:50 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.delegate;


/**
 * Tests the web delegate.  This is a bit naughty as it's not really a unit
 * test - it requires a real PAL to connect to
 *
 * @author M Hill
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.rpc.ServiceException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.delegate.skynode.SkyNodeClient_v074;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.io.Piper;

public class SkyNodeTest extends TestCase
{

   //a skynode service
   private static String NVO_SKYNODE = "http://openskyquery.net/Sky/SkyPortal/SkyPortal.asmx";

   //an astrogrid skynode service
//   private static String AG_SKYNODE = "http://twmbarlwm.star.le.ac.uk:8888/astrogrid-pal-SNAPSHOT/services/AxisDataService05";
   private static String AG_SKYNODE = "http://localhost:8080/pal/services/SkyNode074";
   
   public void testOpenSkyQuery() throws IOException, ServiceException
   {
      SkyNodeClient_v074 client = new SkyNodeClient_v074(new URL(NVO_SKYNODE));
      
      InputStream results = client.askQuery(SimpleQueryMaker.makeConeQuery(20, -30, 0.1));
   }
      
   /** Tests sky node client/server interface on a PAL installation */
   public void testPalQuery() throws IOException, ServiceException
   {
      SkyNodeClient_v074 client = new SkyNodeClient_v074(new URL(AG_SKYNODE));
      
      InputStream results = client.askQuery(SimpleQueryMaker.makeConeQuery(20, -30, 0.1));
      
      Piper.pipe(results, System.out);
   }
      


   public static Test suite()
   {
        // Reflection is used here to add all the testXXX() methods to the suite.
        return new TestSuite(SkyNodeTest.class);
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
 * $Log: SkyNodeTest.java,v $
 * Revision 1.1  2004/11/11 20:42:50  mch
 * Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin
 *
 */




